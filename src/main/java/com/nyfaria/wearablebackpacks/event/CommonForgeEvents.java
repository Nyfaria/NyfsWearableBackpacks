package com.nyfaria.wearablebackpacks.event;


import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import com.nyfaria.wearablebackpacks.cap.BackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.init.TagInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getPlayer().isShiftKeyDown() && event.getPlayer().getMainHandItem().isEmpty()) {
            if (event.getFace() == Direction.UP) {
                if (event.getPlayer().getItemBySlot(EquipmentSlotType.CHEST).getItem() == (ItemInit.BACKPACK.get())) {
                    ItemInit.BACKPACK.get().place(new BlockItemUseContext(event.getPlayer(), Hand.MAIN_HAND, event.getPlayer().getItemBySlot(EquipmentSlotType.CHEST), event.getHitVec()));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClickBlock(PlayerInteractEvent.EntityInteract event) {
        if (!BackpackConfig.INSTANCE.canOpenOthers.get()) return;
        if (event.getPlayer().level.isClientSide) return;
        if (event.getTarget() instanceof PlayerEntity) {
            PlayerEntity targetPlayer = (PlayerEntity) event.getTarget();
            PlayerEntity player = event.getPlayer();
            if (canInteractWithEquippedBackpack(player, targetPlayer)) {
                if (targetPlayer.getItemBySlot(EquipmentSlotType.CHEST).getItem() == (ItemInit.BACKPACK.get())) {
                    ItemStack stack = targetPlayer.getItemBySlot(EquipmentSlotType.CHEST);
                    NetworkHooks.openGui((ServerPlayerEntity) player, new BackpackItem.ContainerProvider(stack.getDisplayName(), BackpackItem.getInventory(stack), player, targetPlayer), a -> {
                        a.writeNbt(BackpackItem.getInventory(stack).serializeNBT());
                    });
                }
            }
        }
    }

    public static boolean canInteractWithEquippedBackpack(PlayerEntity player, PlayerEntity carrier) {
        double distance = player.distanceTo(carrier);
        // Calculate angle between player and carrier.
        double angle = Math.toDegrees(Math.atan2(carrier.getZ() - player.getZ(), carrier.getX() - player.getX()));
        // Calculate difference between angle and the direction the carrier entity is looking.
        angle = ((angle - carrier.yBodyRot - 90) % 360 + 540) % 360 - 180;
        return ((distance <= 1.8) && (Math.abs(angle) < 110 / 2));
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isClientSide) return;
        if (event.getEntity() instanceof MobEntity) {
            MobEntity livingEntity = (MobEntity) event.getEntity();
            if (livingEntity.getType().is(TagInit.BACKPACKABLE)) {
                if (event.getWorld().random.nextInt(100) < BackpackConfig.INSTANCE.entityBackpackChance.get()) {
                    ItemStack backpack = new ItemStack(ItemInit.BACKPACK.get());
                    unpackLootTable(new ResourceLocation(WearableBackpacks.MODID, "backpack/" + livingEntity.getType().getRegistryName().getNamespace() + "/" + livingEntity.getType().getRegistryName().getPath()), livingEntity.level, livingEntity.blockPosition(), livingEntity.getRandom().nextLong(), backpack);
                    livingEntity.setItemSlot(EquipmentSlotType.CHEST, backpack);
                    livingEntity.setDropChance(EquipmentSlotType.CHEST, 1.0f);
                }
            }
        }
    }

    public static void unpackLootTable(ResourceLocation lootTable, World level, BlockPos worldPosition, long lootTableSeed, ItemStack stack) {
        if (lootTable != null && level.getServer() != null) {
            LootTable loottable = level.getServer().getLootTables().get(lootTable);
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) level)).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(worldPosition)).withOptionalRandomSeed(lootTableSeed);
            BackpackHolderAttacher.getBackpackHolderUnwrap(stack).fill(lootcontext$builder.create(LootParameterSets.CHEST), loottable);
        }

    }


    @SubscribeEvent
    public static void onItemPickUp(final EntityItemPickupEvent e) {
        ItemStack toPickup = e.getItem().getItem();
        PlayerEntity player = e.getPlayer();
        ItemStack backpackStack = player.getItemBySlot(EquipmentSlotType.CHEST);
        if (!(backpackStack.getItem() == ItemInit.BACKPACK.get())) return;
        if (!BackpackConfig.INSTANCE.autoAddToBag.get()) return;
        if (!(toPickup.getItem().is(TagInit.BLACKLIST))) {
            BackpackInventory qi = BackpackHolderAttacher.getBackpackHolderUnwrap(backpackStack).getInventory();
            int slots = qi.getSlots();
            for (int s = 0; s < slots; s++) {
                ItemStack currentStack = qi.getStackInSlot(s);
                ItemStack rem2 = toPickup.copy();
                if (currentStack.getItem() == toPickup.getItem() || currentStack.isEmpty()) {
                    rem2 = qi.insertItem(s, rem2, false);
                }
                toPickup.setCount(rem2.getCount());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {

        event.getDrops().stream().filter(item -> item.getItem().getItem() == (ItemInit.BACKPACK.get())).forEach(item -> {
            ItemStack stack = item.getItem();
            BlockPos pos = event.getEntityLiving().blockPosition();
            World level = event.getEntityLiving().level;
            while(level.getBlockState(pos.below()).isAir()){
                pos = pos.below();
            }
            BlockState backpack = BlockInit.BACKPACK.get().defaultBlockState();
            level.setBlockAndUpdate(pos, backpack);
            BlockState blockstate1 = level.getBlockState(pos);
            if (blockstate1.is(backpack.getBlock())) {
                BackpackItem.updateCustomBlockEntityTag(level, null, pos, stack);
//                blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
            }
            item.getItem().shrink(1);
        });

//        ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);

    }

    public static BlockRayTraceResult blockTrace(LivingEntity livingEntity, RayTraceContext.FluidMode rayTraceFluid, int range, boolean downOrFace) {
        World level = livingEntity.level;
        Vector3d start = new Vector3d(livingEntity.getX(), livingEntity.getY() + (double) livingEntity.getEyeHeight(), livingEntity.getZ());
        Vector3d look;
        if (!downOrFace) {
            look = livingEntity.getLookAngle();
        } else {
            look = new Vector3d(0.0, (double) (-range), 0.0);
        }

        Vector3d end = new Vector3d(livingEntity.getX() + look.x * (double) range, livingEntity.getY() + (double) livingEntity.getEyeHeight() + look.y * (double) range, livingEntity.getZ() + look.z * (double) range);
        RayTraceContext context = new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, rayTraceFluid, livingEntity);
        return level.clip(context);
    }
}

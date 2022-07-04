package com.nyfaria.wearablebackpacks.event;


import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import com.nyfaria.wearablebackpacks.cap.BackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.init.TagInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if(!event.getPlayer().getMainHandItem().isEmpty())return;
        if (event.getPlayer().isShiftKeyDown()) {
            if (event.getFace() == Direction.UP) {
                if (event.getPlayer().getItemBySlot(EquipmentSlot.CHEST).is(ItemInit.BACKPACK.get())) {
                    ItemInit.BACKPACK.get().place(new BlockPlaceContext(event.getPlayer(), InteractionHand.MAIN_HAND, event.getPlayer().getItemBySlot(EquipmentSlot.CHEST), event.getHitVec()));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClickBlock(PlayerInteractEvent.EntityInteract event) {
        if (!BackpackConfig.INSTANCE.canOpenOthers.get()) return;
        if (event.getPlayer().level.isClientSide) return;
        if (event.getTarget() instanceof Player targetPlayer) {
            Player player = event.getPlayer();
            if (canInteractWithEquippedBackpack(player, targetPlayer)) {
                if (targetPlayer.getItemBySlot(EquipmentSlot.CHEST).is(ItemInit.BACKPACK.get())) {
                    ItemStack stack = targetPlayer.getItemBySlot(EquipmentSlot.CHEST);
                    NetworkHooks.openGui((ServerPlayer) player, new BackpackItem.ContainerProvider(stack.getDisplayName(), BackpackItem.getInventory(stack), player, targetPlayer), a -> {
                        a.writeNbt(BackpackItem.getInventory(stack).serializeNBT());
                    });
                }
            }
        }
    }

    public static boolean canInteractWithEquippedBackpack(Player player, Player carrier) {
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
        if (event.getEntity() instanceof Mob livingEntity) {
            if (livingEntity.getType().is(TagInit.BACKPACKABLE)) {
                if (event.getWorld().random.nextInt(100) < BackpackConfig.INSTANCE.entityBackpackChance.get()) {
                    ItemStack backpack = new ItemStack(ItemInit.BACKPACK.get());
                    unpackLootTable(new ResourceLocation(WearableBackpacks.MODID, "backpack/" + ForgeRegistries.ENTITIES.getKey(livingEntity.getType()).getNamespace() + "/" + ForgeRegistries.ENTITIES.getKey(livingEntity.getType()).getPath()), livingEntity.level, livingEntity.blockPosition(), livingEntity.getRandom().nextLong(), backpack);
                    livingEntity.setItemSlot(EquipmentSlot.CHEST, backpack);
                    livingEntity.setDropChance(EquipmentSlot.CHEST, 1.0f);
                }
            }
        }
    }

    public static void unpackLootTable(ResourceLocation lootTable, Level level, BlockPos worldPosition, long lootTableSeed, ItemStack stack) {
        if (lootTable != null && level.getServer() != null) {
            LootTable loottable = level.getServer().getLootTables().get(lootTable);
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(worldPosition)).withOptionalRandomSeed(lootTableSeed);
            BackpackHolderAttacher.getBackpackHolderUnwrap(stack).fill(lootcontext$builder.create(LootContextParamSets.CHEST), loottable);
        }

    }


    @SubscribeEvent
    public static void onItemPickUp(final EntityItemPickupEvent e) {
        ItemStack toPickup = e.getItem().getItem();
        Player player = e.getPlayer();
        ItemStack backpackStack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!backpackStack.is(ItemInit.BACKPACK.get())) return;
        if (!BackpackConfig.INSTANCE.autoAddToBag.get()) return;
        if (!toPickup.is(TagInit.BLACKLIST)) {
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

        event.getDrops().stream().filter(item -> item.getItem().is(ItemInit.BACKPACK.get())).forEach(item -> {
            ItemStack stack = item.getItem();
            BlockPos pos = event.getEntityLiving().blockPosition();
            Level level = event.getEntityLiving().level;
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

    public static BlockHitResult blockTrace(LivingEntity livingEntity, ClipContext.Fluid rayTraceFluid, int range, boolean downOrFace) {
        Level level = livingEntity.level;
        Vec3 start = new Vec3(livingEntity.getX(), livingEntity.getY() + (double) livingEntity.getEyeHeight(), livingEntity.getZ());
        Vec3 look;
        if (!downOrFace) {
            look = livingEntity.getLookAngle();
        } else {
            look = new Vec3(0.0, (double) (-range), 0.0);
        }

        Vec3 end = new Vec3(livingEntity.getX() + look.x * (double) range, livingEntity.getY() + (double) livingEntity.getEyeHeight() + look.y * (double) range, livingEntity.getZ() + look.z * (double) range);
        ClipContext context = new ClipContext(start, end, ClipContext.Block.COLLIDER, rayTraceFluid, livingEntity);
        return level.clip(context);
    }
}

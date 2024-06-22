package com.nyfaria.wearablebackpacks;

import com.nyfaria.wearablebackpacks.backpack.BackpackContainer;
import com.nyfaria.wearablebackpacks.backpack.BackpackHolder;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.BlockInit;
import com.nyfaria.wearablebackpacks.init.EntityInit;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import com.nyfaria.wearablebackpacks.init.MenuInit;
import com.nyfaria.wearablebackpacks.init.TagInit;
import com.nyfaria.wearablebackpacks.item.BackpackItem;
import com.nyfaria.wearablebackpacks.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CommonClass {

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        ItemInit.loadClass();
        BlockInit.loadClass();
        EntityInit.loadClass();
        MenuInit.loadClass();
        TagInit.init();
    }

    public static void openOtherBackpack(Player player, Player targetEntity) {
        if (canInteractWithEquippedBackpack(player, targetEntity)) {
            if (hasBackPackEquipped(targetEntity)) {
                ItemStack stack = BackpackHolder.getBackpackStack(targetEntity);
                player.openMenu(new BackpackItem.ContainerProvider(stack.getDisplayName(), BackpackItem.getInventory(stack), player, player));
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

    public static boolean hasBackPackEquipped(Player player) {
        return !BackpackHolder.getBackpackStack(player).isEmpty();
    }

    public static InteractionResult setBackpack(Player player, BlockHitResult hitVec) {
        if (BackpackHolder.getBackpackStack(player).is(ItemInit.BACKPACK.get())) {
            InteractionResult result = ItemInit.BACKPACK.get().place(new BlockPlaceContext(player, InteractionHand.MAIN_HAND, BackpackHolder.getBackpackStack(player), hitVec), true);
            if (!BackpackConfig.INSTANCE.useChestSlot.get() && result == InteractionResult.CONSUME) {
                Services.PLATFORM.getBackpackHolder(player).setBackpackStack(ItemStack.EMPTY);
                ((BackpackBlockEntity) player.level().getBlockEntity(hitVec.getBlockPos())).updateBlock();
            }
            return result;
        }
        return InteractionResult.PASS;
    }

    public static void openBackpack(ServerPlayer player) {
        if (BackpackConfig.INSTANCE.canOpenWhileEquipped.get()) {
            if (player != null) {
                ItemStack stack = BackpackHolder.getBackpackStack(player);
                if (stack.is(ItemInit.BACKPACK.get())) {
                    Services.PLATFORM.openBPMenu(player, new BackpackItem.ContainerProvider(stack.getDisplayName(), BackpackItem.getInventory(stack), player, player));
                }
            }
        }
    }

    public static void dropBackpack(Player player) {
        if (player.getServer().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY))
            return;
        if (BackpackHolder.getBackpackStack(player).is(ItemInit.BACKPACK.get())) {
            player.level().setBlockAndUpdate(player.blockPosition(), BlockInit.BACKPACK.get().defaultBlockState());
            ItemStack pStack = BackpackHolder.getBackpackStack(player);
            NonNullList<ItemStack> stacks = NonNullList.withSize(BackpackConfig.INSTANCE.rows.get() * BackpackConfig.INSTANCE.columns.get(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(pStack.getOrCreateTag().getCompound("Items"), stacks);
            BackpackBlockEntity blockEntity = (BackpackBlockEntity) player.level().getBlockEntity(player.blockPosition());
            blockEntity.setItems(stacks);
            blockEntity.setColor(((DyeableLeatherItem) pStack.getItem()).getColor(pStack));
            blockEntity.setBackpackTag(pStack.getTag());
            blockEntity.updateBlock();
            if (BackpackConfig.INSTANCE.useChestSlot.get()) {
                player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            } else {
                Services.PLATFORM.getBackpackHolder(player).setBackpackStack(ItemStack.EMPTY);
            }
            Services.PLATFORM.updateBlockEntity(player, player.blockPosition(), blockEntity.getColor());
        }
    }

    public static void equipBackPack(EntityAccess entity) {
        if (entity instanceof PathfinderMob livingEntity)
            if (livingEntity.getType().is(TagInit.BACKPACKABLE)) {
                if (livingEntity.level().random.nextInt(100) < BackpackConfig.INSTANCE.entityBackpackChance.get()) {
                    ItemStack backpack = new ItemStack(ItemInit.BACKPACK.get());
                    unpackLootTable(new ResourceLocation(Constants.MODID, "backpack/" + BuiltInRegistries.ENTITY_TYPE.getKey(livingEntity.getType()).getNamespace() + "/" + BuiltInRegistries.ENTITY_TYPE.getKey(livingEntity.getType()).getPath()), livingEntity.level(), livingEntity.blockPosition(), livingEntity.getRandom().nextLong(), backpack);
                    livingEntity.setItemSlot(EquipmentSlot.CHEST, backpack);
                    livingEntity.setDropChance(EquipmentSlot.CHEST, 1.0f);
                }
            }
    }

    public static void unpackLootTable(ResourceLocation lootTable, Level level, BlockPos worldPosition, long lootTableSeed, ItemStack stack) {
        if (level.getServer() != null) {
            LootTable loottable = level.getServer().getLootData().getLootTable(lootTable);
            lootTable = null;
            LootParams.Builder lootparams$builder = (new LootParams.Builder((ServerLevel) level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(worldPosition));
            BackpackContainer container = new BackpackContainer(stack);
            loottable.fill(container, lootparams$builder.create(LootContextParamSets.CHEST), lootTableSeed);
            CompoundTag tag = new CompoundTag();
            ContainerHelper.saveAllItems(tag, container.getStacks());
            stack.getOrCreateTag().put("Items", tag);
        }

    }
}
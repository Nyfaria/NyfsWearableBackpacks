package com.nyfaria.wearablebackpacks;

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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;

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
    public static void setBackpack(Player player, BlockHitResult hitVec) {
        if (BackpackHolder.getBackpackStack(player).is(ItemInit.BACKPACK.get())) {
            InteractionResult result =ItemInit.BACKPACK.get().place(new BlockPlaceContext(player, InteractionHand.MAIN_HAND, BackpackHolder.getBackpackStack(player), hitVec), true);
            if(!BackpackConfig.INSTANCE.useChestSlot.get() && result == InteractionResult.CONSUME){
                Services.PLATFORM.getBackpackHolder(player).setBackpackStack(ItemStack.EMPTY);
                ((BackpackBlockEntity)player.level().getBlockEntity(hitVec.getBlockPos())).updateBlock();
            }
        }
    }
}
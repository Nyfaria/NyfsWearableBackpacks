package com.nyfaria.wearablebackpacks.platform.services;

import com.nyfaria.wearablebackpacks.backpack.BackpackBEMenu;
import com.nyfaria.wearablebackpacks.backpack.BackpackHolder;
import com.nyfaria.wearablebackpacks.backpack.BackpackMenu;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }
    BackpackHolder getBackpackHolder(LivingEntity player);
    MenuType<BackpackMenu> registerBPMenu();
    MenuType<BackpackBEMenu> registerBPBEMenu();
    void openBPMenu(ServerPlayer player, MenuProvider supplier);

    void updateBlockEntity(Player player, BlockPos pos, int color);
}
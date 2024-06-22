package com.nyfaria.wearablebackpacks.platform;

import com.nyfaria.wearablebackpacks.Constants;
import com.nyfaria.wearablebackpacks.backpack.BackpackBEMenu;
import com.nyfaria.wearablebackpacks.backpack.BackpackHolder;
import com.nyfaria.wearablebackpacks.backpack.BackpackMenu;
import com.nyfaria.wearablebackpacks.cap.BackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.networking.UpdateBEPacket;
import com.nyfaria.wearablebackpacks.platform.services.IPlatformHelper;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public BackpackHolder getBackpackHolder(LivingEntity player) {
        return BackpackHolderAttacher.WORN_BACKPACK.get(player);
    }

    @Override
    public MenuType<BackpackMenu> registerBPMenu() {
        return new ExtendedScreenHandlerType<>(BackpackMenu::new);
    }

    @Override
    public MenuType<BackpackBEMenu> registerBPBEMenu() {
        return new ExtendedScreenHandlerType<>(BackpackBEMenu::new);
    }

    @Override
    public void openBPMenu(ServerPlayer player, MenuProvider supplier) {
        ExtendedScreenHandlerFactory factory = new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                buf.writeInt(BackpackConfig.INSTANCE.rows.get() * BackpackConfig.INSTANCE.columns.get());
                buf.writeInt(BackpackConfig.INSTANCE.rows.get());
                buf.writeInt(BackpackConfig.INSTANCE.columns.get());
            }

            @Override
            public Component getDisplayName() {
                return supplier.getDisplayName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return supplier.createMenu(i, inventory, player);
            }
        };
        player.openMenu(factory);
    }

    @Override
    public void updateBlockEntity(Player player, BlockPos pos, int color) {
        ServerPlayNetworking.send((ServerPlayer) player, new UpdateBEPacket(pos,color));
    }
}

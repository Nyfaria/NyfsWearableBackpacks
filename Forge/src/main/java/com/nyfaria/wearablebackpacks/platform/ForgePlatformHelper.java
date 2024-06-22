package com.nyfaria.wearablebackpacks.platform;

import com.nyfaria.wearablebackpacks.backpack.BackpackBEMenu;
import com.nyfaria.wearablebackpacks.backpack.BackpackHolder;
import com.nyfaria.wearablebackpacks.backpack.BackpackMenu;
import com.nyfaria.wearablebackpacks.cap.WornBackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.network.NetworkHandler;
import com.nyfaria.wearablebackpacks.network.PacketUpdateBE;
import com.nyfaria.wearablebackpacks.platform.services.IPlatformHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public BackpackHolder getBackpackHolder(LivingEntity player) {
        return WornBackpackHolderAttacher.getHolderUnwrap(player);
    }

    @Override
    public MenuType<BackpackMenu> registerBPMenu() {
        return IForgeMenuType.create(BackpackMenu::new);
    }

    @Override
    public MenuType<BackpackBEMenu> registerBPBEMenu() {
        return IForgeMenuType.create(BackpackBEMenu::new);
    }

    @Override
    public void openBPMenu(ServerPlayer player, MenuProvider supplier) {
        NetworkHooks.openScreen(player, supplier, buf-> {
            buf.writeInt(BackpackConfig.INSTANCE.rows.get() * BackpackConfig.INSTANCE.columns.get());
            buf.writeInt(BackpackConfig.INSTANCE.rows.get());
            buf.writeInt(BackpackConfig.INSTANCE.columns.get());
        });
    }

    @Override
    public void updateBlockEntity(Player player, BlockPos pos, int color) {
        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) player), new PacketUpdateBE(pos, color));
    }


}
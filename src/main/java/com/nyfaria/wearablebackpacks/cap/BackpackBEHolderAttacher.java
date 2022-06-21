package com.nyfaria.wearablebackpacks.cap;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WearableBackpacks.MODID)
public class BackpackBEHolderAttacher extends CapabilityAttacher {

    public static final Capability<BackpackBEHolder> BACKPACK_BE_CAPABILITY = getCapability(new CapabilityToken<>() {
    });
    public static final ResourceLocation BACKPACK_BE_RL = new ResourceLocation(WearableBackpacks.MODID, "backpackbe");
    private static final Class<BackpackBEHolder> CAPABILITY_CLASS = BackpackBEHolder.class;

    public static BackpackBEHolder getBackpackBEHolderUnwrap(BlockEntity player) {
        return getBackpackBEHolder(player).orElse(null);
    }

    public static LazyOptional<BackpackBEHolder> getBackpackBEHolder(BlockEntity player) {
        return player.getCapability(BACKPACK_BE_CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<BlockEntity> event, BlockEntity BlockEntity) {
        if(BlockEntity instanceof BackpackBlockEntity) {
            genericAttachCapability(event, new BackpackBEHolder(BlockEntity), BACKPACK_BE_CAPABILITY, BACKPACK_BE_RL);
        }
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerBlockEntityAttacher(BackpackBEHolderAttacher::attach, BackpackBEHolderAttacher::getBackpackBEHolder);
    }

}

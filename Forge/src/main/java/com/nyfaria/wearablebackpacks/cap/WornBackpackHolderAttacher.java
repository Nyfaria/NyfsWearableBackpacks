package com.nyfaria.wearablebackpacks.cap;

import com.nyfaria.wearablebackpacks.Constants;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class WornBackpackHolderAttacher extends CapabilityAttacher {
    public static final Capability<WornBackpackHolder> CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(Constants.MODID, "worn_backpack");
    private static final Class<WornBackpackHolder> CAPABILITY_CLASS = WornBackpackHolder.class;

    @SuppressWarnings("ConstantConditions")
    public static WornBackpackHolder getHolderUnwrap(Entity player) {
        return getHolder(player).orElse(null);
    }

    public static LazyOptional<WornBackpackHolder> getHolder(Entity player) {
        return player.getCapability(CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<Entity> event, LivingEntity entity) {
        genericAttachCapability(event, new WornBackpackHolder(entity), CAPABILITY, RESOURCE_LOCATION);
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerEntityAttacher(LivingEntity.class,WornBackpackHolderAttacher::attach, WornBackpackHolderAttacher::getHolder);
    }
}
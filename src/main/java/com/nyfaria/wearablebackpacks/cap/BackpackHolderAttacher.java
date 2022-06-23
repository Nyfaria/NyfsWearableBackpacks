package com.nyfaria.wearablebackpacks.cap;

import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WearableBackpacks.MODID)
public class BackpackHolderAttacher extends CapabilityAttacher {

    @CapabilityInject(BackpackHolder.class)
    public static final Capability<BackpackHolder> BACKPACK_CAPABILITY = null;
    public static final ResourceLocation BACKPACK_RL = new ResourceLocation(WearableBackpacks.MODID, "backpack");
    private static final Class<BackpackHolder> CAPABILITY_CLASS = BackpackHolder.class;

    public static BackpackHolder getBackpackHolderUnwrap(ItemStack player) {
        return getBackpackHolder(player).orElse(null);
    }

    public static LazyOptional<BackpackHolder> getBackpackHolder(ItemStack player) {
        return player.getCapability(BACKPACK_CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<ItemStack> event, ItemStack itemStack) {
        if(itemStack.getItem() == ItemInit.BACKPACK.get()) {
            genericAttachCapability(event, new BackpackHolder(itemStack), BACKPACK_CAPABILITY, BACKPACK_RL);
        }
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerItemStackAttacher(BackpackHolderAttacher::attach, BackpackHolderAttacher::getBackpackHolder);
    }

}

package com.nyfaria.examplemod.init;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.nyfaria.examplemod.ExampleMod.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    private static final List<Pair<Supplier<EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier.Builder>>> attributeSuppliers = new ArrayList<>();

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(MODID + ":" + name));
    }

    private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                         Supplier<AttributeSupplier.Builder> attributeSupplier) {
        RegistryObject<EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(Pair.of((Supplier<EntityType<? extends LivingEntity>>) (Supplier<?>) entityTypeSupplier, attributeSupplier));
        return entityTypeSupplier;
    }

    @SubscribeEvent
    public static void attribs(EntityAttributeCreationEvent e) {
        attributeSuppliers.forEach(p -> e.put(p.getFirst().get(), p.getSecond().get().build()));
    }
}

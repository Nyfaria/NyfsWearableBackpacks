package com.nyfaria.wearablebackpacks.item;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.backpack.BackpackContainer;
import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import com.nyfaria.wearablebackpacks.backpack.BackpackMaterial;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.cap.BackpackBEHolderAttacher;
import com.nyfaria.wearablebackpacks.cap.BackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.client.renderer.SimpleItemRenderer;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import com.nyfaria.wearablebackpacks.init.TagInit;
import com.nyfaria.wearablebackpacks.tooltip.BackpackTooltip;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.network.NetworkHooks;
import org.antlr.v4.runtime.misc.NotNull;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BackpackItem extends GeoArmorItem implements IAnimatable, DyeableLeatherItem {
    private final Block block;
    private final AnimationFactory animationFactory = new AnimationFactory(this);

    public BackpackItem(Block pBlock, Properties pProperties) {
        super(BackpackMaterial.LEATHER, EquipmentSlot.CHEST, pProperties);
        this.block = pBlock;
    }

    public static BackpackInventory getInventory(ItemStack itemStack) {
        return BackpackHolderAttacher.getBackpackHolderUnwrap(itemStack).getInventory();
    }


    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return BackpackConfig.INSTANCE.canEnchantBackpack.get();
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            if(Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.CHEST) == stack){
                tooltip.add(new TranslatableComponent("message.wearablebackpacks.sneak_place"));
            } else {
                tooltip.add(new TranslatableComponent("message.wearablebackpacks.place"));
            }
        } else {
            tooltip.add(new TranslatableComponent("Hold " + "\u00A7e" + "SHIFT" + "\u00A77" + " for More Info"));
        }
    }

    public InteractionResult useOn(UseOnContext pContext) {
        if(pContext.getClickedFace() != Direction.UP || !pContext.getLevel().getBlockState(pContext.getClickedPos()).isCollisionShapeFullBlock(pContext.getLevel(),pContext.getClickedPos()) ){
            return super.useOn(pContext);
        }
        InteractionResult interactionresult = this.place(new BlockPlaceContext(pContext));
        if (!interactionresult.consumesAction() && this.isEdible()) {
            InteractionResult interactionresult1 = this.use(pContext.getLevel(), pContext.getPlayer(), pContext.getHand()).getResult();
            return interactionresult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionresult1;
        } else {
            return interactionresult;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {

        ItemStack stack = playerIn.getItemInHand(handIn);
        if (!playerIn.isShiftKeyDown() && BackpackConfig.INSTANCE.canOpenWithHand.get()) {
            if (!worldIn.isClientSide()) {
                NetworkHooks.openGui((ServerPlayer) playerIn, new ContainerProvider(stack.getDisplayName(), getInventory(stack), playerIn, playerIn), a -> {
                    a.writeNbt(getInventory(stack).serializeNBT());
                });
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, worldIn.isClientSide);

    }


    @Override
    public int getDefense() {
        return BackpackConfig.INSTANCE.backpackDefenseLevel.get();
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        if(BackpackConfig.INSTANCE.inventoryToolTip.get()) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                NonNullList<ItemStack> nonnulllist = NonNullList.create();
                if (getInventory(itemStack) != null) {
                    return Optional.of(new BackpackTooltip(getInventory(itemStack).getStacks(), 64));
                }
            } else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        BackpackHolderAttacher.getBackpackHolderUnwrap(stack).deserializeNBT(nbt.getCompound("inventory"), true);
        super.readShareTag(stack, nbt);
    }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag tag = super.getShareTag(stack);
        if (tag == null) {
            tag = new CompoundTag();
        }
        tag.put("inventory", BackpackHolderAttacher.getBackpackHolderUnwrap(stack).serializeNBT(true));
        return tag;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return BackpackConfig.INSTANCE.backpackDurability.get();
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack inputItem, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if(BackpackConfig.INSTANCE.bundleAdd.get()) {
            if (clickAction == ClickAction.SECONDARY) {
                if (!inputItem.is(TagInit.BLACKLIST) && !stack.isEmpty()) {
                    BackpackInventory qi = getInventory(stack);
                    int slots = qi.getSlots();
                    for (int s = 0; s < slots; s++) {
                        ItemStack currentStack = qi.getStackInSlot(s);
                        ItemStack rem2 = inputItem.copy();
                        if (currentStack.getItem() == inputItem.getItem() || currentStack.isEmpty()) {
                            rem2 = qi.insertItem(s, rem2, false);
                        }
                        inputItem.setCount(rem2.getCount());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        return pContext.getLevel().setBlock(pContext.getClickedPos(), pState, 11);
    }

    public InteractionResult place(BlockPlaceContext pContext) {
        if (!pContext.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockplacecontext = this.updatePlacementContext(pContext);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(blockplacecontext, blockstate)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
                        }
                    }

                    level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
                    SoundType soundtype = blockstate1.getSoundType(level, blockpos, pContext.getPlayer());
                    level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, pContext.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (player == null || !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }

    private BlockState updateBlockStateFromTag(BlockPos pPos, Level pLevel, ItemStack pStack, BlockState pState) {
        BlockState blockstate = pState;
        CompoundTag compoundtag = pStack.getTag();
        if (compoundtag != null) {
            CompoundTag compoundtag1 = compoundtag.getCompound("BlockStateTag");
            StateDefinition<Block, BlockState> statedefinition = pState.getBlock().getStateDefinition();

            for (String s : compoundtag1.getAllKeys()) {
                Property<?> property = statedefinition.getProperty(s);
                if (property != null) {
                    String s1 = compoundtag1.get(s).getAsString();
                    blockstate = updateState(blockstate, property, s1);
                }
            }
        }

        if (blockstate != pState) {
            pLevel.setBlock(pPos, blockstate, 2);
        }

        return blockstate;
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState pState, Property<T> pProperty, String pValueIdentifier) {
        return pProperty.getValue(pValueIdentifier).map((p_40592_) -> {
            return pState.setValue(pProperty, p_40592_);
        }).orElse(pState);
    }

    @Deprecated
    //Forge: Use more sensitive version {@link BlockItem#getPlaceSound(BlockState, IBlockReader, BlockPos, Entity) }
    protected SoundEvent getPlaceSound(BlockState pState) {
        return pState.getSoundType().getPlaceSound();
    }

    //Forge: Sensitive version of BlockItem#getPlaceSound
    protected SoundEvent getPlaceSound(BlockState state, Level world, BlockPos pos, Player entity) {
        return state.getSoundType(world, pos, entity).getPlaceSound();
    }

    public Block getBlock() {
        return this.getBlockRaw() == null ? null : this.getBlockRaw().delegate.get();
    }

    private Block getBlockRaw() {
        return this.block;
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext pContext) {
        BlockState blockstate = this.getBlock().getStateForPlacement(pContext);
        return blockstate != null && this.canPlace(pContext, blockstate) ? blockstate : null;
    }

    protected boolean mustSurvive() {
        return true;
    }

    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
        Player player = pContext.getPlayer();
        CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        return (!this.mustSurvive() || pState.canSurvive(pContext.getLevel(), pContext.getClickedPos())) && pContext.getLevel().isUnobstructed(pState, pContext.getClickedPos(), collisioncontext);
    }

    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext pContext) {
        return pContext;
    }

    protected boolean updateCustomBlockEntityTag(BlockPos pPos, Level pLevel, @Nullable Player pPlayer, ItemStack pStack, BlockState pState) {
        return updateCustomBlockEntityTag(pLevel, pPlayer, pPos, pStack);
    }

    @Nullable
    public static CompoundTag getBlockEntityData(ItemStack p_186337_) {
        return p_186337_.getTagElement("BlockEntityTag");
    }


    public static boolean updateCustomBlockEntityTag(Level pLevel, @Nullable Player pPlayer, BlockPos pPos, ItemStack pStack) {
        MinecraftServer minecraftserver = pLevel.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            CompoundTag compoundtag = getBlockEntityData(pStack);
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity != null) {
                ((BackpackBlockEntity) blockentity).setColor(((DyeableLeatherItem) pStack.getItem()).getColor(pStack));
                ((BackpackBlockEntity) blockentity).setItems((BackpackHolderAttacher.getBackpackHolderUnwrap(pStack).getInventory().getStacks()));
                ((BackpackBlockEntity) blockentity).setBackpackTag(pStack.getTag());
                if (compoundtag != null) {
                    if (!pLevel.isClientSide && blockentity.onlyOpCanSetNbt() && (pPlayer == null || !pPlayer.canUseGameMasterBlocks())) {
                        return false;
                    }
                    CompoundTag compoundtag1 = blockentity.saveWithoutMetadata();
                    CompoundTag compoundtag2 = compoundtag1.copy();
                    compoundtag1.merge(compoundtag);
                    if (!compoundtag1.equals(compoundtag2)) {
                        blockentity.load(compoundtag1);
                        blockentity.setChanged();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack,
                                                  EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return (HumanoidModel<?>) GeoArmorRenderer.getRenderer(BackpackItem.this.getClass(), entityLiving).applyEntityStats(_default)
                        .applySlot(armorSlot).setCurrentItem(entityLiving, itemStack, armorSlot);
            }

            SimpleItemRenderer<BackpackItem> renderer = new SimpleItemRenderer<>();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return BackpackConfig.INSTANCE.canEquipFromInventory.get();
    }

    @Nullable
    @Override
    public Entity createEntity(Level level, Entity location, ItemStack stack) {
        return super.createEntity(level, location, stack);
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    public static class ContainerProvider implements MenuProvider {
        private final Component displayName;
        private final BackpackInventory inventory;
        private final BlockPos pos;
        private final boolean isItem;
        private final int ownerId;

        public ContainerProvider(Component displayName, BackpackInventory inventory, Player accessingPlayer, Player owningPlayer) {
            this.displayName = displayName;
            this.inventory = inventory;
            this.pos = owningPlayer.blockPosition();
            this.isItem = accessingPlayer != owningPlayer;
            this.ownerId = owningPlayer.getId();

        }

        @Override
        public @NotNull
        Component getDisplayName() {
            return this.displayName;
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInv, @NotNull Player player) {
            return new BackpackContainer(id, playerInv, this.inventory, pos, isItem, ownerId);
        }

    }
}

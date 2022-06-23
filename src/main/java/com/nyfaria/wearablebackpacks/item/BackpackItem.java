package com.nyfaria.wearablebackpacks.item;

import com.nyfaria.wearablebackpacks.backpack.BackpackContainer;
import com.nyfaria.wearablebackpacks.backpack.BackpackInventory;
import com.nyfaria.wearablebackpacks.backpack.BackpackMaterial;
import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.cap.BackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.config.BackpackConfig;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.antlr.v4.runtime.misc.NotNull;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

import javax.annotation.Nullable;
import java.util.List;

public class BackpackItem extends GeoArmorItem implements IAnimatable, IDyeableArmorItem {
    private final Block block;
    private final AnimationFactory animationFactory = new AnimationFactory(this);

    public BackpackItem(Block pBlock, Properties pProperties) {
        super(BackpackMaterial.LEATHER, EquipmentSlotType.CHEST, pProperties);
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
    public void appendHoverText(@NotNull ItemStack stack, World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            if(Minecraft.getInstance().player.getItemBySlot(EquipmentSlotType.CHEST) == stack){
                tooltip.add(new TranslationTextComponent("message.wearablebackpacks.sneak_place"));
            } else {
                tooltip.add(new TranslationTextComponent("message.wearablebackpacks.place"));
            }
        } else {
            tooltip.add(new TranslationTextComponent("Hold " + "\u00A7e" + "SHIFT" + "\u00A77" + " for More Info"));
        }
    }

    public ActionResultType useOn(ItemUseContext pContext) {
        if(pContext.getClickedFace() != Direction.UP || !pContext.getLevel().getBlockState(pContext.getClickedPos()).isCollisionShapeFullBlock(pContext.getLevel(),pContext.getClickedPos()) ){
            return super.useOn(pContext);
        }
        ActionResultType interactionresult = this.place(new BlockItemUseContext(pContext));
        if (!interactionresult.consumesAction() && this.isEdible()) {
            ActionResultType interactionresult1 = this.use(pContext.getLevel(), pContext.getPlayer(), pContext.getHand()).getResult();
            return interactionresult1 == ActionResultType.CONSUME ? ActionResultType.FAIL : interactionresult1;
        } else {
            return interactionresult;
        }
    }

    @Override
    public ActionResult<ItemStack> use(@NotNull World worldIn, PlayerEntity playerIn, @NotNull Hand handIn) {

        ItemStack stack = playerIn.getItemInHand(handIn);
        if (!playerIn.isShiftKeyDown() && BackpackConfig.INSTANCE.canOpenWithHand.get()) {
            if (!worldIn.isClientSide()) {
                NetworkHooks.openGui((ServerPlayerEntity) playerIn, new ContainerProvider(stack.getDisplayName(), getInventory(stack), playerIn, playerIn), a -> {
                    a.writeNbt(getInventory(stack).serializeNBT());
                });
            }
        }
        return ActionResult.sidedSuccess(stack, worldIn.isClientSide);

    }


    @Override
    public int getDefense() {
        return BackpackConfig.INSTANCE.backpackDefenseLevel.get();
    }


    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        BackpackHolderAttacher.getBackpackHolderUnwrap(stack).deserializeNBT(nbt.getCompound("inventory"), true);
        super.readShareTag(stack, nbt);
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT tag = super.getShareTag(stack);
        if (tag == null) {
            tag = new CompoundNBT();
        }
        tag.put("inventory", BackpackHolderAttacher.getBackpackHolderUnwrap(stack).serializeNBT(true));
        return tag;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return BackpackConfig.INSTANCE.backpackDurability.get();
    }



    @Override
    public boolean canBeDepleted() {
        return true;
    }

    protected boolean placeBlock(BlockItemUseContext pContext, BlockState pState) {
        return pContext.getLevel().setBlock(pContext.getClickedPos(), pState, 11);
    }

    public ActionResultType place(BlockItemUseContext pContext) {
        if (!pContext.canPlace()) {
            return ActionResultType.FAIL;
        } else {
            BlockItemUseContext blockplacecontext = this.updatePlacementContext(pContext);
            if (blockplacecontext == null) {
                return ActionResultType.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return ActionResultType.FAIL;
                } else if (!this.placeBlock(blockplacecontext, blockstate)) {
                    return ActionResultType.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    World level = blockplacecontext.getLevel();
                    PlayerEntity player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayerEntity) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, blockpos, itemstack);
                        }
                    }


                    SoundType soundtype = blockstate1.getSoundType(level, blockpos, pContext.getPlayer());
                    level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, pContext.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (player == null || !player.abilities.instabuild) {
                        itemstack.shrink(1);
                    }

                    return ActionResultType.sidedSuccess(level.isClientSide);
                }
            }
        }
    }

    private BlockState updateBlockStateFromTag(BlockPos pPos, World pLevel, ItemStack pStack, BlockState pState) {
        BlockState blockstate = pState;
        CompoundNBT compoundtag = pStack.getTag();
        if (compoundtag != null) {
            CompoundNBT compoundtag1 = compoundtag.getCompound("BlockStateTag");
            StateContainer<Block, BlockState> statedefinition = pState.getBlock().getStateDefinition();

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
    protected SoundEvent getPlaceSound(BlockState state, World world, BlockPos pos, PlayerEntity entity) {
        return state.getSoundType(world, pos, entity).getPlaceSound();
    }

    public Block getBlock() {
        return this.getBlockRaw() == null ? null : this.getBlockRaw().delegate.get();
    }

    private Block getBlockRaw() {
        return this.block;
    }

    @Nullable
    protected BlockState getPlacementState(BlockItemUseContext pContext) {
        BlockState blockstate = this.getBlock().getStateForPlacement(pContext);
        return blockstate != null && this.canPlace(pContext, blockstate) ? blockstate : null;
    }

    protected boolean mustSurvive() {
        return true;
    }

    protected boolean canPlace(BlockItemUseContext pContext, BlockState pState) {
        PlayerEntity player = pContext.getPlayer();
        ISelectionContext collisioncontext = player == null ? ISelectionContext.empty() : ISelectionContext.of(player);
        return (!this.mustSurvive() || pState.canSurvive(pContext.getLevel(), pContext.getClickedPos())) && pContext.getLevel().isUnobstructed(pState, pContext.getClickedPos(), collisioncontext);
    }

    @Nullable
    public BlockItemUseContext updatePlacementContext(BlockItemUseContext pContext) {
        return pContext;
    }

    protected boolean updateCustomBlockEntityTag(BlockPos pPos, World pLevel, @Nullable PlayerEntity pPlayer, ItemStack pStack, BlockState pState) {
        return updateCustomBlockEntityTag(pLevel, pPlayer, pPos, pStack);
    }

    @Nullable
    public static CompoundNBT getBlockEntityData(ItemStack p_186337_) {
        return p_186337_.getTagElement("BlockEntityTag");
    }


    public static boolean updateCustomBlockEntityTag(World pLevel, @Nullable PlayerEntity pPlayer, BlockPos pPos, ItemStack pStack) {
        MinecraftServer minecraftserver = pLevel.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            CompoundNBT compoundtag = getBlockEntityData(pStack);
            TileEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity != null) {
                ((BackpackBlockEntity) blockentity).setColor(((IDyeableArmorItem) pStack.getItem()).getColor(pStack));
                ((BackpackBlockEntity) blockentity).setItems((BackpackHolderAttacher.getBackpackHolderUnwrap(pStack).getInventory().getStacks()));
                ((BackpackBlockEntity) blockentity).setBackpackTag(pStack.getTag());
                if (compoundtag != null) {
                    if (!pLevel.isClientSide && blockentity.onlyOpCanSetNbt() && (pPlayer == null || !pPlayer.canUseGameMasterBlocks())) {
                        return false;
                    }
                    CompoundNBT compoundtag1 = blockentity.save(new CompoundNBT());
                    CompoundNBT compoundtag2 = compoundtag1.copy();
                    compoundtag1.merge(compoundtag);
                    if (!compoundtag1.equals(compoundtag2)) {
                        blockentity.load(pLevel.getBlockState(pPos),compoundtag1);
                        blockentity.setChanged();
                        return true;
                    }
                }
            }

            return false;
        }
    }





    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        return BackpackConfig.INSTANCE.canEquipFromInventory.get();
    }

    @Nullable
    @Override
    public Entity createEntity(World level, Entity location, ItemStack stack) {
        return super.createEntity(level, location, stack);
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    public static class ContainerProvider implements INamedContainerProvider {
        private final ITextComponent displayName;
        private final BackpackInventory inventory;
        private final BlockPos pos;
        private final boolean isItem;
        private final int ownerId;

        public ContainerProvider(ITextComponent displayName, BackpackInventory inventory, PlayerEntity accessingPlayer, PlayerEntity owningPlayer) {
            this.displayName = displayName;
            this.inventory = inventory;
            this.pos = owningPlayer.blockPosition();
            this.isItem = accessingPlayer != owningPlayer;
            this.ownerId = owningPlayer.getId();

        }

        @Override
        public @NotNull
        ITextComponent getDisplayName() {
            return this.displayName;
        }

        @Nullable
        @Override
        public Container createMenu(int id, @NotNull PlayerInventory playerInv, @NotNull PlayerEntity player) {
            return new BackpackContainer(id, playerInv, this.inventory, pos, isItem, ownerId);
        }

    }
}

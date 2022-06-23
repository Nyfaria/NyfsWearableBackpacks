package com.nyfaria.wearablebackpacks.block;

import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import com.nyfaria.wearablebackpacks.cap.BackpackHolderAttacher;
import com.nyfaria.wearablebackpacks.init.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class BackpackBlock extends HorizontalBlock {
    private static final VoxelShape SHAPE_NORTH = makeShape(Direction.NORTH);
    private static final VoxelShape SHAPE_EAST = makeShape(Direction.EAST);
    private static final VoxelShape SHAPE_SOUTH = makeShape(Direction.SOUTH);
    private static final VoxelShape SHAPE_WEST = makeShape(Direction.WEST);

    public BackpackBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        IWorld levelaccessor = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        return this.defaultBlockState()/*.setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER))*/
                .setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        if (!pLevel.isClientSide) {

            INamedContainerProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
            if (menuprovider != null) {
                pPlayer.openMenu(menuprovider);
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        BackpackBlockEntity blockentity = (BackpackBlockEntity) world.getBlockEntity(pos);
        ItemStack itemstack = getColoredItemStack(blockentity.getColor());
        itemstack.setTag(blockentity.getBackpackTag());
        CompoundNBT compoundtag = new CompoundNBT();
        ItemStackHelper.saveAllItems(compoundtag, BackpackBlockEntity.getInventory(blockentity));
        BackpackHolderAttacher.getBackpackHolderUnwrap(itemstack).deserializeNBT(compoundtag);
        return itemstack;
    }


    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        Direction direction = pState.getValue(FACING);
        return switch (direction) {
            case NORTH -> SHAPE_NORTH;
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    public static VoxelShape makeShape(Direction direction) {
        VoxelShape shape = VoxelShapes.empty();
        if (direction == Direction.NORTH) {
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.1875, 0, 0.28125, 0.8125, 0.5625, 0.59375), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.25, 0.0625, 0.59375, 0.75, 0.4375, 0.71875), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.1875, 0.5625, 0.28125, 0.8125, 0.75, 0.59375), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.6875, 0.1875, 0.21875, 0.75, 0.6875, 0.28125), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.25, 0.1875, 0.21875, 0.3125, 0.6875, 0.28125), IBooleanFunction.OR);
        } else if (direction == Direction.WEST) {
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.28125, 0, 0.1875, 0.59375, 0.5625, 0.8125), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.59375, 0.0625, 0.25, 0.71875, 0.4375, 0.75), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.28125, 0.5625, 0.1875, 0.59375, 0.75, 0.8125), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.21875, 0.1875, 0.25, 0.28125, 0.6875, 0.3125), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.21875, 0.1875, 0.625, 0.28125, 0.6875, 0.6875), IBooleanFunction.OR);
        } else if (direction == Direction.EAST) {
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.40625, 0, 0.1875, 0.71875, 0.5625, 0.8125), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.28125, 0.0625, 0.25, 0.40625, 0.4375, 0.75), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.40625, 0.5625, 0.1875, 0.71875, 0.75, 0.8125), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.71875, 0.1875, 0.25, 0.78125, 0.6875, 0.3125), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.71875, 0.1875, 0.625, 0.78125, 0.6875, 0.6875), IBooleanFunction.OR);
        } else if (direction == Direction.SOUTH) {
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.1875, 0, 0.40625, 0.8125, 0.5625, 0.71875), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.25, 0.0625, 0.28125, 0.75, 0.4375, 0.40625), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.1875, 0.5625, 0.40625, 0.8125, 0.75, 0.71875), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.6875, 0.1875, 0.71875, 0.75, 0.6875, 0.78125), IBooleanFunction.OR);
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.25, 0.1875, 0.71875, 0.3125, 0.6875, 0.78125), IBooleanFunction.OR);
        }
        return shape;
    }

    public boolean triggerEvent(BlockState pState, World pLevel, BlockPos pPos, int pId, int pParam) {
        super.triggerEvent(pState, pLevel, pPos, pId, pParam);
        TileEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity == null ? false : blockentity.triggerEvent(pId, pParam);
    }

    @Nullable
    public INamedContainerProvider getMenuProvider(BlockState pState, World pLevel, BlockPos pPos) {
        TileEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity instanceof INamedContainerProvider ? (INamedContainerProvider) blockentity : null;
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BackpackBlockEntity();
    }


    @Override
    public boolean removedByPlayer(BlockState state, World pLevel, BlockPos pPos, PlayerEntity pPlayer, boolean willHarvest, FluidState fluid) {
            if (pLevel.getBlockEntity(pPos) instanceof BackpackBlockEntity blockEntity) {
                ItemStack itemstack = getColoredItemStack(blockEntity.getColor());
                itemstack.setTag(blockEntity.getBackpackTag());
                if (pPlayer.isShiftKeyDown()) {
                    CompoundNBT tag = new CompoundNBT();
                    ItemStackHelper.saveAllItems(tag, BackpackBlockEntity.getInventory(blockEntity));
                    BackpackHolderAttacher.getBackpackHolderUnwrap(itemstack).deserializeNBT(tag, true);
                    if (pPlayer.getItemBySlot(EquipmentSlotType.CHEST).isEmpty()) {
                        if (!pLevel.isClientSide) {
                            pPlayer.setItemSlot(EquipmentSlotType.CHEST, itemstack);
                        }
                    } else {
                        if (pPlayer.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ItemInit.BACKPACK.get() && !pLevel.isClientSide) {
                            pPlayer.sendMessage(new TranslationTextComponent("message.wearablebackpacks.limit"), UUID.randomUUID());
                        } else if(!pLevel.isClientSide) {
                            pPlayer.sendMessage(new TranslationTextComponent("message.wearablebackpacks.chestplate"), UUID.randomUUID());
                        }
                        return false;
                    }
                } else {
                    ItemEntity drop = new ItemEntity(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), itemstack);
                    drop.setDefaultPickUpDelay();
                    pLevel.addFreshEntity(drop);
                    InventoryHelper.dropContents(pLevel,pPos,blockEntity);
                }
            }
        return super.removedByPlayer(state, pLevel, pPos, pPlayer, willHarvest, fluid);
    }

    public static ItemStack getColoredItemStack(@Nullable int pColor) {
        ItemStack stack = new ItemStack(ItemInit.BACKPACK.get());
        ((IDyeableArmorItem) stack.getItem()).setColor(stack, pColor);
        return stack;
    }


}

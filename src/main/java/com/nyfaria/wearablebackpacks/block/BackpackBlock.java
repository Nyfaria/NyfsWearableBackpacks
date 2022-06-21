package com.nyfaria.wearablebackpacks.block;

import com.nyfaria.wearablebackpacks.block.entity.BackpackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BackpackBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final VoxelShape SHAPE_NORTH = makeShape(Direction.NORTH);
    private static final VoxelShape SHAPE_EAST = makeShape(Direction.EAST);
    private static final VoxelShape SHAPE_SOUTH = makeShape(Direction.SOUTH);
    private static final VoxelShape SHAPE_WEST = makeShape(Direction.WEST);

    public BackpackBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        LevelAccessor levelaccessor = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        return this.defaultBlockState()/*.setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER))*/
                .setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide){
            ((BackpackBlockEntity)pLevel.getBlockEntity(pPos)).openMenu(pPlayer);
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
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
        VoxelShape shape = Shapes.empty();
        if (direction == Direction.NORTH) {
            shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.28125, 0.8125, 0.5625, 0.59375), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.25, 0.0625, 0.59375, 0.75, 0.4375, 0.71875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0.5625, 0.28125, 0.8125, 0.75, 0.59375), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.6875, 0.1875, 0.21875, 0.75, 0.6875, 0.28125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.25, 0.1875, 0.21875, 0.3125, 0.6875, 0.28125), BooleanOp.OR);
        } else if (direction == Direction.WEST) {
            shape = Shapes.join(shape, Shapes.box(0.28125, 0, 0.1875, 0.59375, 0.5625, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.59375, 0.0625, 0.25, 0.71875, 0.4375, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.28125, 0.5625, 0.1875, 0.59375, 0.75, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.21875, 0.1875, 0.25, 0.28125, 0.6875, 0.3125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.21875, 0.1875, 0.625, 0.28125, 0.6875, 0.6875), BooleanOp.OR);
        } else if (direction == Direction.EAST) {
            shape = Shapes.join(shape, Shapes.box(0.40625, 0, 0.1875, 0.71875, 0.5625, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.28125, 0.0625, 0.25, 0.40625, 0.4375, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.40625, 0.5625, 0.1875, 0.71875, 0.75, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.71875, 0.1875, 0.25, 0.78125, 0.6875, 0.3125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.71875, 0.1875, 0.625, 0.78125, 0.6875, 0.6875), BooleanOp.OR);
        } else if (direction == Direction.SOUTH) {
            shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.40625, 0.8125, 0.5625, 0.71875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.25, 0.0625, 0.28125, 0.75, 0.4375, 0.40625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0.5625, 0.40625, 0.8125, 0.75, 0.71875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.6875, 0.1875, 0.71875, 0.75, 0.6875, 0.78125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.25, 0.1875, 0.71875, 0.3125, 0.6875, 0.78125), BooleanOp.OR);
        }
        return shape;
    }

    public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
        super.triggerEvent(pState, pLevel, pPos, pId, pParam);
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity == null ? false : blockentity.triggerEvent(pId, pParam);
    }

    @Nullable
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity instanceof MenuProvider ? (MenuProvider) blockentity : null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>) p_152135_ : null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BackpackBlockEntity(pPos, pState);
    }
}
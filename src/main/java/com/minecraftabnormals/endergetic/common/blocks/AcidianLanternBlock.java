package com.minecraftabnormals.endergetic.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndRodBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class AcidianLanternBlock extends EndRodBlock implements IWaterLoggable {
	//DOWN, UP, NORTH, SOUTH, WEST, EAST
	protected static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape[] SHAPES = new VoxelShape[]{
			VoxelShapes.or(Block.box(6.0D, 8.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D)),
			VoxelShapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D), Block.box(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D)),
			VoxelShapes.or(Block.box(6.0D, 6.0D, 8.0D, 10.0D, 10.0D, 16.0D), Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D)),
			VoxelShapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 8.0D), Block.box(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D)),
			VoxelShapes.or(Block.box(8.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.box(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D)),
			VoxelShapes.or(Block.box(0.0D, 6.0D, 6.0D, 8.0D, 10.0D, 10.0D), Block.box(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D)),
	};

	public AcidianLanternBlock(Properties builder) {
		super(builder);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(FACING, Direction.UP));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[state.getValue(FACING).get3DDataValue()];
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
		return super.getStateForPlacement(context).setValue(WATERLOGGED, fluidState.is(FluidTags.WATER) && fluidState.getAmount() >= 8);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return state;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		Direction direction = stateIn.getValue(FACING);
		double offset = direction == Direction.UP ? 0.1D : direction == Direction.DOWN ? 1D : 0.5D;
		double xOffset;
		double zOffset;
		switch (direction) {
			default:
			case NORTH:
				xOffset = 0.5D;
				zOffset = -0.1D;
				break;
			case SOUTH:
				xOffset = 0.5D;
				zOffset = 1.1D;
				break;
			case WEST:
				xOffset = -0.1D;
				zOffset = 0.5D;
				break;
			case EAST:
				xOffset = 1.1D;
				zOffset = 0.5D;
				break;
		}
		if (direction == Direction.UP || direction == Direction.DOWN) {
			xOffset = 0.55D;
			zOffset = 0.55D;
		}
		double d0 = pos.getX() + xOffset - (double) (rand.nextFloat() * 0.1F);
		double d1 = pos.getY() + offset - (double) (rand.nextFloat() * 0.1F);
		double d2 = pos.getZ() + zOffset - (double) (rand.nextFloat() * 0.1F);
		if (rand.nextFloat() <= 0.65F) {
			worldIn.addParticle(ParticleTypes.DRAGON_BREATH, d0, d1 + direction.getStepY(), d2, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
		}
	}
}
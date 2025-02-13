package com.minecraftabnormals.endergetic.common.blocks;

import com.minecraftabnormals.endergetic.common.tileentities.FrisbloomStemTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class FrisbloomStemBlock extends Block {
	//The height position of the current stem; 5 layers total
	public static final IntegerProperty LAYER = IntegerProperty.create("layer", 0, 4);
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

	public FrisbloomStemBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(LAYER, 0)
		);
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPE;
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LAYER);
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!world.isAreaLoaded(pos, 0)) return;
		if (!state.canSurvive(world, pos)) {
			world.destroyBlock(pos, true);
		}
	}

	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState ground = worldIn.getBlockState(pos.below());
		return ground.getBlock() == Blocks.END_STONE || ground.getBlock() == EEBlocks.FRISBLOOM_STEM && !worldIn.getBlockState(pos.above()).getMaterial().isLiquid();
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		worldIn.getBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		return canSurvive(stateIn, worldIn, currentPos) ? worldIn.getBlockState(currentPos) : Blocks.AIR.defaultBlockState();
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos().below();
		if (world.getBlockState(pos).getBlock() == Blocks.END_STONE) {
			return this.defaultBlockState();
		} else if (world.getBlockState(pos).getBlock() == this) {
			return world.getBlockState(pos).setValue(LAYER, world.getBlockState(pos).getValue(LAYER) + 1);
		}
		return null;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new FrisbloomStemTileEntity();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
}
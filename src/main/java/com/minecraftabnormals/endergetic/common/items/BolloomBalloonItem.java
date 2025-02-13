package com.minecraftabnormals.endergetic.common.items;

import com.minecraftabnormals.abnormals_core.core.util.EntityUtil;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BalloonColor;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomKnotEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

import java.util.Optional;
import java.util.function.Predicate;

public class BolloomBalloonItem extends Item {
	private final BalloonColor balloonColor;

	public BolloomBalloonItem(Properties properties, BalloonColor balloonColor) {
		super(properties);
		this.balloonColor = balloonColor;
	}

	public BalloonColor getBalloonColor() {
		return this.balloonColor;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide && hasNoEntityTarget(player) && EntityUtil.rayTrace(player, getPlayerReach(player), 1.0F).getType() == Type.MISS && !player.isShiftKeyDown()) {
			Entity ridingEntity = player.getVehicle();
			boolean isRidingBoat = ridingEntity instanceof BoatEntity;
			if (isRidingBoat && canAttachBalloonToTarget(ridingEntity)) {
				attachToEntity(this.balloonColor, ridingEntity);
				player.swing(hand, true);
				if (!player.isCreative()) stack.shrink(1);
				return ActionResult.consume(stack);
			}

			if (!isRidingBoat && canAttachBalloonToTarget(player)) {
				attachToEntity(this.balloonColor, player);
				player.swing(hand, true);
				if (!player.isCreative()) stack.shrink(1);
				return ActionResult.consume(stack);
			}
		}
		return ActionResult.pass(stack);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		BlockPos pos = context.getClickedPos();
		World world = context.getLevel();
		Block block = world.getBlockState(pos).getBlock();

		if (block instanceof FenceBlock) {
			if (this.isAirUpwards(world, pos)) {
				if (!world.isClientSide) {
					ItemStack stack = context.getItemInHand();
					if (this.attachToFence(pos, world, stack)) {
						stack.shrink(1);
						return ActionResultType.SUCCESS;
					}
				}
			} else {
				return ActionResultType.FAIL;
			}
			for (Entity entity : world.getEntitiesOfClass(Entity.class, new AxisAlignedBB(pos))) {
				if (entity instanceof BolloomKnotEntity) {
					if (((BolloomKnotEntity) entity).hasMaxBalloons()) {
						return ActionResultType.FAIL;
					}
				}
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		World world = player.level;
		if (!world.isClientSide && canAttachBalloonToTarget(target)) {
			player.swing(hand, true);
			attachToEntity(this.balloonColor, target);
			if (!player.isCreative()) stack.shrink(1);
			return ActionResultType.CONSUME;
		}
		return ActionResultType.PASS;
	}

	public static boolean canAttachBalloonToTarget(Entity target) {
		return !EETags.EntityTypes.NOT_BALLOON_ATTACHABLE.contains(target.getType()) && ((BalloonHolder) target).getBalloons().size() < (target instanceof BoatEntity ? 4 : 6);
	}

	public static void attachToEntity(BalloonColor color, Entity target) {
		World world = target.level;
		BolloomBalloonEntity balloon = EEEntities.BOLLOOM_BALLOON.get().create(world);
		if (balloon != null) {
			balloon.setColor(color);
			balloon.attachToEntity(target);
			balloon.updateAttachedPosition();
			balloon.setUntied(true);
			world.addFreshEntity(balloon);
		}
	}

	private boolean attachToFence(BlockPos fencePos, World world, ItemStack stack) {
		BolloomKnotEntity setKnot = BolloomKnotEntity.getKnotForPosition(world, fencePos);
		if (setKnot != null && !setKnot.hasMaxBalloons()) {
			setKnot.addBalloon(this.getBalloonColor());
			return true;
		} else if (setKnot == null) {
			BolloomKnotEntity.createStartingKnot(world, fencePos, this.getBalloonColor());
			return true;
		}
		return false;
	}

	private boolean isAirUpwards(World world, BlockPos pos) {
		BlockPos.Mutable mutable = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
		for (int i = 0; i < 4; i++) {
			if (!world.isEmptyBlock(mutable.move(0, 1, 0))) {
				return false;
			}
		}
		return true;
	}

	public static double getPlayerReach(PlayerEntity player) {
		double reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		return (player.isCreative() ? reach : reach - 0.5F);
	}

	public static boolean hasNoEntityTarget(PlayerEntity player) {
		double distance = getPlayerReach(player);
		Vector3d vec3d = player.getEyePosition(1.0F);
		Vector3d vec3d1 = player.getViewVector(1.0F).scale(distance);
		Vector3d vec3d2 = vec3d.add(vec3d1);
		AxisAlignedBB axisalignedbb = player.getBoundingBox().expandTowards(vec3d1).inflate(1.0D);
		double sqrDistance = distance * distance;
		Predicate<Entity> predicate = (entity) -> !entity.isSpectator() && entity.isPickable();
		EntityRayTraceResult entityraytraceresult = rayTraceEntities(player, vec3d, vec3d2, axisalignedbb, predicate, sqrDistance);
		if (entityraytraceresult == null) {
			return true;
		} else {
			return vec3d.distanceToSqr(entityraytraceresult.getLocation()) > sqrDistance;
		}
	}

	/**
	 * Moved here since {@link ProjectileHelper#rayTraceEntities(Entity, Vector3d, Vector3d, AxisAlignedBB, Predicate, double)} is client only
	 */
	private static EntityRayTraceResult rayTraceEntities(Entity shooter, Vector3d startVec, Vector3d endVec, AxisAlignedBB boundingBox, Predicate<Entity> filter, double distance) {
		World world = shooter.level;
		double d0 = distance;
		Entity entity = null;
		Vector3d vector3d = null;

		for (Entity entity1 : world.getEntities(shooter, boundingBox, filter)) {
			AxisAlignedBB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
			Optional<Vector3d> optional = axisalignedbb.clip(startVec, endVec);
			if (axisalignedbb.contains(startVec)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					vector3d = optional.orElse(startVec);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vector3d vector3d1 = optional.get();
				double d1 = startVec.distanceToSqr(vector3d1);
				if (d1 < d0 || d0 == 0.0D) {
					if (entity1.getRootVehicle() == shooter.getRootVehicle() && !entity1.canRiderInteract()) {
						if (d0 == 0.0D) {
							entity = entity1;
							vector3d = vector3d1;
						}
					} else {
						entity = entity1;
						vector3d = vector3d1;
						d0 = d1;
					}
				}
			}
		}
		return entity == null ? null : new EntityRayTraceResult(entity, vector3d);
	}

	public static class BalloonDispenseBehavior extends DefaultDispenseItemBehavior {

		@Override
		protected ItemStack execute(IBlockSource source, ItemStack stack) {
			BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
			World world = source.getLevel();
			BlockState state = world.getBlockState(blockpos);

			for (Entity entity : world.getEntitiesOfClass(Entity.class, new AxisAlignedBB(blockpos))) {
				if (!world.isClientSide && (entity instanceof LivingEntity || entity instanceof BoatEntity) && canAttachBalloonToTarget(entity)) {
					attachToEntity(((BolloomBalloonItem) stack.getItem()).getBalloonColor(), entity);
					stack.shrink(1);
					return stack;
				}
			}

			if (state.getMaterial().isReplaceable()) {
				BolloomBalloonEntity balloon = new BolloomBalloonEntity(world, blockpos);
				balloon.setColor(((BolloomBalloonItem) stack.getItem()).getBalloonColor());
				world.addFreshEntity(balloon);
				stack.shrink(1);
			} else if (!state.getMaterial().isReplaceable() && !state.getBlock().is(BlockTags.FENCES)) {
				return super.execute(source, stack);
			} else if (state.getBlock().is(BlockTags.FENCES)) {
				BolloomKnotEntity setKnot = BolloomKnotEntity.getKnotForPosition(world, blockpos);
				if (setKnot == null) {
					BolloomKnotEntity.createStartingKnot(world, blockpos, ((BolloomBalloonItem) stack.getItem()).getBalloonColor());
					stack.shrink(1);
					return stack;
				} else {
					if (!setKnot.hasMaxBalloons()) {
						setKnot.addBalloon(((BolloomBalloonItem) stack.getItem()).getBalloonColor());
						stack.shrink(1);
					} else {
						return super.execute(source, stack);
					}
				}
			} else {
				return super.execute(source, stack);
			}
			return stack;
		}

	}
}
package com.minecraftabnormals.endergetic.api.entity.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class RayTraceHelper {

	public static RayTraceResult rayTrace(Entity entity, double distance, float delta) {
		return entity.level.clip(new RayTraceContext(
				entity.getEyePosition(delta),
				entity.getEyePosition(delta).add(entity.getViewVector(delta).scale(distance)),
				RayTraceContext.BlockMode.COLLIDER,
				RayTraceContext.FluidMode.NONE,
				entity
		));
	}

	public static RayTraceResult rayTraceWithCustomDirection(Entity entity, float pitch, float yaw, double distance, float delta) {
		return entity.level.clip(new RayTraceContext(
				entity.getEyePosition(delta),
				entity.getEyePosition(delta).add(getVectorForRotation(pitch, yaw).scale(distance)),
				RayTraceContext.BlockMode.COLLIDER,
				RayTraceContext.FluidMode.NONE,
				entity
		));
	}

	public static EntityRayTraceResult rayTraceEntityResult(Entity entity, float pitch, float yaw, double distance, double sqDistance, float delta) {
		Vector3d look = getVectorForRotation(pitch, yaw);
		Vector3d endVec = entity.getEyePosition(delta).add(look.scale(distance));
		AxisAlignedBB axisalignedbb = entity.getBoundingBox().expandTowards(look.scale(distance)).inflate(1.0D, 1.0D, 1.0D);
		EntityRayTraceResult entityRaytraceResult = getEntityHitResult(entity, entity.getEyePosition(delta), endVec, axisalignedbb, (result) -> {
			return !result.isSpectator() && result.isPickable();
		}, sqDistance);
		return entityRaytraceResult;
	}

	public static final Vector3d getVectorForRotation(float pitch, float yaw) {
		float f = pitch * ((float) Math.PI / 180F);
		float f1 = -yaw * ((float) Math.PI / 180F);
		float f2 = MathHelper.cos(f1);
		float f3 = MathHelper.sin(f1);
		float f4 = MathHelper.cos(f);
		float f5 = MathHelper.sin(f);
		return new Vector3d((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
	}

	/**
	 * Copied from Vanilla
	 * It's a raytracing method, but Vanilla's is client only
	 */
	@Nullable
	public static EntityRayTraceResult getEntityHitResult(Entity p_221273_0_, Vector3d p_221273_1_, Vector3d p_221273_2_, AxisAlignedBB p_221273_3_, Predicate<Entity> p_221273_4_, double p_221273_5_) {
		World world = p_221273_0_.level;
		double d0 = p_221273_5_;
		Entity entity = null;
		Vector3d Vector3d = null;

		for (Entity entity1 : world.getEntities(p_221273_0_, p_221273_3_, p_221273_4_)) {
			AxisAlignedBB axisalignedbb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius());
			Optional<Vector3d> optional = axisalignedbb.clip(p_221273_1_, p_221273_2_);
			if (axisalignedbb.contains(p_221273_1_)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					Vector3d = optional.orElse(p_221273_1_);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vector3d Vector3d1 = optional.get();
				double d1 = p_221273_1_.distanceToSqr(Vector3d1);
				if (d1 < d0 || d0 == 0.0D) {
					if (entity1.getRootVehicle() == p_221273_0_.getRootVehicle()) {
						if (d0 == 0.0D) {
							entity = entity1;
							Vector3d = Vector3d1;
						}
					} else {
						entity = entity1;
						Vector3d = Vector3d1;
						d0 = d1;
					}
				}
			}
		}

		if (entity == null) {
			return null;
		} else {
			return new EntityRayTraceResult(entity, Vector3d);
		}
	}
}
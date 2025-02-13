package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;

import java.util.EnumSet;

public class BoofloHuntPuffBugGoal extends Goal {
	private static final float SPEED = 1.0F;
	private BoofloEntity booflo;
	private Path path;
	private int delayCounter;
	private double targetX, targetY, targetZ;

	public BoofloHuntPuffBugGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (!(this.booflo.isBoofed() && !this.booflo.isPregnant() && this.booflo.getBoofloAttackTarget() instanceof PuffBugEntity && this.booflo.getBoofloAttackTarget().isAlive() && !this.booflo.hasCaughtPuffBug())) {
			return false;
		}
		this.path = this.booflo.getNavigation().createPath(this.booflo.getBoofloAttackTarget(), 0);
		if (this.path != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		Entity target = this.booflo.getBoofloAttackTarget();
		return this.booflo.getPassengers().isEmpty() && !this.booflo.hasCaughtPuffBug() && this.booflo.isBoofed() && !this.booflo.isPregnant() && target != null && target.isAlive() && target instanceof PuffBugEntity;
	}

	@Override
	public void start() {
		this.booflo.getNavigation().moveTo(this.path, SPEED);
		this.booflo.setAggressive(true);
		this.delayCounter = 0;

		if (this.booflo.hasCaughtFruit()) {
			this.booflo.setCaughtFruit(false);
			this.booflo.spawnAtLocation(EEItems.BOLLOOM_FRUIT.get());
		}
	}

	@Override
	public void tick() {
		PuffBugEntity target = (PuffBugEntity) this.booflo.getBoofloAttackTarget();

		double distToEnemySqr = this.booflo.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());

		this.delayCounter--;

		if (this.delayCounter <= 0 || target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.booflo.getRandom().nextFloat() < 0.05F) {
			this.booflo.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ(), 10.0F, 10.0F);

			this.delayCounter = 4 + this.booflo.getRandom().nextInt(7);

			if (distToEnemySqr > 256.0D) {
				this.delayCounter += 5;
			}

			if (!this.booflo.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), SPEED)) {
				this.delayCounter += 5;
			}
		}

		if (this.booflo.getPassengers().isEmpty() && this.booflo.getRandom().nextFloat() < 0.1F) {
			this.tryToCatch(target, distToEnemySqr);
		}
	}

	@Override
	public void stop() {
		this.booflo.setAggressive(false);
		this.booflo.getNavigation().stop();
	}

	protected void tryToCatch(PuffBugEntity enemy, double distToEnemySqr) {
		double attackRange = (this.booflo.getBbWidth() * 2.0F * this.booflo.getBbWidth() * 2.0F + enemy.getBbWidth()) * 0.75F;
		if (distToEnemySqr <= attackRange) {
			this.booflo.catchPuffBug(enemy);
		}
	}
}
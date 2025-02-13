package com.minecraftabnormals.endergetic.common.entities.booflo;

import com.minecraftabnormals.abnormals_core.core.api.IAgeableEntity;
import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedEntity;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.minecraftabnormals.endergetic.api.entity.util.EntityItemStackHelper;
import com.minecraftabnormals.endergetic.common.blocks.CorrockBlock.DimensionTypeAccessor;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.ai.AdolescentAttackGoal;
import com.minecraftabnormals.endergetic.common.entities.booflo.ai.AdolescentEatGoal;
import com.minecraftabnormals.endergetic.common.entities.booflo.ai.BoofloNearestAttackableTargetGoal;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class BoofloAdolescentEntity extends EndimatedEntity implements IAgeableEntity {
	private static final DataParameter<Boolean> MOVING = EntityDataManager.defineId(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HAS_FRUIT = EntityDataManager.defineId(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> DESCENTING = EntityDataManager.defineId(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> EATING = EntityDataManager.defineId(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HUNGRY = EntityDataManager.defineId(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> EATEN = EntityDataManager.defineId(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> WANTS_TO_GROW = EntityDataManager.defineId(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Float> FALL_SPEED = EntityDataManager.defineId(BoofloAdolescentEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> BOOF_BOOST_COOLDOWN = EntityDataManager.defineId(BoofloAdolescentEntity.class, DataSerializers.INT);
	public static final Endimation BOOF_ANIMATION = new Endimation(10);
	public static final Endimation EATING_ANIMATION = new Endimation(10);
	private Entity boofloAttackTarget;
	public int growingAge;
	public int forcedAge;
	public int forcedAgeTimer;
	private float prevTailAnimation;
	private float tailAnimation;
	private float tailSpeed;
	private float prevSwimmingAnimation;
	private float swimmingAnimation;
	private float swimmingAnimationSpeed;
	public boolean wasBred;

	public BoofloAdolescentEntity(EntityType<? extends BoofloAdolescentEntity> type, World worldIn) {
		super(type, worldIn);
		this.moveControl = new BoofloAdolescentEntity.BoofloAdolescentMoveController(this);
		this.tailAnimation = this.random.nextFloat();
		this.prevTailAnimation = this.tailAnimation;
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0F)
				.add(Attributes.MOVEMENT_SPEED, 1.7F)
				.add(Attributes.FOLLOW_RANGE, 25.0F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(MOVING, false);
		this.entityData.define(HAS_FRUIT, false);
		this.entityData.define(DESCENTING, false);
		this.entityData.define(EATING, false);
		this.entityData.define(HUNGRY, true);
		this.entityData.define(EATEN, false);
		this.entityData.define(WANTS_TO_GROW, false);
		this.entityData.define(FALL_SPEED, 0.0F);
		this.entityData.define(BOOF_BOOST_COOLDOWN, 0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new AdolescentEatGoal(this));
		this.goalSelector.addGoal(4, new AdolescentAttackGoal(this, 1.1D, true));
		this.goalSelector.addGoal(6, new BoofloAdolescentEntity.RandomFlyingGoal(this, 1.1D, 5));

		this.targetSelector.addGoal(4, new BoofloNearestAttackableTargetGoal<>(this, BolloomFruitEntity.class, true));
	}

	@Override
	public void travel(Vector3d vec3d) {
		if (this.isEffectiveAi() && !this.isInWater()) {
			this.moveRelative(0.015F, vec3d);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
			this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.005D * this.getFallSpeed(), 0.0D));
		} else {
			super.travel(vec3d);
		}
	}

	@Override
	protected PathNavigator createNavigation(World worldIn) {
		return new EndergeticFlyingPathNavigator(this, worldIn) {

			@Override
			public boolean isStableDestination(BlockPos pos) {
				return !this.mob.isOnGround();
			}

		};
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.65F;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.isEndimationPlaying(BoofloAdolescentEntity.EATING_ANIMATION) && this.getAnimationTick() == 9) {
			if (this.level instanceof ServerWorld) {
				((ServerWorld) this.level).sendParticles(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(EEItems.BOLLOOM_FRUIT.get())), this.getX(), this.getY() + (double) this.getBbHeight() / 1.5D, this.getZ(), 10, (double) (this.getBbWidth() / 4.0F), (double) (this.getBbHeight() / 4.0F), (double) (this.getBbWidth() / 4.0F), 0.05D);
			}
			this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.getVehicle() == null) this.setFallSpeed(this.getFallSpeed() + 0.1F);

		if (this.getRandom().nextInt(50000) < 10 && !this.isHungry() && !this.hasFruit()) {
			this.setHungry(true);
		}

		if (this.level.isClientSide) {
			this.prevTailAnimation = this.tailAnimation;
			this.prevSwimmingAnimation = this.swimmingAnimation;
			if (!this.isInWater()) {
				this.tailSpeed = 1.0F;
				this.swimmingAnimationSpeed = 1.0F;
			} else if (this.isMoving()) {
				if (this.tailSpeed < 0.5F) {
					this.tailSpeed = 1.0F;
				} else {
					this.tailSpeed += (0.25F - this.tailSpeed) * 0.1F;
				}
			} else {
				this.tailSpeed += (0.1875F - this.tailSpeed) * 0.1F;
			}
			if (this.getFallSpeed() > 0.0F) {
				if (this.swimmingAnimationSpeed < 0.5F) {
					this.swimmingAnimationSpeed = 1.0F;
				} else {
					this.swimmingAnimationSpeed += ((this.swimmingAnimationSpeed * 2.5F) - this.getFallSpeed()) * 0.1F;
				}
			} else {
				this.swimmingAnimationSpeed = 1.0F;
			}
			this.tailAnimation += this.tailSpeed;
			this.swimmingAnimation += this.swimmingAnimationSpeed;
		}

		if (this.getBoofBoostCooldown() > 0) {
			this.setBoofBoostCooldown(this.getBoofBoostCooldown() - 1);
		}

		if ((this.onGround || this.isPassenger()) && this.doesWantToGrow() && this.level.noCollision(this.getBoundingBox().inflate(2.0F, 0.0F, 2.0F))) {
			this.growUp();
		}

		if (!this.isWorldRemote() && ((!this.isDescenting() && !this.isEating()) && this.getBoofBoostCooldown() <= 0 && (this.onGround || this.isEyeInFluid(FluidTags.WATER)))) {
			this.push(-MathHelper.sin((float) (this.yRot * Math.PI / 180.0F)) * (5 * (random.nextFloat() + 0.1F)) * 0.1F, (random.nextFloat() * 0.45F) + 0.65F, MathHelper.cos((float) (this.yRot * Math.PI / 180.0F)) * (5 * (random.nextFloat() + 0.1F)) * 0.1F);
			this.setPlayingEndimation(BOOF_ANIMATION);
			NetworkUtil.setPlayingAnimationMessage(this, BOOF_ANIMATION);
			this.setFallSpeed(0.0F);
			//Fixes super boosting underwater
			if (this.wasEyeInWater) {
				this.setBoofBoostCooldown(20);
			}
		}

		//Helps them not fall off the edge
		if ((this.getBoofBoostCooldown() <= 0 && !this.onGround) && this.level.dimensionType() == DimensionTypeAccessor.THE_END && !this.isSafePos(this.blockPosition(), 3)) {
			this.setBoofBoostCooldown(20);
			this.setFallSpeed(0.0F);

			if (this.getY() <= 50) {
				this.push(-MathHelper.sin((float) (this.yRot * Math.PI / 180.0F)) * (5 * (random.nextFloat() + 0.1F)) * 0.1F, (random.nextFloat() * 0.45F) + 0.65F, MathHelper.cos((float) (this.yRot * Math.PI / 180.0F)) * (5 * (random.nextFloat() + 0.1F)) * 0.1F);
				this.setPlayingEndimation(BOOF_ANIMATION);
			}
		}

		if (!this.onGround && this.level.dimensionType() == DimensionTypeAccessor.THE_END && !this.isSafePos(this.blockPosition(), 3) && !this.isWorldRemote()) {
			this.push(-MathHelper.sin((float) (this.yRot * Math.PI / 180.0F)) * 0.01F, 0, MathHelper.cos((float) (this.yRot * Math.PI / 180.0F)) * 0.01F);
		}

		if (this.getBoofloAttackTarget() != null && this.canSee(this.getBoofloAttackTarget())) {
			this.yRot = this.yHeadRot;
		}

		if (this.level.isClientSide) {
			if (this.forcedAgeTimer > 0) {
				if (this.forcedAgeTimer % 4 == 0) {
					this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(), this.getY() + 0.5D + (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(), 0.0D, 0.0D, 0.0D);
				}

				this.forcedAgeTimer--;
			}
		} else if (this.isAlive() && this.hasEaten()) {
			int growingAge = this.getGrowingAge();
			if (growingAge < 0) {
				growingAge++;
				this.setGrowingAge(growingAge);
			} else if (growingAge > 0) {
				growingAge--;
				this.setGrowingAge(growingAge);
			}
		}
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.BOOFLO_SPAWN_EGG.get());
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public void die(DamageSource cause) {
		if (this.hasFruit() && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
			this.dropFruit();
		}
		super.die(cause);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("Moving", this.isMoving());
		compound.putBoolean("HasFruit", this.hasFruit());
		compound.putBoolean("Descenting", this.isDescenting());
		compound.putBoolean("IsEating", this.isEating());
		compound.putBoolean("IsHungry", this.isHungry());
		compound.putBoolean("HasEaten", this.hasEaten());
		compound.putBoolean("WantsToGrow", this.doesWantToGrow());
		compound.putFloat("FallSpeed", this.getFallSpeed());
		compound.putInt("BoofBoostCooldown", this.getBoofBoostCooldown());
		compound.putInt("Age", this.getGrowingAge());
		compound.putInt("ForcedAge", this.forcedAge);
		compound.putBoolean("WasBred", this.wasBred);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.setMoving(compound.getBoolean("Moving"));
		this.setHasFruit(compound.getBoolean("HasFruit"));
		this.setDescenting(compound.getBoolean("Descenting"));
		this.setEating(compound.getBoolean("IsEating"));
		this.setHungry(compound.getBoolean("IsHungry"));
		this.setEaten(compound.getBoolean("HasEaten"));
		this.setWantsToGrow(compound.getBoolean("WantsToGrow"));
		this.setFallSpeed(compound.getFloat("FallSpeed"));
		this.setBoofBoostCooldown(compound.getInt("BoofBoostCooldown"));
		this.setGrowingAge(compound.getInt("Age"));
		this.forcedAge = compound.getInt("ForcedAge");
		this.wasBred = compound.getBoolean("WasBred");
	}

	public boolean isMoving() {
		return this.entityData.get(MOVING);
	}

	public void setMoving(boolean moving) {
		this.entityData.set(MOVING, moving);
	}

	public boolean hasFruit() {
		return this.entityData.get(HAS_FRUIT);
	}

	public void setHasFruit(boolean hasFruit) {
		this.entityData.set(HAS_FRUIT, hasFruit);
	}

	public boolean isDescenting() {
		return this.entityData.get(DESCENTING);
	}

	public void setDescenting(boolean descenting) {
		this.entityData.set(DESCENTING, descenting);
	}

	public boolean isEating() {
		return this.entityData.get(EATING);
	}

	public void setEating(boolean eating) {
		this.entityData.set(EATING, eating);
	}

	public boolean isHungry() {
		return this.entityData.get(HUNGRY);
	}

	public void setHungry(boolean hungry) {
		this.entityData.set(HUNGRY, hungry);
	}

	public boolean hasEaten() {
		return this.entityData.get(EATEN);
	}

	public void setEaten(boolean eaten) {
		this.entityData.set(EATEN, eaten);
	}

	public boolean doesWantToGrow() {
		return this.entityData.get(WANTS_TO_GROW);
	}

	public void setWantsToGrow(boolean wantsToGrow) {
		this.entityData.set(WANTS_TO_GROW, wantsToGrow);
	}

	public float getFallSpeed() {
		return this.entityData.get(FALL_SPEED);
	}

	public void setFallSpeed(float speed) {
		this.entityData.set(FALL_SPEED, speed);
	}

	public int getBoofBoostCooldown() {
		return this.entityData.get(BOOF_BOOST_COOLDOWN);
	}

	public void setBoofBoostCooldown(int ticks) {
		this.entityData.set(BOOF_BOOST_COOLDOWN, ticks);
	}

	@Nullable
	public Entity getBoofloAttackTarget() {
		return this.boofloAttackTarget;
	}

	public void setBoofloAttackTarget(@Nullable Entity entity) {
		this.boofloAttackTarget = entity;
	}

	public int getGrowingAge() {
		if (this.level.isClientSide) {
			return -1;
		} else {
			return this.growingAge;
		}
	}

	public void setGrowingAge(int age) {
		int oldAge = this.growingAge;
		this.growingAge = age;
		if ((oldAge < 0 && age >= 0 || oldAge > 0 && age < 0) && this.hasEaten()) {
			this.setWantsToGrow(true);
		}
	}

	public void addGrowth(int growth) {
		this.ageUp(growth, false);
	}

	public void ageUp(int growthSeconds, boolean updateForcedAge) {
		int growingAge = this.getGrowingAge();
		int j = growingAge;
		growingAge += growthSeconds * 20;
		if (growingAge > 0) {
			growingAge = 0;
		}

		int k = growingAge - j;

		this.setGrowingAge(growingAge);
		if (updateForcedAge) {
			this.forcedAge += k;
			if (this.forcedAgeTimer == 0) {
				this.forcedAgeTimer = 40;
			}
		}

		if (this.getGrowingAge() == 0) {
			this.setGrowingAge(this.forcedAge);
		}
	}

	public LivingEntity growUp() {
		if (this.isAlive()) {
			this.spawnAtLocation(EEItems.BOOFLO_HIDE.get(), 1);

			BoofloEntity booflo = EEEntities.BOOFLO.get().create(this.level);
			booflo.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);

			if (this.hasCustomName()) {
				booflo.setCustomName(this.getCustomName());
				booflo.setCustomNameVisible(this.isCustomNameVisible());
			}

			if (this.isLeashed()) {
				booflo.setLeashedTo(this.getLeashHolder(), true);
				this.dropLeash(true, false);
			}

			if (this.getVehicle() != null) {
				booflo.startRiding(this.getVehicle());
			}

			booflo.wasBred = this.wasBred;
			booflo.setHealth(booflo.getMaxHealth());
			this.level.addFreshEntity(booflo);
			this.remove();

			return booflo;
		}
		return this;
	}

	public LivingEntity growDown() {
		if (this.isAlive()) {
			BoofloBabyEntity boofloBaby = EEEntities.BOOFLO_BABY.get().create(this.level);
			boofloBaby.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);

			if (this.hasCustomName()) {
				boofloBaby.setCustomName(this.getCustomName());
				boofloBaby.setCustomNameVisible(this.isCustomNameVisible());
			}

			if (this.isLeashed()) {
				boofloBaby.setLeashedTo(this.getLeashHolder(), true);
				this.dropLeash(true, false);
			}

			if (this.getVehicle() != null) {
				boofloBaby.startRiding(this.getVehicle());
			}

			boofloBaby.wasBred = this.wasBred;
			boofloBaby.setHealth(boofloBaby.getMaxHealth());
			this.level.addFreshEntity(boofloBaby);
			this.remove();

			return boofloBaby;
		}
		return this;
	}

	@OnlyIn(Dist.CLIENT)
	public float getTailAnimation(float ptc) {
		return MathHelper.lerp(ptc, this.prevTailAnimation, this.tailAnimation);
	}

	@OnlyIn(Dist.CLIENT)
	public float getSwimmingAnimation(float ptc) {
		return MathHelper.lerp(ptc, this.prevSwimmingAnimation, this.swimmingAnimation);
	}

	public boolean isSafePos(BlockPos pos, int muliplier) {
		BlockPos newPos = pos;
		for (int y = 0; y < 10 * muliplier; y++) {
			newPos = newPos.below(y);
			BlockState state = this.level.getBlockState(newPos);
			if (state.canOcclude() || (!state.getFluidState().isEmpty() && !state.getFluidState().is(FluidTags.LAVA))) {
				return true;
			}
		}
		return false;
	}

	public void dropFruit() {
		if (!this.isWorldRemote()) {
			this.spawnAtLocation(EEItems.BOLLOOM_FRUIT.get());
			this.setHasFruit(false);
		}
	}

	public boolean isPlayerNear() {
		return this.level.getEntitiesOfClass(PlayerEntity.class, this.getBoundingBox().inflate(2.0F), BoofloEntity.IS_SCARED_BY).size() > 0;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source == DamageSource.IN_WALL || source == DamageSource.FLY_INTO_WALL || super.isInvulnerableTo(source);
	}

	@Override
	protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();

		if (item instanceof SpawnEggItem && ((SpawnEggItem) item).spawnsEntity(itemstack.getTag(), EEEntities.BOOFLO.get())) {
			if (!this.level.isClientSide) {
				BoofloBabyEntity baby = EEEntities.BOOFLO_BABY.get().create(this.level);
				baby.setGrowingAge(-24000);
				baby.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
				this.level.addFreshEntity(baby);
				if (itemstack.hasCustomHoverName()) {
					baby.setCustomName(itemstack.getHoverName());
				}

				EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			}
			return ActionResultType.PASS;
		} else if (item == EEItems.BOLLOOM_FRUIT.get()) {
			EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			this.ageUp((int) ((-this.getGrowingAge() / 20) * 0.1F), true);
			this.setEaten(true);
			return ActionResultType.sidedSuccess(this.level.isClientSide);
		}
		return ActionResultType.PASS;
	}

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		this.setGrowingAge(-24000);
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public Endimation[] getEndimations() {
		return new Endimation[]{
				BOOF_ANIMATION,
				EATING_ANIMATION
		};
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return !this.wasBred;
	}

	@Override
	public boolean hasGrowthProgress() {
		return true;
	}

	@Override
	public void resetGrowthProgress() {
		this.setGrowingAge(-24000);
	}

	@Override
	public boolean canAge(boolean isGrowing) {
		return true;
	}

	@Override
	public LivingEntity attemptAging(boolean isGrowing) {
		return isGrowing ? this.growUp() : this.growDown();
	}

	static class RandomFlyingGoal extends RandomWalkingGoal {

		public RandomFlyingGoal(CreatureEntity booflo, double speed, int chance) {
			super(booflo, speed, chance);
		}

		@Nullable
		protected Vector3d getPosition() {
			Vector3d vec3d = RandomPositionGenerator.getPos(this.mob, 10, 0);

			for (int i = 0; vec3d != null && !this.mob.level.getBlockState(new BlockPos(vec3d)).isPathfindable(this.mob.level, new BlockPos(vec3d), PathType.AIR) && i++ < 10; vec3d = RandomPositionGenerator.getPos(this.mob, 10, 0)) {
				;
			}

			return vec3d;
		}

		@Override
		public boolean canUse() {
			return super.canUse() && !this.mob.isInWater();
		}

		@Override
		public boolean canContinueToUse() {
			return super.canContinueToUse() && !this.mob.isInWater();
		}

	}

	static class BoofloAdolescentMoveController extends MovementController {
		private final BoofloAdolescentEntity booflo;

		BoofloAdolescentMoveController(BoofloAdolescentEntity booflo) {
			super(booflo);
			this.booflo = booflo;
		}

		public void tick() {
			if (this.operation == MovementController.Action.MOVE_TO && !this.booflo.getNavigation().isDone()) {
				Vector3d vec3d = this.booflo.getMoveControllerPathDistance(this.wantedX, this.wantedY, this.wantedZ);

				this.booflo.yRot = this.rotlerp(this.booflo.yRot, this.booflo.getTargetAngleForPathDistance(vec3d), 10.0F);
				this.booflo.yBodyRot = this.booflo.yRot;
				this.booflo.yHeadRot = this.booflo.yRot;

				float f1 = (float) (2 * this.booflo.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
				float f2 = MathHelper.lerp(0.125F, this.booflo.getSpeed(), f1);

				this.booflo.setSpeed(f2);

				this.booflo.setMoving(true);
			} else {
				this.booflo.setMoving(false);
			}
		}
	}
}

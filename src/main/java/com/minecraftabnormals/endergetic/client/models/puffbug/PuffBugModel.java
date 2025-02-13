package com.minecraftabnormals.endergetic.client.models.puffbug;

import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorEntityModel;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorModelRenderer;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;

/**
 * ModelPuffBugInflated - Endergized
 * Created using Tabula 7.0.0
 */
public class PuffBugModel<E extends PuffBugEntity> extends EndimatorEntityModel<E> {
	public EndimatorModelRenderer Body;
	public EndimatorModelRenderer Stinger;
	public EndimatorModelRenderer Neck;
	public EndimatorModelRenderer Head;
	public EndimatorModelRenderer Sensor1;
	public EndimatorModelRenderer Sensor2;

	public EndimatorModelRenderer BodyDeflated;
	public EndimatorModelRenderer StingerDeflated;
	public EndimatorModelRenderer NeckDeflated;
	public EndimatorModelRenderer HeadDeflated;
	public EndimatorModelRenderer Sensor1Deflated;
	public EndimatorModelRenderer Sensor2Deflated;

	public EndimatorModelRenderer BodyDeflatedProjectile;
	public EndimatorModelRenderer StingerDeflatedProjectile;
	public EndimatorModelRenderer NeckDeflatedProjectile;
	public EndimatorModelRenderer HeadDeflatedProjectile;
	public EndimatorModelRenderer Sensor1DeflatedProjectile;
	public EndimatorModelRenderer Sensor2DeflatedProjectile;

	public PuffBugModel() {
		this.texWidth = 32;
		this.texHeight = 16;
		this.Head = new EndimatorModelRenderer(this, 0, 0);
		this.Head.setPos(0.0F, 3.7F, 0.0F);
		this.Head.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
		this.setRotateAngle(Head, -0.136659280431156F, 0.0F, 0.0F);
		this.Stinger = new EndimatorModelRenderer(this, 26, 0);
		this.Stinger.setPos(-0.5F, -3.5F, 0.0F);
		this.Stinger.addBox(0.0F, -4.0F, 0.0F, 1, 4, 0, 0.0F);
		this.Body = new EndimatorModelRenderer(this, 8, 3);
		this.Body.setPos(0.0F, 12.0F, 0.0F);
		this.Body.addBox(-3.0F, -3.5F, -3.0F, 6, 7, 6, 0.0F);
		this.setRotateAngle(Body, 0.0F, 0.045553093477052F, 0.0F);
		this.Sensor1 = new EndimatorModelRenderer(this, 30, 0);
		this.Sensor1.setPos(-2.0F, 3.0F, 0.5F);
		this.Sensor1.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
		this.setRotateAngle(Sensor1, 0.0F, 1.5707963267948966F, 0.7330382858376184F);
		this.Neck = new EndimatorModelRenderer(this, 0, 6);
		this.Neck.setPos(0.0F, 2.5F, 0.0F);
		this.Neck.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(Neck, -0.136659280431156F, 0.0F, 0.0F);
		this.Sensor2 = new EndimatorModelRenderer(this, 30, 0);
		this.Sensor2.setPos(2.0F, 3.0F, 0.5F);
		this.Sensor2.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
		this.setRotateAngle(Sensor2, 0.7330382858376184F, 1.5707963267948966F, 0.0F);
		this.Neck.addChild(this.Head);
		this.Body.addChild(this.Stinger);
		this.Head.addChild(this.Sensor1);
		this.Body.addChild(this.Neck);
		this.Head.addChild(this.Sensor2);

		this.BodyDeflated = new EndimatorModelRenderer(this, 10, 7);
		this.BodyDeflated.setPos(0.0F, 11.0F, 0.0F);
		this.BodyDeflated.addBox(-1.0F, -3.0F, -1.0F, 2, 6, 2, 0.0F);
		this.Sensor1Deflated = new EndimatorModelRenderer(this, 18, 1);
		this.Sensor1Deflated.setPos(-2.0F, 3.0F, 0.5F);
		this.Sensor1Deflated.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
		this.setRotateAngle(Sensor1Deflated, 0.0F, 1.5707963267948966F, 0.7330382858376184F);
		this.HeadDeflated = new EndimatorModelRenderer(this, 0, 0);
		this.HeadDeflated.setPos(0.0F, 3.7F, 0.0F);
		this.HeadDeflated.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
		this.setRotateAngle(HeadDeflated, -0.13962634015954636F, 0.0F, 0.0F);
		this.Sensor2Deflated = new EndimatorModelRenderer(this, 18, 1);
		this.Sensor2Deflated.setPos(2.0F, 3.0F, 0.5F);
		this.Sensor2Deflated.addBox(-0.1F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
		this.setRotateAngle(Sensor2Deflated, 0.7330382858376184F, 1.5707963267948966F, 0.0F);
		this.StingerDeflated = new EndimatorModelRenderer(this, 15, 1);
		this.StingerDeflated.setPos(0.0F, -3.0F, 0.0F);
		this.StingerDeflated.addBox(-0.5F, -4.0F, 0.0F, 1, 4, 0, 0.0F);
		this.NeckDeflated = new EndimatorModelRenderer(this, 0, 6);
		this.NeckDeflated.setPos(0.0F, 3.0F, 0.0F);
		this.NeckDeflated.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(NeckDeflated, -0.13962634015954636F, 0.0F, 0.0F);
		this.HeadDeflated.addChild(this.Sensor1Deflated);
		this.NeckDeflated.addChild(this.HeadDeflated);
		this.HeadDeflated.addChild(this.Sensor2Deflated);
		this.BodyDeflated.addChild(this.StingerDeflated);
		this.BodyDeflated.addChild(this.NeckDeflated);

		this.NeckDeflatedProjectile = new EndimatorModelRenderer(this, 0, 6);
		this.NeckDeflatedProjectile.setPos(0.0F, 6.5F, 0.0F);
		this.NeckDeflatedProjectile.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(NeckDeflatedProjectile, -0.136659280431156F, 0.0F, 0.0F);
		this.BodyDeflatedProjectile = new EndimatorModelRenderer(this, 10, 7);
		this.BodyDeflatedProjectile.setPos(0.0F, 23.0F, 0.0F);
		this.BodyDeflatedProjectile.addBox(-1.0F, 0.5F, -1.0F, 2, 6, 2, 0.0F);
		this.HeadDeflatedProjectile = new EndimatorModelRenderer(this, 0, 0);
		this.HeadDeflatedProjectile.setPos(0.0F, 3.7F, 0.0F);
		this.HeadDeflatedProjectile.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
		this.setRotateAngle(HeadDeflatedProjectile, -0.13962634015954636F, 0.0F, 0.0F);
		this.Sensor2DeflatedProjectile = new EndimatorModelRenderer(this, 18, 1);
		this.Sensor2DeflatedProjectile.setPos(2.0F, 3.0F, 0.5F);
		this.Sensor2DeflatedProjectile.addBox(-0.1F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
		this.setRotateAngle(Sensor2DeflatedProjectile, 0.7330382858376184F, 1.5707963267948966F, 0.08726646259971647F);
		this.StingerDeflatedProjectile = new EndimatorModelRenderer(this, 15, 1);
		this.StingerDeflatedProjectile.setPos(0.0F, 0.5F, 0.0F);
		this.StingerDeflatedProjectile.addBox(-0.5F, -4.0F, 0.0F, 1, 4, 0, 0.0F);
		this.Sensor1DeflatedProjectile = new EndimatorModelRenderer(this, 18, 1);
		this.Sensor1DeflatedProjectile.setPos(-2.0F, 3.0F, 0.5F);
		this.Sensor1DeflatedProjectile.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
		this.setRotateAngle(Sensor1DeflatedProjectile, 0.0F, 1.5707963267948966F, 0.7330382858376184F);
		this.BodyDeflatedProjectile.addChild(this.NeckDeflatedProjectile);
		this.NeckDeflatedProjectile.addChild(this.HeadDeflatedProjectile);
		this.HeadDeflatedProjectile.addChild(this.Sensor2DeflatedProjectile);
		this.BodyDeflatedProjectile.addChild(this.StingerDeflatedProjectile);
		this.HeadDeflatedProjectile.addChild(this.Sensor1DeflatedProjectile);

		this.setDefaultBoxValues();
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		Entity ridingEntity = this.entity.getVehicle();
		if (ridingEntity instanceof BoofloEntity && !(((BoofloEntity) ridingEntity).isEndimationPlaying(BoofloEntity.EAT) && ((BoofloEntity) ridingEntity).getAnimationTick() >= 20)) {
			return;
		}

		if (!this.entity.isInflated() && this.entity.stuckInBlock) {
			packedLightIn = this.getPackedLightForStuck(this.entity);
		}

		this.animateModel(this.entity);
		if (this.entity.isInflated()) {
			this.Body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		} else {
			if (this.entity.isProjectile()) {
				this.BodyDeflatedProjectile.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			} else {
				this.BodyDeflated.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			}
		}
	}

	private int getPackedLightForStuck(PuffBugEntity puffbug) {
		float partialTicks = ClientInfo.getPartialTicks();
		return LightTexture.pack(puffbug.isOnFire() ? 15 : puffbug.level.getBrightness(LightType.BLOCK, this.getStuckLightPos(puffbug, partialTicks)), this.entity.level.getBrightness(LightType.SKY, this.getStuckLightPos(puffbug, partialTicks)));
	}

	private BlockPos getStuckLightPos(PuffBugEntity puffbug, float partialTicks) {
		BlockPos blockpos = new BlockPos(puffbug.getX(), puffbug.getY() + (double) puffbug.getEyeHeight(), puffbug.getX());
		boolean rotationFlag = true;
		float[] rotations = puffbug.getRotationController().getRotations(partialTicks);
		Direction horizontalOffset = Direction.fromYRot(rotations[0]).getOpposite();
		Direction verticalOffset = (rotations[1] <= 180.0F && rotations[1] > 100.0F) ? Direction.UP : Direction.DOWN;

		if (rotations[1] >= 80.0F && rotations[1] <= 100.0F) {
			rotationFlag = false;
		}

		return rotationFlag ? blockpos.relative(horizontalOffset).relative(verticalOffset) : blockpos.relative(horizontalOffset);
	}

	@Override
	public void setupAnim(E puffBug, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(puffBug, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.Body.setShouldScaleChildren(false);

		this.revertBoxesToDefaultValues();

		if (!puffBug.isEndimationPlaying(PuffBugEntity.PUFF_ANIMATION) && !puffBug.isEndimationPlaying(PuffBugEntity.POLLINATE_ANIMATION)) {
			float angle = 0.1F * MathHelper.sin(0.25F * ageInTicks);
			this.Sensor1.zRot += angle;
			this.Sensor2.xRot += angle;
		}

		if (!puffBug.isEndimationPlaying(PuffBugEntity.FLY_ANIMATION)) {
			this.Head.xRot += 0.075F * MathHelper.sin(0.1F * ageInTicks);
			this.HeadDeflated.xRot = this.Head.xRot;
		}

		float[] rotations = puffBug.getRotationController().getRotations(ClientInfo.getPartialTicks());

		this.Body.yRot = rotations[0] * (float) (Math.PI / 180F);
		this.Body.xRot = rotations[1] * (float) (Math.PI / 180F);

		if (puffBug.isPassenger()) {
			this.Body.zRot = 1.57F;
		}

		this.BodyDeflated.yRot = this.Body.yRot;
		this.BodyDeflated.xRot = this.Body.xRot;
		this.BodyDeflatedProjectile.yRot = this.Body.yRot;
		this.BodyDeflatedProjectile.xRot = this.Body.xRot;

		this.NeckDeflated.xRot += -0.56F * puffBug.HIVE_LANDING.getAnimationProgress();
		this.HeadDeflated.xRot += -0.42F * puffBug.HIVE_LANDING.getAnimationProgress();
		this.Sensor1Deflated.zRot += 1.7F * puffBug.HIVE_LANDING.getAnimationProgress();
		this.Sensor2Deflated.xRot += 1.7F * puffBug.HIVE_LANDING.getAnimationProgress();

		this.NeckDeflated.xRot += 0.3F * puffBug.HIVE_SLEEP.getAnimationProgress();
		this.HeadDeflated.xRot += 0.25F * puffBug.HIVE_SLEEP.getAnimationProgress();

		this.Neck.xRot = this.HeadDeflated.xRot;
		this.Head.xRot = this.HeadDeflated.xRot;
		this.Sensor1.zRot = this.Sensor1Deflated.zRot;
		this.Sensor2.xRot = this.Sensor2Deflated.xRot;

		if (puffBug.isProjectile()) {
			this.Neck.yRot = 0.0F;
			this.NeckDeflated.yRot = 0.0F;

			this.Sensor1Deflated.xRot = 0.7F;
			this.Sensor1Deflated.yRot = 0.7F;
			this.Sensor1Deflated.zRot = 0.09F;

			this.Sensor2Deflated.xRot = 0.32F;
			this.Sensor2Deflated.yRot = -2.64F;
			this.Sensor2Deflated.zRot = 0.09F;

			float spin = MathHelper.lerp(ClientInfo.getPartialTicks(), puffBug.prevSpin, puffBug.spin);

			this.NeckDeflatedProjectile.yRot += Math.toRadians(spin);
			this.StingerDeflatedProjectile.yRot += Math.toRadians(spin);
		}
	}

	@Override
	public void animateModel(E puffbug) {
		super.animateModel(puffbug);

		if (puffbug.isEndimationPlaying(PuffBugEntity.CLAIM_HIVE_ANIMATION)) {
			this.setEndimationToPlay(PuffBugEntity.CLAIM_HIVE_ANIMATION);

			this.startKeyframe(5);
			this.rotate(this.Sensor1, 0.0F, 0.0F, 0.45F);
			this.rotate(this.Sensor2, 0.45F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.rotate(this.Sensor1, 0.0F, 0.0F, 0.45F);
			this.rotate(this.Sensor2, 0.45F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		} else if (puffbug.isEndimationPlaying(PuffBugEntity.PUFF_ANIMATION)) {
			this.setEndimationToPlay(PuffBugEntity.PUFF_ANIMATION);

			this.startKeyframe(10);
			this.rotate(this.Neck, 0.4F, 0.0F, 0.0F);
			this.rotate(this.Head, 0.55F, 0.0F, 0.0F);

			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.5F);
			this.rotate(this.Sensor2, -0.5F, 0.0F, 0.0F);

			this.rotate(this.Body, 0.25F, 0.0F, 0.0F);

			this.scale(this.Body, 0.4F, 0.4F, 0.4F);
			this.endKeyframe();

			this.resetKeyframe(10);
		} else if (puffbug.isEndimationPlaying(PuffBugEntity.TELEPORT_TO_ANIMATION)) {
			this.Body.setShouldScaleChildren(true);

			this.setEndimationToPlay(PuffBugEntity.TELEPORT_TO_ANIMATION);

			this.startKeyframe(5);
			this.scale(this.Body, 1.3F, 1.3F, 1.3F);
			this.endKeyframe();

			this.startKeyframe(3);
			this.scale(this.Body, -1.0F, -1.0F, -1.0F);
			this.endKeyframe();

			this.setStaticKeyframe(7);
		} else if (puffbug.isEndimationPlaying(PuffBugEntity.TELEPORT_FROM_ANIMATION)) {
			this.Body.setShouldScaleChildren(true);

			this.setEndimationToPlay(PuffBugEntity.TELEPORT_FROM_ANIMATION);

			this.startKeyframe(5);
			this.scale(this.Body, 1.3F, 1.3F, 1.3F);
			this.endKeyframe();

			this.resetKeyframe(5);
		} else if (puffbug.isEndimationPlaying(PuffBugEntity.ROTATE_ANIMATION)) {
			this.setEndimationToPlay(PuffBugEntity.ROTATE_ANIMATION);

			this.startKeyframe(10);
			this.rotate(this.Head, -0.5F, 0.0F, 0.0F);
			this.rotate(this.Neck, -0.5F, 0.0F, 0.0F);
			this.rotate(this.Stinger, 0.4F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(10);
		} else if (puffbug.isEndimationPlaying(PuffBugEntity.POLLINATE_ANIMATION)) {
			this.setEndimationToPlay(PuffBugEntity.POLLINATE_ANIMATION);

			this.startKeyframe(5);
			this.rotate(this.Neck, 0.0F, 0.0F, 0.17F);
			this.rotate(this.Head, 0.0F, 0.0F, 0.35F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
			this.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.rotate(this.Neck, 0.0F, 0.0F, -0.17F);
			this.rotate(this.Head, 0.0F, 0.0F, -0.35F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
			this.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.rotate(this.Neck, 0.0F, 0.0F, 0.17F);
			this.rotate(this.Head, 0.0F, 0.0F, 0.35F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
			this.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.rotate(this.Neck, 0.0F, 0.0F, -0.17F);
			this.rotate(this.Head, 0.0F, 0.0F, -0.35F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
			this.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.rotate(this.Neck, 0.0F, 0.0F, 0.17F);
			this.rotate(this.Head, 0.0F, 0.0F, 0.35F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
			this.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.rotate(this.Neck, 0.0F, 0.0F, -0.17F);
			this.rotate(this.Head, 0.0F, 0.0F, -0.35F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
			this.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.rotate(this.Neck, 0.0F, 0.0F, 0.17F);
			this.rotate(this.Head, 0.0F, 0.0F, 0.35F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
			this.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.rotate(this.Neck, 0.0F, 0.0F, -0.17F);
			this.rotate(this.Head, 0.0F, 0.0F, -0.35F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
			this.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.rotate(this.Neck, 0.0F, 0.0F, 0.17F);
			this.rotate(this.Head, 0.0F, 0.0F, 0.35F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
			this.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.rotate(this.Neck, 0.0F, 0.0F, -0.17F);
			this.rotate(this.Head, 0.0F, 0.0F, -0.35F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
			this.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(10);
			this.scale(this.Body, 0.4F, 0.4F, 0.4F);
			this.endKeyframe();

			this.resetKeyframe(10);
		} else if (puffbug.isEndimationPlaying(PuffBugEntity.MAKE_ITEM_ANIMATION)) {
			this.setEndimationToPlay(PuffBugEntity.MAKE_ITEM_ANIMATION);

			this.startKeyframe(10);
			this.scale(this.Body, 0.2F, 0.2F, 0.2F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, 0.5F);
			this.rotate(this.Sensor2, 0.5F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(10);

			this.startKeyframe(10);
			this.scale(this.Body, 0.2F, 0.2F, 0.2F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, 0.5F);
			this.rotate(this.Sensor2, 0.5F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(10);

			this.startKeyframe(10);
			this.scale(this.Body, 0.2F, 0.2F, 0.2F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, 0.5F);
			this.rotate(this.Sensor2, 0.5F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(10);

			this.startKeyframe(10);
			this.scale(this.Body, 0.2F, 0.2F, 0.2F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, 0.5F);
			this.rotate(this.Sensor2, 0.5F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(10);

			this.startKeyframe(10);
			this.scale(this.Body, 0.4F, 0.4F, 0.4F);
			this.rotate(this.Sensor1, 0.0F, 0.0F, 0.6F);
			this.rotate(this.Sensor2, 0.6F, 0.0F, 0.0F);
			this.rotate(this.Head, -0.35F, 0.0F, 0.0F);
			this.rotate(this.Neck, -0.25F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(10);
		} else if (puffbug.isEndimationPlaying(PuffBugEntity.LAND_ANIMATION)) {
			this.setEndimationToPlay(PuffBugEntity.LAND_ANIMATION);

			this.setStaticKeyframe(3);

			this.startKeyframe(3);
			this.rotate(this.Sensor1DeflatedProjectile, 0.0F, 0.0F, 2.05F);
			this.rotate(this.Sensor2DeflatedProjectile, 1.6F, 0.0F, -0.44F);

			this.rotate(this.NeckDeflatedProjectile, -0.5F, 0.0F, 0.0F);
			this.rotate(this.HeadDeflatedProjectile, -0.7F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(3);
			this.rotate(this.Sensor1DeflatedProjectile, 0.0F, 0.0F, 2.05F);
			this.rotate(this.Sensor2DeflatedProjectile, 1.6F, 0.0F, -0.44F);

			this.rotate(this.NeckDeflatedProjectile, 0.5F, 0.0F, 0.0F);
			this.rotate(this.HeadDeflatedProjectile, 0.7F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(3);
			this.rotate(this.Sensor1DeflatedProjectile, 0.0F, 0.0F, 0.0F);
			this.rotate(this.Sensor2DeflatedProjectile, 0.0F, 0.0F, 0.0F);

			this.rotate(this.NeckDeflatedProjectile, -0.44F, 0.0F, 0.0F);
			this.rotate(this.HeadDeflatedProjectile, -0.35F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(3);
			this.rotate(this.Sensor1DeflatedProjectile, 0.0F, 0.0F, 0.0F);
			this.rotate(this.Sensor2DeflatedProjectile, 0.0F, 0.0F, 0.0F);

			this.rotate(this.NeckDeflatedProjectile, 0.15F, 0.0F, 0.0F);
			this.rotate(this.HeadDeflatedProjectile, 0.26F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(3);
			this.rotate(this.HeadDeflatedProjectile, 0.0F, 0.0F, 0.0F);

			this.rotate(this.NeckDeflatedProjectile, -0.15F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(2);
		} else if (puffbug.isEndimationPlaying(PuffBugEntity.PULL_ANIMATION)) {
			this.setEndimationToPlay(PuffBugEntity.PULL_ANIMATION);

			this.startKeyframe(5);
			this.rotate(this.NeckDeflatedProjectile, 0.65F, 0.0F, 0.0F);
			this.rotate(this.HeadDeflatedProjectile, 0.4F, 0.0F, 0.0F);

			this.offset(this.BodyDeflatedProjectile, 0.0F, 0.5F / 6.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(10);
			this.rotate(this.NeckDeflatedProjectile, 0.23F, 0.0F, 0.0F);
			this.rotate(this.HeadDeflatedProjectile, 0.1F, 0.0F, 0.0F);

			this.offset(this.BodyDeflatedProjectile, 0.0F, 0.25F / 6.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(10);
		}
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(EndimatorModelRenderer EndimatorModelRenderer, float x, float y, float z) {
		EndimatorModelRenderer.xRot = x;
		EndimatorModelRenderer.yRot = y;
		EndimatorModelRenderer.zRot = z;
	}
}
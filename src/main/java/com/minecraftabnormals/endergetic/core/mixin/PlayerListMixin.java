package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.network.entity.S2CUpdateBalloonsMessage;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.PlayerData;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.List;

@Mixin(PlayerList.class)
public final class PlayerListMixin {
	@Shadow
	@Final
	private PlayerData playerIo;
	@Shadow
	@Final
	private MinecraftServer server;

	@SuppressWarnings("deprecation")
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerList;save(Lnet/minecraft/entity/player/ServerPlayerEntity;)V", shift = At.Shift.AFTER), method = "remove")
	private void removeBalloons(ServerPlayerEntity player, CallbackInfo info) {
		List<BolloomBalloonEntity> balloons = ((BalloonHolder) player).getBalloons();
		if (!balloons.isEmpty()) {
			ServerWorld serverWorld = (ServerWorld) player.level;
			for (BolloomBalloonEntity balloon : balloons) {
				serverWorld.despawn(balloon);
				balloon.removed = true;
			}
			serverWorld.getChunk(player.xChunk, player.zChunk).markUnsaved();
		}
	}

	@Inject(at = @At("RETURN"), method = "placeNewPlayer")
	private void spawnBalloons(NetworkManager netManager, ServerPlayerEntity player, CallbackInfo info) {
		ServerWorld serverWorld = (ServerWorld) player.level;

		CompoundNBT compound = this.server.getWorldData().getLoadedPlayerTag();
		if (!(compound != null && player.getName().getString().equals(this.server.getSingleplayerName()))) {
			try {
				File playerDataFile = new File(this.playerIo.getPlayerDataFolder(), player.getStringUUID() + ".dat");
				if (playerDataFile.exists() && playerDataFile.isFile()) {
					compound = CompressedStreamTools.readCompressed(playerDataFile);
				}
			} catch (Exception exception) {
				EndergeticExpansion.LOGGER.warn("Failed to load player data for {}", player.getName().getString());
			}
		}

		if (compound != null && compound.contains("Balloons", 9)) {
			ListNBT balloonsTag = compound.getList("Balloons", 10);
			if (!balloonsTag.isEmpty()) {
				for (int i = 0; i < balloonsTag.size(); i++) {
					Entity entity = EntityType.loadEntityRecursive(balloonsTag.getCompound(i), serverWorld, (balloon -> !serverWorld.addWithUUID(balloon) ? null : balloon));
					if (entity instanceof BolloomBalloonEntity) {
						((BolloomBalloonEntity) entity).attachToEntity(player);
						EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new S2CUpdateBalloonsMessage(player));
					}
				}
			}
		}
	}

}

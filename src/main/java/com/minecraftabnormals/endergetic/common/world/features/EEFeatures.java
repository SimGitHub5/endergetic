package com.minecraftabnormals.endergetic.common.world.features;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.endergetic.common.world.configs.CorrockArchConfig;
import com.minecraftabnormals.endergetic.common.world.configs.CorrockBranchConfig;
import com.minecraftabnormals.endergetic.common.world.configs.CorrockTowerConfig;
import com.minecraftabnormals.endergetic.common.world.configs.WeightedFeatureConfig;
import com.minecraftabnormals.endergetic.common.world.features.corrock.*;
import com.minecraftabnormals.endergetic.common.world.features.corrock.tower.LargeCorrockTowerFeature;
import com.minecraftabnormals.endergetic.common.world.features.corrock.tower.MediumCorrockTowerFeature;
import com.minecraftabnormals.endergetic.common.world.features.corrock.tower.SmallCorrockTowerFeature;
import com.minecraftabnormals.endergetic.common.world.placements.EEPlacements;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class EEFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_GRASS = createFeature("poise_bush", () -> new PoiseBushFeature(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_TALLGRASS = createFeature("poise_tallgrass", () -> new TallPoiseBushFeature(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_CLUSTER = createFeature("poise_cluster", () -> new PoiseClusterFeature(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> BOLLOOM_BUD = createFeature("bolloom_bud", () -> new BolloomBudFeature(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> PUFFBUG_HIVE = createFeature("puffbug_hive", () -> new PuffBugHiveFeature(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_DOME = createFeature("poise_dome", () -> new PoiseDomeFeature(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_TREE = createFeature("poise_tree", () -> new PoiseTreeFeature(NoFeatureConfig.CODEC));

	public static final RegistryObject<Feature<ProbabilityConfig>> CORROCK_PATCH = createFeature("corrock_patch", () -> new CorrockPatchFeature(ProbabilityConfig.CODEC));
	public static final RegistryObject<Feature<SphereReplaceConfig>> GROUND_PATCH = createFeature("ground_patch", () -> new GroundPatchFeature(SphereReplaceConfig.CODEC));
	public static final RegistryObject<Feature<CorrockBranchConfig>> CORROCK_BRANCH = createFeature("corrock_branch", () -> new CorrockBranchFeature(CorrockBranchConfig.CODEC));
	public static final RegistryObject<Feature<ProbabilityConfig>> SMALL_CORROCK_TOWER = createFeature("small_corrock_tower", () -> new SmallCorrockTowerFeature(ProbabilityConfig.CODEC));
	public static final RegistryObject<Feature<CorrockTowerConfig>> MEDIUM_CORROCK_TOWER = createFeature("medium_corrock_tower", () -> new MediumCorrockTowerFeature(CorrockTowerConfig.CODEC));
	public static final RegistryObject<Feature<CorrockTowerConfig>> LARGE_CORROCK_TOWER = createFeature("large_corrock_tower", () -> new LargeCorrockTowerFeature(CorrockTowerConfig.CODEC));
	public static final RegistryObject<Feature<ProbabilityConfig>> CORROCK_SHELF = createFeature("corrock_shelf", () -> new CorrockShelfFeature(ProbabilityConfig.CODEC));
	public static final RegistryObject<Feature<CorrockArchConfig>> CORROCK_ARCH = createFeature("corrock_arch", () -> new CorrockArchFeature(CorrockArchConfig.CODEC));

	public static final RegistryObject<Feature<WeightedFeatureConfig>> WEIGHTED_FEATURES = createFeature("weighted_features", () -> new WeightedMultiFeature(WeightedFeatureConfig.CODEC));
	public static final RegistryObject<Feature<EndGatewayConfig>> ENDERGETIC_GATEWAY = createFeature("gateway", () -> new EndergeticEndGatewayFeature(EndGatewayConfig.CODEC));

	private static <F extends Feature<?>> RegistryObject<F> createFeature(String name, Supplier<F> feature) {
		return FEATURES.register(name, feature);
	}

	public static final class Configured {
		public static final ConfiguredFeature<?, ?> POISE_DOME = EEFeatures.POISE_DOME.get().configured(IFeatureConfig.NONE).decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(3, 0.02F, 1)));
		public static final ConfiguredFeature<?, ?> POISE_TREE = EEFeatures.POISE_TREE.get().configured(IFeatureConfig.NONE).decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(2, 0.05F, 1)));
		public static final ConfiguredFeature<?, ?> POISE_CLUSTER = EEFeatures.POISE_CLUSTER.get().configured(IFeatureConfig.NONE).decorated(EEPlacements.NOISE_HEIGHTMAP_32.get().configured(new NoiseDependant(-0.8D, 4, 22)));
		public static final ConfiguredFeature<?, ?> PUFFBUG_HIVE = EEFeatures.PUFFBUG_HIVE.get().configured(IFeatureConfig.NONE).decorated(EEPlacements.NOISE_HEIGHTMAP_32.get().configured(new NoiseDependant(-0.8D, 9, 25)));
		public static final ConfiguredFeature<?, ?> BOLLOOM_BUD = EEFeatures.BOLLOOM_BUD.get().configured(IFeatureConfig.NONE).decorated(EEPlacements.NOISE_HEIGHTMAP_32.get().configured(new NoiseDependant(-0.9D, 90, 90)));
		public static final ConfiguredFeature<?, ?> POISE_GRASS = EEFeatures.POISE_GRASS.get().configured(IFeatureConfig.NONE).decorated(Features.Placements.HEIGHTMAP_DOUBLE).decorated(Placement.COUNT_NOISE.configured(new NoiseDependant(-0.8D, 5, 10)));
		public static final ConfiguredFeature<?, ?> TALL_POISE_GRASS = EEFeatures.POISE_TALLGRASS.get().configured(IFeatureConfig.NONE).decorated(EEPlacements.NOISE_HEIGHTMAP_32.get().configured(new NoiseDependant(-0.8D, 0, 7)));
		public static final ConfiguredFeature<?, ?> END_GATEWAY = EEFeatures.ENDERGETIC_GATEWAY.get().configured(EndGatewayConfig.knownExit(ServerWorld.END_SPAWN_POINT, true)).decorated(Placement.END_GATEWAY.configured(IPlacementConfig.NONE));
		public static final ConfiguredFeature<?, ?> END_GATEWAY_DELAYED = EEFeatures.ENDERGETIC_GATEWAY.get().configured(EndGatewayConfig.delayedExitSearch());
		public static final ConfiguredFeature<?, ?> CORROCK_PATCH = EEFeatures.CORROCK_PATCH.get().configured(new ProbabilityConfig(0.3F)).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(8);
		public static final ConfiguredFeature<?, ?> SPARSE_CORROCK_BRANCH = EEFeatures.CORROCK_BRANCH.get().configured(new CorrockBranchConfig(ImmutableList.of(Blocks.END_STONE.defaultBlockState(), EEBlocks.CORROCK_END_BLOCK.get().defaultBlockState()), 0.4F, 0.5F)).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(5);
		public static final ConfiguredFeature<?, ?> CORROCK_BRANCH = EEFeatures.CORROCK_BRANCH.get().configured(new CorrockBranchConfig(ImmutableList.of(EEBlocks.CORROCK_END_BLOCK.get().defaultBlockState(), EEBlocks.EUMUS.get().defaultBlockState()), 0.5F, 0.6F)).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(64);
		public static final ConfiguredFeature<?, ?> SMALL_CORROCK_TOWER = EEFeatures.SMALL_CORROCK_TOWER.get().configured(new ProbabilityConfig(0.25F)).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(2);
		public static final ConfiguredFeature<?, ?> MEDIUM_CORROCK_TOWER = EEFeatures.MEDIUM_CORROCK_TOWER.get().configured(new CorrockTowerConfig(3, 4, 0.5F, 0.075F)).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(2);
		public static final ConfiguredFeature<?, ?> LARGE_CORROCK_TOWER = EEFeatures.LARGE_CORROCK_TOWER.get().configured(new CorrockTowerConfig(2, 5, 0.5F, 0.1F)).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(2);
		public static final ConfiguredFeature<?, ?> CORROCK_TOWER = EEFeatures.WEIGHTED_FEATURES.get().configured(WeightedFeatureConfig.createFromPairs(Pair.of(SMALL_CORROCK_TOWER, 6), Pair.of(MEDIUM_CORROCK_TOWER, 12), Pair.of(LARGE_CORROCK_TOWER, 4))).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(256);
		public static final ConfiguredFeature<?, ?> CORROCK_SHELF = EEFeatures.CORROCK_SHELF.get().configured(new ProbabilityConfig(0.75F)).decorated(EEPlacements.HEIGHTMAP_SPREAD_LOWER.get().configured(IPlacementConfig.NONE).squared()).count(6);
		public static final ConfiguredFeature<?, ?> CORROCK_ARCH = EEFeatures.CORROCK_ARCH.get().configured(new CorrockArchConfig(0.1F, 0.25F, 13.0F, 22.0F, 7.0F)).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(24);

		private static <FC extends IFeatureConfig> void register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
			Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(EndergeticExpansion.MOD_ID, name), configuredFeature);
		}

		public static void registerConfiguredFeatures() {
			register("poise_dome", POISE_DOME);
			register("poise_tree", POISE_TREE);
			register("poise_cluster", POISE_CLUSTER);
			register("puffbug_hive", PUFFBUG_HIVE);
			register("bolloom_bud", BOLLOOM_BUD);
			register("poise_grass", POISE_GRASS);
			register("tall_poise_grass", TALL_POISE_GRASS);
			register("end_gateway", END_GATEWAY);
			register("end_gateway_delayed", END_GATEWAY_DELAYED);
			register("corrock_patch", CORROCK_PATCH);
			register("sparse_corrock_branch", SPARSE_CORROCK_BRANCH);
			register("corrock_branch", CORROCK_BRANCH);
			register("small_corrock_tower", SMALL_CORROCK_TOWER);
			register("medium_corrock_tower", MEDIUM_CORROCK_TOWER);
			register("large_corrock_tower", LARGE_CORROCK_TOWER);
			register("corrock_tower", CORROCK_TOWER);
			register("corrock_shelf", CORROCK_SHELF);
			register("corrock_arch", CORROCK_ARCH);
		}
	}
}
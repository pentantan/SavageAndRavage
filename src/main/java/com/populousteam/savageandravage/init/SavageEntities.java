package com.populousteam.savageandravage.init;

import com.populousteam.savageandravage.SavageAndRavage;
import com.populousteam.savageandravage.entity.*;
import com.populousteam.savageandravage.entity.illager.DefenderEntity;
import com.populousteam.savageandravage.entity.illager.GrieferIllagerEntity;
import com.populousteam.savageandravage.entity.illager.PoultryFarmerIllagerEntity;
import com.populousteam.savageandravage.entity.illager.ScavengersEntity;
import com.populousteam.savageandravage.entity.projectile.BestialBrewEntity;
import com.populousteam.savageandravage.entity.projectile.CreeperSporeEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SavageEntities {
    public static final EntityType<CreepieEntity> CREEPIES = EntityType.Builder.create(CreepieEntity::new, EntityClassification.MONSTER).setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(0.6F, 1.7F).build(prefix("creepie"));
    public static final EntityType<GrieferIllagerEntity> GRIEFER_ILLAGER = EntityType.Builder.create(GrieferIllagerEntity::new, EntityClassification.MONSTER).setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(0.6F, 2.05F).build(prefix("griefer_illager"));
    public static final EntityType<DefenderEntity> DEFENDER = EntityType.Builder.create(DefenderEntity::new, EntityClassification.MONSTER).setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(0.6F, 1.95F).build(prefix("defender"));
    public static final EntityType<CreeperSporeEntity> CREEPER_SPORE = EntityType.Builder.<CreeperSporeEntity>create(CreeperSporeEntity::new, EntityClassification.MISC).setTrackingRange(100).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).setCustomClientFactory(CreeperSporeEntity::new).size(0.25F, 0.25F).build(prefix("creeper_spore"));
    public static final EntityType<PoultryFarmerIllagerEntity> POULTRY_FARMER = EntityType.Builder.create(PoultryFarmerIllagerEntity::new, EntityClassification.MONSTER).setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(0.6F, 1.95F).build(prefix("poultry_farmer"));
    public static final EntityType<SavagelingEntity> SAVAGELING = EntityType.Builder.create(SavagelingEntity::new, EntityClassification.MONSTER).setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(0.6F, 0.75F).build(prefix("savageling"));
    public static final EntityType<SkeletonVillagerEntity> SKELETONVILLAGER = EntityType.Builder.create(SkeletonVillagerEntity::new, EntityClassification.MONSTER).setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(0.6F, 1.95F).build(prefix("skeleton_villager"));
    public static final EntityType<ScavengersEntity> SCAVENGER = EntityType.Builder.create(ScavengersEntity::new, EntityClassification.MONSTER).setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(0.6F, 1.95F).build(prefix("scavenger"));
    public static final EntityType<FriendlyRavagerEntity> FRIENDLY_RAVAGER = EntityType.Builder.create(FriendlyRavagerEntity::new, EntityClassification.CREATURE).setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(1.95F, 2.2F).build(prefix("friendly_ravager"));
    public static final EntityType<BestialBrewEntity> BESTIAL_BREW = EntityType.Builder.<BestialBrewEntity>create(BestialBrewEntity::new, EntityClassification.MISC).setTrackingRange(100).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).setCustomClientFactory(BestialBrewEntity::new).size(0.3F, 0.3F).build(prefix("beast_brew"));
    //public static final EntityType<RunesEntity> RUNES_ENTITY = EntityType.Builder.create(RunesEntity::new, EntityClassification.MISC).setTrackingRange()
    public static final EntityType<HyenaEntity> HYENA = EntityType.Builder.create(HyenaEntity::new, EntityClassification.CREATURE).setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(0.8F, 1.1F).build(prefix("hyena"));

    private static String prefix(String path) {
        return SavageAndRavage.MOD_ID + "." + path;
    }

    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(DEFENDER.setRegistryName("defender"));
        event.getRegistry().register(CREEPIES.setRegistryName("creepie"));
        event.getRegistry().register(GRIEFER_ILLAGER.setRegistryName("griefer_illager"));
        event.getRegistry().register(CREEPER_SPORE.setRegistryName("creeper_spores"));
        event.getRegistry().register(POULTRY_FARMER.setRegistryName("poultry_farmer"));
        event.getRegistry().register(SAVAGELING.setRegistryName("savageling"));
        event.getRegistry().register(SKELETONVILLAGER.setRegistryName("skeleton_villager"));
        event.getRegistry().register(SCAVENGER.setRegistryName("scavenger"));
        event.getRegistry().register(FRIENDLY_RAVAGER.setRegistryName("friendly_ravager"));
        event.getRegistry().register(BESTIAL_BREW.setRegistryName("beast_brew"));

        event.getRegistry().register(HYENA.setRegistryName("hyena"));

        //event.getRegistry().register(RUNES_ENTITY.setRegistryName("runes"));
    }

    public static void addEntitySpawn() {
        Biomes.SAVANNA.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(HYENA, 5, 3, 5));
        Biomes.SAVANNA_PLATEAU.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(HYENA, 6, 4, 6));
        Biomes.SHATTERED_SAVANNA.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(HYENA, 5, 3, 5));
        Biomes.SHATTERED_SAVANNA_PLATEAU.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(HYENA, 6, 4, 6));
    }
}
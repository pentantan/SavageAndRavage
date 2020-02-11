package populousteam.savageandravage.entity;

import populousteam.savageandravage.entity.ai.CreepiesSwellGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;

public class CreepieEntity extends MonsterEntity {
    private static final DataParameter<Integer> STATE = EntityDataManager.createKey(CreepieEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(CreepieEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(CreepieEntity.class, DataSerializers.BOOLEAN);
    private int lastActiveTime;
    private int timeSinceIgnited;
    private int fuseTime = 20;
    private int explosionRadius = 2;
    private int droppedSkulls;

    private LivingEntity owner;
    private UUID ownerUniqueId;

    public CreepieEntity(EntityType<? extends CreepieEntity> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 0;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new CreepiesSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, OcelotEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, CatEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.15D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, PlayerEntity.class, true) {
            @Override
            public boolean shouldExecute() {
                return getOwner() == null && super.shouldExecute();
            }

            @Override
            public boolean shouldContinueExecuting() {
                return getOwner() == null && super.shouldContinueExecuting();
            }
        });
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
    }

    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    public int getMaxFallHeight() {
        return this.getAttackTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
    }

    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        this.timeSinceIgnited = (int) ((float) this.timeSinceIgnited + distance * 1.5F);
        if (this.timeSinceIgnited > this.fuseTime - 5) {
            this.timeSinceIgnited = this.fuseTime - 5;
        }

    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(STATE, -1);
        this.dataManager.register(POWERED, false);
        this.dataManager.register(IGNITED, false);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.dataManager.get(POWERED)) {
            compound.putBoolean("powered", true);
        }

        if (this.ownerUniqueId != null) {
            compound.putUniqueId("OwnerUUID", this.ownerUniqueId);
        }
        compound.putShort("Fuse", (short) this.fuseTime);
        compound.putByte("ExplosionRadius", (byte) this.explosionRadius);
        compound.putBoolean("ignited", this.hasIgnited());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(POWERED, compound.getBoolean("powered"));
        if (compound.contains("Fuse", 99)) {
            this.fuseTime = compound.getShort("Fuse");
        }

        if (compound.contains("ExplosionRadius", 99)) {
            this.explosionRadius = compound.getByte("ExplosionRadius");
        }
        if (compound.hasUniqueId("OwnerUUID")) {
            this.ownerUniqueId = compound.getUniqueId("OwnerUUID");
        }
        if (compound.getBoolean("ignited")) {
            this.ignite();
        }

    }


    public void setOwner(@Nullable LivingEntity ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld) this.world).getEntityByUuid(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }

        return this.owner;
    }

    protected float getSoundVolume() {
        return 0.5F;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        if (this.isAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;
            if (this.hasIgnited()) {
                this.setCreeperState(1);
            }

            int i = this.getCreeperState();
            if (i > 0 && this.timeSinceIgnited == 0) {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, this.getSoundVolume(), 0.5F);
            }
            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
        }

        super.tick();
    }

    public float getRenderScale() {
        return 0.6F;
    }


    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CREEPER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        Entity entity = source.getTrueSource();
        if (entity != this && entity instanceof CreepieEntity) {
            CreepieEntity creeperentity = (CreepieEntity) entity;
            if (creeperentity.ableToCauseSkullDrop()) {
                creeperentity.incrementDroppedSkulls();
                this.entityDropItem(Items.CREEPER_HEAD);
            }
        }

    }

    public boolean attackEntityAsMob(Entity entityIn) {
        return true;
    }

    /**
     * Returns true if the creeper is powered by a lightning bolt.
     */
    public boolean getPowered() {
        return this.dataManager.get(POWERED);
    }

    /**
     * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
     */
    @OnlyIn(Dist.CLIENT)
    public float getCreeperFlashIntensity(float partialTicks) {
        return MathHelper.lerp(partialTicks, (float) this.lastActiveTime, (float) this.timeSinceIgnited) / (float) (this.fuseTime - 2);
    }

    /**
     * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
     */
    public int getCreeperState() {
        return this.dataManager.get(STATE);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setCreeperState(int state) {
        this.dataManager.set(STATE, state);
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(LightningBoltEntity lightningBolt) {
        super.onStruckByLightning(lightningBolt);
        this.dataManager.set(POWERED, true);
    }

    protected boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            player.swingArm(hand);
            if (!this.world.isRemote) {
                this.ignite();
                itemstack.damageItem(1, player, (p_213625_1_) -> {
                    p_213625_1_.sendBreakAnimation(hand);
                });
                return true;
            }
        } else if (itemstack.getItem() == Items.BONE_MEAL) {
            if (this.world.isRemote) {
                for (int i = 0; i < 2; i++) {
                    this.world.addParticle(ParticleTypes.SNEEZE, this.getPosition().getX() + this.world.rand.nextFloat() - 0.5F, this.getPosition().getY() + this.world.rand.nextFloat() - 0.5F, this.getPosition().getZ() + this.world.rand.nextFloat() - 0.5F, 0.0D, 0.0D, 0.0D);
                }
            }
            if (!this.world.isRemote) {
                itemstack.shrink(1);

                if (this.rand.nextFloat() < 0.05F) {

                    CreeperEntity creeperEntity = EntityType.CREEPER.create(this.world);
                    creeperEntity.setLocationAndAngles(this.getPosition().getX() + 0.5F, this.getPosition().getY(), this.getPosition().getZ() + 0.5F, 0.0F, 0.0F);

                    this.world.addEntity(creeperEntity);

                    this.remove();

                }
            }
        }

        return super.processInteract(player, hand);
    }

    /**
     * Creates an explosion as determined by this creeper's power and explosion radius.
     */
    private void explode() {
        if (!this.world.isRemote) {
            float f = this.getPowered() ? 1.8F * this.getRenderScale() : 0.8F * this.getRenderScale();
            this.dead = true;
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float) this.explosionRadius * f, Explosion.Mode.NONE);
            this.remove();
            this.spawnLingeringCloud();
        }

    }

    private void spawnLingeringCloud() {
        Collection<EffectInstance> collection = this.getActivePotionEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.world, this.posX, this.posY, this.posZ);
            areaeffectcloudentity.setRadius(1.5F);
            areaeffectcloudentity.setRadiusOnUse(-0.2F);
            areaeffectcloudentity.setWaitTime(60);
            areaeffectcloudentity.setDuration(60);
            areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());

            for (EffectInstance effectinstance : collection) {
                areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
            }

            this.world.addEntity(areaeffectcloudentity);
        }

    }

    public boolean hasIgnited() {
        return this.dataManager.get(IGNITED);
    }

    public void ignite() {
        this.dataManager.set(IGNITED, true);
    }

    /**
     * Returns true if an entity is able to drop its skull due to being blown up by this creeper.
     * <p>
     * Does not test if this creeper is charged; the caller must do that. However, does test the doMobLoot gamerule.
     */
    public boolean ableToCauseSkullDrop() {
        return this.getPowered() && this.droppedSkulls < 1;
    }

    public void incrementDroppedSkulls() {
        ++this.droppedSkulls;
    }

    public boolean canAttack(LivingEntity target) {
        return this.owner != target && super.canAttack(target);
    }

    public Team getTeam() {

        LivingEntity livingentity = this.getOwner();
        if (livingentity != null) {
            return livingentity.getTeam();
        }

        return super.getTeam();
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isOnSameTeam(Entity entityIn) {

        LivingEntity livingentity = this.getOwner();
        if (entityIn == livingentity) {
            return true;
        }

        if (livingentity != null) {
            return livingentity.isOnSameTeam(entityIn);
        }


        return super.isOnSameTeam(entityIn);
    }

    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof CreeperEntity) && !(target instanceof GhastEntity)) {
            if (target instanceof WolfEntity) {
                WolfEntity wolfentity = (WolfEntity) target;
                if (wolfentity.isTamed() && wolfentity.getOwner() == owner) {
                    return false;
                }
            }

            if (target == owner) {
                return false;
            } else if (target instanceof CreepieEntity && ((CreepieEntity) target).getOwner() == owner) {
                return false;
            } else if (owner.isOnSameTeam(target)) {
                return false;
            } else if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity) owner).canAttackPlayer((PlayerEntity) target)) {
                return false;
            } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity) target).isTame()) {
                return false;
            } else {
                return !(target instanceof CatEntity) || !((CatEntity) target).isTamed();
            }
        } else {
            return false;
        }
    }

    class CopyOwnerTargetGoal extends TargetGoal {
        private final EntityPredicate field_220803_b = (new EntityPredicate()).setLineOfSiteRequired().setUseInvisibilityCheck();
        private LivingEntity attacker;

        public CopyOwnerTargetGoal(CreatureEntity creature) {
            super(creature, false);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = CreepieEntity.this.getOwner();
            if (livingentity != null) {

                this.attacker = livingentity.getRevengeTarget();
                CreepieEntity.this.setAttackTarget(attacker);

                if (this.attacker == null) {
                    if (livingentity instanceof MobEntity) {
                        this.attacker = ((MobEntity) livingentity).getAttackTarget();

                        return this.attacker != CreepieEntity.this && this.isSuitableTarget(this.attacker, field_220803_b) && CreepieEntity.this.shouldAttackEntity(this.attacker, livingentity);
                    } else {
                        this.attacker = livingentity.getLastAttackedEntity();
                        return this.attacker != CreepieEntity.this && this.isSuitableTarget(this.attacker, field_220803_b) && CreepieEntity.this.shouldAttackEntity(this.attacker, livingentity);
                    }
                } else {
                    return this.attacker != CreepieEntity.this && this.isSuitableTarget(this.attacker, field_220803_b) && CreepieEntity.this.shouldAttackEntity(this.attacker, livingentity);
                }

            }
            return false;
        }


        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            super.startExecuting();

            CreepieEntity.this.setAttackTarget(attacker);
        }
    }
}
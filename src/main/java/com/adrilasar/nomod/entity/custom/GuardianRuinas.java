package com.adrilasar.nomod.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Random;

@Mod.EventBusSubscriber
public class GuardianRuinas extends ZombieEntity implements IAnimatable {
    private static final double MIN_DELTA = 0.005;
    private static final int RANDOM_FREQ = 40;
    private static final int BASIC_PROB = 90;
    private static final DataParameter<String> ATTACK_TYPE = EntityDataManager.defineId(GuardianRuinas.class, DataSerializers.STRING);
    private int count;
    private AnimationFactory factory = new AnimationFactory(this);

    public GuardianRuinas(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
        count = 0;
    }

    public static AttributeModifierMap setAttributes(){
        return ZombieEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.ATTACK_DAMAGE, 5.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
                .add(Attributes.FOLLOW_RANGE, 35.0D).build();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(this.isAggressive()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation(getEntityData().get(ATTACK_TYPE), true));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("andar", true));
            return PlayState.CONTINUE;
        }
        else return PlayState.STOP;
    }

    /**
     * If the entity has moved more than MIN_MOV units in either the x or z direction, then the entity is moving
     */
    private boolean isMoving() {
        return Math.abs(this.getDeltaMovement().x()) > MIN_DELTA || Math.abs(this.getDeltaMovement().z()) > MIN_DELTA;
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    public void killed(ServerWorld p_241847_1_, LivingEntity p_241847_2_) {
    }

    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    public void setAggressive(boolean p_213395_1_) {
        this.randomAttack();
        super.setAggressive(p_213395_1_);
    }

    /**
     * Randomly choose between two attacks every RANDOM_FREQ ticks being agressive
     */
    private void randomAttack() {
        if(count > RANDOM_FREQ) {
            Random rand = new Random();
            int randomNum = rand.nextInt(100);

            if(randomNum < BASIC_PROB) setAttackType("ataquebasico");
            else setAttackType("ataquegiratorio");
            count=0;
        }
        count++;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ATTACK_TYPE, "ataquebasico");
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAT_STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.DOLPHIN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

    public void setAttackType(String attackType) {
        this.getEntityData().set(ATTACK_TYPE, attackType);
    }
}

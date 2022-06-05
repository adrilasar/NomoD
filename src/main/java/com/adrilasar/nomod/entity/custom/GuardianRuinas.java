package com.adrilasar.nomod.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombieEntity;
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


    public static final double MIN_MOV = 0.005;
    public static String attackType;
    private AnimationFactory factory = new AnimationFactory(this);

    public GuardianRuinas(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
        randomAttack();
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation(attackType, true));
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
        if(Math.abs(this.getDeltaMovement().x()) > MIN_MOV || Math.abs(this.getDeltaMovement().z()) > MIN_MOV){
            return true;
        }
        return false;
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

    /**
     * Creates a new thread that will randomly choose between two attacks every 3 seconds
     */
    private void randomAttack() {
        new Thread(){
            public void run(){
                try {
                    Random rand = new Random();
                    int randomNum;
                    while(true){
                        randomNum = rand.nextInt(100);
                        if(randomNum<80) attackType = "ataquebasico";
                        else attackType = "ataquegiratorio";
                        Thread.sleep(3000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
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
}

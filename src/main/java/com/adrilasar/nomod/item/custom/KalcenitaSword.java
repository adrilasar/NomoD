package com.adrilasar.nomod.item.custom;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class KalcenitaSword extends SwordItem
{
    public KalcenitaSword(IItemTier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 30, 2));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
package com.adrilasar.nomod.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum ModItemTier implements IItemTier
{

    KALCENITA(4, 2032, 9.5F, 5F, 20,
            () -> Ingredient.of(ModItems.KALCENITA_INGOT.get()));

    private final int harvestLevel;
    private final int maxUses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    ModItemTier(int harvestLevel, int maxUses, float speed,
                float attackDamageBonus, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantability = enchantability;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    @Override
    public int getUses() {
        return maxUses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial.get();
    }
}

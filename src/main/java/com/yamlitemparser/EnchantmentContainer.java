package com.yamlitemparser;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentContainer {

    private Enchantment enchantment;
    private int minLevel, maxLevel;
    private double chance;

    public EnchantmentContainer(Enchantment enchantment, int maxLevel, int minLevel, double chance) {
        this.enchantment = enchantment;
        this.maxLevel = maxLevel;
        this.minLevel = minLevel;
        this.chance = chance;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public boolean isGuaranteed(){
        return chance >= 100;
    }
}

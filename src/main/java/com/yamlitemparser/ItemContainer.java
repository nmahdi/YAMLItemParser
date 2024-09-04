package com.yamlitemparser;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ItemContainer implements Cloneable{

    private static final char COLOR_SYMBOL = '&';

    private final Material material;
    private int minAmount = 1, maxAmount = 1;
    private String displayName;
    private final ArrayList<String> lore = new ArrayList<>();
    private final ArrayList<EnchantmentContainer> guaranteedEnchantments = new ArrayList<>();
    private final ArrayList<EnchantmentContainer> bonusEnchantments = new ArrayList<>();
    private int gEnchantmentAmount = -1;
    private int bEnchantmentAmount = -1;
    private boolean overrideBonusEnchantments = false;
    private boolean glow = false;
    private final ArrayList<ItemFlag> itemFlags = new ArrayList<>();
    private int modelData = -1;
    private int minDurability = -1, maxDurability = -1;
    private boolean unbreakable = false;
    private final HashMap<Attribute, AttributeModifier> attributes = new HashMap<>();
    private ArrayList<PotionEffect> effects = new ArrayList<>();
    private Color potionColor;
    private String skullURL;

    public ItemContainer(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean staticAmount(){
        return minAmount == maxAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public boolean hasDisplayName(){
        return displayName != null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = ChatColor.translateAlternateColorCodes(COLOR_SYMBOL, displayName);
    }

    public boolean hasLore(){
        return !lore.isEmpty();
    }

    public void addLore(String line){
        lore.add(ChatColor.translateAlternateColorCodes(COLOR_SYMBOL, line));
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    public boolean hasGuaranteedEnchantments(){
        return !guaranteedEnchantments.isEmpty();
    }

    public ArrayList<EnchantmentContainer> getGuaranteedEnchantments() {
        return guaranteedEnchantments;
    }

    public void addGuaranteedEnchantment(EnchantmentContainer container){
        guaranteedEnchantments.add(container);
    }

    public boolean hasBonusEnchantments(){
        return !bonusEnchantments.isEmpty();
    }

    public ArrayList<EnchantmentContainer> getBonusEnchantments() {
        return bonusEnchantments;
    }

    public void addBonusEnchantment(EnchantmentContainer container){
        bonusEnchantments.add(container);
    }

    public boolean hasGuaranteedEnchantmentAmount(){
        return gEnchantmentAmount != -1;
    }

    public int getGuaranteedEnchantmentAmount() {
        return gEnchantmentAmount;
    }

    public void setGuaranteedEnchantmentAmount(int gEnchantmentAmount) {
        this.gEnchantmentAmount = gEnchantmentAmount;
    }

    public boolean hasBonusEnchantmentAmount(){
        return bEnchantmentAmount != -1;
    }

    public int getBonusEnchantmentAmount() {
        return bEnchantmentAmount;
    }

    public void setBonusEnchantmentAmount(int bEnchantmentAmount) {
        this.bEnchantmentAmount = bEnchantmentAmount;
    }

    public boolean overrideBonusEnchantments() {
        return overrideBonusEnchantments;
    }

    public void setOverrideBonusEnchantments(boolean overrideBonusEnchantments) {
        this.overrideBonusEnchantments = overrideBonusEnchantments;
    }

    public void setGlowing(boolean glowing){
        this.glow = glowing;
    }

    public boolean isGlowing(){
        return glow;
    }

    public boolean hasFlags(){
        return !itemFlags.isEmpty();
    }

    public void addItemFlag(ItemFlag flag){
        itemFlags.add(flag);
    }

    public ArrayList<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public boolean hasModelData(){
        return modelData != -1;
    }

    public void setModelData(int modelData){
        this.modelData = modelData;
    }

    public int getModelData() {
        return modelData;
    }

    public boolean hasDurability(){
        return minDurability != -1 && maxDurability != -1;
    }

    public boolean staticDurability(){
        return minDurability == maxDurability;
    }

    public int getMinDurability() {
        return minDurability;
    }

    public void setMinDurability(int minDurability) {
        this.minDurability = minDurability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public void setMaxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public boolean hasAttributes(){
        return !attributes.isEmpty();
    }

    public HashMap<Attribute, AttributeModifier> getAttributes() {
        return attributes;
    }

    public void addAttribute(Attribute attribute, AttributeModifier attributeModifier){
        this.attributes.put(attribute, attributeModifier);
    }

    public boolean isPotion(){
        return material == Material.POTION || material == Material.LINGERING_POTION || material == Material.SPLASH_POTION;
    }

    public boolean hasEffects(){
        return !effects.isEmpty();
    }

    public ArrayList<PotionEffect> getEffects() {
        return effects;
    }

    public void addEffect(PotionEffect effect){
        this.effects.add(effect);
    }

    public boolean hasColor(){
        return potionColor != null;
    }

    public Color getPotionColor() {
        return potionColor;
    }

    public void setPotionColor(Color color) {
        this.potionColor = color;
    }

    public boolean isSkull(){
        return material == Material.PLAYER_HEAD;
    }

    public boolean hasSkullURL(){
        return skullURL != null;
    }

    public String getSkullURL() {
        return skullURL;
    }

    public void setSkullURL(String skullURL) {
        this.skullURL = skullURL;
    }

    @SuppressWarnings("all")
    public ItemStack build(){
        Random random = ThreadLocalRandom.current();

        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();

        stack.setAmount(random.nextInt(minAmount, staticAmount() ? maxAmount+1 : maxAmount));

        if(hasDisplayName()){
            meta.setDisplayName(displayName);
        }
        if(hasLore()){
            meta.setLore(lore);
        }
        if(hasModelData()){
            meta.setCustomModelData(modelData);
        }

        int bonusAmount = hasBonusEnchantmentAmount() ? bEnchantmentAmount : bonusEnchantments.size();

        for(EnchantmentContainer enchantmentContainer : bonusEnchantments){
            if(bonusAmount == 0) break;
            double chance = random.nextDouble(100);
            if(chance <= enchantmentContainer.getChance()){
                meta.addEnchant(enchantmentContainer.getEnchantment(), random.nextInt(enchantmentContainer.getMinLevel(), enchantmentContainer.getMaxLevel()+1), true);
                bonusAmount--;
            }
        }
        if(!hasBonusEnchantmentAmount()) bonusAmount = 0;

        ArrayList<EnchantmentContainer> guaranteed = new ArrayList<>(guaranteedEnchantments);
        int guaranteedAmount = hasGuaranteedEnchantmentAmount() ? gEnchantmentAmount : guaranteedEnchantments.size();
        if(overrideBonusEnchantments) guaranteedAmount += bonusAmount;

        int amountToRemove = guaranteedEnchantments.size()-guaranteedAmount;
        for(int i = 0; i < amountToRemove; i++){
            guaranteed.remove(random.nextInt(guaranteed.size()));
        }
        for(EnchantmentContainer enchantmentContainer : guaranteed){
            meta.addEnchant(enchantmentContainer.getEnchantment(), random.nextInt(enchantmentContainer.getMinLevel(), enchantmentContainer.getMaxLevel()+1), true);
        }

        if(hasFlags()){
            for(ItemFlag flag : itemFlags){
                meta.addItemFlags(flag);
            }
        }

        if(hasDurability()){
            if(meta instanceof Damageable damageable){
                double percent = material.getMaxDurability()*(random.nextDouble(minDurability, maxDurability+1)/100);
                damageable.setDamage((int)percent);
            }
        }

        meta.setUnbreakable(unbreakable);

        if(hasAttributes()){
            for(Attribute attribute : attributes.keySet()){
                meta.addAttributeModifier(attribute, attributes.get(attribute));
            }
        }

        if(isSkull() && hasSkullURL()){
            SkullMeta skullMeta = (SkullMeta) meta;
            PlayerProfile profile = Bukkit.createPlayerProfile(UUID.fromString("105ca6b8-65d9-46ab-82aa-05e1fc902d67"), "yaml_parser");
            try{
                profile.getTextures().setSkin(new URL("https://textures.minecraft.net/texture/"+skullURL));
            }catch(MalformedURLException e){
                throw new RuntimeException(e);
            }
            skullMeta.setOwnerProfile(profile);
        }

        if(isPotion()){
            PotionMeta potionMeta = (PotionMeta) meta;
            if(hasEffects()){
                for(PotionEffect effect : effects){
                    potionMeta.addCustomEffect(effect, true);
                }
            }
            if(hasColor()){
                potionMeta.setColor(potionColor);
            }
        }

        stack.setItemMeta(meta);

        return stack;
    }

    public ItemContainer clone() throws CloneNotSupportedException {
        return (ItemContainer) super.clone();
    }
}

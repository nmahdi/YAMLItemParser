package com.yamlitemparser;

import org.bukkit.*;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class YAMLItemParser {

    private Random random = ThreadLocalRandom.current();

    public static final String DOT = ".";
    public static final String MATERIAL = "material";
    public static final String DISPLAY_NAME = "name";
    public static final String AMOUNT = "amount";
    public static final String LORE = "lore";
    public static final String ENCHANTS = "enchantments";
    public static final String GUARANTEED_ENCHANTS_AMOUNT = "guaranteed-enchantment-amount";
    public static final String BONUS_ENCHANTS_AMOUNT = "bonus-enchantment-amount";
    public static final String OVERRIDE_BONUS_ENCHANTS = "override-bonus-enchantments";
    public static final String GLOW = "glow";
    public static final String FLAGS = "item-flags";
    public static final String DURABILITY = "durability";
    public static final String UNBREAKABLE = "unbreakable";
    public static final String ATTRIBUTES = "attributes";
    public static final String ATTRIBUTE_VALUE = "value";
    public static final String ATTRIBUTE_OPERATION = "operation";
    public static final String ATTRIBUTE_EQUIPMENT = "equipment-slot";
    public static final String SKULL_URL = "skull-url";
    public static final String POTION_EFFECTS = "potion-effects";
    public static final String POTION_COLOR = "potion-color";

    public ItemContainer parseFromConfiguration(FileConfiguration yml, String itemName){
        itemName = itemName + DOT;
        ItemContainer container = new ItemContainer(Material.valueOf(yml.getString(itemName + MATERIAL).toUpperCase()));

        if(yml.contains(itemName + DISPLAY_NAME)){
            container.setDisplayName(yml.getString(itemName + DISPLAY_NAME));
        }

        if(yml.contains(itemName + AMOUNT)){
            String[] amount = yml.getString(itemName + AMOUNT).split("-");
            container.setMinAmount(Integer.parseInt(amount[0]));
            container.setMaxAmount(amount.length > 1 ? Integer.parseInt(amount[1]) : container.getMinAmount());
        }

        if(yml.contains(itemName + LORE)){
            for(String line : yml.getStringList(itemName + LORE)){
                container.addLore(line);
            }
        }

        if(yml.contains(itemName + ENCHANTS)){
            for(String line : yml.getStringList(itemName + ENCHANTS)){
                String[] split = line.split(":");
                String[] level = split[1].split("-");

                int minLevel = Integer.parseInt(level[0]);
                int maxLevel = minLevel;
                if(level.length > 1){
                    maxLevel = Integer.parseInt(level[1]);
                }

                double chance = 100;
                if(split.length > 2){
                    chance = Double.parseDouble(split[2]);
                }

                EnchantmentContainer enchantmentContainer = new EnchantmentContainer(Registry.ENCHANTMENT.get(NamespacedKey.minecraft(split[0].toLowerCase())), minLevel, maxLevel, chance);
                if(enchantmentContainer.isGuaranteed()){
                    container.addGuaranteedEnchantment(enchantmentContainer);
                }else{
                    container.addBonusEnchantment(enchantmentContainer);
                }
            }
        }

        if (yml.contains(itemName + GUARANTEED_ENCHANTS_AMOUNT)) {
            container.setGuaranteedEnchantmentAmount(yml.getInt(itemName + GUARANTEED_ENCHANTS_AMOUNT));
        }
        if(yml.contains(itemName + BONUS_ENCHANTS_AMOUNT)){
            container.setBonusEnchantmentAmount(yml.getInt(itemName + BONUS_ENCHANTS_AMOUNT));
        }
        if(yml.contains(itemName + OVERRIDE_BONUS_ENCHANTS)){
            container.setOverrideBonusEnchantments(yml.getBoolean(itemName + OVERRIDE_BONUS_ENCHANTS));
        }

        if(yml.contains(itemName + GLOW)){
            container.setGlowing(yml.getBoolean(itemName + GLOW));
        }

        if(yml.contains(itemName + DURABILITY)){
            String[] durability = yml.getString(itemName + DURABILITY).split("-");
            container.setMinDurability(Integer.parseInt(durability[0]));
            container.setMaxDurability(durability.length > 1 ? Integer.parseInt(durability[1]) : container.getMinDurability());
        }

        if(yml.contains(itemName + UNBREAKABLE)){
            container.setUnbreakable(yml.getBoolean(itemName + UNBREAKABLE));
        }

        if(yml.contains(itemName + ATTRIBUTES)){
            ConfigurationSection section = yml.getConfigurationSection(itemName + ATTRIBUTES);
            Set<String> attributes = section.getKeys(false);
            for(AttributeParser attributeParser : AttributeParser.values()){
                if(attributeParser.getAttribute() == null){
                    continue;
                }
                if(attributes.contains(attributeParser.getKeyword())){
                    container.addAttribute(attributeParser.getAttribute(), new AttributeModifier(UUID.randomUUID(), "modifier", section.getDouble(itemName + ATTRIBUTES + DOT + ATTRIBUTE_VALUE), AttributeModifier.Operation.valueOf(section.getString(itemName + ATTRIBUTES + DOT + ATTRIBUTE_OPERATION)), EquipmentSlot.valueOf(section.getString(itemName + ATTRIBUTES + DOT + ATTRIBUTE_EQUIPMENT))));
                }
            }
        }

        if (yml.contains(itemName + FLAGS)) {
            List<String> flags = yml.getStringList(itemName + FLAGS);
            for(String flag : flags){
                container.addItemFlag(ItemFlag.valueOf(flag));
            }
        }

        if(container.isPotion()) {
            if(yml.contains(itemName + POTION_EFFECTS)) {
                List<String> effects = yml.getStringList(itemName + POTION_EFFECTS);
                for (String effect : effects) {
                    String[] parsed = effect.split(":");
                    PotionEffectType potionEffectType = Registry.EFFECT.get(NamespacedKey.minecraft(parsed[0]));
                    if (potionEffectType != null) {
                        container.addEffect(new PotionEffect(potionEffectType, Integer.parseInt(parsed[2]) * 20, Integer.parseInt(parsed[1]) - 1));
                    }
                }
            }
            if(yml.contains(itemName + POTION_COLOR)){
                String[] colors = yml.getString(itemName + POTION_COLOR).split(":");
                container.setPotionColor(Color.fromRGB(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2])));
            }
        }

        if(container.isSkull() && yml.contains(itemName + SKULL_URL)){
            container.setSkullURL(yml.getString(itemName + SKULL_URL));
        }

        return container;
    }

    public ItemContainer parseFromConfiguration(ItemContainer mergedContainer, FileConfiguration yml, String itemName) throws CloneNotSupportedException {
        itemName = itemName + DOT;
        ItemContainer container = mergedContainer.clone();

        if(yml.contains(itemName + DISPLAY_NAME)){
            container.setDisplayName(yml.getString(itemName + DISPLAY_NAME));
        }

        if(yml.contains(itemName + AMOUNT)){
            String[] amount = yml.getString(itemName + AMOUNT).split("-");
            container.setMinAmount(Integer.parseInt(amount[0]));
            container.setMaxAmount(amount.length > 1 ? Integer.parseInt(amount[1]) : container.getMinAmount());
        }

        if(yml.contains(itemName + LORE)){
            for(String line : yml.getStringList(itemName + LORE)){
                container.addLore(line);
            }
        }

        if(yml.contains(itemName + ENCHANTS)){
            for(String line : yml.getStringList(itemName + ENCHANTS)){
                String[] split = line.split(":");
                String[] level = split[1].split("-");

                int minLevel = Integer.parseInt(level[0]);
                int maxLevel = minLevel;
                if(level.length > 1){
                    maxLevel = Integer.parseInt(level[1]);
                }

                double chance = 100;
                if(split.length > 2){
                    chance = Double.parseDouble(split[2]);
                }

                EnchantmentContainer enchantmentContainer = new EnchantmentContainer(Registry.ENCHANTMENT.get(NamespacedKey.minecraft(split[0].toLowerCase())), minLevel, maxLevel, chance);
                if(enchantmentContainer.isGuaranteed()){
                    container.addGuaranteedEnchantment(enchantmentContainer);
                }else{
                    container.addBonusEnchantment(enchantmentContainer);
                }
            }
        }

        if (yml.contains(itemName + GUARANTEED_ENCHANTS_AMOUNT)) {
            container.setGuaranteedEnchantmentAmount(yml.getInt(itemName + GUARANTEED_ENCHANTS_AMOUNT));
        }
        if(yml.contains(itemName + BONUS_ENCHANTS_AMOUNT)){
            container.setBonusEnchantmentAmount(yml.getInt(itemName + BONUS_ENCHANTS_AMOUNT));
        }
        if(yml.contains(itemName + OVERRIDE_BONUS_ENCHANTS)){
            container.setOverrideBonusEnchantments(yml.getBoolean(itemName + OVERRIDE_BONUS_ENCHANTS));
        }

        if(yml.contains(itemName + GLOW)){
            container.setGlowing(yml.getBoolean(itemName + GLOW));
        }

        if(yml.contains(itemName + DURABILITY)){
            String[] durability = yml.getString(itemName + DURABILITY).split("-");
            container.setMinDurability(Integer.parseInt(durability[0]));
            container.setMaxDurability(durability.length > 1 ? Integer.parseInt(durability[1]) : container.getMinDurability());
        }

        if(yml.contains(itemName + UNBREAKABLE)){
            container.setUnbreakable(yml.getBoolean(itemName + UNBREAKABLE));
        }

        if(yml.contains(itemName + ATTRIBUTES)){
            ConfigurationSection section = yml.getConfigurationSection(itemName + ATTRIBUTES);
            Set<String> attributes = section.getKeys(false);
            for(AttributeParser attributeParser : AttributeParser.values()){
                if(attributeParser.getAttribute() == null){
                    continue;
                }
                if(attributes.contains(attributeParser.getKeyword())){
                    container.addAttribute(attributeParser.getAttribute(), new AttributeModifier(UUID.randomUUID(), "modifier", section.getDouble(itemName + ATTRIBUTES + DOT + ATTRIBUTE_VALUE), AttributeModifier.Operation.valueOf(section.getString(itemName + ATTRIBUTES + DOT + ATTRIBUTE_OPERATION)), EquipmentSlot.valueOf(section.getString(itemName + ATTRIBUTES + DOT + ATTRIBUTE_EQUIPMENT))));
                }
            }
        }

        if (yml.contains(itemName + FLAGS)) {
            List<String> flags = yml.getStringList(itemName + FLAGS);
            for(String flag : flags){
                container.addItemFlag(ItemFlag.valueOf(flag));
            }
        }

        if(container.isPotion()) {
            if(yml.contains(itemName + POTION_EFFECTS)) {
                List<String> effects = yml.getStringList(itemName + POTION_EFFECTS);
                for (String effect : effects) {
                    String[] parsed = effect.split(":");
                    PotionEffectType potionEffectType = Registry.EFFECT.get(NamespacedKey.minecraft(parsed[0]));
                    if (potionEffectType != null) {
                        container.addEffect(new PotionEffect(potionEffectType, Integer.parseInt(parsed[2]) * 20, Integer.parseInt(parsed[1]) - 1));
                    }
                }
            }
            if(yml.contains(itemName + POTION_COLOR)){
                String[] colors = yml.getString(itemName + POTION_COLOR).split(":");
                container.setPotionColor(Color.fromRGB(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2])));
            }
        }

        if(container.isSkull() && yml.contains(itemName + SKULL_URL)){
            container.setSkullURL(yml.getString(itemName + SKULL_URL));
        }

        return container;
    }

    public ItemStack buildFromContainer(ItemContainer container){
        ItemStack itemStack = new ItemStack(container.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        int amount = container.getMinAmount();
        if(!container.staticAmount()){
            amount = random.nextInt(container.getMinAmount(), container.getMaxAmount()+1);
        }
        itemStack.setAmount(amount);

        if(container.hasDisplayName()){
            itemMeta.setDisplayName(container.getDisplayName());
        }

        if(container.hasLore()){
            lore.addAll(container.getLore());
        }

        if(container.hasModelData()){
            itemMeta.setCustomModelData(container.getModelData());
        }

        int bonusAmount = container.getBonusEnchantmentAmount();
        if(!container.hasBonusEnchantmentAmount()) bonusAmount = container.getBonusEnchantments().size();

        for(EnchantmentContainer enchantmentContainer : container.getBonusEnchantments()){
            if(bonusAmount == 0) break;
            double chance = random.nextDouble(100);
            if(chance <= enchantmentContainer.getChance()){
                itemMeta.addEnchant(enchantmentContainer.getEnchantment(), random.nextInt(enchantmentContainer.getMinLevel(), enchantmentContainer.getMaxLevel()+1), true);
                bonusAmount--;
            }
        }
        if(!container.hasBonusEnchantmentAmount()) bonusAmount = 0;

        ArrayList<EnchantmentContainer> guaranteedEnchantments = new ArrayList<>(container.getGuaranteedEnchantments());
        int guaranteedAmount = container.getGuaranteedEnchantmentAmount();
        if(container.overrideBonusEnchantments()) guaranteedAmount += bonusAmount;
        if(!container.hasGuaranteedEnchantmentAmount()) guaranteedAmount = container.getGuaranteedEnchantments().size();


        int amountToRemove = guaranteedEnchantments.size()-guaranteedAmount;
        for(int i = 0; i < amountToRemove; i++){
            guaranteedEnchantments.remove(random.nextInt(guaranteedEnchantments.size()));
        }
        for(EnchantmentContainer enchantment : guaranteedEnchantments){
            itemMeta.addEnchant(enchantment.getEnchantment(), random.nextInt(enchantment.getMinLevel(), enchantment.getMaxLevel()+1), true);
        }

        if(container.isGlowing()){
            itemMeta.setEnchantmentGlintOverride(true);
        }

        if(container.hasFlags()){
            for(ItemFlag flag : container.getItemFlags()){
                itemMeta.addItemFlags(flag);
            }
        }

        if(container.hasDurability()){
            if(itemMeta instanceof Damageable damageable) {
                double percent = container.getMaterial().getMaxDurability() * (random.nextDouble(container.getMinDurability(), container.getMaxDurability() + 1) / 100);
                damageable.setDamage((int) percent);
            }
        }

        if(container.isUnbreakable()){
            itemMeta.setUnbreakable(true);
        }

        if(container.hasAttributes()) {
            for (AttributeParser attributeParser : AttributeParser.values()) {
                if (container.getAttributes().containsKey(attributeParser.getAttribute())){
                    itemMeta.addAttributeModifier(attributeParser.getAttribute(), container.getAttributes().get(attributeParser.getAttribute()));
                }
            }
        }

        if(container.isSkull() && container.hasSkullURL()){
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            PlayerProfile profile = Bukkit.createPlayerProfile(UUID.fromString("105ca6b8-65d9-46ab-82aa-05e1fc902d67"), "custom_item");
            try {
                profile.getTextures().setSkin(new URL("https://textures.minecraft.net/texture/"+container.getSkullURL()));
                skullMeta.setOwnerProfile(profile);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        if(container.isPotion()){
            PotionMeta potionMeta = (PotionMeta) itemMeta;
            if(container.hasEffects()){
                for(PotionEffect effect : container.getEffects()){
                    potionMeta.addCustomEffect(effect, true);
                }
            }
            if(container.hasColor()){
                potionMeta.setColor(container.getPotionColor());
            }
        }


        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}

package com.yamlitemparser;


import org.bukkit.attribute.Attribute;
import org.w3c.dom.Attr;

public enum AttributeParser {

    MaxHealth("health", "GENERIC_MAX_HEALTH"),
    FollowRange("follow-range", "GENERIC_FOLLOW_RANGE"),
    KnockbackResistance("knockback-resistance", "GENERIC_KNOCKBACK_RESISTANCE"),
    MovementSpeed("speed", "GENERIC_MOVEMENT_SPEED"),
    FlyingSpeed("flying-speed", "GENERIC_FLYING_SPEED"),
    AttackDamage("damage", "GENERIC_ATTACK_DAMAGE"),
    AttackSpeed("attack-speed", "GENERIC_ATTACK_SPEED"),
    Armor("armor", "GENERIC_ARMOR"),
    ArmorToughness("armor-toughness", "GENERIC_ARMOR_TOUGHNESS"),
    Luck("luck", "GENERIC_LUCK"),
    MaxAbsorption("absorption", "GENERIC_MAX_ABSORPTION"),
    HorseJump("horse-jump", "HORSE_JUMP_STRENGTH"),
    ZombieSpawnReinforcements("zombie-reinforcement", "ZOMBIE_SPAWN_REINFORCEMENTS");

    private final String keyword;
    private final Attribute attribute;

    AttributeParser(String keyword, String attribute){
        this.keyword = keyword;
        this.attribute = Attribute.valueOf(attribute);
    }

    public String getKeyword() {
        return keyword;
    }

    public Attribute getAttribute() {
        return attribute;
    }
}

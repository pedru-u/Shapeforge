package com.pedru.NEA;

import java.util.ArrayList;

public class SaveData {
    public int saveVersion = 1;

    public int gameLevel;

    public int slashDamage;
    public int projectileDamage;

    public int AttackClass;
    public int health;
    public int maxHealth;
    public int speed;

    public int healthPotionCount;
    public int gold;

    public ArrayList<AbilityEntry> abilities = new ArrayList<>();

    public SaveData() {}

    public static class AbilityEntry{
        public String name;
        public int level;

        public AbilityEntry(){}
        public AbilityEntry(String name, int level){
            this.name = name;
            this.level = level;
        }
    }
}

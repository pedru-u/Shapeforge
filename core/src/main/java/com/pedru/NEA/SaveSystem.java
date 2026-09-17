package com.pedru.NEA;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

public class SaveSystem {
    private static final String PREF_NAME= "game_save";
    private static final String SAVE_NAME = "save_data";

    public static void save(Player player){
        SaveData data = new SaveData();

        data.gameLevel = GameScreen.gameLevel;

        data.slashDamage = player.getSlashDamage();
        data.projectileDamage = player.getProjectileDamage();

        data.AttackClass = player.getAttackClass();
        data.health = player.getHealth();
        data.maxHealth = player.getMaxHealth();
        data.speed = player.getSpeed();

        data.healthPotionCount = player.getHealthPotionCount();
        data.gold = player.getGold();

        for(Ability ability : player.getAbilities()){
            data.abilities.add(
                new SaveData.AbilityEntry(ability.getName().name(),ability.getLevel())
            );
        }

        Json json = new Json();
        String jsonText = json.toJson(data);

        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        prefs.putString(SAVE_NAME, jsonText);
        prefs.flush();

    }
}

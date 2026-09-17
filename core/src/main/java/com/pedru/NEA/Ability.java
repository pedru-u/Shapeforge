package com.pedru.NEA;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public abstract class Ability {
    protected int level;
    protected Name name;
    protected double cooldown;
    protected double timer;
    protected boolean abilityReady;
    protected float levelUpFactor;
    protected boolean active;
    protected Texture buttonTexture;
    protected Texture iconTexture;
    public enum Name{
        BLINK,
        SPEED,
        KA,
        BOMB,
        STOP
    }

    public Ability(int level){
        this.level = level;
    }

    abstract void use(Player player, double angle, double distance, Grid grid);

    public Name getName() {
        return name;
    }

    public void upgrade(){
        this.level++;
    }

    public boolean isReady() {
        return abilityReady;
    }

    public void increaseCounter(){
        this.timer += Gdx.graphics.getDeltaTime();
        if(timer >= (cooldown-(level*levelUpFactor))){
            this.abilityReady = true;
        }else{
            this.abilityReady = false;
        }

    }


    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getTimer() {
        return timer;
    }

    public double getCooldown() {
        return cooldown;
    }



    public void setActive(boolean active) {
        this.active = active;
    }

    public int getLevel() {
        return level;
    }

    public float getLevelUpFactor() {
        return levelUpFactor;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public boolean isActive() {
        return active;
    }

    public Texture getIconTexture() {
        return iconTexture;
    }

    public void setIconTexture(Texture iconTexture) {
        this.iconTexture = iconTexture;
    }

    public Texture getButtonTexture() {
        return buttonTexture;
    }

    public void setButtonTexture(Texture buttonTexture) {
        this.buttonTexture = buttonTexture;
    }
}

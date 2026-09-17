package com.pedru.NEA;

import com.badlogic.gdx.Gdx;

public class KillAura extends Ability{
    private float activeTime = 5;
    private float activeTimer = 0;
    private int baseDamage = 3;
    private double baseRadius = 50;
    public KillAura(int level){
        super(level);
        this.cooldown = 5;
        this.levelUpFactor = 0.2f;
        this.timer = cooldown;
        this.name = Name.KA;
        this.active = false;
    }
    @Override
    public void use(Player player, double angle, double distance, Grid grid){
        active = true;
        setTimer(0);
    }

    @Override
    public void increaseCounter(){
        if(!active){
            this.timer += Gdx.graphics.getDeltaTime();
            activeTimer = 0;
            if(timer >=(cooldown-(level*levelUpFactor))){
                this.abilityReady = true;
            }else{
                this.abilityReady = false;
            }
        }else{
            activeTimer += Gdx.graphics.getDeltaTime();
            if(activeTimer >= activeTime + (level)){
                activeTimer = 0;
                active = false;
                //in player create a circle that kills the enemies if ka is activated
            }
        }
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public double getBaseRadius() {
        return baseRadius;
    }
}

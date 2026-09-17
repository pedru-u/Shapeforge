package com.pedru.NEA;

import com.badlogic.gdx.Gdx;

public class Stop extends Ability{
    private float activeTimer = 0;
    private float activeTime = 2f;
    public Stop(int level){
        super(level);
        this.cooldown = 10;
        this.levelUpFactor = 0.2f;
        this.timer = cooldown;
        this.name = Name.STOP;
        this.active = false;
    }
    @Override
    public void use(Player player, double angle, double distance, Grid grid){
        this.active = true;
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
            if(activeTimer >= activeTime + (level*levelUpFactor*activeTime)){
                activeTimer = 0;
                active = false;
            }
        }
    }
}

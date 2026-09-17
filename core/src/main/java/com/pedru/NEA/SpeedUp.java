package com.pedru.NEA;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import static com.pedru.NEA.GameScreen.player;

public class SpeedUp extends Ability {
    private float activeTime = 2;
    private float activeTimer = 0;
    private int baseIncrease = 5;

    public SpeedUp(int level){
        super(level);
        this.name = Name.SPEED;
        this.cooldown = 4;
        this.levelUpFactor = 0.5f;
        this.timer = cooldown;
        this.active = false;
    }

    @Override
    public void use(Player player, double angle,double distance,Grid grid){
        float stepConstant = 1f/(player.getSpeed() + baseIncrease + level);
        player.setStepConstant(stepConstant);
        active = true;
        setTimer(0);
    }


    @Override
    public void increaseCounter(){
        if(!active){
            this.timer += Gdx.graphics.getDeltaTime();
            activeTimer = 0;
            if(timer >= (cooldown-(level*levelUpFactor))){
                this.abilityReady = true;
            }else{
                this.abilityReady = false;
            }
        }else{
            activeTimer += Gdx.graphics.getDeltaTime();
            if(activeTimer >= activeTime + (level)){
                activeTimer = 0;
                active = false;
                player.setStepConstant( 1f/(player.getSpeed()));
            }
        }

    }


}

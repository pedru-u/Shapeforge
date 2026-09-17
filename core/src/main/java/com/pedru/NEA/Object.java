package com.pedru.NEA;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.pedru.NEA.GameScreen.gameLevel;

public class Object extends Entity{
    private int initialHealth;
    private int maxHealthBarWidth = 30;
    private int healthBarHeight = 5;

    public Object(int startX, int startY, int speed, int Class, int health, Square[][] grid){
        super(startX,startY,speed,Class,health,grid);
        this.initialHealth = health;
        this.type = Type.OBJECT;
    }

    public boolean checkDeath(Player player){
        if(this.health<=0){
            int randomNumber = random.nextInt(11)+1;
            if(randomNumber ==10){
                player.setHealthPotionCount(player.getHealthPotionCount()+1);
            }
            int goldReward = random.nextInt(11)-5;
            player.addGold(goldReward + 4*gameLevel);
            return true;
        }
        return false;
    }

    public void displayHealthBar(ShapeRenderer sr){
        float healthBarWidth = ((float) health /initialHealth) * maxHealthBarWidth;
        sr.setColor(0.46f, 0.86f, 0.46f,1f);
        sr.rect(this.drawX-15, this.drawY + 20,healthBarWidth,healthBarHeight);
    }

    public void drawObject(ShapeRenderer sr){
        sr.setColor(92/255f,64/255f,51/255f,1f);
        sr.circle(this.drawX,this.drawY,10);
        displayHealthBar(sr);

    }
}

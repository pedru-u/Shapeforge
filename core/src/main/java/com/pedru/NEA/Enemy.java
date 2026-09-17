package com.pedru.NEA;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Enemy extends Entity{
    private Sprite slashSprite;
    private int slashFrameCounter = 6;
    private float kaTimer = 1.5f;
    private float kaCooldown = 1.5f;
    private float maxHealthBarWidth = 30;
    private float healthBarHeight = 5;
    private float initialHealth;
    //Health bar color (0.46f, 0.86f, 0.46f,1f);
    public Enemy(int startX,int startY, int speed, int Class, int health, Square[][] grid, Texture slashTexture){
        super(startX,startY,speed, 3, health,grid);
        this.initialHealth = health;
        this.stepConstant = 1f/speed;
        this.Class = Class;
        if(Class == 1){
            this.attackCooldown = 1f;
        }else if(Class == 3){
            this.attackCooldown = 2f;
        }
        this.type = Type.ENEMY;
        this.slashSprite = new Sprite(slashTexture);
    }

    public void drawEnemy(ShapeRenderer sr){
        sr.circle(drawX, drawY,10);
    }

    public void displayHealthBar(ShapeRenderer sr){
        float healthBarWidth = (health/initialHealth) * maxHealthBarWidth;
        sr.setColor(0.46f, 0.86f, 0.46f,1f);
        sr.rect(this.drawX-15, this.drawY + 20,healthBarWidth,healthBarHeight);
    }


    public boolean playerInSight(Player player, ArrayList<Square> path){
        //Get vector from player to enemy (playerX-EnemyX,playerY-EnemyY)
        float playerX = player.getDrawX();
        float playerY = player.getDrawY();
        float playerVectorX = playerX - drawX;
        float playerVectorY = playerY - drawY;
        //get vector of monster direction of sight (monster to next square location)
        if(path.isEmpty()){
            return true;
        }
        Square nextSquare = path.get(0);

        float squareX = nextSquare.getIsoCenterX();
        float squareY = nextSquare.getIsoCenterY();
        float enemyVectorX = squareX - drawX;
        float enemyVectorY = squareY - drawY;
        //dot product the vectors
        float dotProduct = (playerVectorX*enemyVectorX) + (playerVectorY*enemyVectorY);
        float playerVectorSize = (float) Math.sqrt((playerVectorX*playerVectorX)+(playerVectorY*playerVectorY));
        float enemyVectorSize = (float) Math.sqrt((enemyVectorX*enemyVectorX)+(enemyVectorY*enemyVectorY));
        //if arccos(dot product divided by the size of both vectors multiplied) < 90 then the player is within sight otherwise not.
        double angle = Math.acos((dotProduct)/(playerVectorSize*enemyVectorSize));

        return angle <= ((Math.PI/2));
    }

    public Sprite getSlashSprite() {
        return slashSprite;
    }

    public int getSlashFrameCounter() {
        return slashFrameCounter;
    }

    public void setSlashFrameCounter(int slashFrameCounter) {
        this.slashFrameCounter = slashFrameCounter;
    }

    public float getKaTimer() {
        return kaTimer;
    }

    public void setKaTimer(float kaTimer) {
        this.kaTimer = kaTimer;
    }

    public float getKaCooldown() {
        return kaCooldown;
    }

}

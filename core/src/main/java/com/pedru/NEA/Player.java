package com.pedru.NEA;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Player extends Entity{
    private ArrayList<Ability> abilities = new ArrayList<>();
    private int healthPotionCount;
    private int maxHealth;
    private int gold;
    private int baseSpeed;
    public Player(int startX, int startY, int speed, int Class, int health, Square[][] grid){
        super(startX,startY,speed,Class,health,grid);
        this.stepConstant = 1f/speed;
        this.baseSpeed = speed;
        this.Class = Class;
        this.maxHealth = health;
        if(Class == 1){
            this.attackCooldown = 0.3f;
        }else if(Class == 2){
            this.attackCooldown = 0.6f;
        }
        this.type = Type.PLAYER;
    }

    public void drawPlayer(ShapeRenderer sr){
        sr.circle(drawX, drawY,10);
    }

    public void addAbility(Ability ability){
        if(abilities.size() < 3){
            abilities.add(ability);
        }
    }

    public void upgradeAbility(Ability ability){
        for(Ability a : abilities){
            if(a.getName() == ability.getName()){
                ability.upgrade();
            }
        }
    }

    public void usePotion(){
        this.health = Math.min((int)(health+0.3*maxHealth), maxHealth);
    }

    public void upgradePlayer(){
        this.maxHealth +=50;
        this.slashDamage += 5;
        this.projectileDamage += 3;
        this.speed +=2;
        this.health = Math.min(health + 50, maxHealth);
    }



    public int getHealthPotionCount() {
        return healthPotionCount;
    }

    public void setHealthPotionCount(int healthPotionCount) {
        this.healthPotionCount = healthPotionCount;
    }

    public void useAbility(Ability ability, double angle, double distance, Grid grid, ArrayList<Projectile> projectileList){
        ability.use(this,angle,distance,grid);
        if(ability.getName() == Ability.Name.BOMB){
            Bomb bomb = (Bomb) ability;
            double xTarget = drawX + Math.cos(angle)*distance;
            double yTarget = drawY + Math.sin(angle)*distance;
            double xSpeed = Math.cos(angle)*bomb.getBomb().getSpeed();
            double ySpeed = Math.sin(angle)*bomb.getBomb().getSpeed();
            bomb.getBomb().setxTarget(xTarget);
            bomb.getBomb().setyTarget(yTarget);
            bomb.getBomb().setxSpeed(xSpeed);
            bomb.getBomb().setySpeed(ySpeed);
            bomb.getBomb().setRadius(bomb.getBaseExplosionRadius()+(bomb.getBaseExplosionRadius()*bomb.getLevelUpFactor()*bomb.getLevel()));
            bomb.getBomb().setDamage((int)(bomb.getBaseDamage()+(bomb.getBaseDamage()*bomb.getLevelUpFactor()*bomb.getLevel())));
            System.out.println("Ability Used in Player:" + bomb.getName());
            bomb.getBomb().setAmmoType(Projectile.ammo.BOMB);
            projectileList.add(bomb.getBomb());

        }
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public void setBaseSpeed(int baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addGold(int gold){
        this.gold += gold;
    }

    public int getGold(){
        return gold;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}

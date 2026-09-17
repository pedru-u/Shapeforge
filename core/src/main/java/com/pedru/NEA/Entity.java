package com.pedru.NEA;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

import static com.pedru.NEA.GameScreen.*;
import static java.lang.Math.abs;

public class Entity {
    protected int gridX, gridY, speed, health;
    protected float timer = 0f , stepConstant, moveDistanceX, moveDistanceY, drawX, drawY, attackCooldown, attackTimer, moveProgress;
    protected int Class;
    protected ArrayList<Square> path;
    protected int slashDamage;
    protected int projectileDamage;
    protected Type type;
    public enum Type{
        PLAYER,
        ENEMY,
        OBJECT
    }
    //0 for objects
    //1 archer
    //2 fighter
    //3 enemy slash

    public Entity(int startX, int startY, int speed, int Class, int health, Square[][] gameGrid){
        this.gridX = startX;
        this.gridY = startY;
        this.speed = speed;
        this.Class = Class;
        this.path = new ArrayList<>();
        this.health = health;
        this.drawX = gameGrid[gridX][gridY].getIsoCenterX();
        this.drawY = gameGrid[gridX][gridY].getIsoCenterY();
    }

    public boolean increaseCounter(boolean adjacent){
        this.timer += Gdx.graphics.getDeltaTime();
        if(this.timer >= stepConstant){
            this.timer = 0f;
            return true;
        }
        this.moveProgress = timer/stepConstant;
        if(!path.isEmpty() && !adjacent){
            this.moveDistanceX = (path.get(0).getIsoCenterX() - this.drawX) * moveProgress;
            this.moveDistanceY = (path.get(0).getIsoCenterY() - this.drawY) * moveProgress;
            this.drawX += moveDistanceX;
            this.drawY += moveDistanceY;
        }
        return false;
    }

    public void moveToNextSquare(){
        setX(path.get(0).getX());
        setY(path.get(0).getY());
        setDrawX(path.get(0).getIsoCenterX());
        setDrawY(path.get(0).getIsoCenterY());
        path.remove(0);
    }

    public boolean increaseAttackCounter(){
        this.attackTimer += Gdx.graphics.getDeltaTime();
        if(this.attackTimer >= attackCooldown){
            return true;
        }
        return false;
    }



    public boolean attack(double xTarget, double yTarget, ArrayList<Enemy> enemyList,ArrayList<Projectile> projectiles){
        switch(this.Class){
            case 0:
                break;
            case 1:
                //attack using triangle
                double xDiff = xTarget - this.getDrawX();
                double yDiff = yTarget - this.getDrawY();
                double angle = Math.atan2(yDiff,xDiff);
                Projectile projectile = new Projectile(5,this.getDrawX(),this.getDrawY(), this);
                double xMove = projectile.getSpeed() * Math.cos(angle);
                double yMove = projectile.getSpeed() * Math.sin(angle);
                projectile.setxSpeed(xMove);
                projectile.setySpeed(yMove);
                projectile.setDamage(this.projectileDamage);
                projectiles.add(projectile);
                break;
            case 2:
                //attack using slash
                double attackAngle = Math.atan2(yTarget - this.getDrawY(),xTarget - this.getDrawX());
                if(!enemyList.isEmpty()){
                    for(Enemy enemy : enemyList){
                        double dx = enemy.getDrawX() - this.getDrawX();
                        double dy = enemy.getDrawY() - this.getDrawY();
                        double radius = (dx*dx) + (dy*dy);
                        double enemyAngle = Math.atan2(dy,dx);
                        double diff = Math.atan2(Math.sin(attackAngle - enemyAngle),(Math.cos(attackAngle - enemyAngle)));
                        //check if in radius and attackAngle + angle <pi/2 or attackAngle - angle <pi/2
                        if(radius <= 60*60 && (abs(diff) <=Math.PI/2)){
                            ShapeRenderer sr = new ShapeRenderer();
                            sr.begin(ShapeRenderer.ShapeType.Filled);
                            sr.setColor(Color.WHITE);
                            enemy.drawEnemy(sr);
                            sr.end();
                            enemy.damage(slashDamage);
                        }
                    }
                }
                if(!objectList.isEmpty()){
                    for(Object object : objectList){
                        double dx = object.getDrawX() - this.getDrawX();
                        double dy = object.getDrawY() - this.getDrawY();
                        double radius = (dx*dx) + (dy*dy);
                        double enemyAngle = Math.atan2(dy,dx);
                        double diff = Math.atan2(Math.sin(attackAngle - enemyAngle),(Math.cos(attackAngle - enemyAngle)));
                        //check if in radius and attackAngle + angle <pi/2 or attackAngle - angle <pi/2
                        if(radius <= 60*60 && (abs(diff) <=Math.PI/2)){
                            ShapeRenderer sr = new ShapeRenderer();
                            sr.begin(ShapeRenderer.ShapeType.Filled);
                            sr.setColor(Color.WHITE);
                            object.drawObject(sr);
                            sr.end();
                            object.damage(slashDamage);
                        }
                    }
                }
                return true;
            case 3:
                double dx = xTarget - this.getDrawX();
                double dy = yTarget - this.getDrawY();
                double radius = (dx*dx) + (dy*dy);
                if(radius <= 50*50){
                    player.damage(slashDamage);
                }
                return true;
                //enemy slash
        }
        return false;
    }

    public Boolean checkCollisions(ArrayList<Projectile> projectileList){
        if(!projectileList.isEmpty()){
            for(int i = projectileList.size()-1; i>=0; i--){
                Projectile projectile = projectileList.get(i);
                if(projectile.isExploded()){
                    continue;
                }
                double dx = this.getDrawX() - projectile.getX();
                double dy = this.getDrawY() - projectile.getY();
                double distance = (dx*dx) + (dy*dy);
                if(projectile.getAmmoType() == Projectile.ammo.BOMB && this instanceof Enemy){
                    //bomb draw radius is 20 and enemy draw radius is 10
                    if(distance < (projectile.getRadius()*0.5+10)*(0.5*projectile.getRadius()+10)){
                        projectile.explode(enemyList);
                        bomb.setTimer(0);
                        return null;
                    }

                    //get difference, and allow for a little discrepancy due to floating point errors (just the 20 pixels of the radius)
                }else{
                    if(distance < (15)*(15) && !(projectile.getOwner().getType() == this.getType())){
                        int damage = projectile.getDamage();
                        damage(damage);
                        projectileList.remove(projectile);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void displayHealthBar(){

    }

    public void damage(int damage){
        this.health -= damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getX(){
        return gridX;
    }
    public int getY(){
        return gridY;
    }
    public void setX(int x){
        this.gridX = x;
    }
    public void setY(int y){
        this.gridY = y;
    }

    public float getDrawY() {
        return drawY;
    }

    public void setDrawY(float drawY) {
        this.drawY = drawY;
    }

    public float getDrawX() {
        return drawX;
    }

    public void setDrawX(float drawX) {
        this.drawX = drawX;
    }

    public ArrayList<Square> getPath() {
        return path;
    }

    public void setPath(ArrayList<Square> path) {
        this.path = path;
    }

    public int getAttackClass() {
        return Class;
    }

    public void setAttackClass(int aClass) {
        this.Class = aClass;
    }

    public Type getType() {
        return type;
    }

    public void setAttackTimer(float attackTimer) {
        this.attackTimer = attackTimer;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setStepConstant(float stepConstant) {
        this.stepConstant = stepConstant;
    }

    public float getStepConstant() {
        return stepConstant;
    }

    public float getAttackCooldown() {
        return attackCooldown;
    }

    public int getSlashDamage() {
        return slashDamage;
    }

    public void setSlashDamage(int slashDamage) {
        this.slashDamage = slashDamage;
    }

    public int getProjectileDamage() {
        return projectileDamage;
    }

    public void setProjectileDamage(int projectileDamage) {
        this.projectileDamage = projectileDamage;
    }
}



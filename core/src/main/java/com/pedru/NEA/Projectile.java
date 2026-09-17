package com.pedru.NEA;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Projectile {

    protected int speed;
    protected double x, y, xSpeed, ySpeed;
    protected Entity owner;
    protected ammo ammoType;
    private double xTarget;
    private double yTarget;
    private double radius;
    private int bombCounter = 6;
    private boolean exploded;
    private int damage;


    public enum ammo{
        BOMB,
        ARROW
    }

    public Projectile(int speed, double spawnX, double spawnY, Entity owner){
        this.speed = speed;
        this.x = spawnX;
        this.y = spawnY;
        this.owner = owner;
        this.ammoType = ammo.ARROW;
    }

    public void move(){
        setX(this.x + this.xSpeed);
        setY(this.y + this.ySpeed);
    }

    public void draw(ShapeRenderer sr){
        if(ammoType == ammo.BOMB){
            sr.setColor(Color.BLACK);
            sr.circle((float) x,(float) y,(int)(0.5*radius));
        }
        else if(owner instanceof Player){
                sr.setColor(Color.BLUE);
                sr.circle((float) x,(float) y,5);
        }
        else{
            sr.setColor(Color.RED);
            sr.circle((float) x,(float) y,5);
        }

    }

    public void explode(ArrayList<Enemy> enemyList){
        for(Enemy enemy : enemyList){
            double enemyX = enemy.getDrawX();
            double enemyY = enemy.getDrawY();
            double xDiff = enemyX - this.getX();
            double yDiff = enemyY - this.getY();
            double distanceSquared = xDiff*xDiff + yDiff*yDiff;
            if(distanceSquared <= radius*radius){
                ShapeRenderer sr = new ShapeRenderer();
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(Color.WHITE);
                enemy.drawEnemy(sr);
                sr.end();
                enemy.damage(damage);

            }
        }
        this.exploded = true;
        this.bombCounter = 0;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public Entity getOwner() {
        return owner;
    }

    public ammo getAmmoType() {
        return ammoType;
    }

    public double getyTarget() {
        return yTarget;
    }

    public void setyTarget(double yTarget) {
        this.yTarget = yTarget;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getxTarget() {
        return xTarget;
    }

    public void setxTarget(double xTarget) {
        this.xTarget = xTarget;
    }

    public void setAmmoType(ammo ammoType) {
        this.ammoType = ammoType;
    }

    public int getBombCounter() {
        return bombCounter;
    }

    public void setBombCounter(int bombCounter) {
        this.bombCounter = bombCounter;
    }

    public boolean isExploded() {
        return exploded;
    }



}

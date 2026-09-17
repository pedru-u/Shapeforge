package com.pedru.NEA;

import java.util.ArrayList;


public class Bomb extends Ability{
    private double baseExplosionRadius = 40;
    private double baseDamage = 10;
    private Projectile bomb;


    public Bomb(int level){
        super(level);
        this.cooldown = 5;
        this.name = Name.BOMB;
        this.timer = cooldown;
        this.levelUpFactor = 0.2f;
    }
    @Override
    public void use(Player player, double angle, double distance, Grid grid){
        this.bomb = new Projectile(3,player.getDrawX(),player.getDrawY(), player);
        setTimer(0);
    }

    public double getBaseExplosionRadius() {
        return baseExplosionRadius;
    }

    public void setBaseExplosionRadius(double baseExplosionRadius) {
        this.baseExplosionRadius = baseExplosionRadius;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(double baseDamage) {
        this.baseDamage = baseDamage;
    }

    public Projectile getBomb() {
        return bomb;
    }


}

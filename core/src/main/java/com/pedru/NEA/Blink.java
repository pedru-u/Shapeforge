package com.pedru.NEA;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Blink extends Ability{
    private float maxDistance = 100;


    public Blink(int level){
        super(level);
        this.name = Name.BLINK;
        this.cooldown = 5;
        this.levelUpFactor =  0.5f;
        this.timer = cooldown;
    }

    @Override
    void use(Player player, double angle,double distance, Grid grid){
        ShapeRenderer sr = new ShapeRenderer();
        int deviceWidth = 1920;
        int deviceHeight = 1080;
        double playerDrawX = player.getDrawX();
        double playerDrawY = player.getDrawY();
        double Distance = (Math.min(distance, maxDistance + (levelUpFactor * level * maxDistance)));
        double moveX = (Distance*Math.cos(angle));
        double moveY = (Distance*Math.sin(angle));
        double newPlayerDrawX = playerDrawX + moveX;
        double newPlayerDrawY = playerDrawY + moveY;
        Square tempSquare = grid.gameGrid[0][0];
        float width = tempSquare.getw();
        float height = tempSquare.geth();

        float offsetX = (0.5f*(deviceWidth) - width);
        float offsetY = (0.125f*deviceHeight - 8f*height);

        float Xp = (float) newPlayerDrawX-offsetX;
        float Xy = (float) newPlayerDrawY-offsetY;

        float gridX = 0.5f*((Xp/width) + (Xy/height));
        float gridY = 0.5f*((Xy/height) - (Xp/width));

        int coordinateX = Math.round(gridX);
        int coordinateY = Math.round(gridY);


        if(((coordinateX < 0 || coordinateX>99 || coordinateY <0 || coordinateY > 99) || (!grid.gameGrid[coordinateX][coordinateY].isTraversable()))){
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.BLACK);
            sr.circle((float)newPlayerDrawX,(float)newPlayerDrawY,10);
            sr.end();
        }else{
            player.setX(coordinateX);
            player.setY(coordinateY);
            Square playerSquare = grid.gameGrid[player.getX()][player.getY()];
            player.setDrawX(playerSquare.getIsoCenterX());
            player.setDrawY(playerSquare.getIsoCenterY());
            ArrayList<Square> empty = new ArrayList<>();
            player.setPath(empty);
            setTimer(0);
        }


        //set cooldown to started and shit
    }


}

package com.pedru.NEA;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import static java.lang.Math.abs;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private static ShapeRenderer sr;
    private static PolygonSpriteBatch polyBatch;
    public static final int deviceWidth = 1920;
    public static final int deviceHeight = 1080;
    private static SpriteBatch floor_batch;
    private static SpriteBatch sprites;
    public static int playerClass;
    public static boolean menu = true;
    public static boolean end = false;
    public static boolean create;
    public static boolean load;
    private static GameScreen game;
    private MainMenu menuScreen;





    @Override
    public void create() {
        sr = new ShapeRenderer();
        floor_batch = new SpriteBatch();
        polyBatch = new PolygonSpriteBatch();
        sprites = new SpriteBatch();
        menuScreen = new MainMenu(sr,sprites);
        createNewGame();


    }
    public static void createNewGame(){
        game = new GameScreen(sr,floor_batch,polyBatch,sprites);
    }

    @Override
    public void render() {
        //menu();
        if(menu){
            menuScreen.run();
        }else{
            end = game.run();
            menu = end;
        }

    }

    @Override
    public void dispose() {
        sr.dispose();
        floor_batch.dispose();
        polyBatch.dispose();
    }
}

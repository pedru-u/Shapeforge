package com.pedru.NEA;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Json;

import static com.pedru.NEA.GameScreen.deviceHeight;

public class MainMenu {
    private ShapeRenderer sr;
    private SpriteBatch sprites;
    private boolean classScreen = false;
    private boolean noSave = false;
    private GlyphLayout layout = new GlyphLayout();
    private BitmapFont font = new BitmapFont();
    public static Texture mainMenu = new Texture("mainMenu.png");
    public static Texture createButton = new Texture("createButton.png");
    public static Texture loadButton = new Texture("loadButton.png");
    public static Texture exitButton = new Texture("exitButton.png");
    public static Texture fighterButton =  new Texture("fighterButton.png");
    public static Texture archerButton = new Texture("archerButton.png");
    public static Texture classMenu = new Texture("classMenu.png");
    public MainMenu(ShapeRenderer sr, SpriteBatch sprites){
        this.sr = sr;
        this.sprites = sprites;
    }

    public void run(){
        if(classScreen){
            int potentialPlayerClass = displayClassScreen();
            if(potentialPlayerClass >0){
                int playerClass = potentialPlayerClass;
                Main.playerClass = playerClass;
                GameScreen.gameLevel =1;
                Main.load = false;
                Main.createNewGame();
                Main.create = true;
                Main.menu = false;
            }
        }else{
            Button[] menuButtons = {new Button(460,637,1000,200), new Button(460,375,1000,200), new Button(460,113,1000,200)};
            sprites.begin();
            sprites.draw(mainMenu,0,0);
            sprites.draw(createButton,460,637,1000,200);
            sprites.draw(loadButton,460,375,1000,200);
            sprites.draw(exitButton,460,113,1000,200);
            if(noSave){
                font.getData().setScale(2f);
                font.setColor(Color.RED);
                String noSaveMessage = "No saves found";
                layout.setText(font,noSaveMessage);
                float x = 960 - layout.width/2;
                float y = 375 - layout.height;
                font.draw(sprites,layout,x,y);
            }
            sprites.end();
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.RED);
            for(int i = 0;i<menuButtons.length;i++){
                float mouseX = Gdx.input.getX();
                float mouseY = deviceHeight - Gdx.input.getY();

                if(menuButtons[i].onButton(mouseX,mouseY)){
                    for (int n = 0; n < 4; n++) {
                        sr.rect(menuButtons[i].getX() + n, menuButtons[i].getY() + n, menuButtons[i].getWidth() - 2 * n, menuButtons[i].getHeight() - 2 * n);
                    }
                    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                        switch(i){
                            case 0:
                                classScreen =  true;
                                break;
                            case 1:
                                Preferences prefs = Gdx.app.getPreferences("game_save");
                                System.out.println(prefs.contains("save_data"));
                                if(prefs.contains("save_data")){
                                    Json json = new Json();
                                    SaveData data = json.fromJson(SaveData.class,prefs.getString("save_data"));
                                    Main.load = true;
                                    System.out.println(data.AttackClass);
                                    Main.playerClass = data.AttackClass;
                                    Main.createNewGame();
                                    Main.create = false;
                                    Main.menu = false;
                                }else{
                                    noSave = true;
                                }

                                break;
                            case 2:
                                Gdx.app.exit();
                                break;
                        }
                    }
                }

            }
            sr.end();
            //display the main menu
        }
    }



    public int displayClassScreen(){
        Button[] classButtons = {new Button(62,620,1000,200),new Button(62,260,1000,200)};
        sprites.begin();
        sprites.draw(classMenu,0,0);
        sprites.draw(fighterButton,62,620,1000,200);
        sprites.draw(archerButton,62,260,1000,200);
        sprites.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        for(int i =0; i< classButtons.length;i++){
            float mouseX = Gdx.input.getX();
            float mouseY = deviceHeight - Gdx.input.getY();
            if(classButtons[i].onButton(mouseX,mouseY)){
                for (int n = 0; n < 4; n++) {
                    sr.rect(classButtons[i].getX() + n, classButtons[i].getY() + n, classButtons[i].getWidth() - 2 * n, classButtons[i].getHeight() - 2 * n);
                }
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                    switch(i){
                        case 0:
                            sr.end();
                            classScreen = false;
                            return 2;
                        case 1:
                            sr.end();
                            classScreen = false;
                            return 1;
                    }
                }
            }

        }
        sr.end();
        return -1;
    }

}

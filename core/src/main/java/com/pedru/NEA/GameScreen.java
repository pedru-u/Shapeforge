package com.pedru.NEA;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.pedru.NEA.Main.playerClass;


public class GameScreen {
    private Grid grid;
    private ShapeRenderer sr;
    private BitmapFont font = new BitmapFont();
    private GlyphLayout layout = new GlyphLayout();
    GLProfiler glProfiler;
    public static Pixmap pix;
    private Square[][] gameArea;
    private PolygonSpriteBatch polyBatch;
    private SpriteBatch sprites;
    public static Texture TraversableColorTexture;
    public static Texture NonTraversableColorTexture;
    public static final int deviceWidth = 1920;
    public static final int deviceHeight = 1080;
    public static Pathfinding pathfinding;
    public static Player player;
    public boolean move;
    public boolean enemyMove;
    private SpriteBatch floor_batch;
    private Enemy testEnemy;
    private ArrayList<Projectile> projectileList;
    public static ArrayList<Enemy> enemyList;
    public static ArrayList<Object> objectList;
    private final int amountXSquares = 100;
    private final int amountYSquares = 100;
    private ArrayList<Square> enemyPath;
    int randomX = random.nextInt(amountXSquares);
    int randomY = random.nextInt(amountYSquares);
    boolean spotted;
    private Texture slashTexture;
    private Sprite playerSlashSprite;
    private int slashFrameCounter = 6;
    private float attackBufferTimer;
    private boolean attackBuffered;
    private boolean attackReady = true;
    public boolean upgradeScreen = false;
    public boolean shopScreenShown = false;
    public boolean paused = false;
    private Map Map;
    ArrayList<Integer> mapList = new ArrayList<>();
    private int bombCounter = 6;
    private boolean timeStopped = false;
    private SpeedUp speed = new SpeedUp(0);
    private Blink blink = new Blink(0);
    public static Bomb bomb = new Bomb(0);
    private KillAura ka = new KillAura(0);
    private Stop stop = new Stop(0);
    private Ability[] allAbilities = {speed,blink,bomb,ka,stop};
    private ArrayList<Ability> upgradeAbilities = new ArrayList<>();
    private boolean firstShopFrame = true;
    int numberOfPotions;
    int potionCost;
    int abilityCost;
    int upgradeCost;
    int abilityNumber;
    int numberOfAbility;
    int numberOfUpgrade;
    public static int gameLevel = 1;
    private int currentNumberofRooms;
    public Texture BlinkAbilityIcon = new Texture(Gdx.files.internal("BlinkAbility.png"));
    public Texture StopAbilityIcon = new Texture(Gdx.files.internal("StopAbility.png"));
    public Texture SpeedUpAbilityIcon = new Texture(Gdx.files.internal("SpeedUpAbility.png"));
    public Texture KillAuraAbilityIcon = new Texture(Gdx.files.internal("KillAuraAbility.png"));
    public Texture BombAbilityIcon = new Texture(Gdx.files.internal("BombAbility.png"));
    public Texture BlinkAbilityButton = new Texture(Gdx.files.internal("BlinkAbilityButton.png"));
    public Texture StopAbilityButton = new Texture(Gdx.files.internal("StopAbilityButton.png"));
    public Texture SpeedUpAbilityButton = new Texture(Gdx.files.internal("SpeedAbilityButton.png"));
    public Texture KillAuraAbilityButton = new Texture(Gdx.files.internal("KillAuraAbilityButton.png"));
    public Texture BombAbilityButton = new Texture(Gdx.files.internal("BombAbilityButton.png"));
    public Texture LevelUpScreen = new Texture(Gdx.files.internal("LevelUpScreen.png"));
    public Texture goldTexture = new Texture(Gdx.files.internal("gold.png"));
    public Texture shopScreen = new Texture(Gdx.files.internal("shopScreen.png"));
    public Texture upgradeShop = new Texture(Gdx.files.internal("upgradeShop.png"));
    public Texture healthPotionShop = new Texture(Gdx.files.internal("healthPotionShop.png"));
    public Texture healthPotion = new Texture(Gdx.files.internal("healthPotion.png"));
    public Texture saveButton = new Texture("saveButton.png");
    public Texture continueButton = new Texture("continueButton.png");
    public Texture pauseMenu = new Texture("pauseMenu.png");
    public Texture quitButton = new Texture("quitButton.png");
    public Texture menuButton = new Texture("menuButton.png");
    public Texture gameOverMenu = new Texture("gameOverMenu.png");
    private Sprite healthSprite = new Sprite(healthPotion);
    private Sprite goldSprite = new Sprite(goldTexture);



    public GameScreen(ShapeRenderer sr, SpriteBatch floor_batch, PolygonSpriteBatch polyBatch, SpriteBatch sprites) {
        blink.setIconTexture(BlinkAbilityIcon);
        blink.setButtonTexture(BlinkAbilityButton);
        speed.setIconTexture(SpeedUpAbilityIcon);
        speed.setButtonTexture(SpeedUpAbilityButton);
        stop.setIconTexture(StopAbilityIcon);
        stop.setButtonTexture(StopAbilityButton);
        ka.setIconTexture(KillAuraAbilityIcon);
        ka.setButtonTexture(KillAuraAbilityButton);
        bomb.setIconTexture(BombAbilityIcon);
        bomb.setButtonTexture(BombAbilityButton);
        pix = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pix.setColor(0x2E6F40FF);
        pix.fill();
        TraversableColorTexture = new Texture(pix);
        pix.setColor(0x58CCEDFF);
        pix.fill();
        NonTraversableColorTexture = new Texture(pix);
        Map = new Map();
        grid = new Grid(18, amountXSquares,amountYSquares,Map);
        grid.createGrid();
        gameArea = grid.gameGrid;
        pathfinding = new Pathfinding(grid);
        slashTexture = new Texture(Gdx.files.internal("slash.png"));
        player = new Player(0,0,10, playerClass, 500,gameArea);
        player.setSlashDamage(10);
        player.setProjectileDamage(3);
        projectileList = new ArrayList<>();
        enemyList = new ArrayList<>();
        objectList = new ArrayList<>();
        this.sr = sr;
        this.floor_batch = floor_batch;
        this.polyBatch = polyBatch;
        this.sprites = sprites;
        //165 172 slash sprite
        playerSlashSprite = new Sprite(slashTexture);
        Preferences prefs = Gdx.app.getPreferences("game_save");
        if(Main.load){
            Json json = new Json();
            SaveData data = json.fromJson(SaveData.class,prefs.getString("save_data"));
            gameLevel = data.gameLevel;
            player.setSlashDamage(data.slashDamage);
            player.setProjectileDamage(data.projectileDamage);
            player.setAttackClass(data.AttackClass);
            player.setHealth(data.health);
            player.setMaxHealth(data.maxHealth);
            player.setSpeed(data.speed);
            player.setHealthPotionCount(data.healthPotionCount);
            player.setGold(data.gold);
            for(SaveData.AbilityEntry abilityEntry:data.abilities){
                Ability.Name name = Ability.Name.valueOf(abilityEntry.name);
                for(Ability ability: allAbilities){
                    if(ability.getName() == name){
                        player.addAbility(ability);
                        ability.setLevel(abilityEntry.level);
                    }
                }
            }
        }
        handleMapPicking();
        startNewRoom();




    }

    public boolean run(){
        if(!paused){
            if(!upgradeScreen){
                if(player.getHealth()<=0){
                    displayDeathScreen();
                    return Main.menu;
                }
                checkRoomDone();
                ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1f);
                if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
                    paused = true;
                }
                handlePlayerAttackBuffer();
                polyBatch.begin();
                drawMap();
                polyBatch.end();
                handlePlayerMovement();
                handlePlayerAbilities();
                handlePlayerAttack();
                handlePlayerUsePotion();
                movePlayer();

                handleEnemyDeaths();
                handleEnemyLoop();
                sr.begin(ShapeRenderer.ShapeType.Filled);
                handleObjects();
                if(player.checkCollisions(projectileList)){
                    sr.setColor(Color.ORANGE);
                }else{
                    sr.setColor(Color.GREEN);
                }

                player.drawPlayer(sr);
                sr.end();
                sr.begin(ShapeRenderer.ShapeType.Filled);
                handleProjectiles();
                sr.end();
                drawSlash();
                displayPlayerHud();
            }else if(shopScreenShown){
                showShopScreen();

            }else{
                showUpgradeScreen();
            }
        }else{
            return displayPauseMenu();
        }

        return false;
    }

    public void displayDeathScreen(){
        Button[] pauseButtons = {new Button(460,540,1000,200), new Button(460,180,1000,200)};
        sprites.begin();
        sprites.draw(gameOverMenu,0,0);
        sprites.draw(menuButton,460,540);
        sprites.draw(quitButton,460,180);
        sprites.end();
        for(int i =0; i< pauseButtons.length;i++){
            float mouseX = Gdx.input.getX();
            float mouseY = deviceHeight - Gdx.input.getY();
            if (pauseButtons[i].onButton(mouseX, mouseY)) {
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.setColor(Color.RED);
                for (int n = 0; n < 4; n++) {
                    sr.rect(pauseButtons[i].getX() + n, pauseButtons[i].getY() + n, pauseButtons[i].getWidth() - 2 * n, pauseButtons[i].getHeight() - 2 * n);
                }
                sr.end();
                Preferences prefs = Gdx.app.getPreferences("game_save");
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                    switch(i){
                        case 0:
                            Main.menu = true;
                            prefs.clear();
                            prefs.flush();
                            break;
                        case 1:
                            prefs.clear();
                            prefs.flush();
                            Gdx.app.exit();
                            break;
                    }
                }

            }
        }
    }

    public void handlePlayerUsePotion(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            if(player.getHealthPotionCount() > 0){
                player.usePotion();
                player.setHealthPotionCount(player.getHealthPotionCount()-1);
            }
        }
    }

    public boolean displayPauseMenu(){
        Button[] pauseButtons = {new Button(460,540,1000,200), new Button(460,180,1000,200)};
        sprites.begin();
        sprites.draw(pauseMenu,0,0);
        sprites.draw(continueButton,460,540);
        sprites.draw(saveButton,460,180);
        sprites.end();
        for(int i =0; i< pauseButtons.length;i++){
            float mouseX = Gdx.input.getX();
            float mouseY = deviceHeight - Gdx.input.getY();
            if (pauseButtons[i].onButton(mouseX, mouseY)) {
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.setColor(Color.RED);
                for (int n = 0; n < 4; n++) {
                    sr.rect(pauseButtons[i].getX() + n, pauseButtons[i].getY() + n, pauseButtons[i].getWidth() - 2 * n, pauseButtons[i].getHeight() - 2 * n);
                }
                sr.end();
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                    switch(i){
                        case 0:
                            paused = false;
                            return false;
                        case 1:
                            paused = false;
                            SaveSystem.save(player);
                            return true;

                    }
                }

            }
        }
        return false;
    }

    public boolean checkBombAnimation(double x, double y, double radius, Projectile projectile){
        if(projectile.getBombCounter() <6){
            sr.setColor(Color.WHITE);
            sr.circle((float) x,(float) y,(float) (0.2f*radius*projectile.getBombCounter()));
            projectile.setBombCounter(projectile.getBombCounter() + 1);
            return true;
        }
        return false;
    }

    public void checkRoomDone(){
        if(enemyList.isEmpty()){
            upgradeScreen = true;
            if(!mapList.isEmpty()){
                return;
            }
            shopScreenShown = true;
            player.upgradePlayer();
            handleMapPicking();

        }
    }

    public void displayPlayerHud(){
        float healthBarProgress = (float) player.getHealth()/(float) (player.getMaxHealth());
        float maxBarWidth = 600f;
        float barHeight = 100;
        float barWidth = maxBarWidth*healthBarProgress;
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.46f, 0.86f, 0.46f,1f);
        sr.rect(20,60,barWidth,barHeight);
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.BLACK);
        for(int i =0;i<6;i++){
            sr.rect(20+i,60+i,maxBarWidth-2*i,barHeight-2*i);
        }
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        float width = 218;
        float height = 218;
        ArrayList<Ability> playerAbilities = player.getAbilities();
        for(int i =0; i<3;i++){
            switch(i){
                case 0:
                    sr.circle(1437f+width/2,(28f+height/2),width/2);
                    break;
                case 1:
                    sr.circle(1694f+width/2,28f+height/2,width/2);
                    break;
                case 2:
                    sr.circle(1694f+width/2,285+height/2,width/2);
                    break;
            }
        }
        sr.end();
        sprites.begin();
        //Drawing abilities
        for(int i =0; i<3;i++){
            switch(i){
                case 0:
                    if(playerAbilities.size() >= i+1){
                        if(!playerAbilities.get(i).isReady()){
                            float abilityCooldown = (float) (playerAbilities.get(i).getCooldown()-(playerAbilities.get(i).getLevel()*playerAbilities.get(i).getLevelUpFactor()) - playerAbilities.get(i).getTimer());
                            String abilityCooldownText = String.format("%.1f", abilityCooldown);
                            font.getData().setScale(3f);
                            font.setColor(Color.WHITE);
                            layout.setText(font,abilityCooldownText);

                            float x = 1437f+width/2 - layout.width/2f;
                            float y = (28f+height/2) + layout.height/2f;
                            font.draw(sprites,layout,x,y);
                        }
                        else{
                            Sprite abilitySprite = new Sprite(playerAbilities.get(i).getIconTexture());
                            abilitySprite.setPosition(1437,28);
                            abilitySprite.setSize(218,218);
                            abilitySprite.draw(sprites);
                        }

                    }
                    break;
                case 1:
                    if(playerAbilities.size() >= i+1){
                        if(!playerAbilities.get(i).isReady()){
                            float abilityCooldown = (float) (playerAbilities.get(i).getCooldown()-(playerAbilities.get(i).getLevel()*playerAbilities.get(i).getLevelUpFactor()) - playerAbilities.get(i).getTimer());
                            String abilityCooldownText = String.format("%.1f", abilityCooldown);
                            font.getData().setScale(3f);
                            font.setColor(Color.WHITE);
                            layout.setText(font,abilityCooldownText);

                            float x = 1694f+width/2 - layout.width/2f;
                            float y = (28f+height/2) + layout.height/2f;
                            font.draw(sprites,layout,x,y);
                        }

                        else{
                            Sprite abilitySprite = new Sprite(playerAbilities.get(i).getIconTexture());
                            abilitySprite.setPosition(1694,28);
                            abilitySprite.setSize(218,218);
                            abilitySprite.draw(sprites);
                        }
                    }
                    break;
                case 2:
                    if(playerAbilities.size() >= i+1){
                        if(!playerAbilities.get(i).isReady()){
                            float abilityCooldown = (float) (playerAbilities.get(i).getCooldown()-(playerAbilities.get(i).getLevel()*playerAbilities.get(i).getLevelUpFactor()) - playerAbilities.get(i).getTimer());
                            String abilityCooldownText = String.format("%.1f", abilityCooldown);
                            font.getData().setScale(3f);
                            font.setColor(Color.WHITE);
                            layout.setText(font,abilityCooldownText);

                            float x = 1694f+width/2 - layout.width/2f;
                            float y = (285f+height/2) + layout.height/2f;
                            font.draw(sprites,layout,x,y);
                        }
                        else{
                            Sprite abilitySprite = new Sprite(playerAbilities.get(i).getIconTexture());
                            abilitySprite.setPosition(1694,285);
                            abilitySprite.setSize(218,218);
                            abilitySprite.draw(sprites);
                        }
                    }
                    break;
            }

        }
        //Drawing Gold
        goldSprite.setPosition(1810,934);
        goldSprite.setSize(110,146);
        goldSprite.draw(sprites);
        String playerGold =  Integer.toString(player.getGold());
        font.getData().setScale(3f);
        layout.setText(font,playerGold);
        float x = 1810 - layout.width - 20;
        float y = 934f+(146/2f) + layout.height/2;
        font.draw(sprites,layout,x,y);
        //Drawing Health Potions
        //sprite x = 1812, y = 775, width = 108, y = 155
        healthSprite.setPosition(1812,775);
        healthSprite.setSize(108,155);
        healthSprite.draw(sprites);
        String playerPotionCount = Integer.toString(player.getHealthPotionCount());
        layout.setText(font,playerPotionCount);
        x = 1812 - layout.width-20;
        y = 775 +(155/2f) + layout.height/2 -10;
        font.draw(sprites,layout,x,y);
        //Showing level and rooms
        //Level -> x = 20, y = 1055
        font.getData().setScale(5f);
        String levelData = ("Level: " + gameLevel);
        layout.setText(font,levelData);
        font.draw(sprites,layout,20,1055);
        int roomNumber = currentNumberofRooms - mapList.size();
        String roomData = ("Room: " + roomNumber + "/" + currentNumberofRooms);
        layout.setText(font,roomData);
        font.draw(sprites,layout,20,972);
        sprites.end();
    }

    public void showShopScreen(){
        if(firstShopFrame){
            numberOfPotions = random.nextInt(3*gameLevel)+1;
            numberOfAbility = gameLevel;
            numberOfUpgrade = gameLevel;
            potionCost = random.nextInt(20*gameLevel+1)-10*gameLevel +20*gameLevel;
            abilityCost = random.nextInt((40*gameLevel)+1) -20*gameLevel + 50*gameLevel;
            upgradeCost = random.nextInt((40*gameLevel)+1) - 20*gameLevel + 50*gameLevel;
            abilityNumber = random.nextInt(player.getAbilities().size());
            firstShopFrame = false;
        }
        sprites.begin();
        sprites.draw(shopScreen,0,0);

        font.getData().setScale(3f);
        String potionStock = ("Stock: " + numberOfPotions);
        layout.setText(font,potionStock);
        float xAddition = 8;
        float yAddition = 350;
        ArrayList<Button> shopButtons = new ArrayList<>();
        shopButtons.add(new Button(108,45,500,810));
        if(numberOfPotions !=0){
            sprites.draw(healthPotionShop,108,45);
            font.draw(sprites,layout, 108+xAddition,45+yAddition-layout.height);
            String potionCostData = ("Cost: " + potionCost);
            if(player.getGold()<=potionCost){
                font.setColor(Color.RED);
            }
            layout.setText(font,potionCostData);
            font.draw(sprites,layout,108,45);
            font.setColor(Color.WHITE);
        }
        shopButtons.add(new Button(710,45,500,810));
        if(numberOfUpgrade !=0){
            sprites.draw(upgradeShop,710,45);
            String abilityCostData = ("Cost: " + upgradeCost);
            if(player.getGold()<=upgradeCost){
                font.setColor(Color.RED);
            }
            layout.setText(font,abilityCostData);
            font.draw(sprites,layout,710,45);
            font.setColor(Color.WHITE);
        }
        shopButtons.add(new Button(1312,45,500,810));
        if(numberOfAbility != 0){
            sprites.draw(player.getAbilities().get(abilityNumber).getButtonTexture(),1312f,45f);
            String abilityCostData = ("Cost: " + abilityCost);
            if(player.getGold()<=abilityCost){
                font.setColor(Color.RED);
            }
            layout.setText(font,abilityCostData);
            font.draw(sprites,layout,1312,45);
            font.setColor(Color.WHITE);
        }

        goldSprite.setPosition(1810,934);
        goldSprite.setSize(110,146);
        goldSprite.draw(sprites);
        String playerGold =  Integer.toString(player.getGold());
        font.getData().setScale(3f);
        layout.setText(font,playerGold);
        float x = 1810 - layout.width - 20;
        float y = 934f+(146/2f) + layout.height/2;
        font.draw(sprites,layout,x,y);


        sprites.end();
        for(int i=0;i<shopButtons.size();i++) {
            float mouseX = Gdx.input.getX();
            float mouseY = deviceHeight - Gdx.input.getY();
            if (shopButtons.get(i).onButton(mouseX, mouseY)) {
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.setColor(Color.RED);
                for (int n = 0; n < 4; n++) {
                    sr.rect(shopButtons.get(i).getX() + n, shopButtons.get(i).getY() + n, shopButtons.get(i).getWidth() - 2 * n, shopButtons.get(i).getHeight() - 2 * n);
                }
                sr.end();
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                    switch(i) {
                        case 0:
                            if (player.getGold() >= potionCost && numberOfPotions != 0) {
                                player.setHealthPotionCount(player.getHealthPotionCount() + 1);
                                player.setGold(player.getGold() - potionCost);
                                numberOfPotions--;
                            }
                            break;
                        case 1:
                            if (player.getGold() >= upgradeCost && numberOfUpgrade != 0) {
                                player.upgradePlayer();
                                player.setGold(player.getGold() - upgradeCost);
                                numberOfUpgrade--;
                            }
                            break;
                        case 2:
                            if (player.getGold() >= abilityCost && numberOfAbility != 0) {
                                player.upgradeAbility(player.getAbilities().get(abilityNumber));
                                player.setGold(player.getGold() - abilityCost);
                                numberOfAbility--;
                            }
                            break;
                    }
                }
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            shopScreenShown = false;
            firstShopFrame = true;
            gameLevel++;
        }

    }

    public void showUpgradeScreen(){
        ArrayList<Ability> playerAbilities = player.getAbilities();
        for(Ability ability : playerAbilities){
            ability.setTimer(ability.getCooldown());
            ability.setActive(false);
        }
        ArrayList<Button> abilityButtons = new ArrayList<>();
        if(upgradeAbilities.isEmpty()){
            for(int i =0; i<4; i++){
                int abilityNumber = random.nextInt(5)+1;
                Ability chosenAbility = null;
                switch(abilityNumber){
                    case 1:
                        chosenAbility = blink;
                        break;
                    case 2:
                        chosenAbility = speed;
                        break;
                    case 3:
                        chosenAbility = stop;
                        break;
                    case 4:
                        chosenAbility = ka;
                        break;
                    case 5:
                        chosenAbility = bomb;
                        break;
                }
                if(!upgradeAbilities.contains(chosenAbility)){
                    upgradeAbilities.add(chosenAbility);
                }else{
                    i--;
                }

            }
        }
        if(playerAbilities.size() == 3){
            upgradeAbilities.set(0,playerAbilities.get(0));
            upgradeAbilities.set(1,playerAbilities.get(1));
            upgradeAbilities.set(2,playerAbilities.get(2));

        }
        int j = 1;
        sprites.begin();
        sprites.draw(LevelUpScreen,0,0);
        for(Ability ability : upgradeAbilities){
            Sprite abilitySprite = new Sprite(ability.getButtonTexture());
            abilitySprite.setSize(500,810);
            switch(j){
                case 1:
                    abilityButtons.add(new Button(108,45,500,810));
                    abilitySprite.setPosition(108,45);
                    abilitySprite.draw(sprites);
                    break;
                case 2:
                    abilityButtons.add(new Button(710,45,500,810));
                    abilitySprite.setPosition(710,45);
                    abilitySprite.draw(sprites);
                    break;
                case 3:
                    abilityButtons.add(new Button(1312,45,500,810));
                    abilitySprite.setPosition(1312,45);
                    abilitySprite.draw(sprites);
                    break;
            }
            j++;
        }
        sprites.end();
        for(int i=0;i<abilityButtons.size();i++){
            float mouseX = Gdx.input.getX();
            float mouseY = deviceHeight - Gdx.input.getY();
            if(abilityButtons.get(i).onButton(mouseX,mouseY)){
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.setColor(Color.RED);
                for(int n =0;n<4;n++){
                    sr.rect(abilityButtons.get(i).getX()+n,abilityButtons.get(i).getY()+n,abilityButtons.get(i).getWidth()-2*n,abilityButtons.get(i).getHeight()-2*n);
                }
                sr.end();
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                    if(playerAbilities.contains(upgradeAbilities.get(i))){
                        player.upgradeAbility(upgradeAbilities.get(i));
                    }else{
                        player.addAbility(upgradeAbilities.get(i));
                    }
                    upgradeAbilities.clear();
                    startNewRoom();
                    upgradeScreen = false;
                    break;
                };
            }
        }

    }

    public void startNewRoom(){
        grid.setMap(mapList.get(0));
        mapList.remove(0);
        pathfinding.setGrid(grid);
        player.setPath(new ArrayList<Square>());
        handlePlayerSpawn();
        enemyList.clear();
        projectileList.clear();
        objectList.clear();
        handleEnemySpawns();
        handleObjectSpawns();
    }

    public void handleMapPicking(){
        int maxNumberOfMaps = Math.min(6 + 2*gameLevel,25);
        int maxMapNumberOffset = Math.min(gameLevel,3);
        int mapNumberOffset = random.nextInt(maxMapNumberOffset*2 +1) - maxMapNumberOffset;
        int numberOfMaps = maxNumberOfMaps/2 + mapNumberOffset;
        for(int i = 0; i<numberOfMaps;i++){
            int mapPointer = random.nextInt(24);
            if(mapList.contains(mapPointer)){
                i--;
                continue;
            }
            mapList.add(mapPointer);
        }
        currentNumberofRooms = numberOfMaps;
    }

    public void handlePlayerSpawn(){
        //runs when a new map is loaded in
        int playerSpawnRange = 5;
        boolean valid = false;
        while (!valid){
            int[] coordinateSet = new int[2];
            int corner = random.nextInt(5) + 1;
            switch(corner){
                case 1:
                    coordinateSet = new int[]{50, 50};
                    break;
                case 2:
                    coordinateSet = new int[]{10, 10};
                    break;
                case 3:
                    coordinateSet = new int[]{10,90};
                    break;
                case 4:
                    coordinateSet = new int[]{90,10};
                    break;
                case 5:
                    coordinateSet = new int[]{90,90};
                    break;
            }
            int[] offsets = {random.nextInt(playerSpawnRange*2 + 1)-5+coordinateSet[0],random.nextInt(playerSpawnRange*2+1)-5+coordinateSet[1]};
            int x = offsets[0];
            int y = offsets[1];
            Square potentialSquare = grid.gameGrid[x][y];
            if(potentialSquare.isTraversable()){
                player.setX(x);
                player.setY(y);
                player.setDrawX(potentialSquare.getIsoCenterX());
                player.setDrawY(potentialSquare.getIsoCenterY());
                valid = true;
            }
        }
    }

    public void handleEnemySpawns(){
        int maxEnemies = Math.min(25,6*gameLevel);
        int middleMaxEnemies = maxEnemies/2;
        int enemyNumberOffsetRange = 2*gameLevel;
        int enemyNumberOffset = random.nextInt(enemyNumberOffsetRange*2 + 1)-(enemyNumberOffsetRange/2);
        int enemyNumber = middleMaxEnemies + enemyNumberOffset;
        for(int i= 0;i<enemyNumber;i++){
            int enemyMaxHealth = 30+6*gameLevel;
            int enemyHealthOffsetRange = 2*gameLevel;
            int enemyHealthOffset = random.nextInt(enemyHealthOffsetRange*2 + 1)-(enemyHealthOffsetRange/2);
            int enemyHealth = (enemyMaxHealth/2) + enemyHealthOffset;
            int enemyMaxDamage = 10+ 2*gameLevel;
            int enemyDamageOffsetRange = gameLevel;
            int enemyDamageOffset = random.nextInt(enemyDamageOffsetRange*2 + 1)-(enemyDamageOffsetRange/2);
            int enemyDamage = (enemyMaxDamage/2) + enemyDamageOffset;
            int enemyClassOffset = random.nextBoolean() ?-1 : 1;
            int enemyClass = 2 + enemyClassOffset;
            int enemyMaxSpeed = Math.min(100,(enemyClass+1)*5 + 2*gameLevel);
            int enemySpeedOffsetRange = (enemyClass+1)/2*gameLevel;
            int enemySpeedOffset = random.nextInt(enemySpeedOffsetRange*2 +1) -(enemySpeedOffsetRange/2);
            int enemySpeed = (enemyMaxSpeed/2) + enemySpeedOffset;
            int enemySpawnRange = 5;
            enemyDamage = (enemyClass == 1) ?enemyDamage/2:enemyDamage;
            boolean valid = false;
            while (!valid) {
                int[] coordinateSet = new int[2];
                int corner = random.nextInt(5) + 1;
                switch (corner) {
                    case 1:
                        coordinateSet = new int[]{50, 50};
                        break;
                    case 2:
                        coordinateSet = new int[]{10, 10};
                        break;
                    case 3:
                        coordinateSet = new int[]{10, 90};
                        break;
                    case 4:
                        coordinateSet = new int[]{90, 10};
                        break;
                    case 5:
                        coordinateSet = new int[]{90, 90};
                        break;
                }
                int[] offsets = {random.nextInt(enemySpawnRange * 2 + 1) - 5 + coordinateSet[0], random.nextInt(enemySpawnRange * 2 + 1) - 5 + coordinateSet[1]};
                int x = offsets[0];
                int y = offsets[1];
                Square potentialSquare = grid.gameGrid[x][y];
                int dx = Math.abs(player.getX() -x);
                int dy = Math.abs(player.getY() - y);
                int maxDistancePlayer = Math.max(dx,dy);
                if (potentialSquare.isTraversable() && maxDistancePlayer>=30/gameLevel) {
                    enemyList.add(new Enemy(x, y, enemySpeed,enemyClass,enemyHealth,grid.gameGrid,slashTexture));
                    enemyList.get(enemyList.size()-1).setSlashDamage(enemyDamage);
                    enemyList.get(enemyList.size()-1).setProjectileDamage(enemyDamage/2);
                    valid = true;
                }
            }
        }
    }

    public void handleObjectSpawns(){
        int[] coordinateSet = new int[]{50, 50};
        int numberOfObjects = random.nextInt(3)+1;
        for(int i =0; i<numberOfObjects;i++){
            boolean valid = false;
            while (!valid) {
                int xOffset = random.nextInt(50*2) -50;
                int yOffset = random.nextInt(50*2)- 50;
                int xCoordinate = coordinateSet[0]+xOffset;
                int yCoordinate = coordinateSet[1]+yOffset;
                Square potentialSquare = grid.gameGrid[xCoordinate][yCoordinate];
                if(potentialSquare.isTraversable()){
                    int objectHealth = 10*gameLevel + random.nextInt(11)-5;
                    Object object  = new Object(xCoordinate,yCoordinate,0,0,objectHealth,grid.gameGrid);
                    object.setDrawX(potentialSquare.getIsoCenterX());
                    object.setDrawY(potentialSquare.getIsoCenterY());
                    objectList.add(object);
                    valid = true;
                }
            }
        }
    }

    public void handleObjects(){
        for(int i = objectList.size()-1; i>=0;i--){
            Object object = objectList.get(i);
            if(object.checkDeath(player)){
                objectList.remove(i);
            }else{
                object.checkCollisions(projectileList);
                object.drawObject(sr);
            }

        }
    }

    public void handlePlayerAbilities(){
        // get mouse calculate angle send in the angle
        for(int i = 0; i<player.getAbilities().size();i++){
            Ability ability = player.getAbilities().get(i);
            ability.increaseCounter();
            if(ability.getName() == Ability.Name.KA && ability.isActive()){

                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.setColor(Color.WHITE);
                for(int j =0; j<5;j++){
                    sr.circle(player.getDrawX(),player.getDrawY(), (float) (((KillAura) ability).getBaseRadius()+ability.getLevelUpFactor() * ability.getLevel() *  ((KillAura) ability).getBaseRadius()  - j));
                }
                sr.end();
            }
            if(ability.getName() == Ability.Name.STOP){
                if(ability.isActive()){
                    timeStopped = true;
                }else{
                    timeStopped = false;
                }
            }
            if(Gdx.input.isKeyPressed(i+8) && ability.isReady()){
                if(ability.getName() == Ability.Name.BLINK || ability.getName() == Ability.Name.BOMB){
                    double mouseX = Gdx.input.getX();
                    double mouseY = deviceHeight - Gdx.input.getY();
                    double xDiff = mouseX - player.getDrawX();
                    double yDiff = mouseY - player.getDrawY();
                    double angle = Math.atan2(yDiff,xDiff);
                    double distance = Math.sqrt(yDiff*yDiff + xDiff*xDiff);
                    player.useAbility(ability,angle,distance,grid, projectileList);
                    break;
                }else if(ability.getName() == Ability.Name.SPEED || ability.getName() == Ability.Name.STOP ||ability.getName() == Ability.Name.KA){
                    player.useAbility(ability,0,0,null, projectileList);
                    break;
                }

                //cant use more than 1 ability per frame

            }
        }
    }

    public void handlePlayerAttackBuffer(){
        if(attackBuffered){
            attackBufferTimer += Gdx.graphics.getDeltaTime();
            if(attackBufferTimer > player.getAttackCooldown() -0.1){
                attackBuffered = false;
            }
        }
    }

    public void drawMap(){
        for(Square[] squares : gameArea){
            for(Square square : squares){
                /*square.drawOutline(sr)
                if(square.color == square.blue){
                    square.drawSquare(sr);
                }*/
                square.drawTop(polyBatch);
                //square.drawIsoTop(sr);
                /*if(path != null && path.contains(square)){
                    square.drawPath(sr);
                }*/
            }
        }
    }

    public void handleEnemyLoop(){
        if(!enemyList.isEmpty()){
            for(Enemy enemy : enemyList){
                sr.begin(ShapeRenderer.ShapeType.Filled);
                Boolean hit = enemy.checkCollisions(projectileList);
                if(hit == null) {
                    sr.setColor(Color.WHITE);
                }
                else if(hit){
                    sr.setColor(Color.WHITE);
                }
                else{
                    sr.setColor(Color.RED);
                }
                for(Ability ability : player.getAbilities()){
                    if(ability.getName() == Ability.Name.KA && ability.isActive()){
                        KillAura tempKA = (KillAura) ability;
                        double diffX = enemy.getDrawX() - player.getDrawX();
                        double diffY = enemy.getDrawY() - player.getDrawY();
                        double radiusSquared = Math.pow((tempKA.getBaseRadius()+(tempKA.getLevelUpFactor() * tempKA.getLevel() * tempKA.getBaseRadius())),2);
                        double distanceSquared = diffX*diffX + diffY*diffY;
                        if(distanceSquared <= radiusSquared) {
                            if(enemy.getKaTimer() >= (enemy.getKaCooldown()-(ability.getLevelUpFactor()*ability.getLevel()*ability.getLevelUpFactor()))){
                                int damage = tempKA.getBaseDamage() + (int) (tempKA.getLevelUpFactor() * tempKA.getLevel());
                                enemy.damage(damage);
                                sr.setColor(Color.WHITE);
                                enemy.drawEnemy(sr);
                                enemy.setKaTimer(0);
                            }else{
                                enemy.setKaTimer(enemy.getKaTimer() + Gdx.graphics.getDeltaTime());
                            }

                        }
                    }
                }
                if(spotted || enemy.playerInSight(player,enemy.getPath())){
                    enemy.setPath(pathfinding.aStar(grid.gameGrid[enemy.getX()][enemy.getY()],grid.gameGrid[player.getX()][player.getY()]));
                    if(!enemy.getPath().isEmpty()){
                        enemy.getPath().remove(0);
                    }
                    spotted = true;
                }else if (!enemy.getPath().isEmpty()){
                    enemy.setPath(pathfinding.aStar(grid.gameGrid[enemy.getX()][enemy.getY()],grid.gameGrid[randomX][randomY]));
                    if(!enemy.getPath().isEmpty()){
                        enemy.getPath().remove(0);
                    }
                }else{
                    int randomX = random.nextInt(amountXSquares);
                    int randomY = random.nextInt(amountYSquares);
                    enemy.setPath(pathfinding.aStar(grid.gameGrid[enemy.getX()][enemy.getY()],grid.gameGrid[randomX][randomY]));
                    if(!enemy.getPath().isEmpty()){
                        enemy.getPath().remove(0);
                    }
                }
                int dx = Math.abs(player.getX() - enemy.getX());
                int dy = Math.abs(player.getY() - enemy.getY());

                int stopDistance = (enemy.Class == 1) ?10:2;

                boolean adjacent = (Math.max(dx,dy)) <= stopDistance;
                if(!timeStopped){
                    enemyMove = enemy.increaseCounter(adjacent);
                    if(enemyMove && !enemy.getPath().isEmpty() && !adjacent){
                        enemy.moveToNextSquare();
                    }
                }





                enemy.drawEnemy(sr);

                enemy.displayHealthBar(sr);
                sr.end();
                if(!timeStopped){
                    boolean enemyAttackReady = enemy.increaseAttackCounter();
                    if(enemyAttackReady){
                        double xTarget = player.getDrawX();
                        double yTarget = player.getDrawY();
                        if(enemy.getAttackClass() == 1){
                            if(enemy.playerInSight(player,enemy.getPath())){
                                enemy.attack(xTarget,yTarget, enemyList, projectileList);
                                enemy.setAttackTimer(0);
                            }
                        }else{
                            double xDiff = xTarget - enemy.getDrawX();
                            double yDiff = yTarget - enemy.getDrawY();
                            double distance = xDiff*xDiff + yDiff*yDiff;
                            if(distance < (50*50)){
                                if(enemy.attack(xTarget,yTarget,enemyList,projectileList)){
                                    Sprite enemySlashSprite = enemy.getSlashSprite();
                                    sprites.begin();
                                    double angle = Math.atan2(yDiff,xDiff);
                                    enemySlashSprite.setSize(25,25);
                                    enemySlashSprite.setPosition(enemy.getDrawX()-12.5f,enemy.getDrawY());
                                    enemySlashSprite.setOrigin(12.5f,0);
                                    enemySlashSprite.setRotation((float) (angle*(180/Math.PI)) -90);
                                    enemySlashSprite.draw(sprites);
                                    sprites.end();
                                    enemy.setSlashFrameCounter(-1);
                                    enemy.setAttackTimer(0);
                                }
                            }
                        }
                    }
                }

                if(enemy.getSlashFrameCounter() < 6){
                    sprites.begin();
                    enemy.getSlashSprite().setPosition(enemy.getDrawX()-12.5f, enemy.getDrawY());
                    enemy.getSlashSprite().draw(sprites);
                    sprites.end();
                    enemy.setSlashFrameCounter(enemy.getSlashFrameCounter() +1);
                }

            }
        }
    }

    public void handlePlayerAttack(){
        attackReady = player.increaseAttackCounter();
        if((Gdx.input.isKeyPressed(Input.Keys.Q))||attackBuffered){
            if(attackReady){
                double mouseX = Gdx.input.getX();
                double mouseY = deviceHeight - Gdx.input.getY();
                player.setAttackTimer(0);
                if(player.attack(mouseX, mouseY, enemyList, projectileList)){
                    double xDiff = mouseX - player.getDrawX();
                    double yDiff = mouseY - player.getDrawY();
                    double angle = Math.atan2(yDiff,xDiff);
                    sprites.begin();
                    playerSlashSprite.setPosition(player.getDrawX()-25,player.getDrawY());
                    playerSlashSprite.setOrigin(25,0);
                    playerSlashSprite.setRotation((float) (angle*(180/Math.PI)) -90);
                    playerSlashSprite.draw(sprites);
                    sprites.end();
                    slashFrameCounter = 0;
                    attackBuffered = false;
                    player.setAttackTimer(0);
                }
            }else if (Gdx.input.isKeyPressed(Input.Keys.Q)){
                attackBufferTimer = 0;
                attackBuffered = true;
            }

        }
    }

    public void movePlayer(){
        move = player.increaseCounter(false);
        if(move && !player.getPath().isEmpty()){
            player.moveToNextSquare();
        }
    }

    public void handleEnemyDeaths(){
        for(int i = enemyList.size()-1; i>=0; i--){
            if(enemyList.get(i).getHealth()<=0){
                int goldReward = random.nextInt(11)-5;
                player.addGold(goldReward*gameLevel + 4*gameLevel);
                enemyList.remove(i);
            }
        }
    }

    public void handleProjectiles(){
        if(!projectileList.isEmpty()){
            for(int i = projectileList.size() -1; i>=0;i--){
                Projectile projectile = projectileList.get(i);
                projectile.move();
                if(projectile.getAmmoType() == Projectile.ammo.BOMB){
                    double bombX = projectile.getX();
                    double bombY = projectile.getY();
                    double xTarget = projectile.getxTarget();
                    double yTarget = projectile.getyTarget();
                    double xDiff = bombX - xTarget;
                    double yDiff = bombY - yTarget;
                    double distanceSquared = xDiff*xDiff +yDiff*yDiff;
                    //radius of bomb = 20
                    if(distanceSquared <= (0.5*projectile.getRadius())){
                        projectile.explode(enemyList);
                        bomb.setTimer(0);
                        continue;
                    }
                }
                if(projectile.getX() > 1920 || projectile.getX() <0 || projectile.getY() > 1080 || projectile.getY() <0){
                    projectileList.remove(projectile);
                }
                if(projectile.getAmmoType() == Projectile.ammo.BOMB){
                    if(projectile.isExploded()){
                        if(!checkBombAnimation(projectile.getX(), projectile.getY(),projectile.getRadius(),projectile)){
                            projectileList.remove(i);
                        }
                    }else{
                        projectile.draw(sr);
                    }
                }else{
                    projectile.draw(sr);
                }
            }
        }
    }

    public void drawSlash(){
        if(slashFrameCounter <6){
            sprites.begin();
            playerSlashSprite.setPosition(player.getDrawX()-25,player.getDrawY());
            playerSlashSprite.draw(sprites);
            sprites.end();
            slashFrameCounter++;
        }
    }

    public void handlePlayerMovement(){
        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
            double mouseX = Gdx.input.getX();
            double mouseY = deviceHeight - Gdx.input.getY();
            Square tempSquare = grid.gameGrid[0][0];
            float width = tempSquare.getw();
            float height = tempSquare.geth();

            float offsetX = (0.5f*(deviceWidth) - width);
            float offsetY = (0.125f*deviceHeight - 8f*height);

            float Xp = (float) mouseX-offsetX;
            float Xy = (float) mouseY-offsetY;

            float gridX = 0.5f*((Xp/width) + (Xy/height));
            float gridY = 0.5f*((Xy/height) - (Xp/width));

            int coordinateX = Math.round(gridX);
            int coordinateY = Math.round(gridY);

            if(coordinateX < (amountXSquares) && coordinateY < (amountYSquares) && coordinateX >=0 && coordinateY >= 0){
                player.setPath(pathfinding.aStar(grid.gameGrid[player.getX()][player.getY()],grid.gameGrid[coordinateX][coordinateY]));
                if(!player.getPath().isEmpty()){
                    player.getPath().remove(0);
                }
            }
        }
    }
}



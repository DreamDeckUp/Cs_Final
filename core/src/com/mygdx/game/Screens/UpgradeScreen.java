package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Components.Component;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Upgrade;

public class UpgradeScreen implements Screen, InputProcessor {
    private Stage stage;
    private final Game game;
    private Array<Component> components;
    private double[] currentLvls = {0,0,0,0,0,0};

    private SpriteBatch[] batches;
    private Texture[] icons;
    private TextureRegion[] textureRegions;
    private Sprite[] sprites;
    private boolean isFirstFrame = true;

    private Sprite[] progressSprites;
    private TextureRegion[] progressRegions;
    private Texture progressBack;

    private long initialTime = TimeUtils.millis();

    private Label currencyLabel;
    private int currency;
    private int cost = 1;

    private boolean drawShape;
    private ShapeRenderer shapeRenderer;
    private boolean[] isDescribingUpgrade;


    public UpgradeScreen(Game aGame) {
        game=aGame;
        stage = new Stage(new ScreenViewport());
        currency = ((MyGdxGame)(game)).getCurrency();

        components = ((MyGdxGame)(aGame)).getComponents();

        for (int i = 0; i<6; i++){
            currentLvls[i] = components.get(i).getLvl();
        }

        drawShape = false;
        shapeRenderer = new ShapeRenderer();
        isDescribingUpgrade = new boolean[6];
        isDescribingUpgrade[0] = false;
        isDescribingUpgrade[1] = false;
        isDescribingUpgrade[2] = false;
        isDescribingUpgrade[3] = false;
        isDescribingUpgrade[4] = false;
        isDescribingUpgrade[5] = false;

        //Creating title label
        final Label title = new Label("Upgrades & Components", MyGdxGame.gameSkin);
        title.setFontScale(2f);
        title.setY(Gdx.graphics.getHeight()*58/64);
        title.setAlignment(Align.center);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        //Creating the back button
        TextButton backButton = new TextButton("Back", MyGdxGame.gameSkin);
        backButton.getLabel().setFontScale(0.9f, 0.9f);
        backButton.setWidth(stage.getWidth()/10);
        backButton.setHeight(Gdx.graphics.getHeight()*3/32);
        backButton.setPosition(Gdx.graphics.getWidth()/32, Gdx.graphics.getHeight()*1/64);
        //Adding the listener to the back button
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(backButton);

        //Creating the currency label
        currencyLabel = new Label("Currency : "+currency, MyGdxGame.gameSkin);
        currencyLabel.setFontScale(1.5f);
        currencyLabel.setY(Gdx.graphics.getHeight()*58/64);
        currencyLabel.setX(Gdx.graphics.getWidth()*3/64);
        currencyLabel.setAlignment(Align.left);
        currencyLabel.setWidth(Gdx.graphics.getWidth()/10);
        stage.addActor(currencyLabel);

        //Initializing the array values
        batches = new SpriteBatch[18];
        icons = new Texture[6];
        textureRegions = new TextureRegion[6];
        sprites = new Sprite[6];
        Label[] labels = new Label[6];
        progressRegions = new TextureRegion[6];
        progressSprites = new Sprite[12];

        //Creating array of upgrade names
        String[] cNames = {"Fuel Tank", "Efficiency", "Power", "Air Resistance", "Launcher Strength", "Weight"};

        //Creating the informational texts
        String[] uDescriptions = new String[6];
        uDescriptions[0] = "Upgrades the amount of fuel your\n rocket can use, but also\n augments the weight of your rocket.";
        uDescriptions[1] = "Reduces the amount of fuel used\n every second, but also reduces\n the power of your rocket.";
        uDescriptions[2] = "Augments the power of your rocket,\n but also augments the amount\n of fuel used every second.";
        uDescriptions[3] = "Reduces your rocket's air resistance.";
        uDescriptions[4] = "Augments the initial rocket boost,\n but also augments your\n rocket's air resistance.";
        uDescriptions[5] = "Reduces your rocket's weight.\n Reducing it too much will have\n a negative impact on its\n performance.";
        String[] uEffects = new String[6];
        uEffects[0] = "Fuel: +5\nWeight: +2";
        uEffects[1] = "Efficiency (Fuel consumption): -2/40\nPower: -3/50";
        uEffects[2] = "Power: +5/50\nEfficiency (Fuel consumption): +1/40";
        uEffects[3] = "Air Resistance: -1/20";
        uEffects[4] = "Launcher Strength: +1/20\nAir Resistance: -1/20";
        uEffects[5] = "Weight: -2";

        //Creating the layout
        for (int i = 0; i < 6; i++) {
            //Initialising the SpriteBatches
            batches[i] = new SpriteBatch();

            //CREATING THE UPGRADES SECTION
            icons[i] = ((MyGdxGame) (game)).getComponents().get(i).getArt();

            textureRegions[i] = new TextureRegion(icons[i], 128, 128);

            sprites[i] = new Sprite(textureRegions[i]);
            sprites[i].setScale(1f);
            sprites[i].setPosition((i < 3)?Gdx.graphics.getWidth()/6*(i)+/*spacing*/Gdx.graphics.getWidth()*(i+1)/64:Gdx.graphics.getWidth()/6*(i-3)+/*spacing*/Gdx.graphics.getWidth()*(i-2)/64,
                    (i < 3)?Gdx.graphics.getHeight()*40/64:Gdx.graphics.getHeight()*16/64);
            sprites[i].setSize(Gdx.graphics.getWidth()*8/49, Gdx.graphics.getHeight()*1/4);

            //Adding the Upgrade Buttons
            String[] uNames = {"Fuel", "Efficiency", "Power", "Aerodynamic", "Launcher", "Optimization",};
            Upgrade upgrade = new Upgrade(uNames[i], (i < 3)?Gdx.graphics.getWidth()/6*(i)+Gdx.graphics.getWidth()*(i+1)/64:Gdx.graphics.getWidth()/6*(i-3)+Gdx.graphics.getWidth()*(i-2)/64,
                    (i < 3)?Gdx.graphics.getHeight()*32/64:Gdx.graphics.getHeight()*8/64);

            stage.getRoot().addActorAt(3+i+(4*i), upgrade.getUpgradeButton());

            //Setting the fuel upgrade button
            if (i == 0) {

                upgrade.getUpgradeButton().addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (currency - cost >= 0) {
                        components.get(0).setLvl(components.get(0).getLvl() + 5);
                        currentLvls[0] = components.get(0).getLvl();
                        components.get(5).setLvl(components.get(5).getLvl() + 2);
                        currentLvls[5] = components.get(5).getLvl();
                        currency -= cost;
                    }}

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }
            //Setting the efficiency upgrade button
            if (i == 1) {

                upgrade.getUpgradeButton().addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (currency-cost >= 0) {
                            components.get(1).setLvl(components.get(1).getLvl() - 2);
                            currentLvls[1] = components.get(1).getLvl();
                            components.get(2).setLvl(components.get(2).getLvl() - 3);
                            currentLvls[2] = components.get(2).getLvl();
                            currency -= cost;
                        }
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }
            //Setting the power upgrade button
            if (i == 2) {
                upgrade.getUpgradeButton().addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (currency-cost >= 0) {
                            components.get(2).setLvl(components.get(2).getLvl() + 5);
                            currentLvls[2] = components.get(2).getLvl();
                            components.get(1).setLvl(components.get(1).getLvl() + 1);
                            currentLvls[1] = components.get(1).getLvl();
                            currency -= cost;
                        }
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }
            //Setting the air upgrade button
            if (i == 3) {
                upgrade.getUpgradeButton().addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (currency-cost >= 0) {
                            components.get(3).setLvl(components.get(3).getLvl() + 1);
                            currentLvls[3] = components.get(3).getLvl();
                            currency -= cost;
                        }
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }
            //Setting the canon upgrade button
            if (i == 4) {
                upgrade.getUpgradeButton().addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (currency-cost >= 0) {
                            components.get(4).setLvl(components.get(4).getLvl() + 1);
                            currentLvls[4] = components.get(4).getLvl();
                            components.get(3).setLvl(components.get(3).getLvl() - 1);
                            currentLvls[3] = components.get(3).getLvl();
                            currency-=cost;
                        }
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }
            //Setting the weight upgrade button
            if (i == 5) {
                upgrade.getUpgradeButton().addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (currency-cost >= 0) {
                            components.get(5).setLvl(components.get(5).getLvl() - 2);
                            currentLvls[5] = components.get(5).getLvl();
                            currency-=cost;
                        }
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }

            //CREATING THE INFO BUTTONS
            //Creating the info buttons
            TextButton infoBtn = new TextButton("?", MyGdxGame.gameSkin);
            infoBtn.setSize(Gdx.graphics.getWidth()*16/441, Gdx.graphics.getHeight()*1/18);
            infoBtn.setPosition((i < 3)?Gdx.graphics.getWidth()*8/63*(i+1)+/*spacing*/Gdx.graphics.getWidth()*(i+1)/64+Gdx.graphics.getWidth()*16/441*i:Gdx.graphics.getWidth()*8/63*(i-2)+/*spacing*/Gdx.graphics.getWidth()*(i-2)/64+Gdx.graphics.getWidth()*16/441*(i-3),
                    (i < 3)?Gdx.graphics.getHeight()*463/576:Gdx.graphics.getHeight()*247/576);

            stage.addActor(infoBtn);

            //Creating the labels displaying the informational texts
            Label upgradeName = new Label(uNames[i], MyGdxGame.gameSkin);
            upgradeName.setFontScale(4f);
            upgradeName.setBounds(Gdx.graphics.getWidth()*1/32, Gdx.graphics.getHeight()*40/64, Gdx.graphics.getWidth()*24/49, Gdx.graphics.getHeight()*7/36);
            upgradeName.setVisible(false);


            Label upgradeDescription = new Label("\n\n\n\n\n\n\n\n\n\n\n"+uDescriptions[i]+"\n\n\n"+ uEffects[i], MyGdxGame.gameSkin);
            upgradeDescription.setFontScale(1.9f);
            upgradeDescription.setVisible(false);
            upgradeDescription.setBounds(Gdx.graphics.getWidth()*1/32, Gdx.graphics.getHeight()*40/64, Gdx.graphics.getWidth()*24/49, Gdx.graphics.getHeight()*7/36);

            stage.getRoot().addActor(upgradeName);
            stage.getRoot().addActor(upgradeDescription);

            //Adding the listeners
            if (i == 0){
                infoBtn.addListener(new InputListener(){
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                        setDrawShape(!getDrawShape());
                        isDescribingUpgrade[0] = true;
                    }
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }
            else if (i == 1){
                infoBtn.addListener(new InputListener(){
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                        setDrawShape(!getDrawShape());
                        isDescribingUpgrade[1] = true;
                    }
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }
            else if (i == 2){
                infoBtn.addListener(new InputListener(){
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                        setDrawShape(!getDrawShape());
                        isDescribingUpgrade[2] = true;
                    }
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }
            else if (i == 3){
                infoBtn.addListener(new InputListener(){
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                        setDrawShape(!getDrawShape());
                        isDescribingUpgrade[3] = true;
                    }
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }
            else if (i == 4){
                infoBtn.addListener(new InputListener(){
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                        setDrawShape(!getDrawShape());
                        isDescribingUpgrade[4] = true;
                    }
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }
            else if (i == 5){
                infoBtn.addListener(new InputListener(){
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                        setDrawShape(!getDrawShape());
                        isDescribingUpgrade[5] = true;
                    }
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }

            //CREATING THE COMPONENT SECTION
            //Adding the component Labels
            labels[i] = new Label(cNames[i], MyGdxGame.gameSkin);
            labels[i].setFontScale(1.5f);
            labels[i].setHeight(Gdx.graphics.getHeight()*3/64);
            labels[i].setWidth(Gdx.graphics.getWidth()*963/3136);
            labels[i].setAlignment(Align.center);
            labels[i].setPosition(Gdx.graphics.getWidth()*1453/6272+Gdx.graphics.getWidth()*1683/3136-labels[i].getWidth()/2,
                    Gdx.graphics.getHeight()*106/128-Gdx.graphics.getHeight()*i*3/64-Gdx.graphics.getHeight()*i*6/64);

            stage.addActor(labels[i]);

            //Adding the component progress bars
            batches[i+6] = new SpriteBatch();
            batches[i+12] = new SpriteBatch();

            //Adding the progress bar outline
            Texture progressBar = new Texture(Gdx.files.internal("imgs/frontProgress.png"));
            TextureRegion barRegion = new TextureRegion(progressBar);

            progressSprites[i+6] = new Sprite(barRegion);
            progressSprites[i+6].setSize(Gdx.graphics.getWidth()*963/3136, progressBar.getHeight());

            float x = Gdx.graphics.getWidth()*1453/6272+Gdx.graphics.getWidth()*1683/3136- progressSprites[i+6].getWidth()/2;
            float y = Gdx.graphics.getHeight()*105/128-/*Width of the bar*/Gdx.graphics.getHeight()*(i+1)*3/64-/*Spacing*/Gdx.graphics.getHeight()*i*6/64;

            progressSprites[i+6].setPosition(x, y);

            //Adding the progress bar fill
            progressBack = new Texture(Gdx.files.internal("imgs/backProgress.png"));

            if (i == 3 || i == 1)
                progressRegions[i] = new TextureRegion(progressBack, Gdx.graphics.getWidth()*963/3136*(int)(components.get(i).getMaxLvl()-components.get(i).getLvl())/((int)components.get(i).getMaxLvl()-(int)(components.get(i).getMinLvl())),
                        (int)progressSprites[i+6].getHeight());
            else
                progressRegions[i] = new TextureRegion(progressBack, Gdx.graphics.getWidth()*963/3136*(int)(components.get(i).getLvl()-components.get(i).getMinLvl())/((int)components.get(i).getMaxLvl()-(int)(components.get(i).getMinLvl())),
                    (int)progressSprites[i+6].getHeight());

            progressSprites[i] = new Sprite(progressRegions[i]);
            progressSprites[i].setPosition(x, y);

        }

        //Creating the text button exiting the upgrade descriptions
        TextButton closeBtn = new TextButton("X", MyGdxGame.gameSkin);
        closeBtn.setSize(Gdx.graphics.getWidth()*16/441, Gdx.graphics.getHeight()*1/18);
        closeBtn.setPosition(Gdx.graphics.getWidth()*8/63*(2+1)+/*spacing*/Gdx.graphics.getWidth()*(2+1)/64+Gdx.graphics.getWidth()*16/441,
                Gdx.graphics.getHeight()*463/576);

        closeBtn.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                setDrawShape(!getDrawShape());
                for (int i = 0; i < 6; i++){
                    isDescribingUpgrade[i] = false;
                }
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        closeBtn.setDisabled(true);
        closeBtn.setVisible(false);
        stage.addActor(closeBtn);

    }

    @Override
    public void show() {
        Gdx.app.log("UpgradeScreen","show");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.7f,0.7f,0.7f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        long finalTime = TimeUtils.millis();

        //Changing the frame of the sprites' animation
        if ((finalTime - initialTime) >= 150) {

            for (int i = 0; i < 6; i++) {

                TextureRegion temp;

                if (isFirstFrame) {
                    temp = new TextureRegion(icons[i], 256, 128);
                    temp.setRegionX(128);
                } else {
                    temp = new TextureRegion(icons[i], 128, 128);
                }

                textureRegions[i] = temp;
                sprites[i].setRegion(textureRegions[i]);
            }
            if (isFirstFrame)
                isFirstFrame = false;
            else
                isFirstFrame = true;

            initialTime = TimeUtils.millis();
        }

        //Drawing the sprites
        for (int i = 0; i < 12; i++) {
            batches[i].begin();

            if (i < 6) {

                sprites[i].draw(batches[i]);

                //Changing the length of the yellow progress bar sprites
                if (i == 3 || i == 1)
                    progressRegions[i] = new TextureRegion(progressBack, Gdx.graphics.getWidth()*963/3136*(int)(components.get(i).getMaxLvl()-currentLvls[i])/((int)components.get(i).getMaxLvl()-(int)(components.get(i).getMinLvl())),
                            (int)progressSprites[i+6].getHeight());
                else
                    progressRegions[i] = new TextureRegion(progressBack, Gdx.graphics.getWidth()*963/3136*(int)(currentLvls[i]-components.get(i).getMinLvl())/((int)components.get(i).getMaxLvl()-(int)(components.get(i).getMinLvl())),
                            (int)progressSprites[i+6].getHeight());

                float x = Gdx.graphics.getWidth()*1453/6272+Gdx.graphics.getWidth()*1683/3136- progressSprites[i+6].getWidth()/2;
                float y = Gdx.graphics.getHeight()*105/128-/*Width of the bar*/Gdx.graphics.getHeight()*(i+1)*3/64-/*Spacing*/Gdx.graphics.getHeight()*i*6/64;

                progressSprites[i] = new Sprite(progressRegions[i]);
                progressSprites[i].setPosition(x, y);
            }
            progressSprites[i].draw(batches[i]);
            batches[i].end();

        }

        //Replacing the value of currency for the current currency
        stage.getRoot().removeActor(currencyLabel);
        currencyLabel = new Label("Currency : "+currency, MyGdxGame.gameSkin);
        currencyLabel.setFontScale(1.5f);
        currencyLabel.setY(Gdx.graphics.getHeight()*58/64);
        currencyLabel.setX(Gdx.graphics.getWidth()*1/64);
        currencyLabel.setAlignment(Align.left);
        currencyLabel.setWidth(Gdx.graphics.getWidth()/10);
        stage.getRoot().addActorAt(2, currencyLabel);

        //if the drawShape value is true
        if (drawShape){

            //Drawing the shape
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(Gdx.graphics.getWidth()*1/64, Gdx.graphics.getHeight()*1/8, Gdx.graphics.getWidth()*1683/3136, Gdx.graphics.getHeight()*3/4);
            shapeRenderer.end();

            //Hiding and disabling the other Actors
            for (int i = 0; i < 6; i++){

                //Disabling and hiding the upgrade buttons
                ((TextButton)(stage.getRoot().getChild(3+(5*i)))).setTouchable(Touchable.disabled);
                ((TextButton)(stage.getRoot().getChild(3+(5*i)))).setVisible(false);

                //Disabling and hiding the info buttons
                ((TextButton)(stage.getRoot().getChild(4+(5*i)))).setTouchable(Touchable.disabled);
                ((TextButton)(stage.getRoot().getChild(4+(5*i)))).setVisible(false);

                //Enabling and showing the closeBtn
                stage.getRoot().getChildren().get(33).setVisible(true);
                ((TextButton)(stage.getRoot().getChildren().get(33))).setDisabled(false);

                //Showing the correct upgrade name and description
                if (isDescribingUpgrade[i] == true){
                    stage.getRoot().getChildren().get((i+1)*5).setVisible(true);
                    stage.getRoot().getChildren().get(1+(i+1)*5).setVisible(true);
                }
            }
        }
        else {
            for (int i = 0; i < 6; i++) {

                //Showing and enabling the upgrade buttons
                ((TextButton) (stage.getRoot().getChild(3 + (5 * i)))).setTouchable(Touchable.enabled);
                ((TextButton) (stage.getRoot().getChild(3 + (5 * i)))).setVisible(true);

                //Showing and enabling the info buttons
                ((TextButton) (stage.getRoot().getChild(4 + (5 * i)))).setTouchable(Touchable.enabled);
                ((TextButton) (stage.getRoot().getChild(4 + (5 * i)))).setVisible(true);

                //Hiding and disabling the closeBtn
                stage.getRoot().getChildren().get(33).setVisible(false);
                ((TextButton) (stage.getRoot().getChildren().get(33))).setDisabled(true);

                //Making sure all upgrades names and descriptions are invisible
                stage.getRoot().getChildren().get((i + 1) * 5).setVisible(false);
                stage.getRoot().getChildren().get(1 + (i + 1) * 5).setVisible(false);
            }
        }

        //Drawing the stage
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        ((MyGdxGame)game).setComponents(components);
        ((MyGdxGame)game).setCurrency(currency);
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void setDrawShape(boolean dS){ drawShape = dS;}

    public boolean getDrawShape(){ return drawShape;}
}

package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;

public class TitleScreen implements Screen {

    private Stage stage;
    private Game game;

    public TitleScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        //Adding the Background to the screen
        addBackground();

        //Adding the button leading to the upgrade screen
        TextButton upgradeButton = new TextButton("Upgrades", MyGdxGame.gameSkin);
        upgradeButton.setWidth(Gdx.graphics.getWidth()/5);
        upgradeButton.setPosition(Gdx.graphics.getWidth()/6,Gdx.graphics.getHeight()/5);
        //Adding a listener to the button that displays the upgrae screen upon being clicked
        upgradeButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new UpgradeScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        //Adding the button to the stage
        stage.addActor(upgradeButton);

        //Adding a button leading to the option screen
        TextButton optionsButton = new TextButton("Options", MyGdxGame.gameSkin);
        optionsButton.setWidth(Gdx.graphics.getWidth()/5);
        optionsButton.setPosition(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/6-optionsButton.getWidth(),Gdx.graphics.getHeight()/5);
        //Adding a listener to the button that displays the option screen upon being clicked
        optionsButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new OptionScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        //Adding the button to the stage
        stage.addActor(optionsButton);

        //Adding a button leading to the game screen
        TextButton playButton = new TextButton("Play", MyGdxGame.gameSkin);
        playButton.setWidth(Gdx.graphics.getWidth()/3);
        playButton.setHeight(Gdx.graphics.getHeight()/8);
        playButton.setPosition(Gdx.graphics.getWidth()/2-playButton.getWidth()/2,Gdx.graphics.getHeight()/18);
        //Adding a listener to the button that displays the game screen upon being clicked
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        //Adding the button to the stage
        stage.addActor(playButton);
    }
    //Method that adds the background to the stage
    public void addBackground(){
        Texture texture = new Texture(Gdx.files.internal("imgs/autopilot_logo.png"));
        Image background = new Image(texture);
        background.setScaling(Scaling.fill);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition(0, 0);
        stage.addActor(background);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
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

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

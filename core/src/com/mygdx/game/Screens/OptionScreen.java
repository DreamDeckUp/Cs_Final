package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;

public class OptionScreen implements Screen {

    private Stage stage;
    private Game game;

    public OptionScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        //Adding the background to the screen
        addBackground();

        //Creating a label that says Options and adding it to the stage
        Label title = new Label("Options", MyGdxGame.gameSkin);
        title.setAlignment(Align.center);
        title.setFontScale(3f);
        title.setY(Gdx.graphics.getHeight()*5/6);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        //Creating a label that says A.I. and adding it to the stage
        Label aiLabel = new Label("A.I.", MyGdxGame.gameSkin);
        aiLabel.setFontScale(2f);
        aiLabel.setPosition(Gdx.graphics.getWidth()*7/20, Gdx.graphics.getHeight()*2/3-aiLabel.getHeight()/2);
        stage.addActor(aiLabel);

        //Adding a checkbox that changes whether the A.I. is on or off
        final CheckBox aiBox = new CheckBox("", MyGdxGame.gameSkin);
        aiBox.getImage().setScaling(Scaling.fill);
        aiBox.getImageCell().size(Gdx.graphics.getHeight()*1/16, Gdx.graphics.getHeight()*1/16);
        aiBox.setSize(Gdx.graphics.getHeight()*1/16, Gdx.graphics.getHeight()*1/16);
        aiBox.setPosition(Gdx.graphics.getWidth()*25/40, Gdx.graphics.getHeight()*2/3-aiBox.getHeight()*3/8);

        //Checking if the checkBox should be checked or not
        aiBox.setChecked(((MyGdxGame)(game)).getIsAiPlaying());

        //Adding a listener to the checkbox that changes a boolean value
        aiBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((MyGdxGame)(game)).setIsAiPlaying((aiBox.isChecked())?true:false);
            }
        });
        //Adding the checkbox to the stage
        stage.addActor(aiBox);

        //Creating a label that says Music and adding it to the stage
        Label musicLabel = new Label("Music", MyGdxGame.gameSkin);
        musicLabel.setFontScale(2f);
        musicLabel.setPosition(Gdx.graphics.getWidth()*7/20, Gdx.graphics.getHeight()*13/24-musicLabel.getHeight()/2);
        stage.addActor(musicLabel);

        //Adding a checkbox that changes whether the A.I. is on or off
        final CheckBox musicBox = new CheckBox("", MyGdxGame.gameSkin);
        musicBox.getImage().setScaling(Scaling.fill);
        musicBox.getImageCell().size(Gdx.graphics.getHeight()*1/16, Gdx.graphics.getHeight()*1/16);
        musicBox.setSize(Gdx.graphics.getHeight()*1/16, Gdx.graphics.getHeight()*1/16);
        musicBox.setPosition(Gdx.graphics.getWidth()*25/40, Gdx.graphics.getHeight()*13/24-musicBox.getHeight()*3/8);

        //Checking if the checkBox should be checked or not
        musicBox.setChecked(((MyGdxGame)(game)).getIsMusicOn());

        //Adding a listener to the checkbox that changes a boolean value
        musicBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((MyGdxGame)(game)).setIsMusicOn((musicBox.isChecked())?true:false);
            }
        });
        //Adding the checkbox to the stage
        stage.addActor(musicBox);

        //Creating a label that says Sound Effects and adding it to the stage
        Label effectsLabel = new Label("Sound Effects", MyGdxGame.gameSkin);
        effectsLabel.setFontScale(2f);
        effectsLabel.setPosition(Gdx.graphics.getWidth()*7/20, Gdx.graphics.getHeight()*5/12-effectsLabel.getHeight()/2);
        stage.addActor(effectsLabel);

        //Adding a checkbox that changes whether the A.I. is on or off
        final CheckBox effectsBox = new CheckBox("", MyGdxGame.gameSkin);
        effectsBox.getImage().setScaling(Scaling.fill);
        effectsBox.getImageCell().size(Gdx.graphics.getHeight()*1/16, Gdx.graphics.getHeight()*1/16);
        effectsBox.setSize(Gdx.graphics.getHeight()*1/16, Gdx.graphics.getHeight()*1/16);
        effectsBox.setPosition(Gdx.graphics.getWidth()*25/40, Gdx.graphics.getHeight()*5/12-effectsBox.getHeight()*3/8);

        //Checking if the checkBox should be checked or not
        effectsBox.setChecked(((MyGdxGame)(game)).getIsEffectsOn());

        //Adding a listener to the checkbox that changes a boolean value
        effectsBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((MyGdxGame)(game)).setIsEffectsOn((effectsBox.isChecked())?true:false);
            }
        });
        //Adding the checkbox to the stage
        stage.addActor(effectsBox);

        //Creating a button leading to the title screen
        TextButton backButton = new TextButton("Back",MyGdxGame.gameSkin);
        backButton.setWidth(Gdx.graphics.getWidth()/4);
        backButton.setPosition(Gdx.graphics.getWidth()/2-backButton.getWidth()/2,Gdx.graphics.getHeight()/6-backButton.getHeight()/2);
        //Adding a listener that displays the title screen
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
        //Adding the button to the stage
        stage.addActor(backButton);
    }

    public void addBackground(){
        Texture texture = new Texture(Gdx.files.internal("imgs/options_background.png"));
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
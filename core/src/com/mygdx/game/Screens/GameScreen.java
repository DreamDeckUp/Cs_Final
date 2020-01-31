package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ParallaxBackground;
import com.sun.org.apache.xpath.internal.operations.Or;

/**
 * Created by julienvillegas on 17/01/2017.
 */
public class GameScreen implements Screen {

    private Stage stage;
    private Game game;
    private OrthographicCamera camera;

    public GameScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        camera = (OrthographicCamera)stage.getViewport().getCamera();


        Array<Texture> textures = new Array<Texture>();
        for(int i = 2; i <=6;i++){
            textures.add(new Texture(Gdx.files.internal("parallax/img"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }



        final ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(1);

        stage.addActor(parallaxBackground);

        Label title = new Label("Playing Screen", MyGdxGame.gameSkin);
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight()*2/3);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        TextButton speedButton = new TextButton("+Speed", MyGdxGame.gameSkin);
        speedButton.setWidth(Gdx.graphics.getWidth()/5);
        speedButton.setPosition(0, Gdx.graphics.getHeight()-speedButton.getHeight()-10);
        speedButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                parallaxBackground.setSpeed(parallaxBackground.getSpeed()+1);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(speedButton);

        TextButton nospeedButton = new TextButton("-Speed", MyGdxGame.gameSkin);
        nospeedButton.setWidth(Gdx.graphics.getWidth()/5);
        nospeedButton.setPosition(0, Gdx.graphics.getHeight()-(2*nospeedButton.getHeight())-10);
        nospeedButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                parallaxBackground.setSpeed(parallaxBackground.getSpeed()-1);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(nospeedButton);


        TextButton pauseButton = new TextButton("Stop",MyGdxGame.gameSkin);
        pauseButton.setWidth(Gdx.graphics.getWidth()/5);
        pauseButton.setPosition(Gdx.graphics.getWidth()-(pauseButton.getWidth()+10),Gdx.graphics.getHeight()-(2*pauseButton.getHeight()-20));
        pauseButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                parallaxBackground.setSpeed(0);

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(pauseButton);

        TextButton backButton = new TextButton("Back",MyGdxGame.gameSkin);
        backButton.setWidth(Gdx.graphics.getWidth()/5);
        backButton.setPosition(Gdx.graphics.getWidth()-(backButton.getWidth()+10),Gdx.graphics.getHeight()-(backButton.getHeight()-20));
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


    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(135/256f,206/256f,235/256f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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


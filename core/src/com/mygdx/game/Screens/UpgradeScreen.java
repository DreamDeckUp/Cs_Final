package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;



public class UpgradeScreen implements Screen {
    private Stage stage;
    private Game game;
    public UpgradeScreen(Game aGame) {
        game=aGame;
        stage = new Stage(new ScreenViewport());



        final Label title = new Label("Upgrades", MyGdxGame.gameSkin);
        title.setFontScale(2f);
        title.setY(Gdx.graphics.getHeight()*5/6);
        title.setAlignment(Align.center);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        TextButton backButton = new TextButton("Back", MyGdxGame.gameSkin);
        backButton.setWidth(stage.getWidth()/10);
        backButton.setPosition(0, 0);
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

        float gameWidth = Gdx.graphics.getWidth();
        float gameHeight = Gdx.graphics.getHeight();


        SpriteBatch batcher = new SpriteBatch();
        List<Object> list = new List<Object>(MyGdxGame.gameSkin);
        Label[] strings = new Label[20];
        Label temp;
        for (int i = 0, k = 0; i < 20; i++) {
            temp = new Label("String: " + i, MyGdxGame.gameSkin);
            strings[k++] = temp;

        }
        list.setItems(strings);
        ScrollPane scrollPane = new ScrollPane(list);
        scrollPane.setBounds(900, 0, gameWidth, gameHeight);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setPosition(gameWidth / 2 - scrollPane.getWidth() / 4,
                gameHeight / 2 - scrollPane.getHeight() / 4);
        scrollPane.setTransform(true);
        scrollPane.setScale(0.5f);
        stage.addActor(scrollPane);

        scrollPane.getChildren().items[0].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("yeh");
            }
        });
        Gdx.input.setInputProcessor(stage);

    }
    @Override
    public void show() {
        Gdx.app.log("UpgradeScreen","show");
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

    }
}

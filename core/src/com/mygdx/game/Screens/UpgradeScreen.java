package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;

public class UpgradeScreen implements Screen {
    private Stage stage;
    private Game game;
    public UpgradeScreen(Game aGame) {
        game=aGame;
        stage = new Stage(new ScreenViewport());
        Label title = new Label("Upgrades", MyGdxGame.gameSkin);
        title.setFontScale(2f);
        title.setY(Gdx.graphics.getHeight()*5/6);
        title.setAlignment(Align.center);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);




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

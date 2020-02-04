package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Upgrade;


public class UpgradeScreen implements Screen {
    private Stage stage;
    private Game game;
    static Array<Upgrade> upgrades;
    Container upgradeGroup;
    static int count= 0;
    int i;
    public UpgradeScreen(Game aGame) {
        game=aGame;
        stage = new Stage(new ScreenViewport());



        final Label title = new Label("Upgrades", MyGdxGame.gameSkin);
        title.setFontScale(2f);
        title.setY(Gdx.graphics.getHeight()*7/8);
        title.setAlignment(Align.center);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        TextButton backButton = new TextButton("Back", MyGdxGame.gameSkin);
        backButton.setWidth(stage.getWidth()/10);
        backButton.setPosition(Gdx.graphics.getWidth()/20, 20);
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

        TextButton playButton = new TextButton("play", MyGdxGame.gameSkin);
        playButton.setWidth(stage.getWidth()/10);
        playButton.setPosition(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/20-playButton.getWidth(), 20);
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
        stage.addActor(playButton);

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
        scrollPane.setBounds(200, 0, gameWidth, gameHeight);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setPosition(gameWidth / 20 ,
                gameHeight * 1/5);
        scrollPane.setTransform(true);
        scrollPane.setScale(1.25f);
        scrollPane.setHeight(Gdx.graphics.getHeight()*3/7);
        scrollPane.setWidth(scrollPane.getPrefWidth());
        stage.addActor(scrollPane);

        upgradeGroup = new Container();

        upgrades = new Array();
        Upgrade rocketBoost = new Upgrade(new Texture(Gdx.files.internal("imgs/rocket.png")),20,(int)gameWidth/2+200,(int)(scrollPane.getY()+scrollPane.getHeight()/2));
        upgrades.add(rocketBoost);
        Upgrade fuelTank = new Upgrade(new Texture(Gdx.files.internal("imgs/tank.jpg")),25,(int)gameWidth/2+200,(int)(scrollPane.getY()+scrollPane.getHeight()/2));
        upgrades.add(fuelTank);

        for(i = 0;i<upgrades.size;i++){
            strings[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    upgradeGroup.setActor(UpgradeScreen.upgrades.get(UpgradeScreen.count));
                    stage.addActor(upgrades.get(UpgradeScreen.count).getUpgradeButton());

                }
            });
            count++;
        }

        Gdx.input.setInputProcessor(stage);
        stage.addActor(upgradeGroup);

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

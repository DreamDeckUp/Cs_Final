package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Screens.TitleScreen;

public class MyGdxGame extends Game {
	Texture texture;
	Stage stage;
	public static Skin gameSkin;
	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		gameSkin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
		this.setScreen(new TitleScreen(this));

	}



	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		texture.dispose();
	}
}

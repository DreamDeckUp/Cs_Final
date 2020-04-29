package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Components.AirComponent;
import com.mygdx.game.Components.CanonComponent;
import com.mygdx.game.Components.Component;
import com.mygdx.game.Components.EfficiencyComponent;
import com.mygdx.game.Components.FuelComponent;
import com.mygdx.game.Components.PowerComponent;
import com.mygdx.game.Components.WeightComponent;
import com.mygdx.game.Screens.TitleScreen;

public class MyGdxGame extends Game {
	Texture texture;
	Stage stage;
	public static Skin gameSkin;
	private boolean isAiPlaying;
	private boolean isMusicOn;
	private boolean isEffectsOn;
	private Array<Component> components;
	private int currency;

	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		gameSkin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
		this.setScreen(new TitleScreen(this));
		currency = 1;

		isAiPlaying = true;
		isMusicOn = true;
		isEffectsOn = true;

		components = new Array<>();

		components.addAll(new FuelComponent(new Texture(Gdx.files.internal("icons/fuel_icon.png"))), new EfficiencyComponent(new Texture(Gdx.files.internal("icons/motor_efficiency_icon.png"))), new PowerComponent(new Texture(Gdx.files.internal("icons/motor_power_icon.png"))), new AirComponent(new Texture(Gdx.files.internal("icons/air_icon.png"))), new CanonComponent(new Texture(Gdx.files.internal("icons/canon_icon.png"))), new WeightComponent(new Texture(Gdx.files.internal("icons/weight_icon.png"))));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}

	public Array<Component> getComponents(){
		return components;
	}

	public void setComponents(Array<Component> components){this.components = components;}

	public int getCurrency(){
		return currency;
	}

	public void setCurrency(int currency){
		this.currency = currency;
	}

	public boolean getIsAiPlaying(){
		return isAiPlaying;
	}

	public void setIsAiPlaying(boolean isAiPlaying){this.isAiPlaying = isAiPlaying;
		System.out.println("isAiPlaying is "+isAiPlaying);}

	public boolean getIsMusicOn(){return isMusicOn;}

	public void setIsMusicOn(boolean isMusicOn){this.isMusicOn = isMusicOn;
		System.out.println("isMusicOn is "+isMusicOn);}

	public boolean getIsEffectsOn(){return isEffectsOn;}

	public void setIsEffectsOn(boolean isEffectsOn){this.isEffectsOn = isEffectsOn;
		System.out.println("isEffectsOn is "+isEffectsOn);}
}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Components.Component;

public class Upgrade extends Actor {
    private String name;
    private static TextButton upgradeButton;
    private float x,y;

    public Upgrade(String name, float x,float y){
        this.x=x;
        this.y=y;
        setOrigin(x,y);
        this.name = name;

        upgradeButton = new TextButton(name, MyGdxGame.gameSkin);
        upgradeButton.getLabel().setFontScale(0.7f, 0.7f);
        upgradeButton.setSize(Gdx.graphics.getWidth()*8/49,Gdx.graphics.getHeight()*2/16);
        upgradeButton.setPosition(x,y);

    }

    public TextButton getUpgradeButton() {
        return upgradeButton;
    }

    public void setUpgradeButton(TextButton upgradeButton) {
        Upgrade.upgradeButton = upgradeButton;
    }

    public  void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}

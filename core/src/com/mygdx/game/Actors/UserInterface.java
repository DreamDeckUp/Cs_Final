package com.mygdx.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.GameScreen;

public class UserInterface extends Group {
    static OrthographicCamera camera = GameScreen.camera;
    public TextButton right, left, launchButton;
    public ImageButton boostButton;
    final static float PADDING = 10;
    static boolean drawSprite = true;

    Vector2 rightCorner;
    Vector2 leftCorner;


    public UserInterface(Texture texture) {
        TextureRegion region = new TextureRegion(texture);
        region.setRegion(0,0,texture.getWidth()/2,texture.getHeight());
        Drawable drawable = new TextureRegionDrawable(region);


        //CONTROL BUTTONS
        right = new TextButton("->", MyGdxGame.gameSkin);
        left = new TextButton("<-", MyGdxGame.gameSkin);
        right.setWidth(right.getWidth()*3);
        left.setWidth(left.getWidth()*3);
        launchButton = new TextButton("Launch", MyGdxGame.gameSkin);
        boostButton = new ImageButton(drawable);
        boostButton.setHeight(launchButton.getHeight());
        boostButton.setWidth(boostButton.getWidth()*3);
        boostButton.setHeight(boostButton.getHeight()*3);
        addActor(right);
        addActor(left);
        addActor(launchButton);
        addActor(boostButton);


    }

    public void moveButtons() {
        rightCorner = new Vector2(camera.position.x+ Gdx.graphics.getWidth()/2, camera.position.y-Gdx.graphics.getHeight()/2);
        leftCorner = new Vector2(camera.position.x-Gdx.graphics.getWidth()/2, camera.position.y-Gdx.graphics.getHeight()/2);
        //Control buttons position
        launchButton.setPosition(rightCorner.x-launchButton.getWidth()-PADDING, rightCorner.y+right.getHeight()+boostButton.getHeight()+PADDING);
        right.setPosition(rightCorner.x-right.getWidth()-PADDING,rightCorner.y+PADDING);
        left.setPosition(leftCorner.x+PADDING,leftCorner.y+PADDING);
        boostButton.setPosition(rightCorner.x-boostButton.getWidth()-PADDING,right.getY()+right.getHeight()+PADDING);
    }
}

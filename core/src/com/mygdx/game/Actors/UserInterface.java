package com.mygdx.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Stats;

public class UserInterface extends Group {
    OrthographicCamera camera = GameScreen.camera;
    public TextButton right, left, launchButton;
    public ImageButton boostButton;
    final static float PADDING = 10;
    FuelGauge fuelGauge;

    Vector2 rightCorner;
    Vector2 leftCorner;


    public UserInterface(Texture texture) {
        TextureRegion region = new TextureRegion(texture);
        region.setRegion(0,0,texture.getWidth()/2,texture.getHeight());
        Drawable drawable = new TextureRegionDrawable(region);

        rightCorner = new Vector2(0,0);
        leftCorner = new Vector2(0,0);


        //CONTROL BUTTONS
        right = new TextButton("->", MyGdxGame.gameSkin);
        left = new TextButton("<-", MyGdxGame.gameSkin);
        right.setWidth(right.getWidth()*3);
        left.setWidth(left.getWidth()*3);
        launchButton = new TextButton("Launch", MyGdxGame.gameSkin);
        boostButton = new ImageButton(drawable);
        fuelGauge = new FuelGauge();
        boostButton.setHeight(launchButton.getHeight());
        boostButton.setWidth(boostButton.getWidth()*3);
        boostButton.setHeight(boostButton.getHeight()*3);
        addActor(fuelGauge);
        addActor(right);
        addActor(left);
        addActor(launchButton);
        addActor(boostButton);
        debug(false);


    }

    public void moveButtons() {
        rightCorner.set(camera.position.x+ Gdx.graphics.getWidth()/2, camera.position.y-Gdx.graphics.getHeight()/2);
        leftCorner.set(camera.position.x-Gdx.graphics.getWidth()/2, camera.position.y-Gdx.graphics.getHeight()/2);
        //Control buttons position
        launchButton.setPosition(rightCorner.x-launchButton.getWidth()-PADDING, rightCorner.y+right.getHeight()+boostButton.getHeight()+PADDING);
        right.setPosition(rightCorner.x-right.getWidth()-PADDING,rightCorner.y+PADDING);
        left.setPosition(leftCorner.x+PADDING,leftCorner.y+PADDING);
        boostButton.setPosition(rightCorner.x-boostButton.getWidth()-PADDING,right.getY()+right.getHeight()+PADDING);
        fuelGauge.frontSprite.setPosition(left.getX(),left.getY()+left.getHeight()+10);
        fuelGauge.backSprite.setPosition(left.getX(),left.getY()+left.getHeight()+10);
    }
    public void removeControls(){
        right.setVisible(false);
        boostButton.setVisible(false);
        left.setVisible(false);
        fuelGauge.setVisible(false);

    }
    public void removeLaunchButton(){
        launchButton.setVisible(false);
    }
    public void addUI(){
        for(Actor i : getChildren()){
            addActor(i);
            i.setVisible(true);
        }

    }
    public void debug(boolean isDebug){
        for(Actor i : getChildren()){
            i.setDebug(isDebug);
        }
    }
    public void updateFuelGauge(){
        fuelGauge.backSprite.setSize(Stats.currentFuel*20, fuelGauge.backSprite.getHeight());
        if(Stats.currentFuel<=0){
            fuelGauge.backSprite.setSize(0,0);
        }
    }
    class FuelGauge extends Actor{
        Texture front,back;
        Sprite frontSprite,backSprite;
        public FuelGauge(){
            super();
            front = new Texture(Gdx.files.internal("imgs/frontProgress.png"));
            back = new Texture(Gdx.files.internal("imgs/backProgress.png"));
            frontSprite =  new Sprite(front);
            backSprite =  new Sprite(back);
            backSprite.setSize((float)Stats.fuel*20,backSprite.getHeight());
            frontSprite.setSize((float)Stats.fuel*20,backSprite.getHeight());

        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

            batch.draw(backSprite,
                    backSprite.getX(),
                    backSprite.getY(),
                    backSprite.getOriginX(),
                    backSprite.getOriginY(),
                    backSprite.getWidth(),
                    backSprite.getHeight(),
                    backSprite.getScaleX(),
                    backSprite.getScaleY(),
                    backSprite.getRotation()
            );
            batch.draw(frontSprite,
                    frontSprite.getX(),
                    frontSprite.getY(),
                    frontSprite.getOriginX(),
                    frontSprite.getOriginY(),
                    frontSprite.getWidth(),
                    frontSprite.getHeight(),
                    frontSprite.getScaleX(),
                    frontSprite.getScaleY(),
                    frontSprite.getRotation());

        }

    }

}

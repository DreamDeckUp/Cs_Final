package com.mygdx.game.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Cannon extends Actor {
    Texture cannonSheet;
    public Animation<TextureRegion> cannonAnimation;
    Sprite sprite;
    Ground ground;

    private final int FRAME_COLS = 2, FRAME_ROWS = 4;
    public float stateTime;
    public Body body;
    public boolean drawSprite = true;

    public Cannon(Texture texture, Ground ground){
        this.ground = ground;
        cannonSheet = texture;
        //More Animation stuff
        TextureRegion[][] tmp2 = TextureRegion.split(cannonSheet,
                cannonSheet.getWidth()/2,
                cannonSheet.getHeight()/4);
        TextureRegion[] cannonFrames = new TextureRegion[8];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                cannonFrames[index++] = tmp2[i][j];
            }
        }
        cannonAnimation = new Animation<TextureRegion>(0.25f, cannonFrames);
        sprite = new Sprite(cannonFrames[0]);
        sprite.setPosition(0, ground.sprite.getHeight()+25);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        TextureRegion currentCannonFrame = cannonAnimation.getKeyFrame(stateTime, false);
        if(drawSprite){
            batch.draw(currentCannonFrame,
                    sprite.getX(),
                    sprite.getY(),
                    sprite.getOriginX(),
                    sprite.getOriginY(),
                    sprite.getWidth(),
                    sprite.getHeight(),
                    sprite.getScaleX()*4,
                    sprite.getScaleY()*4,
                    sprite.getRotation()
            );
        }
    }
}

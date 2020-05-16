package com.mygdx.game.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Screens.GameScreen;

public class Rocket extends Actor {
    Texture rocketSheet;
    Animation<TextureRegion> rocketAnimation;
    Sprite rocket;

    private static final int FRAME_COLS = 4, FRAME_ROWS = 1;
    public static float stateTime;
    public static Body body;
    public static boolean drawRocket = false;

    public Rocket(Texture rocketSheet, World world){
        this.rocketSheet=rocketSheet;

        TextureRegion[][] tmp = TextureRegion.split(rocketSheet,
                rocketSheet.getWidth() / FRAME_COLS,
                rocketSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] rocketFrames = new TextureRegion[FRAME_ROWS*FRAME_COLS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                rocketFrames[index++] = tmp[i][j];
            }
        }
        rocketAnimation = new Animation<TextureRegion>(0.025f, rocketFrames);
        stateTime = 0f;
        rocket = new Sprite(rocketFrames[0]);

        //Rocket physics
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(rocket.getX()+rocket.getWidth()/2, rocket.getY()+rocket.getHeight()/2);

        body = world.createBody(bodyDef);
        body.setGravityScale(0);
        body.setTransform(75,200,-45);
        body.setFixedRotation(true);

        //Rocket SHAPE
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rocket.getWidth()/2, 68, new Vector2(rocket.getX(),rocket.getY()+34    ), 0);

        //Rocket Fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.001f;
        body.createFixture(fixtureDef);
        shape.dispose();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        rocket.setPosition(body.getPosition().x-rocket.getWidth()/2, body.getPosition().y-rocket.getHeight()/2);
        rocket.setRotation((float)Math.toDegrees(body.getAngle()));
        TextureRegion currentRocketFrame = rocketAnimation.getKeyFrame(stateTime,true);
        if(drawRocket){
            batch.draw(currentRocketFrame, rocket.getX(), rocket.getY(), rocket.getOriginX(), rocket.getOriginY(), rocket.getWidth(), rocket.getHeight(), rocket.getScaleX(), rocket.getScaleY(), rocket.getRotation());
        }
    }
    public static void launch(Body body,Vector2 direction){
        drawRocket = true;
        body.setGravityScale(1);
        body.applyLinearImpulse(direction,Vector2.Zero, true);

    }
}

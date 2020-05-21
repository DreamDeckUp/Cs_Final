package com.mygdx.game.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Stats;

public class Rocket extends Actor {
    Texture rocketSheet;
    Animation<TextureRegion> rocketAnimation;
    public Sprite sprite;


    private static final int FRAME_COLS = 4, FRAME_ROWS = 1;
    public float stateTime;
    public Body body;
    public boolean drawSprite = false;
    public static float currentFuel = Stats.currentFuel;

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
        sprite = new Sprite(rocketFrames[0]);

        //Rocket physics
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX()+sprite.getWidth()/2), (sprite.getY()+sprite.getHeight()/2));

        body = world.createBody(bodyDef);
        body.setGravityScale(0);
        //body.setTransform(75,200,-45);
        body.setFixedRotation(true);
        body.setAngularDamping(2f*(float)Stats.airResist);
        body.setTransform(50,200,-45);

        //Rocket SHAPE
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, 68, new Vector2(sprite.getX(),sprite.getY()+34    ), 0);

        //Rocket Fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.01f*(float)Stats.weight;
        body.createFixture(fixtureDef);
        shape.dispose();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        sprite.setPosition((body.getPosition().x)-sprite.getWidth()/2, (body.getPosition().y)-sprite.getHeight()/2);
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        TextureRegion currentRocketFrame = rocketAnimation.getKeyFrame(stateTime,true);
        if(drawSprite){
            batch.draw(currentRocketFrame,
                    sprite.getX(),
                    sprite.getY(),
                    sprite.getOriginX(),
                    sprite.getOriginY(),
                    sprite.getWidth(),
                    sprite.getHeight(),
                    sprite.getScaleX(),
                    sprite.getScaleY(),
                    sprite.getRotation());
        }
    }
    public void launch(Body body,Vector2 direction){
        System.out.println(direction);
        drawSprite = true;
        body.setGravityScale(1);
        body.applyLinearImpulse(direction, Vector2.Zero, true);

    }

    public Texture getRocketSheet() {
        return rocketSheet;
    }

    public void setRocketSheet(Texture rocketSheet) {
        this.rocketSheet = rocketSheet;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

}

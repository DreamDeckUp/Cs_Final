package com.mygdx.game.Actors;

import com.badlogic.gdx.graphics.Texture;
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

public class Ground extends Actor {
    Texture texture;
    Sprite sprite;
    public Body body;
    public boolean drawSprite = true;
    public Ground(Texture texture, World world){
        //Mirroring textures
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion region = new TextureRegion(texture);
        region.setRegion(0,0,texture.getWidth()*100000,texture.getHeight());
        sprite = new Sprite(region);

        //GROUND BODY
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.KinematicBody;
        groundDef.position.set(0,(sprite.getHeight()/2));
        body = world.createBody(groundDef);

        //GROUND SHAPE
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox((sprite.getWidth()/2), (sprite.getHeight()/2), new Vector2(0,(-sprite.getHeight()/2)), 0);
        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;
        groundFixtureDef.density = 1f;
        body.createFixture(groundFixtureDef);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        sprite.setPosition((body.getPosition().x)-sprite.getWidth()/2, (body.getPosition().y)-sprite.getHeight()/2);
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));

        if (drawSprite) {
            batch.draw(sprite,
                    sprite.getX(),
                    sprite.getY(),
                    sprite.getOriginX(),
                    sprite.getOriginY(),
                    sprite.getWidth(),
                    sprite.getHeight(),
                    sprite.getScaleX(),
                    sprite.getScaleY(),
                    sprite.getRotation()
            );
        }
    }
}

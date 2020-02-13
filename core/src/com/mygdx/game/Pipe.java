package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Pipe {
    BodyDef pipeDef;
    public Body pipeBody;
    FixtureDef fixtureDef;
    PolygonShape shape;

    public Pipe(World world, Texture img, int x, int y){
        pipeDef = new BodyDef();
        pipeDef.type = BodyDef.BodyType.DynamicBody;
        pipeDef.position.set(x+img.getWidth()/2,y+img.getHeight()/2);

        pipeBody = world.createBody(pipeDef);

        shape = new PolygonShape();
        shape.setAsBox(img.getWidth()/2, img.getHeight()/2);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0001f;

        pipeBody.createFixture(fixtureDef);
        shape.dispose();

    }

}

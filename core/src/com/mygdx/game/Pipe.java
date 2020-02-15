package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Screens.GameScreen;

public class Pipe extends Actor {
    FixtureDef fixtureDef;
    PolygonShape shape;
    Sprite img;
    Vector2 pos;
    Vector2 size;
    Body pipeBody;
    final float PIXEL_TO_METERS = 100f;

    public Pipe(Sprite img, Vector2 pos){
        this.img=img;
        this.pos=pos;
        Vector2 imgPos = new Vector2(img.getX(),img.getY());
        pipeBody = GameScreen.groundBody;



        size = new Vector2(img.getWidth()/2,img.getHeight()/2);

        shape = new PolygonShape();
        shape.setAsBox(img.getWidth()/2, img.getHeight()/2, imgPos.add(pos), 0);



        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0001f;

        pipeBody.createFixture(fixtureDef);
        shape.dispose();
        pos.sub(size);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        batch.draw(img,pos.x+pipeBody.getPosition().x,pos.y+pipeBody.getPosition().y,img.getWidth(),img.getHeight());
    }
}

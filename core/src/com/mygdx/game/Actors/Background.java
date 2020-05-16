package com.mygdx.game.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Background extends Actor {
    Sprite sprite;
    public boolean drawSprite = true;
    Ground ground;

    public Background(Texture texture, Ground ground){
        this.ground = ground;
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        TextureRegion region= new TextureRegion(texture);
        region.setRegion(0,0,texture.getWidth()*100000, texture.getHeight());
        sprite = new Sprite(region);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        sprite.setPosition(ground.body.getPosition().x-sprite.getWidth()/2, ground.body.getPosition().y+ground.sprite.getHeight()/2);
        sprite.setRotation((float)Math.toDegrees(ground.body.getAngle()));
        if(drawSprite) {
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

package com.mygdx.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ParallaxBackground extends Actor {

    private int scroll;
    private Array<Texture> layers;
    private Texture grassTexture;
    private final int LAYER_SPEED_DIFFERENCE = 2;
    public int thing = 0;

    float x,y,width,heigth,scaleX,scaleY;
    int originX, originY,rotation,srcX,srcY;
    boolean flipX,flipY;

    private int speed;

    public ParallaxBackground(Array<Texture> textures){
        layers = textures;

        grassTexture = new Texture(Gdx.files.internal("imgs/grass.png"));
        grassTexture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        TextureRegion textureRegion = new TextureRegion(grassTexture);
        textureRegion.setRegion(10,0,grassTexture.getWidth()*8,grassTexture.getHeight());

        for(int i = 0; i <textures.size;i++){
            layers.get(i).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        scroll = 0;
        speed = 0;

        x = y = originX = originY = rotation = srcY = 0;
        width =  Gdx.graphics.getWidth();
        heigth = Gdx.graphics.getHeight();
        scaleX = scaleY = 1;
        flipX = flipY = false;
    }

    public void setSpeed(int newSpeed){

        this.speed = newSpeed;
    }
    public int getSpeed(){
        return speed;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        scroll+=speed;
        for(int i = 0;i<layers.size;i++) {
            srcX = scroll + i*this.LAYER_SPEED_DIFFERENCE *scroll;
            batch.draw(layers.get(i), x, y, originX, originY, width, heigth,scaleX,scaleY,rotation,srcX,srcY,layers.get(i).getWidth(),layers.get(i).getHeight(),flipX,flipY);
        }
        int sourceX = scroll + 4*this.LAYER_SPEED_DIFFERENCE *scroll;

        batch.draw(grassTexture,x, y, originX, originY, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/6, scaleX, scaleY, rotation, sourceX, srcY, grassTexture.getWidth(), grassTexture.getHeight(), flipX, flipY);
    }
}


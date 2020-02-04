package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Upgrade extends Actor {
    Texture art;
    static Texture backProgressBar;
    static Texture frontProgressBar;
    static TextButton upgradeButton;
    int maxLvl;
    int lvl;
    int pixelPerProgress;
    int x,y;


    public Upgrade(Texture art, final int maxLvl,int x,int y){
        this.art=art;
        this.maxLvl=maxLvl;
        this.x=x;
        this.y=y;
        setOrigin(x,y);
        backProgressBar = new Texture(Gdx.files.internal("imgs/backProgress.png"));
        frontProgressBar = new Texture(Gdx.files.internal("imgs/frontProgress.png"));
        lvl=0;
        pixelPerProgress = frontProgressBar.getWidth()/maxLvl;

        upgradeButton = new TextButton("Upgrade", MyGdxGame.gameSkin);
        upgradeButton.setSize(frontProgressBar.getWidth(),100);
        upgradeButton.setPosition(getOriginX()-upgradeButton.getWidth()/2,getOriginY()-frontProgressBar.getHeight()-upgradeButton.getHeight()-15);
        upgradeButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(lvl==maxLvl){
                    return;
                }
                else{
                    lvl++;
                }
                Gdx.app.log("lvl",""+lvl);

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });




    }
    public int multFunction(int level){
        return level;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        batch.draw(art, getOriginX()-150, getOriginY()+10,getOriginX(),getOriginY(),300,200,1f,1f,0,0,0,art.getWidth(),art.getHeight(),false,false);
        batch.draw(backProgressBar,getOriginX()-backProgressBar.getWidth()/2, getOriginY()-backProgressBar.getHeight(),pixelPerProgress*lvl,backProgressBar.getHeight());
        batch.draw(frontProgressBar,getOriginX()-frontProgressBar.getWidth()/2,getOriginY()-frontProgressBar.getHeight(),pixelPerProgress*maxLvl,backProgressBar.getHeight());
    }

    public Texture getArt() {
        return art;
    }

    public void setArt(Texture art) {
        this.art = art;
    }

    public static Texture getBackProgressBar() {
        return backProgressBar;
    }

    public static void setBackProgressBar(Texture backProgressBar) {
        Upgrade.backProgressBar = backProgressBar;
    }

    public static Texture getFrontProgressBar() {
        return frontProgressBar;
    }

    public static void setFrontProgressBar(Texture frontProgressBar) {
        Upgrade.frontProgressBar = frontProgressBar;
    }

    public static TextButton getUpgradeButton() {
        return upgradeButton;
    }

    public static void setUpgradeButton(TextButton upgradeButton) {
        Upgrade.upgradeButton = upgradeButton;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public void setMaxLvl(int maxLvl) {
        this.maxLvl = maxLvl;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getPixelPerprogress() {
        return pixelPerProgress;
    }

    public void setPixelPerprogress(int pixelPerProgress) {
        this.pixelPerProgress = pixelPerProgress;
    }
}

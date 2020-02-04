package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
    Table table;
    public Upgrade(Texture art, final int maxLvl,int x,int y){
        this.art=art;
        this.maxLvl=maxLvl;
        this.x=x;
        this.y=y;

        backProgressBar = new Texture(Gdx.files.internal("imgs/backProgress.png"));
        frontProgressBar = new Texture(Gdx.files.internal("imgs/frontProgress.png"));
        lvl=0;
        pixelPerProgress = frontProgressBar.getWidth()/maxLvl;

        upgradeButton = new TextButton("Upgrade", MyGdxGame.gameSkin);
        upgradeButton.setSize(100,35);
        upgradeButton.setPosition(100,100);
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
        batch.draw(art, this.x,this.y+10+art.getHeight());
        batch.draw(backProgressBar,this.x,this.y+10,pixelPerProgress*lvl,backProgressBar.getHeight());
        batch.draw(frontProgressBar,this.x,this.y+10,pixelPerProgress*maxLvl,backProgressBar.getHeight());
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

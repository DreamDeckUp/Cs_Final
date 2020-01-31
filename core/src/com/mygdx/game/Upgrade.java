package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Upgrade extends Actor {
    Texture art;
    static Texture backProgressBar;
    static Texture frontProgressBar;
    static TextButton upgradeButton;
    int maxLvl;
    int lvl;
    int pixelPerprogress;
    public Upgrade(Texture art,int maxLvl){
        this.art=art;
        this.maxLvl=maxLvl;

        backProgressBar = new Texture(Gdx.files.internal("imgs/backProgress.png"));
        frontProgressBar = new Texture(Gdx.files.internal("imgs/frontProgress.png"));
        lvl=0;
        pixelPerprogress = frontProgressBar.getWidth()/maxLvl;

        upgradeButton = new TextButton("Upgrade", MyGdxGame.gameSkin);
        upgradeButton.setSize(getWidth()*2/3,getHeight()/5);
        upgradeButton.setPosition(getWidth()/2-upgradeButton.getWidth()/2,upgradeButton.getHeight()/2);
        upgradeButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                lvl++;

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

        batch.draw(backProgressBar,20,30,pixelPerprogress*lvl,backProgressBar.getHeight());
        batch.draw(frontProgressBar,20,30,pixelPerprogress*lvl,backProgressBar.getHeight());
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
        return pixelPerprogress;
    }

    public void setPixelPerprogress(int pixelPerprogress) {
        this.pixelPerprogress = pixelPerprogress;
    }
}

package com.mygdx.game.Components;

import com.badlogic.gdx.graphics.Texture;

public abstract class Component{
    private Texture art;
    private double maxLvl;
    private double minLvl;
    private double lvl;

    public Component(Texture art){
        this.art = art;
    }

    public abstract double multFunction();

    public Texture getArt() {
        return art;
    }

    public void setArt(Texture art) {
        this.art = art;
    }

    public double getMaxLvl() {
        return maxLvl;
    }

    public void setMaxLvl(double maxLvl) {
        this.maxLvl = maxLvl;
    }

    public double getLvl() {
        return lvl;
    }

    public void setLvl(double lvl) {
        if (lvl >= maxLvl)
            this.lvl = maxLvl;
        else if (lvl <= minLvl)
            this.lvl = minLvl;
        else
            this.lvl = lvl;
    }

    public void setMinLvl(double minLvl){
        this.minLvl = minLvl;
    }
    public double getMinLvl(){
        return minLvl;
    }
    @Override
    public String toString() {
        return String.valueOf(multFunction());
    }
}

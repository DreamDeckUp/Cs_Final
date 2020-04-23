package com.mygdx.game.Components;

import com.badlogic.gdx.graphics.Texture;

public class FuelComponent extends Component {

    public FuelComponent(Texture art){
        super(art);
        super.setMinLvl(0);
        super.setMaxLvl(100);
        super.setLvl(20);
    }

    public double multFunction(){
        return super.getLvl();
    }

    public void setLvl(double lvl){
        super.setLvl(lvl);
        System.out.println("Changed fuel component");
    }
}
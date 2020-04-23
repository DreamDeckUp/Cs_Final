package com.mygdx.game.Components;

import com.badlogic.gdx.graphics.Texture;

public class EfficiencyComponent extends Component {

    public EfficiencyComponent(Texture art){
        super(art);
        super.setMinLvl(1);
        super.setMaxLvl(40);
        super.setLvl(10);
    }

    public double multFunction(){
        return super.getLvl();
    }

    public void setLvl(double lvl){
        super.setLvl(lvl);
        System.out.println("Changed eff component");
    }
}

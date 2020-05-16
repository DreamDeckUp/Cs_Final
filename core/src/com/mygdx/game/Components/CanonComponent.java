package com.mygdx.game.Components;

import com.badlogic.gdx.graphics.Texture;

public class CanonComponent extends Component {

    public CanonComponent(Texture art){
        super(art);
        super.setMinLvl(0);
        super.setMaxLvl(20);
        super.setLvl(1);
    }

    public double multFunction(){
        return super.getLvl();
    }

    public void setLvl(double lvl){
        super.setLvl(lvl);
        System.out.println("Changed canon component");
    }
}

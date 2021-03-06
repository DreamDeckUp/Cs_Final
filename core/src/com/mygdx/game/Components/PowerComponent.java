package com.mygdx.game.Components;

import com.badlogic.gdx.graphics.Texture;

public class PowerComponent extends Component {

    public PowerComponent(Texture art){
        super(art);
        super.setMinLvl(1);
        super.setMaxLvl(50);
        super.setLvl(15);
    }

    public double multFunction(){
        try {
            return 20 * Math.pow(Math.log10(super.getLvl()), 2);
        }
        catch (Exception e){
            System.out.println("Unnacceptable value for lvl");
            return 0;
        }
    }

    public void setLvl(double lvl){
        super.setLvl(lvl);
        System.out.println("Changed power component");
    }
}

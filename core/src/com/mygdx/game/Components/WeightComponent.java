package com.mygdx.game.Components;

import com.badlogic.gdx.graphics.Texture;

public class WeightComponent extends Component {

    public WeightComponent(Texture art){
        super(art);
        super.setMinLvl(-20);
        super.setMaxLvl(20);
        super.setLvl(5);
    }

    public double multFunction(){
        try{
            return Math.pow(super.getLvl()/5, 2)+1;
        }
        catch (Exception e){
            System.out.println("Error in weight calculation.");
            return 1;
        }
    }

    public void setLvl(double lvl){
        super.setLvl(lvl);
        System.out.println("Changed weight component");
    }
}

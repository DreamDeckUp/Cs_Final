package com.mygdx.game.Components;

import com.badlogic.gdx.graphics.Texture;

public class AirComponent extends Component {

    public AirComponent(Texture art){
        super(art);
        super.setMinLvl(0.5);
        super.setMaxLvl(20.5);
        super.setLvl(4.5);
    }

    public double multFunction(){
        try{
            //To understand this function, follow the link, the air resistance function is in blue: https://www.desmos.com/calculator/rwfquyorcg
            return 20/super.getLvl();
        }
        catch (Exception e){
            System.out.println("Unnacceptable value of lvl");
            return 0;
        }
    }
    public void setLvl(double lvl){
        super.setLvl(lvl);
    }
}

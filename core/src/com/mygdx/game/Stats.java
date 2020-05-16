package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Components.Component;
import com.mygdx.game.Screens.GameScreen;

public class Stats {

    public static Array<Component> components = MyGdxGame.getComponents();
    public static double fuel = components.get(0).multFunction();
    public static double efficiency = components.get(1).multFunction();
    public static double power = components.get(2).multFunction();
    public static double airResist = components.get(3).multFunction();
    public static double cannonStrength = components.get(4).multFunction();
    public static double weight =  components.get(5).multFunction();
    public static float currentFuel = (float)fuel;
    public static int currency = ((MyGdxGame)(GameScreen.game)).getCurrency();
}

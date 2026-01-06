package com.esiea.pootp.Object;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Monster.Monster;

public abstract class ObjectMonster{
    protected int weight; 

    public ObjectMonster(int weight){
        this.weight = weight; 
    }

    public int getWeight(){
        return weight;
    }

    public String use(Monster monster, Battle battle){
        return "";
    }

    public String getName(){
        return "ObjectMonster";
    }

}

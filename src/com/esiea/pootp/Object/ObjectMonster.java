package com.esiea.pootp.Object;

import com.esiea.pootp.Monster.Monster;

public abstract class ObjectMonster{
    protected int weight; 

    public ObjectMonster(int weight){
        this.weight = weight; 
    }

    public int getWeight(){
        return weight;
    }

    public void use(Monster monster){
    }

}

package com.esiea.pootp.Monster;

public class ElectricMonster extends Monster{
    private double paralysisChance;

    public ElectricMonster(String name, int health, int power, int defense, int speed, double paralysisChance) {
        super(name, health, power, defense, speed);
        this.paralysisChance = paralysisChance;
    }
    
}

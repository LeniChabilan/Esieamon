package com.esiea.pootp.Monster;

public class GrassMonster extends NatureMonster {
    private double healChance;

    public GrassMonster(String name, int health, int power, int defense, int speed, double healChance) {
        super(name, health, power, defense, speed);
        this.healChance = healChance;
    }
    
}

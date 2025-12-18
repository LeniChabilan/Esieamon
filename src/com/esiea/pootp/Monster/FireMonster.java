package com.esiea.pootp.Monster;

public class FireMonster extends Monster {
    private double burnChance;

    public FireMonster(String name, int health, int power, int defense, int speed, double burnChance) {
        super(name, health, power, defense, speed);
        this.burnChance = burnChance;
    }

    public double getBurnChance() {
        return burnChance;
    }
}

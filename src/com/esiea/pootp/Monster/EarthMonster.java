package com.esiea.pootp.Monster;

public class EarthMonster extends Monster {
    private double burrowChance;

    public EarthMonster(String name, int maxHealth, int power, int defense, int speed, double burrowChance) {
        super(name, maxHealth, power, defense, speed);
        this.burrowChance = burrowChance;
    }
}

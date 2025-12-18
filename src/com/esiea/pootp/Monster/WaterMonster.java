package com.esiea.pootp.Monster;

public class WaterMonster extends Monster {
    private double floodChance;
    private double fallChance;

    public WaterMonster(String name, int maxHealth, int power, int defense, int speed, double floodChance, double fallChance) {
        super(name, maxHealth, power, defense, speed);
        this.floodChance = floodChance;
        this.fallChance = fallChance;
    }

    public double getFloodChance() {
        return floodChance;
    }
    public double getFallChance() {
        return fallChance;
    }
}

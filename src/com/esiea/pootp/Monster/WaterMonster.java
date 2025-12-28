package com.esiea.pootp.Monster;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Ground.FloodedGround;

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

    @Override
    public boolean applyGround(Battle battle) {
        if (Math.random() < this.floodChance) {
            battle.setGround(new FloodedGround(this.fallChance));
            return true;
        }
        return false;
    }
}

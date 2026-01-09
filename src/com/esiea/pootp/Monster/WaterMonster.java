package com.esiea.pootp.Monster;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Ground.FloodedGround;

/**
 * Monstre de type Eau. Peut inonder le terrain et provoquer des chutes.
 */
public class WaterMonster extends Monster {
    private double floodChance;
    private double fallChance;

    /**
     * @param name nom
     * @param maxHealth PV max
     * @param power attaque
     * @param defense défense
     * @param speed vitesse
     * @param floodChance probabilité d'inonder le terrain
     * @param fallChance probabilité de chute sur terrain inondé
     */
    public WaterMonster(String name, int maxHealth, int power, int defense, int speed, double floodChance, double fallChance) {
        super(name, maxHealth, power, defense, speed);
        this.floodChance = floodChance;
        this.fallChance = fallChance;
    }

    /** @return probabilité d'inondation */
    public double getFloodChance() {
        return floodChance;
    }
    /** @return probabilité de chute */
    public double getFallChance() {
        return fallChance;
    }

    @Override
    /**
     * Tente d'appliquer le terrain Inondé dans la bataille.
     */
    public boolean applyGround(Battle battle) {
        if (Math.random() < this.floodChance) {
            battle.setGround(new FloodedGround(this.fallChance));
            return true;
        }
        return false;
    }
}

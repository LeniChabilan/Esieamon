package com.esiea.pootp.Monster;

import com.esiea.pootp.Status.BurrowedStatus;

/**
 * Monstre de type Terre. Peut s'enfouir (état Enfoui) selon une probabilité.
 */
public class EarthMonster extends Monster {
    private double burrowChance;

    /**
     * @param name nom
     * @param maxHealth PV max
     * @param power attaque
     * @param defense défense
     * @param speed vitesse
     * @param burrowChance probabilité de s'enfouir
     */
    public EarthMonster(String name, int maxHealth, int power, int defense, int speed, double burrowChance) {
        super(name, maxHealth, power, defense, speed);
        this.burrowChance = burrowChance;
    }

    /** @return probabilité d'enfouissement */
    public double getBurrowChance() {
        return burrowChance;
    }

    @Override
    /**
     * Tente d'appliquer l'état Enfoui à soi-même.
     */
    public boolean applyStatus(Monster defender) {
        if (Math.random() < this.burrowChance) {
            if (this.getStatus().getName() == "Normal") {
                this.setStatus(new BurrowedStatus());
                return true;   
            }
        }
        return false;
    }

}

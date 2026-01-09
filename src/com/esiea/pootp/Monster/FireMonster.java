package com.esiea.pootp.Monster;

import com.esiea.pootp.Status.BurnedStatus;

/**
 * Monstre de type Feu. Peut infliger l'état Brûlé selon une probabilité.
 */
public class FireMonster extends Monster {
    private double burnChance;

    /**
     * @param name nom
     * @param health PV max
     * @param power attaque
     * @param defense défense
     * @param speed vitesse
     * @param burnChance probabilité de brûlure (0.0 à 1.0)
     */
    public FireMonster(String name, int health, int power, int defense, int speed, double burnChance) {
        super(name, health, power, defense, speed);
        this.burnChance = burnChance;
    }

    /**
     * @return probabilité d'infliger Brûlé
     */
    public double getBurnChance() {
        return burnChance;
    }

    @Override
    /**
     * Tente d'appliquer l'état Brûlé au défenseur.
     */
    public boolean applyStatus(Monster defender) {
        if (Math.random() < this.burnChance) {
            if (defender.getStatus().getName() == "Normal") {
                defender.setStatus(new BurnedStatus());
                return true;
            }
        }
        return false;
    }
}
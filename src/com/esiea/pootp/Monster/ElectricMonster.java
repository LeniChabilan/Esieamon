package com.esiea.pootp.Monster;

import com.esiea.pootp.Status.ParalyzedStatus;
import com.esiea.pootp.Status.Status;

/**
 * Monstre de type Électrique. Peut paralyser l'adversaire selon une probabilité.
 */
public class ElectricMonster extends Monster{
    private double paralysisChance;

    /**
     * @param name nom
     * @param health PV max
     * @param power attaque
     * @param defense défense
     * @param speed vitesse
     * @param paralysisChance probabilité de paralysie
     */
    public ElectricMonster(String name, int health, int power, int defense, int speed, double paralysisChance) {
        super(name, health, power, defense, speed);
        this.paralysisChance = paralysisChance;
    }

    /** @return probabilité de paralysie */
    public double getParalysisChance() {
        return paralysisChance;
    }

    @Override
    /**
     * Tente d'appliquer Paralysé au défenseur.
     */
    public boolean applyStatus(Monster defender) {
        if (Math.random() < this.paralysisChance) {
            if (defender.getStatus().getName() == "Normal" || defender.getStatus().getName() == "Paralysé") {
                defender.setStatus(new ParalyzedStatus());
                return true;   
            }
        }
        return false;
    }
    
}

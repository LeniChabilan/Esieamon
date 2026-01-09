package com.esiea.pootp.Monster;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Status.NormalStatus;

/**
 * Monstre de type Plante (hérite des avantages de Nature).
 * Peut se guérir d'un statut selon une probabilité.
 */
public class GrassMonster extends NatureMonster {
    private double healChance;

    /**
     * @param name nom
     * @param health PV max
     * @param power attaque
     * @param defense défense
     * @param speed vitesse
     * @param healChance probabilité de guérison de statut
     */
    public GrassMonster(String name, int health, int power, int defense, int speed, double healChance) {
        super(name, health, power, defense, speed);
        this.healChance = healChance;
    }

    /** @return probabilité de guérison */
    public double getHealChance() {
        return healChance;
    }

    @Override
    /**
     * Effet spécial: possibilité de se débarrasser d'un statut.
     */
    public String applySpecialEffect(Battle battle) {
        double randomValue = Math.random();
        if (randomValue < healChance) {
            if (this.getStatus().getName() != "Normal") {
                this.setStatus(new NormalStatus());
                return this.getName() + " est guéri de son statut grâce au terrain inondé !";
            }
        }
        return "";
    }
    
}

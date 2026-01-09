package com.esiea.pootp.Monster;

import com.esiea.pootp.Status.PoisonedStatus;

/**
 * Monstre de type Insecte (Nature). Empoisonne l'adversaire après un certain 
 * nombre d'attaques spéciales réussies.
 */
public class InsectMonster extends NatureMonster {

    private int nbAttacksSpe = 0;

    public InsectMonster(String name, int health, int power, int defense, int speed) {
        super(name, health, power, defense, speed);
    }

    /** @return nombre d'attaques spéciales cumulées */
    public int getNbAttacksSpe() {
        return nbAttacksSpe;
    }

    /** @param nbAttacksSpe nouveau compteur d'attaques spéciales */
    public void setNbAttacksSpe(int nbAttacksSpe) {
        this.nbAttacksSpe = nbAttacksSpe;
    }

    @Override
    /**
     * Empoisonne le défenseur toutes les 3 attaques spéciales.
     */
    public boolean applyStatus(Monster defender) {
        super.applyStatus(defender);
        nbAttacksSpe++;
        if (nbAttacksSpe >= 3) {
            if (defender.getStatus().getName() == "Normal") {
                defender.setStatus(new PoisonedStatus());
                nbAttacksSpe = 0;
                return true;
            }
        }
        return false;
    }

}
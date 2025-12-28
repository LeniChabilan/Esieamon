package com.esiea.pootp.Monster;

import com.esiea.pootp.Status.PoisonedStatus;

public class InsectMonster extends NatureMonster {

    private int nbAttacksSpe = 0;

    public InsectMonster(String name, int health, int power, int defense, int speed) {
        super(name, health, power, defense, speed);
    }

    public int getNbAttacksSpe() {
        return nbAttacksSpe;
    }

    public void setNbAttacksSpe(int nbAttacksSpe) {
        this.nbAttacksSpe = nbAttacksSpe;
    }

    @Override
    public boolean applyStatus(Monster defender) {
        super.applyStatus(defender);
        nbAttacksSpe++;
        if (nbAttacksSpe >= 3) {
            nbAttacksSpe = 0;
            defender.setStatus(new PoisonedStatus());
            return true;
        }
        return false;
    }

}
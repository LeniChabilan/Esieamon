package com.esiea.pootp.Monster;

import com.esiea.pootp.Status.BurrowedStatus;

public class EarthMonster extends Monster {
    private double burrowChance;

    public EarthMonster(String name, int maxHealth, int power, int defense, int speed, double burrowChance) {
        super(name, maxHealth, power, defense, speed);
        this.burrowChance = burrowChance;
    }

    public double getBurrowChance() {
        return burrowChance;
    }

    @Override
    public boolean applyStatus(Monster defender) {
        if (Math.random() < this.burrowChance) {
            if (defender.getStatus().getName() == "Normal") {
                defender.setStatus(new BurrowedStatus());
                return true;   
            }
        }
        return false;
    }

}

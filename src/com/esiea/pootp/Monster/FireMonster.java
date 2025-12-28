package com.esiea.pootp.Monster;

import com.esiea.pootp.Status.BurnedStatus;

public class FireMonster extends Monster {
    private double burnChance;

    public FireMonster(String name, int health, int power, int defense, int speed, double burnChance) {
        super(name, health, power, defense, speed);
        this.burnChance = burnChance;
    }

    public double getBurnChance() {
        return burnChance;
    }

    @Override
    public boolean applyStatus(Monster defender) {
        if (Math.random() < this.burnChance) {
            if (defender.getStatus().getName() == "Normal" || defender.getStatus().getName() == "Brûlé") {
                defender.setStatus(new BurnedStatus());
                return true;
            }
        }
        return false;
    }
}
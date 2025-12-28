package com.esiea.pootp.Monster;

import com.esiea.pootp.Status.ParalyzedStatus;
import com.esiea.pootp.Status.Status;

public class ElectricMonster extends Monster{
    private double paralysisChance;

    public ElectricMonster(String name, int health, int power, int defense, int speed, double paralysisChance) {
        super(name, health, power, defense, speed);
        this.paralysisChance = paralysisChance;
    }

    public double getParalysisChance() {
        return paralysisChance;
    }

    @Override
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

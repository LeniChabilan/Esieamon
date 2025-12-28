package com.esiea.pootp.Ground;

public class FloodedGround extends Ground {
    private double fallChance;


    public FloodedGround(double fallChance) {
        super("Inondé");
        this.fallChance = fallChance;
    }


    public double getFallChance() {
        return fallChance;
    }

    
    
}

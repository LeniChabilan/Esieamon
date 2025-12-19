package com.esiea.pootp.Ground;

public class FloodedGround extends Ground {
    private double floodEffectiveness;


    public FloodedGround(double floodEffectiveness) {
        super("Flooded Ground");
        this.floodEffectiveness = floodEffectiveness;
    }


    public double getFloodEffectiveness() {
        return floodEffectiveness;
    }

    
    
}

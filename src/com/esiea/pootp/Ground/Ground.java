package com.esiea.pootp.Ground;

import com.esiea.pootp.Monster.Monster;

public abstract class Ground {

    private String name;
    private int duration;

    public Ground(String name) {
        this.name = name;
        this.duration = 0;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public void applyGroundEffect(Monster monster1, Monster monster2) {
        // Default implementation (can be overridden by subclasses)
    }
}
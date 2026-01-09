package com.esiea.pootp.Ground;

import java.util.HashMap;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Monster.Monster;

/**
 * Représente un type de terrain affectant potentiellement le combat.
 * Chaque terrain possède un nom et une durée de persistance en tours.
 */
public abstract class Ground {

    private String name;
    private int duration;

    /**
     * Crée un terrain avec le nom fourni.
     * @param name nom du terrain
     */
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

    /**
     * Incrémente la durée du terrain d'un tour.
     */
    public void incrementDuration() {
        this.duration++;
    }

    public HashMap<String, String> applyGroundEffect(Monster monster1, Monster monster2, Battle battle) {
        // Implémentation par défaut (peut être surchargée par les sous-classes)
        return new HashMap<>();
    }
}
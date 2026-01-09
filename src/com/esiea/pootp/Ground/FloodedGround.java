package com.esiea.pootp.Ground;

import java.util.HashMap;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Monster.WaterMonster;

/**
 * Terrain Inondé: chaque tour, les monstres non Eau ont une chance de glisser
 * et de perdre des PV, devenant incapables d'attaquer ce tour. Le terrain
 * disparaît après une durée aléatoire (1 à 3 tours).
 */
public class FloodedGround extends Ground {
    private double fallChance;
    private int maxDuration;


    /**
     * @param fallChance probabilité pour un monstre non Eau de glisser à chaque tour
     */
    public FloodedGround(double fallChance) {
        super("Inondé");
        this.fallChance = fallChance;
        // Durée aléatoire entre 1 et 3 tours
        this.maxDuration = (int)(Math.random() * 3) + 1;
    }


    /** @return probabilité de glissade */
    public double getFallChance() {
        return fallChance;
    }

    @Override
    /**
     * Applique les effets du terrain: dégâts et blocage possible.
     * Renseigne les clés: monster1_attackAble, monster2_attackAble,
     * monster1_statusEffect, monster2_statusEffect, groundCured.
     */
    public HashMap<String, String> applyGroundEffect(Monster monster1, Monster monster2, Battle battle) {
        HashMap<String, String> result = new HashMap<>();
        // Incrémente la durée du terrain à chaque tour
        this.incrementDuration();
        boolean monster1Falls = Math.random() < fallChance && !(monster1 instanceof WaterMonster);
        boolean monster2Falls = Math.random() < fallChance && !(monster2 instanceof WaterMonster);
        
        if (monster1Falls) {
            int fallDamage = monster1.getPower() / 4;
            monster1.setCurrentHealth(monster1.getCurrentHealth() - fallDamage);
            result.put("monster1_statusEffect", monster1.getName() + " a glissé et a pris " + fallDamage + " points de dégâts !");
            result.put("monster1_attackAble", "false");
        } else {
            result.put("monster1_attackAble", "true");
        }
        
        if (monster2Falls) {
            int fallDamage = monster2.getPower() / 4;
            monster2.setCurrentHealth(monster2.getCurrentHealth() - fallDamage);
            result.put("monster2_statusEffect", monster2.getName() + " a glissé et a pris " + fallDamage + " points de dégâts !");
            result.put("monster2_attackAble", "false");
        } else {
            result.put("monster2_attackAble", "true");
        }

        // Le terrain dure entre 1 et 3 tours
        if (this.getDuration() >= maxDuration) {
            result.put("groundCured", "true");
            battle.setGround(new NormalGround());
            return result;
        }
        
        return result;
    }
    
    
}

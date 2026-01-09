package com.esiea.pootp.Object.Potion;

import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Object.ObjectMonster;
import com.esiea.pootp.Battle.Battle;

/**
 * Potion consommable appliquant un soin (HP) ou un bonus temporaire (Attaque, Défense, Vitesse)
 * au monstre ciblé. Le poids dépend de l'efficacité de la potion.
 */
public class Potion extends ObjectMonster {

    private PotionEfficiency potionEfficiency;
    private PotionType potionType; 

    /**
     * Crée une potion d'un certain type et d'une certaine efficacité.
     * Le poids est déterminé automatiquement selon l'efficacité:
     * NORMAL=1, SUPER=2, HYPER=3.
     * @param potionEfficiency efficacité de la potion
     * @param potionType type d'effet (HP/ATTACK/DEFENSE/SPEED)
     */
    public Potion(PotionEfficiency potionEfficiency, PotionType potionType) {

        super(0);
            switch (potionEfficiency) {
                case NORMAL -> this.weight = 1;
                case SUPER -> this.weight = 2;
                case HYPER -> this.weight = 3;
            }
        this.potionEfficiency = potionEfficiency;
        this.potionType = potionType;   
    }

    /** @return efficacité de la potion */
    public PotionEfficiency getPotionEfficiency() {
        return potionEfficiency;
    }

    /** @return type d'effet de la potion */
    public PotionType getPotionType() {
        return potionType;
    }

    @Override
    /**
     * @return un nom lisible sous la forme "EFFICIENCY TYPE Potion"
     */
    public String getName() {
        return potionEfficiency + " " + potionType + " Potion";
    }

    @Override
    /**
     * Applique l'effet de la potion au monstre ciblé et retourne un message.
     */
    public String use(Monster monster, Battle battle) {
        switch (potionType) {
            case HP -> {
                int healAmount = switch (potionEfficiency) {
                    case NORMAL -> 20;
                    case SUPER -> 50;
                    case HYPER -> 100;
                };
                monster.heal(healAmount);
                return healAmount + " HP restaurés.";
            }
            case ATTACK -> {
                int boostAmount = switch (potionEfficiency) {
                    case NORMAL -> 15;
                    case SUPER -> 25;
                    case HYPER -> 40;
                };
                monster.boostAttack(boostAmount);
                return "Attaque augmentée de " + boostAmount + ".";
            }
            case DEFENSE -> {
                int boostAmount = switch (potionEfficiency) {
                    case NORMAL -> 15;
                    case SUPER -> 25;
                    case HYPER -> 40;
                };
                monster.boostDefense(boostAmount);           
                return "Défense augmentée de " + boostAmount + ".";
            }
            case SPEED -> {
                int boostAmount = switch (potionEfficiency) {
                    case NORMAL -> 15;
                    case SUPER -> 25;
                    case HYPER -> 40;
                };
                monster.boostSpeed(boostAmount);
                return "Vitesse augmentée de " + boostAmount + ".";
            }
            default -> {
                return "";
            }
        }
    }
    
}

package com.esiea.pootp.Object.Potion;

import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Object.ObjectMonster;
import com.esiea.pootp.Battle.Battle;


public class Potion extends ObjectMonster {

    private PotionEfficiency potionEfficiency;
    private PotionType potionType; 

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

    public PotionEfficiency getPotionEfficiency() {
        return potionEfficiency;
    }

    public PotionType getPotionType() {
        return potionType;
    }

    @Override
    public String getName() {
        return potionEfficiency + " " + potionType + " Potion";
    }

    @Override
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

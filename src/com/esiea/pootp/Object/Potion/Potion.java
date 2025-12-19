package com.esiea.pootp.Object.Potion;

import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Object.ObjectMonster;



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
    public void use(Monster monster) {
        switch (potionType) {
            case HP -> {
                int healAmount = switch (potionEfficiency) {
                    case NORMAL -> 20;
                    case SUPER -> 50;
                    case HYPER -> 100;
                };
                monster.heal(healAmount);
                System.out.println("Used " + potionEfficiency + " " + potionType + " potion. Healed " + healAmount + " HP.");
            }
            case ATTACK -> {
                int boostAmount = switch (potionEfficiency) {
                    case NORMAL -> 5;
                    case SUPER -> 10;
                    case HYPER -> 20;
                };
                monster.boostAttack(boostAmount);
                System.out.println("Used " + potionEfficiency + " " + potionType + " potion. Increased attack by " + boostAmount + ".");
            }
            case DEFENSE -> {
                int boostAmount = switch (potionEfficiency) {
                    case NORMAL -> 5;
                    case SUPER -> 10;
                    case HYPER -> 20;
                };
                monster.boostDefense(boostAmount);
                System.out.println("Used " + potionEfficiency + " " + potionType + " potion. Increased defense by " + boostAmount + ".");
            }
            case SPEED -> {
                int boostAmount = switch (potionEfficiency) {
                    case NORMAL -> 5;
                    case SUPER -> 10;
                    case HYPER -> 20;
                };
                monster.boostSpeed(boostAmount);
                System.out.println("Used " + potionEfficiency + " " + potionType + " potion. Increased speed by " + boostAmount + ".");
            }
        }
    }
    
}

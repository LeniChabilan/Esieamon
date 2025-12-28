package com.esiea.pootp.Attack;

import com.esiea.pootp.Monster.*;
import java.util.HashMap;

public class AttackMonster extends Attack {
    public int power;
    public int nbUses;
    public int maxUses;
    public double failureRate;
    public AttackType type;

    public AttackMonster(String name, int power, int maxUses, double failureRate, AttackType type) {
        super(name);
        this.power = power;
        this.maxUses = maxUses;
        this.nbUses = maxUses;
        this.failureRate = failureRate;
        this.type = type;
    }

    @Override
    public HashMap<String, String> performAttack(Monster attacker, Monster defender) {
        HashMap<String, String> result = new HashMap<>();

        // Check for available uses
        if (nbUses <= 0) {
            return result;
        }
        nbUses--;

        // Check for failure
        if (Math.random() < failureRate) {
            result.put("attackName", name);
            result.put("attackerName", attacker.getName());
            result.put("defenderName", defender.getName());
            result.put("damage", "0");
            result.put("effectiveness", "l'attaque a échoué !");
            return result;
        }

        // Handle status application
        if (this.type != AttackType.NORMAL) {
            boolean statusApplied = attacker.applyStatus(defender);
            if (statusApplied) {
                result.put("status", defender.getName() + " est maintenant " + defender.getStatus().getName() + " !");
            }
        }

        // Calculate damage
        double coef = 0.85 + Math.random() * 0.15;
        double typeEffectiveness = getTypeEffectiveness(this.type, defender);
        double base = ((11.0 * this.power * attacker.getPower()) / (25.0 * defender.getDefense())) + 2.0;
        int damage = (int) Math.round(base * coef * typeEffectiveness);

        // Apply damage
        defender.currentHealth -= damage;
        if (defender.currentHealth < 0) {
            defender.currentHealth = 0;
        }

        // Prepare result
        result.put("damage", Integer.toString(damage));
        if (typeEffectiveness > 1.0) {
            result.put("effectiveness", "c'est super efficace !");
        } else if (typeEffectiveness < 1.0) {
            result.put("effectiveness", "ce n'est pas très efficace");
        } else {
            result.put("effectiveness", "efficacité normale");
        }
        result.put("attackName", name);
        result.put("attackerName", attacker.getName());
        result.put("defenderName", defender.getName());
        return result;
    }

    private double getTypeEffectiveness(AttackType attackType, Monster defender) {
        double effectiveness = 1.0;
        switch (attackType) {
            case FIRE:
                if (defender instanceof NatureMonster) {
                    effectiveness = 2.0;
                }
                if (defender instanceof WaterMonster) {
                    effectiveness = 0.5;
                }
                break;
            case WATER:
                if (defender instanceof FireMonster) {
                    effectiveness = 2.0;
                }
                if (defender instanceof ElectricMonster) {
                    effectiveness = 0.5;
                }
                break;
            case GRASS:
            case INSECT:
                if (defender instanceof EarthMonster) {
                    effectiveness = 2.0;
                }
                if (defender instanceof FireMonster) {
                    effectiveness = 0.5;
                }
                break;
            case ELECTRIC:
                if (defender instanceof WaterMonster) {
                    effectiveness = 2.0;
                }
                if (defender instanceof EarthMonster) {
                    effectiveness = 0.5;
                }
                break;
            case EARTH:
                if (defender instanceof ElectricMonster) {
                    effectiveness = 2.0;
                }
                if (defender instanceof NatureMonster) {
                    effectiveness = 0.5;
                }
                break;
            default:
                break;
        }
        return effectiveness;
    }   

    public int getPower() {
        return power;
    }

    public int getNbUses() {
        return nbUses;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public double getFailureRate() {
        return failureRate;
    }

    public AttackType getType() {
        return type;
    }
}

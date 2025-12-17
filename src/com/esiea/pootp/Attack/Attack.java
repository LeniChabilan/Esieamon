package com.esiea.pootp.Attack;

import com.esiea.pootp.Monster.*;

public class Attack {
    public String name;
    public int power;
    public int nbUses;
    public int maxUses;
    public double failureRate;
    public AttackType type;

    public Attack(String name, int power, int maxUses, double failureRate, AttackType type) {
        this.name = name;
        this.power = power;
        this.maxUses = maxUses;
        this.nbUses = maxUses;
        this.failureRate = failureRate;
        this.type = type;
    }

    public void performAttack(Monster attacker, Monster defender) {
        if (nbUses <= 0) {
            System.out.println(attacker.getName() + " cannot use " + name + " anymore!");
            return;
        }
        nbUses--;

        // Calculate damage
        double coef = 0.85 + Math.random() * 0.15;
        double base = ((11.0 * this.power * attacker.getPower()) / (25.0 * defender.getDefense())) + 2.0;
        int damage = (int) Math.round(base * coef);


        // Apply damage
        defender.currentHealth -= damage;
        if (defender.currentHealth < 0) {
            defender.currentHealth = 0;
        }
    }

    public String getName() {
        return name;
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

package com.esiea.pootp.Monster;

import java.util.List;
import java.util.ArrayList;
import com.esiea.pootp.Attack.Attack;

public abstract class Monster {
    public String name;
    public int health;
    public int power;
    public int defense;
    public int speed;
    public int currentHealth;
    public List<Attack> attacks;

    public Monster(String name, int health, int power, int defense, int speed) {
        this.name = name;
        this.health = health;
        this.power = power;
        this.defense = defense;
        this.speed = speed;
        this.currentHealth = health;
        this.attacks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getPower() {
        return power;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public void heal(int amount) {
        currentHealth += amount;
        if (currentHealth > health) {
            currentHealth = health;
        }
    }

    public void boostAttack(int amount) {
        power += amount;
    }

    public void boostDefense(int amount) {
        defense += amount;
    }

    public void boostSpeed(int amount) {
        speed += amount;
    }
}
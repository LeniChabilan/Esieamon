package com.esiea.pootp.Monster;

import java.util.List;
import java.util.ArrayList;
import com.esiea.pootp.Attack.AttackMonster;
import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Status.NormalStatus;
import com.esiea.pootp.Status.Status;

public abstract class Monster {
    public String name;
    public int health;
    public int power;
    public int defense;
    public int speed;
    public int currentHealth;
    public List<AttackMonster> attacks;
    private Status status;

    public Monster(String name, int health, int power, int defense, int speed) {
        this.name = name;
        this.health = health;
        this.power = power;
        this.defense = defense;
        this.speed = speed;
        this.currentHealth = health;
        this.attacks = new ArrayList<>();
        this.status = new NormalStatus();
    }

    public boolean hasAvailableAttacks() {
        for (AttackMonster attack : attacks) {
            if (attack.nbUses > 0) {
                return true;
            }
        }
        return false;
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

    public Status getStatus() {
        return status;
    }

    public List<AttackMonster> getAttacks() {
        return attacks;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean applyStatus(Monster defender) {
        return false;
    }

    public boolean applyGround(Battle battle) {
        return false;
    }

    public String applyPassiveEffect(Battle battle) {
        return "";
    }
}
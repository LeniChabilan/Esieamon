package com.esiea.pootp.Monster;

import java.util.List;
import java.util.ArrayList;
import com.esiea.pootp.Attack.AttackMonster;
import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Status.NormalStatus;
import com.esiea.pootp.Status.Status;

/**
 * Modèle de base pour un monstre d'Esieamon.
 * Chaque monstre possède des statistiques (PV, attaque, défense, vitesse), une liste d'attaques
 * et un statut (par défaut normal). Des méthodes de support permettent de soigner, booster et
 * appliquer des effets spéciaux/passifs.
 */
public abstract class Monster {
    protected String name;
    protected int health;
    protected int power;
    protected int defense;
    protected int speed;
    protected int currentHealth;
    protected List<AttackMonster> attacks;
    protected Status status;

    /**
     * Construit un monstre avec ses statistiques initiales.
     * @param name nom du monstre
     * @param health points de vie (PV) max
     * @param power puissance d'attaque
     * @param defense défense
     * @param speed vitesse
     */
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

    /**
     * Indique si au moins une attaque du monstre possède encore des PP.
     * @return true si une attaque est disponible, false sinon
     */
    public boolean hasAvailableAttacks() {
        for (AttackMonster attack : attacks) {
            if (attack.getNbUses() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return le nom du monstre
     */
    public String getName() {
        return name;
    }

    /** @return les PV max du monstre */
    public int getHealth() {
        return health;
    }

    /** @return la puissance d'attaque du monstre */
    public int getPower() {
        return power;
    }

    /** @return la défense du monstre */
    public int getDefense() {
        return defense;
    }

    /** @return la vitesse du monstre */
    public int getSpeed() {
        return speed;
    }

    /**
     * Définit la défense du monstre.
     * @param defense
     */
    public void setDefense(int defense) {
        this.defense = defense;
    }


    /**     
     * @return les PV actuels du monstre
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**     
     * @return le statut actuel du monstre
     */ 
    public Status getStatus() {
        return status;
    }

    /**     
     * @return la liste des attaques du monstre
     */
    public List<AttackMonster> getAttacks() {
        return attacks;
    }

    /**     
     * Définit les PV actuels du monstre
     * @param currentHealth
     */
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * Soigne le monstre d'une certaine quantité (bornée au maximum de PV).
     * @param amount quantité de PV à rendre
     */
    public void heal(int amount) {
        currentHealth += amount;
        if (currentHealth > health) {
            currentHealth = health;
        }
    }

    /**
     * Augmente la puissance d'attaque du monstre.
     * @param amount quantité d'augmentation
     */
    public void boostAttack(int amount) {
        power += amount;
    }

    /**
     * Augmente la défense du monstre.
     * @param amount quantité d'augmentation
     */
    public void boostDefense(int amount) {
        defense += amount;
    }

    /**
     * Augmente la vitesse du monstre.
     * @param amount quantité d'augmentation
     */
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
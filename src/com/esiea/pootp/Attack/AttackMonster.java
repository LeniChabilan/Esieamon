package com.esiea.pootp.Attack;

import com.esiea.pootp.Battle.*;
import com.esiea.pootp.Monster.*;
import java.util.HashMap;

/**
 * Représente une attaque classique portée par un monstre (avec puissance, PP,
 * taux d'échec et type). Gère l'application des statuts, du terrain et des dégâts.
 */
public class AttackMonster extends Attack {
    private int power;
    private int nbUses;
    private int maxUses;
    private double failureRate;
    private AttackType type;
    /**
     * Construit une attaque de monstre.
     * @param name nom de l'attaque
     * @param power puissance de l'attaque
     * @param maxUses nombre maximum d'utilisations (PP)
     * @param failureRate probabilité d'échec (0.0 à 1.0)
     * @param type type de l'attaque (FEU, EAU, etc.)
     */
    public AttackMonster(String name, int power, int maxUses, double failureRate, AttackType type) {
        super(name);
        this.power = power;
        this.maxUses = maxUses;
        this.nbUses = maxUses;
        this.failureRate = failureRate;
        this.type = type;
    }

    @Override
    public HashMap<String, String> performAttack(Monster attacker, Monster defender, Battle battle) {
        HashMap<String, String> result = new HashMap<>();
        // Vérifier les PP restants
        if (nbUses <= 0) {
            return result;
        }
        nbUses--;
        // Vérifier l'échec éventuel
        if (Math.random() < failureRate) {
            result.put("attackName", name);
            result.put("attackerName", attacker.getName());
            result.put("defenderName", defender.getName());
            result.put("damage", "0");
            result.put("effectiveness", "l'attaque a échoué !");
            return result;
        }
        // Appliquer les statuts et/ou le terrain si nécessaire
        if (this.type != AttackType.NORMAL) {
            boolean statusApplied = attacker.applyStatus(defender);
            if (statusApplied && this.type != AttackType.EARTH) {
                result.put("status", defender.getName() + " est maintenant " + defender.getStatus().getName() + " !");
            }
            else if (statusApplied && this.type == AttackType.EARTH) {
                result.put("status", attacker.getName() + " est maintenant " + attacker.getStatus().getName() + " !");
            }

            boolean groundApplied = attacker.applyGround(battle);
            if (groundApplied) {
                result.put("ground", "Le terrain est maintenant " + battle.getGround().getName() + " !");
            }
        }
        // Calcul des dégâts
        double coef = 0.85 + Math.random() * 0.15;
        double typeEffectiveness = getTypeEffectiveness(this.type, defender);
        double base = ((11.0 * this.power * attacker.getPower()) / (25.0 * defender.getDefense())) + 2.0;
        int damage = (int) Math.round(base * coef * typeEffectiveness);
        // Appliquer les dégâts
        defender.setCurrentHealth(defender.getCurrentHealth() - damage);
        if (defender.getCurrentHealth() < 0) {
            defender.setCurrentHealth(0);
        }
        // Préparer le résultat
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



    /**
     * Calcule l'efficacité de l'attaque en fonction du type du défenseur.
     * @param attackType
     * @param defender
     * @return
     */
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

    /**
     * @return puissance de l'attaque
     */
    public int getPower() {
        return power;
    }

    /**
     * @return PP restants
     */
    public int getNbUses() {
        return nbUses;
    }

    /**
     * @return PP maximum
     */
    public int getMaxUses() {
        return maxUses;
    }

    /**
     * @return probabilité d'échec (0.0 à 1.0)
     */
    public double getFailureRate() {
        return failureRate;
    }

    /**
     * @return type de l'attaque
     */
    public AttackType getType() {
        return type;
    }
}

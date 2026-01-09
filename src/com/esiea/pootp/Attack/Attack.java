package com.esiea.pootp.Attack;

import java.util.HashMap;

import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Battle.Battle;

/**
 * Attaque abstraite pouvant être exécutée par un monstre.
 * Les sous-classes définissent le calcul des dégâts et des effets.
 */
public abstract class Attack {
    protected String name;

    /**
     * @param name nom de l'attaque
     */
    public Attack(String name) {
        this.name = name;
    }

    /**
     * @return nom de l'attaque
     */
    public String getName() {
        return name;
    }

    /**
     * Exécute l'attaque et retourne un résultat clé/valeur décrivant les effets.
     * Les clés possibles incluent: damage, effectiveness, status, ground, attackerName, defenderName, attackName
     * @param attacker monstre attaquant
     * @param defender monstre défenseur
     * @param battle   contexte de bataille
     * @return un HashMap décrivant le résultat
     */
    public HashMap<String, String> performAttack(Monster attacker, Monster defender, Battle battle) {
        return new HashMap<>();
    }
}

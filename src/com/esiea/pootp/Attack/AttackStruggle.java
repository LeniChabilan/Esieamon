package com.esiea.pootp.Attack;

import java.util.HashMap;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Battle.Battle;

/**
 * Attaque de base "Lutte" disponible pour tous les monstres.
 * Cette attaque inflige des dégâts faibles mais constants, sans effet secondaire.
 * Les dégâts sont calculés selon la formule :
 * damage = 20.0 * (puissance / défense) * coefficient_aléatoire
 * où le coefficient aléatoire est entre 0,85 et 1,00.
 */
public class AttackStruggle extends Attack {
    /**
     * Construit une attaque "Lutte" avec le nom officiel en français.
     */
    public AttackStruggle() {
        super("Lutte");
    }

    /**
     * Exécute l'attaque "Lutte" sur le monstre défenseur.
     * Applique les dégâts sans effet secondaire et retourne un résumé avec:
     * @param attacker monstre qui attaque
     * @param defender monstre qui se défend
     * @param battle contexte de la bataille (non utilisé ici)
     * @return résultat de l'attaque avec clés et valeurs en chaîne
     */
    @Override
    public HashMap<String, String> performAttack(Monster attacker, Monster defender, Battle battle) {
        HashMap<String, String> result = new HashMap<>();
        double coefficient = 0.85 + Math.random() * 0.15;
        int damage = (int)(20.0 * attacker.getPower() / defender.getDefense() * coefficient);
        defender.currentHealth -= damage;
        if (defender.currentHealth < 0) {
            defender.currentHealth = 0;
        }
        result.put("attackName", this.name);
        result.put("attackerName", attacker.getName());
        result.put("defenderName", defender.getName());
        result.put("damage", Integer.toString(damage));
        result.put("effectiveness", "");
        return result;
    }
    
}

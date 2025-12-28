package com.esiea.pootp.Attack;

import java.util.HashMap;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Battle.Battle;

public class AttackStruggle extends Attack {
    public AttackStruggle() {
        super("Lutte");
    }

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

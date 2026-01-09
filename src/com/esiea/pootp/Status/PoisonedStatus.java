package com.esiea.pootp.Status;

import java.util.HashMap;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Ground.FloodedGround;
import com.esiea.pootp.Ground.Ground;

/**
 * Statut Empoisonné: inflige des dégâts chaque tour. Se guérit automatiquement
 * si le terrain est Inondé.
 */
public class PoisonedStatus extends Status {
    public PoisonedStatus() {
        super("Empoisonné");
    }

    @Override
    /**
     * Dégâts en fonction de la puissance du monstre. Guéri sur terrain inondé.
     */
    public HashMap<String, String> performStatus(Monster monster, Ground ground) {
        HashMap<String, String> result = new HashMap<>();
        result.put("attackAble", "true");
        if (ground instanceof FloodedGround) {
            result.put("statusCured", "true");
            monster.setStatus(new NormalStatus());
            return result;
        }

        int poisonDamage = monster.getPower() / 10;
        monster.setCurrentHealth(monster.getCurrentHealth() - poisonDamage);
        result.put("statusEffect",  monster.getName() + " subit " + poisonDamage + " points de dégâts d'empoisonnement.");
        return result;
    }
}

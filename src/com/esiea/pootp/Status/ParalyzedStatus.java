package com.esiea.pootp.Status;

import java.util.HashMap;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Ground.Ground;

/**
 * Statut Paralysé: chance d'être incapable d'attaquer (25%).
 * La probabilité de guérison augmente avec le nombre de tours.
 */
public class ParalyzedStatus extends Status {
    int nbTurnsParalyzed = 0;

    public ParalyzedStatus() {
        super("Paralysé");
    }

    @Override
    /**
     * Augmente un compteur de tours, puis tire la guérison et la possibilité d'attaque.
     */
    public HashMap<String, String> performStatus(Monster monster, Ground ground) {
        HashMap<String, String> result = new HashMap<>();

        nbTurnsParalyzed++;
        if (Math.random()  < nbTurnsParalyzed / 6.0) {
            result.put("attackAble", "true");
            result.put("statusCured", "true");
            monster.setStatus(new NormalStatus());
            return result;
        }

        if (Math.random() < 0.25) {
            result.put("attackAble", "false");
        } else {
            result.put("attackAble", "true");
        }
        
        return result;
    }
}

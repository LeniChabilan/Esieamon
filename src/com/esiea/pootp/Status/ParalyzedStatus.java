package com.esiea.pootp.Status;

import java.util.HashMap;
import com.esiea.pootp.Monster.Monster;

public class ParalyzedStatus extends Status {
    int nbTurnsParalyzed = 0;

    public ParalyzedStatus() {
        super("Paralysé");
    }

    @Override
    public HashMap<String, String> performStatus(Monster monster) {
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

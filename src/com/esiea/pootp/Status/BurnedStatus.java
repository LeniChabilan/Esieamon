package com.esiea.pootp.Status;

import java.util.HashMap;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Ground.Ground;
import com.esiea.pootp.Ground.FloodedGround;

public class BurnedStatus extends Status {
    public BurnedStatus() {
        super("Brûlé");
    }

    @Override
    public HashMap<String, String> performStatus(Monster monster, Ground ground) {
        HashMap<String, String> result = new HashMap<>();
        result.put("attackAble", "true");
        if (ground instanceof FloodedGround) {
            result.put("statusCured", "true");
            monster.setStatus(new NormalStatus());
            return result;
        }

        int burnDamage = monster.getPower() / 10;
        monster.setCurrentHealth(monster.getCurrentHealth() - burnDamage);
        result.put("statusEffect",  monster.getName() + " subit " + burnDamage + " points de dégâts de brûlure.");
        return result;
    }
    
}

package com.esiea.pootp.Monster;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Ground.FloodedGround;
import com.esiea.pootp.Status.NormalStatus;

public class GrassMonster extends NatureMonster {
    private double healChance;

    public GrassMonster(String name, int health, int power, int defense, int speed, double healChance) {
        super(name, health, power, defense, speed);
        this.healChance = healChance;
    }

    public double getHealChance() {
        return healChance;
    }

    @Override
    public String applyPassiveEffect(Battle battle) {
        String passiveEffectMessage = super.applyPassiveEffect(battle);

        double randomValue = Math.random();
        if (randomValue < healChance) {
            if (this.getStatus().getName() != "Normal") {
                this.setStatus(new NormalStatus());
                return passiveEffectMessage + "\n" + this.getName() + " est guéri de son statut grâce au terrain inondé !";
            }
        }
        return passiveEffectMessage;
    }
    
}

package com.esiea.pootp.Monster;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Ground.FloodedGround;

public abstract class NatureMonster extends Monster{ 
    public NatureMonster(String name, int health, int power, int defense, int speed) {
        super(name, health, power, defense, speed);
    }

    @Override
    public String applyPassiveEffect(Battle battle) {
        if (battle.getGround() instanceof FloodedGround && this.getCurrentHealth() < this.getHealth()) {
            int healAmount = (int) (this.health * 0.05);
            this.heal(healAmount);
            return this.getName() + " récupère " + healAmount + " points de vie grâce au terrain inondé !";
        }
        return "";
    }
}

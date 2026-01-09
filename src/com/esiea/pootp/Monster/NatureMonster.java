package com.esiea.pootp.Monster;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Ground.FloodedGround;

/**
 * Classe de base pour les monstres de type Nature, bénéficiant d'effets liés
 * aux terrains naturels (ex: terrain inondé).
 */
public abstract class NatureMonster extends Monster{ 
    public NatureMonster(String name, int health, int power, int defense, int speed) {
        super(name, health, power, defense, speed);
    }

    @Override
    /**
     * Effet passif: sur terrain inondé, régénère 5% des PV max à chaque tour.
     */
    public String applyPassiveEffect(Battle battle) {
        if (battle.getGround() instanceof FloodedGround && this.getCurrentHealth() < this.getHealth()) {
            int healAmount = (int) (this.health * 0.05);
            this.heal(healAmount);
            return this.getName() + " récupère " + healAmount + " points de vie grâce au terrain inondé !";
        }
        return "";
    }
}

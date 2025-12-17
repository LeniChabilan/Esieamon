package com.esiea.pootp;

import com.esiea.pootp.Attack.Attack;
import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Monster.*;
import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Attack.AttackType;

public class EsieamonExecutable {
    
    public static void main(String[] args) {
        Player player1 = new Player("Ash");
        Player player2 = new Player("Misty");

        FireMonster flareon = new FireMonster("Flareon", 110, 60, 35, 70, 0.18);
        Attack ember = new Attack("Ember", 40, 25, 0.1, AttackType.FIRE);
        Attack flameThrower = new Attack("Flamethrower", 90, 15, 0.05, AttackType.FIRE);
        flareon.attacks.add(ember);
        flareon.attacks.add(flameThrower);
        player1.monsters.add(flareon);

        
        WaterMonster squirtle = new WaterMonster("Squirtle", 100, 50, 40, 60, 0.2, 0.1);
        Attack waterGun = new Attack("Water Gun", 40, 25, 0.1, AttackType.WATER);
        Attack hydroPump = new Attack("Hydro Pump", 90, 15, 0.05, AttackType.WATER);
        squirtle.attacks.add(waterGun);
        squirtle.attacks.add(hydroPump);
        player2.monsters.add(squirtle);

        Battle battle = new Battle(player1, player2);

        System.out.println("Début du combat entre " + battle.player1.name + " et " + battle.player2.name);

        battle.startBattle();
    }

}

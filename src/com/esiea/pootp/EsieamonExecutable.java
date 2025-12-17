package com.esiea.pootp;

import com.esiea.pootp.Attack.Attack;
import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Monster.FireMonster;
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

        FireMonster torchic = new FireMonster("Torchic", 95, 48, 28, 68, 0.17);
        Attack fireSpin = new Attack("Fire Spin", 35, 15, 0.1, AttackType.FIRE);
        Attack heatWave = new Attack("Heat Wave", 95, 10, 0.05, AttackType.FIRE);
        torchic.attacks.add(fireSpin);
        torchic.attacks.add(heatWave);
        player2.monsters.add(torchic);

        Battle battle = new Battle(player1, player2);

        System.out.println("Battle started between " + battle.player1.name + " and " + battle.player2.name);

        battle.startBattle();
    }

}

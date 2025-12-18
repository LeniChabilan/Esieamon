package com.esiea.pootp;

import java.util.Scanner;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Player.Player;


public class EsieamonExecutable {
    
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Nom Joueur 1 ");
        String player1Name = scanner.nextLine();

        Player player1 = new Player(player1Name);

        System.out.println("Nom Joueur 2 ");
        String player2Name = scanner.nextLine();
        Player player2 = new Player(player2Name);

        Battle battle = new Battle(player1, player2);

        battle.startBattle();
    }
}
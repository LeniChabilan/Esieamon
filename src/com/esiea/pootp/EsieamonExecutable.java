package com.esiea.pootp;

import com.esiea.pootp.Battle.BattleTerminal;
import com.esiea.pootp.Battle.BattleGUI;

import java.util.Scanner;

public class EsieamonExecutable {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Esieamon ===");
        System.out.println("Choisissez le mode de jeu:");
        System.out.println("1. Mode Terminal");
        System.out.println("2. Mode Graphique");
        System.out.print("Votre choix (1-2): ");
        
        int choice = -1;
        while (choice < 1 || choice > 2) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 2) {
                    System.out.println("Choix invalide. Veuillez entrer 1 ou 2.");
                    System.out.print("Votre choix (1-2): ");
                }
            } else {
                System.out.println("Veuillez entrer un nombre valide.");
                System.out.print("Votre choix (1-2): ");
                scanner.next();
            }
        }
        
        if (choice == 1) {
            // Mode Terminal
            BattleTerminal battle = new BattleTerminal();
            battle.startBattle();
        } else {
            // Mode Graphique - JavaFX
            BattleGUI battle = new BattleGUI();
            battle.launch(args);
        }
    }
}
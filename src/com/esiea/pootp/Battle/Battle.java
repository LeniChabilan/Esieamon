package com.esiea.pootp.Battle;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Attack.Attack;
import com.esiea.pootp.Monster.Monster;

import java.util.List;
import java.util.Scanner;
import com.esiea.pootp.Parser.Parser;

public class Battle {
    public Player player1;
    public Player player2;
    private Parser parser;

    public Battle(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.parser = new Parser();
        try {
            parser.parseFile("./src/com/esiea/pootp/Parser/game_data.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOver() {
        return !player1.hasUsableMonsters() || !player2.hasUsableMonsters();
    }

    public void displayWinner() {
        if (player1.hasUsableMonsters()) {
            System.out.println("Le joueur " + player1.name + " a gagné la bataille !");
        } else if (player2.hasUsableMonsters()) {
            System.out.println("Le joueur " + player2.name + " a gagné la bataille !");
        } else {
            System.out.println("La bataille s'est terminée par un match nul !");
        }
    }

    public void displayCurrentStatus() {
        System.out.println("\nStatut actuel des monstres:");
        System.out.println(player1.name + "'s " + player1.getCurrentMonster().getName() + ": " + player1.getCurrentMonster().getCurrentHealth() + " HP");
        System.out.println(player2.name + "'s " + player2.getCurrentMonster().getName() + ": " + player2.getCurrentMonster().getCurrentHealth() + " HP");
    }


    private static void selectMonstersForPlayer(Player player, Parser parser) {

        Scanner scanner = new Scanner(System.in);

        List<Monster> availableMonsters = parser.getAvailableMonsters();
        
        System.out.println("\n--- Sélection pour " + player.name + " ---");
        System.out.println("Monstres disponibles :");
        
        for (int i = 0; i < availableMonsters.size(); i++) {
            Monster monster = availableMonsters.get(i);
            System.out.println((i + 1) + ". " + monster.getName() + 
                             " - HP:" + monster.getHealth() + 
                             " ATK:" + monster.getPower() + 
                             " DEF:" + monster.getDefense() + 
                             " SPD:" + monster.getSpeed() +
                             " (" + monster.attacks.size() + " attaques)");
        }
        
        System.out.print("\nCombien de monstres voulez-vous (1-" + availableMonsters.size() + ") ? ");
        int nbMonsters = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne
        
        for (int i = 0; i < nbMonsters; i++) {
            System.out.print("Choisissez le monstre " + (i + 1) + " (1-" + availableMonsters.size() + ") : ");
            int choice = scanner.nextInt() - 1;
            scanner.nextLine();
            
            if (choice >= 0 && choice < availableMonsters.size()) {
                Monster selectedMonster = availableMonsters.get(choice);
                Monster monsterCopy = parser.getMonsterCopy(selectedMonster.getName());
                
                if (monsterCopy != null) {
                    player.monsters.add(monsterCopy);
                    System.out.println("✓ " + monsterCopy.getName() + " ajouté avec " + 
                                     monsterCopy.attacks.size() + " attaques !");
                }
            }
        }
    }

    public void startBattle() {

        selectMonstersForPlayer(player1, parser);
        selectMonstersForPlayer(player2, parser);


        while (!isOver()) {
            Attack attack1 = chooseAttack(player1);
            Attack attack2 = chooseAttack(player2);

            if (player1.getCurrentMonster().getSpeed() >= player2.getCurrentMonster().getSpeed()) {
                attack1.performAttack(player1.getCurrentMonster(), player2.getCurrentMonster());
                if (player2.getCurrentMonster().getCurrentHealth() > 0) {
                    attack2.performAttack(player2.getCurrentMonster(), player1.getCurrentMonster());
                }
            } else {
                attack2.performAttack(player2.getCurrentMonster(), player1.getCurrentMonster());
                if (player1.getCurrentMonster().getCurrentHealth() > 0) {
                    attack1.performAttack(player1.getCurrentMonster(), player2.getCurrentMonster());
                }
            }
            displayCurrentStatus();
        }
        displayWinner();
    }

    public Attack chooseAttack(Player player) {
        Scanner scanner = new Scanner(System.in);
        
        // Get current monster and its attacks
        var currentMonster = player.getCurrentMonster();
        var attacks = currentMonster.attacks;
        
        // Display the list of attacks
        System.out.println("\nChoisissez une attaque pour " + currentMonster.getName() + ":");
        for (int i = 0; i < attacks.size(); i++) {
            Attack attack = attacks.get(i);
            System.out.println((i + 1) + ". " + attack.getName() + 
                              " (Puissance: " + attack.getPower() + 
                              ", PP: " + attack.getNbUses() + "/" + attack.getMaxUses() + ")");
        }
        
        // Get user choice
        int choice = -1;
        while (choice < 1 || choice > attacks.size()) {
            System.out.print("Votre choix (1-" + attacks.size() + "): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > attacks.size()) {
                    System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et " + attacks.size());
                }
            } else {
                System.out.println("Veuillez entrer un nombre valide.");
                scanner.next(); // Clear invalid input
            }
        }
        
        return attacks.get(choice - 1);
    }
}

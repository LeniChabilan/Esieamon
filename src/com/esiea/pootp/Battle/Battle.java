package com.esiea.pootp.Battle;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Attack.Attack;
import com.esiea.pootp.Monster.Monster;

import java.util.List;
import java.util.HashMap;
import java.util.Scanner;
import com.esiea.pootp.Parser.Parser;

public class Battle {
    public Player player1;
    public Player player2;
    private Parser parser;
    private int teamSize = 3;

    private static final String COLOR_BLUE = "\u001B[94m";
    private static final String COLOR_ORANGE = "\u001B[33m";
    private static final String COLOR_RESET = "\u001B[0m";

    public Battle() {
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
        System.out.println(COLOR_BLUE + player1.name + "'s " + player1.getCurrentMonster().getName() + ": " + player1.getCurrentMonster().getCurrentHealth() + " HP" + COLOR_RESET);
        System.out.println(COLOR_ORANGE + player2.name + "'s " + player2.getCurrentMonster().getName() + ": " + player2.getCurrentMonster().getCurrentHealth() + " HP" + COLOR_RESET);
    }


    private void selectMonstersForPlayer(Player player, Parser parser, String color) {
        Scanner scanner = new Scanner(System.in);
        List<Monster> availableMonsters = parser.getAvailableMonsters();
        

        System.out.println("\n" + color + "--- Sélection pour " + player.name + " ---" + COLOR_RESET);
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
        for (int i = 0; i < teamSize; i++) {
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
        // Player setup
        Scanner scanner = new Scanner(System.in);
        System.out.print(COLOR_BLUE + "Nom du Joueur 1: " + COLOR_RESET);
        String player1Name = scanner.nextLine();
        this.player1 = new Player(player1Name);

        System.out.print(COLOR_ORANGE + "Nom du Joueur 2: " + COLOR_RESET);
        String player2Name = scanner.nextLine();
        this.player2 = new Player(player2Name);

        //team size
        System.out.print("Taille de l'équipe (nombre de monstres par joueur): ");
        this.teamSize = scanner.nextInt();
        scanner.nextLine(); 

        // Monster selection
        selectMonstersForPlayer(player1, parser, COLOR_BLUE);
        selectMonstersForPlayer(player2, parser, COLOR_ORANGE);

        // Battle loop
        while (!isOver()) {
            Attack attack1 = chooseAttack(player1, COLOR_BLUE);
            Attack attack2 = chooseAttack(player2, COLOR_ORANGE);

            if (player1.getCurrentMonster().getSpeed() >= player2.getCurrentMonster().getSpeed()) {
                displayAttackAction(attack1.performAttack(player1.getCurrentMonster(), player2.getCurrentMonster())); 
                if (player2.getCurrentMonster().getCurrentHealth() > 0) {
                    displayAttackAction(attack2.performAttack(player2.getCurrentMonster(), player1.getCurrentMonster()));
                }
            } else {
                displayAttackAction(attack2.performAttack(player2.getCurrentMonster(), player1.getCurrentMonster()));
                if (player1.getCurrentMonster().getCurrentHealth() > 0) {
                    displayAttackAction(attack1.performAttack(player1.getCurrentMonster(), player2.getCurrentMonster()));
                }
            }
            displayCurrentStatus();
        }
        displayWinner();
    }

    private void displayAttackAction(HashMap<String, String> attackResult) {
        String attackerName = attackResult.get("attackerName");
        String defenderName = attackResult.get("defenderName");
        String attackName = attackResult.get("attackName");
        String damage = attackResult.get("damage");
        String effectiveness = attackResult.get("effectiveness");

        System.out.println("\u001B[31m\n" + attackerName + " utilise " + attackName + " sur " + defenderName + " !");
        System.out.println("Cela inflige " + damage + " points de dégâts. " + effectiveness + "\u001B[0m");
    }

    public Attack chooseAttack(Player player, String color) {
        Scanner scanner = new Scanner(System.in);
        
        // Get current monster and its attacks
        var currentMonster = player.getCurrentMonster();
        var attacks = currentMonster.attacks;
        
        // Display the list of attacks
        System.out.println("\n" + color + player.getName() +", une attaque pour " + currentMonster.getName() + ":" + COLOR_RESET);
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

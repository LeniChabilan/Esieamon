package com.esiea.pootp.Battle;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Attack.Attack;
import com.esiea.pootp.Attack.AttackMonster;
import com.esiea.pootp.Attack.AttackStruggle;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Battle.ActionType;
import com.esiea.pootp.Object.ObjectMonster;
import com.esiea.pootp.Ground.*;

import java.util.List;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import com.esiea.pootp.Parser.Parser;

public class Battle {
    public Player player1;
    public Player player2;
    private Parser parser;
    private int teamSize = 3;
    private Ground ground = new NormalGround();

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

    public void setGround(Ground ground) {
        this.ground = ground;
    }

    public Ground getGround() {
        return this.ground;
    }

    public boolean isOver() {
        return !player1.hasUsableMonsters() || !player2.hasUsableMonsters();
    }

    public void displayWinner() {
        if (player1.hasUsableMonsters()) {
            System.out.println("\nLe joueur " + player1.name + " a gagné la bataille !");
        } else if (player2.hasUsableMonsters()) {
            System.out.println("\nLe joueur " + player2.name + " a gagné la bataille !");
        } else {
            System.out.println("\nLa bataille s'est terminée par un match nul !");
        }
    }

    public void displayCurrentStatus() {
        System.out.println("\nStatut actuel des monstres: (Terrain: " + ground.getName() + ")");
        String player1Status = player1.getCurrentMonster().getStatus().getName();
        String player1Line = COLOR_BLUE + player1.name + "'s " + player1.getCurrentMonster().getName() + ": " + player1.getCurrentMonster().getCurrentHealth() + " HP";
        if (!player1Status.equals("Normal")) {
            player1Line += " (" + player1Status + ")";
        }
        System.out.println(player1Line + COLOR_RESET);
        
        String player2Status = player2.getCurrentMonster().getStatus().getName();
        String player2Line = COLOR_ORANGE + player2.name + "'s " + player2.getCurrentMonster().getName() + ": " + player2.getCurrentMonster().getCurrentHealth() + " HP";
        if (!player2Status.equals("Normal")) {
            player2Line += " (" + player2Status + ")";
        }
        System.out.println(player2Line + COLOR_RESET);
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
        int nbMonstersChoosen = 0;
        while (nbMonstersChoosen < teamSize) {
            System.out.print("Choisissez le monstre " + (nbMonstersChoosen + 1) + " (1-" + availableMonsters.size() + ") : ");
            int choice = scanner.nextInt() - 1;
            scanner.nextLine();
            
            if (choice >= 0 && choice < availableMonsters.size()) {
                Monster selectedMonster = availableMonsters.get(choice);
                Monster monsterCopy = parser.getMonsterCopy(selectedMonster.getName());
                
                if (monsterCopy != null) {
                    player.monsters.add(monsterCopy);
                    System.out.println("✓ " + monsterCopy.getName() + " ajouté avec " + 
                                     monsterCopy.attacks.size() + " attaques !");
                    nbMonstersChoosen++;
                }
            }

            else {
                System.out.println("Choix invalide. Veuillez réessayer.");
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
            // Players 1 choose actions
            ActionType action1 = chooseAction(player1, COLOR_BLUE);
            Attack attack1 = null;
            Integer switchIndex1 = null;
            Integer itemIndex1 = null;
            switch (action1) {
                case ATTACK:
                    attack1 = chooseAttack(player1, COLOR_BLUE);
                    break;
                case SWITCH:
                    switchIndex1 = chooseMonster(player1, COLOR_BLUE);
                    break;
                case ITEM:
                    itemIndex1 = chooseItem(player1, COLOR_BLUE);
                    break;
            }

            // Player 2 choose actions
            ActionType action2 = chooseAction(player2, COLOR_ORANGE);
            Attack attack2 = null;
            Integer switchIndex2 = null;
            Integer itemIndex2 = null;
            switch (action2) {
                case ATTACK:
                    attack2 = chooseAttack(player2, COLOR_ORANGE);
                    break;
                case SWITCH:
                    switchIndex2 = chooseMonster(player2, COLOR_ORANGE);
                    break;
                case ITEM:
                    itemIndex2 = chooseItem(player2, COLOR_ORANGE);
                    break;
            }

            // Perform switch actions
            if (action1 == ActionType.SWITCH) {
                switchMonster(player1, switchIndex1, COLOR_BLUE);
            }
            if (action2 == ActionType.SWITCH) {
                switchMonster(player2, switchIndex2, COLOR_ORANGE);
            }

            // Perform Item actions
            if (action1 == ActionType.ITEM) {
                useItem(player1, itemIndex1, COLOR_BLUE);
            }
            if (action2 == ActionType.ITEM) {
                useItem(player2, itemIndex2, COLOR_ORANGE);
            }

            // Perform Status effects before attacks
            HashMap<String, String> statusEffect1 = player1.getCurrentMonster().getStatus().performStatus(player1.getCurrentMonster());
            HashMap<String, String> statusEffect2 = player2.getCurrentMonster().getStatus().performStatus(player2.getCurrentMonster());

            if (statusEffect1.containsKey("statusCured") && Boolean.parseBoolean(statusEffect1.get("statusCured"))) {
                System.out.println(COLOR_BLUE + player1.getCurrentMonster().getName() + " n'est plus " + player1.getCurrentMonster().getStatus().getName() + " !" + COLOR_RESET);
            }
            if (statusEffect2.containsKey("statusCured") && Boolean.parseBoolean(statusEffect2.get("statusCured"))) {
                System.out.println(COLOR_ORANGE + player2.getCurrentMonster().getName() + " n'est plus " + player2.getCurrentMonster().getStatus().getName() + " !" + COLOR_RESET);
            }

            boolean canAttack1 = !statusEffect1.containsKey("attackAble") || Boolean.parseBoolean(statusEffect1.get("attackAble"));
            boolean canAttack2 = !statusEffect2.containsKey("attackAble") || Boolean.parseBoolean(statusEffect2.get("attackAble"));
            if (attack1 != null && !canAttack1) {
                System.out.println(COLOR_BLUE + player1.getCurrentMonster().getName() + " est " + player1.getCurrentMonster().getStatus().getName() + " et ne peut pas attaquer !" + COLOR_RESET);
                attack1 = null;
            }
            if (attack2 != null && !canAttack2) {
                System.out.println(COLOR_ORANGE + player2.getCurrentMonster().getName() + " est " + player2.getCurrentMonster().getStatus().getName() + " et ne peut pas attaquer !" + COLOR_RESET);
                attack2 = null;
            }

            // Perform attack actions
            if (attack1 != null && attack2 != null) {
                // Both players chose to attack
                if (player1.getCurrentMonster().getSpeed() >= player2.getCurrentMonster().getSpeed()) {
                    displayAttackAction(attack1.performAttack(player1.getCurrentMonster(), player2.getCurrentMonster(), this)); 
                    if (player2.getCurrentMonster().getCurrentHealth() > 0) {
                        displayAttackAction(attack2.performAttack(player2.getCurrentMonster(), player1.getCurrentMonster(), this));
                    }
                    else {
                        displayMonsterKO(player2.getCurrentMonster(), COLOR_ORANGE);
                    }
                } else {
                    displayAttackAction(attack2.performAttack(player2.getCurrentMonster(), player1.getCurrentMonster(), this));
                    if (player1.getCurrentMonster().getCurrentHealth() > 0) {
                        displayAttackAction(attack1.performAttack(player1.getCurrentMonster(), player2.getCurrentMonster(), this));
                    }
                    else {
                        displayMonsterKO(player1.getCurrentMonster(), COLOR_BLUE);
                    }
                }
            } else if (attack1 != null) {
                // Only player 1 attacks
                displayAttackAction(attack1.performAttack(player1.getCurrentMonster(), player2.getCurrentMonster(), this));
                if (player2.getCurrentMonster().getCurrentHealth() <= 0) {
                    displayMonsterKO(player2.getCurrentMonster(), COLOR_ORANGE);
                }
            } else if (attack2 != null) {
                // Only player 2 attacks
                displayAttackAction(attack2.performAttack(player2.getCurrentMonster(), player1.getCurrentMonster(), this));
                if (player1.getCurrentMonster().getCurrentHealth() <= 0) {
                    displayMonsterKO(player1.getCurrentMonster(), COLOR_BLUE);
                }
            }
            displayCurrentStatus();

            // Check for KO and switch monsters if needed
            if (player1.getCurrentMonster().getCurrentHealth() <= 0 && player1.hasUsableMonsters()) {
                switchMonster(player1, chooseMonster(player1, COLOR_BLUE), COLOR_BLUE);
            }
            if (player2.getCurrentMonster().getCurrentHealth() <= 0 && player2.hasUsableMonsters()) {
                switchMonster(player2, chooseMonster(player2, COLOR_ORANGE), COLOR_ORANGE);
            }
        }
        displayWinner();
    }

    private ActionType chooseAction(Player player, String color) {
        Scanner scanner = new Scanner(System.in);
        boolean canSwitch = player.getAvailableMonstersMap().size() > 1;
        boolean hasItems = !player.getInventory().isEmpty();

        System.out.println("\n" + color + player.getName() + ", choisissez une action:" + COLOR_RESET);
        System.out.println("1. Attaquer");
        if (canSwitch) {
            System.out.println("2. Changer de monstre");
        } else {
            System.out.println("2. Changer de monstre (indisponible - un seul monstre en vie)");
        }
        if (hasItems) {
            System.out.println("3. Utiliser un objet");
        } else {
            System.out.println("3. Utiliser un objet (indisponible - aucun objet)");
        }

        int choice = -1;
        while (choice < 1 || choice > 3 || (choice == 2 && !canSwitch) || (choice == 3 && !hasItems)) {
            System.out.print("Votre choix (1-3): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et 3");
                } else if (choice == 2 && !canSwitch) {
                    System.out.println("Vous ne pouvez pas changer de monstre : un seul monstre encore en vie.");
                    choice = -1;
                } else if (choice == 3 && !hasItems) {
                    System.out.println("Vous n'avez aucun objet !");
                    choice = -1;
                }
            } else {
                System.out.println("Veuillez entrer un nombre valide.");
                scanner.next(); 
            }
        }

        switch (choice) {
            case 1:
                return ActionType.ATTACK;
            case 2:
                return ActionType.SWITCH;
            case 3:
                return ActionType.ITEM;
            default:
                return ActionType.ATTACK; 
        }
    }

    private void switchMonster(Player player, int monsterIndex, String color) {
        player.currentMonsterIndex = monsterIndex;
        System.out.println(color + "\n" + player.getName() + " a changé de monstre pour " + player.getCurrentMonster().getName() + " !" + COLOR_RESET);
    }

    private int chooseMonster(Player player, String color) {
        Scanner scanner = new Scanner(System.in);

        HashMap<Integer, Integer> indexMap = new HashMap<>();
        System.out.println("\n" + color + player.getName() + ", choisissez un monstre à envoyer au combat:" + COLOR_RESET);
        int displayIndex = 1;
        for (int i = 0; i < player.monsters.size(); i++) {
            Monster monster = player.monsters.get(i);
            if (monster.currentHealth > 0 && i != player.currentMonsterIndex) {
                System.out.println(displayIndex + ". " + monster.getName() + " (HP: " + monster.currentHealth + ")");
                indexMap.put(displayIndex, i);
                displayIndex++;
            }
        }

        int choice = -1;
        while (choice < 1 || choice >= displayIndex) {
            System.out.print("Votre choix (1-" + (displayIndex - 1) + "): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice >= displayIndex) {
                    System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et " + (displayIndex - 1));
                }
            } else {
                System.out.println("Veuillez entrer un nombre valide.");
                scanner.next(); 
            }
        }

        System.out.println("L'index choisi est : " + indexMap.get(choice));
        return indexMap.get(choice);
    }

    private Integer chooseItem(Player player, String color) {
        List<ObjectMonster> inventory = player.getInventory();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n" + color + player.getName() + ", choisissez un objet à utiliser:" + COLOR_RESET);
        for (int i = 0; i < inventory.size(); i++) {
            ObjectMonster item = inventory.get(i);
            System.out.println((i + 1) + ". " + item.getName());
        }
        int choice = -1;
        while (choice < 1 || choice > inventory.size()) {
            System.out.print("Votre choix (1-" + inventory.size() + "): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > inventory.size()) {
                    System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et " + inventory.size());
                }
            } else {
                System.out.println("Veuillez entrer un nombre valide.");
                scanner.next(); 
            }
        }
        return choice - 1;
    }

    private void useItem(Player player, int itemIndex, String color) {
        ObjectMonster item = player.getInventory().get(itemIndex);
        System.out.println(color + "\n" + player.getName() + " utilise " + item.getName() + " !" + COLOR_RESET);
        item.use(player.getCurrentMonster());
        player.getInventory().remove(itemIndex);
    }

    public void displayMonsterKO(Monster monster, String color) {
        System.out.println(color + "\n" + monster.getName() + " est K.O. !" + COLOR_RESET);
    }

    private void displayAttackAction(HashMap<String, String> attackResult) {
        String attackerName = attackResult.get("attackerName");
        String defenderName = attackResult.get("defenderName");
        String attackName = attackResult.get("attackName");
        String damage = attackResult.get("damage");
        String effectiveness = attackResult.get("effectiveness");
        String status = attackResult.get("status");
        String ground = attackResult.get("ground");

        if (attackResult.size() == 0) {
            return;
        }

        System.out.println("\u001B[31m\n" + attackerName + " utilise " + attackName + " sur " + defenderName + " !");
        System.out.println("Cela inflige " + damage + " points de dégâts. " + effectiveness + "\u001B[0m");
        if (status != null) {
            System.out.println("\u001B[35m" + status + "\u001B[0m");
        }
        if (ground != null) {
            System.out.println("\u001B[36m" + ground + "\u001B[0m");
        }
    }

    public Attack chooseAttack(Player player, String color) {
        Scanner scanner = new Scanner(System.in);
        
        // Get current monster and its attacks
        var currentMonster = player.getCurrentMonster();
        var attacks = currentMonster.attacks;

        if (currentMonster.hasAvailableAttacks() == false) {
            System.out.println(color + "\n" + player.getName() + ", votre monstre " + currentMonster.getName() + " n'a plus d'attaques disponibles !" + COLOR_RESET);
            System.out.println("Vous allez utiliser lutte !");
            return new AttackStruggle();
        }

        else {
            // Filter available attacks
            List<AttackMonster> availableAttacks = new ArrayList<>();
            for (AttackMonster attack : attacks) {
                if (attack.getNbUses() > 0) {
                    availableAttacks.add(attack);
                }
            }

            // Display the list of attacks
            System.out.println("\n" + color + player.getName() +", une attaque pour " + currentMonster.getName() + ":" + COLOR_RESET);
            int displayIndex = 1;
            for (int i = 0; i < attacks.size(); i++) {
                AttackMonster attack = attacks.get(i);
                if (attack.getNbUses() > 0) {
                    System.out.println(displayIndex + ". " + attack.getName() + 
                                    " (Puissance: " + attack.getPower() + 
                                    ", PP: " + attack.getNbUses() + "/" + attack.getMaxUses() + ")");
                    displayIndex++;
                } else {
                    System.out.println("   " + attack.getName() + " (indisponible - PP: 0/" + attack.getMaxUses() + ")");
                }
            }
            
            // Get user choice
            int choice = -1;
            while (choice < 1 || choice > availableAttacks.size()) {
                System.out.print("Votre choix (1-" + availableAttacks.size() + "): ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    if (choice < 1 || choice > availableAttacks.size()) {
                        System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et " + availableAttacks.size());
                    }
                } else {
                    System.out.println("Veuillez entrer un nombre valide.");
                    scanner.next();
                }
            }
            
            return availableAttacks.get(choice - 1);
        }
    }
}

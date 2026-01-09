package com.esiea.pootp.Battle;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Attack.Attack;
import com.esiea.pootp.Attack.AttackMonster;
import com.esiea.pootp.Attack.AttackStruggle;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Object.ObjectMonster;
import com.esiea.pootp.Parser.Parser;

import java.util.List;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Interface terminal pour jouer à Esieamon dans la console.
 * Gère la sélection des monstres, la boucle de combat et les actions des joueurs.
 */
public class BattleTerminal extends Battle {

    public BattleTerminal() {
        super();
    }

    private boolean isOver() {
        return !player1.hasUsableMonsters() || !player2.hasUsableMonsters();
    }


    private void switchMonster(Player player, int monsterIndex, String color) {
        player.setCurrentMonsterIndex(monsterIndex);
        System.out.println(color + "\n" + player.getName() + " a changé de monstre pour " + player.getCurrentMonster().getName() + " !" + COLOR_RESET);
    }

    private void displayWinner() {
        if (player1.hasUsableMonsters()) {
            System.out.println("\nLe joueur " + player1.getName() + " a gagné la bataille !");
        } else if (player2.hasUsableMonsters()) {
            System.out.println("\nLe joueur " + player2.getName() + " a gagné la bataille !");
        } else {
            System.out.println("\nLa bataille s'est terminée par un match nul !");
        }
    }

    private void displayCurrentStatus() {
        System.out.println("\nStatut actuel des monstres: (Terrain: " + ground.getName() + ")");
        String player1Status = player1.getCurrentMonster().getStatus().getName();
        String player1Line = COLOR_BLUE + player1.getName() + "'s " + player1.getCurrentMonster().getName() + ": " + player1.getCurrentMonster().getCurrentHealth() + " HP";
        if (!player1Status.equals("Normal")) {
            player1Line += " (" + player1Status + ")";
        }
        System.out.println(player1Line + COLOR_RESET);
        
        String player2Status = player2.getCurrentMonster().getStatus().getName();
        String player2Line = COLOR_ORANGE + player2.getName() + "'s " + player2.getCurrentMonster().getName() + ": " + player2.getCurrentMonster().getCurrentHealth() + " HP";
        if (!player2Status.equals("Normal")) {
            player2Line += " (" + player2Status + ")";
        }
        System.out.println(player2Line + COLOR_RESET);
    }

    private void displayMonsterSwitch(Player player, String color) {
        System.out.println(color + "\n" + player.getName() + " a changé de monstre pour " + player.getCurrentMonster().getName() + " !" + COLOR_RESET);
    }

    private void selectMonstersForPlayer(Player player, Parser parser, String color) {
        Scanner scanner = new Scanner(System.in);
        List<Monster> availableMonsters = parser.getAvailableMonsters();
        
        System.out.println("\n" + color + "--- Sélection pour " + player.getName() + " ---" + COLOR_RESET);
        System.out.println("Monstres disponibles :");
        
        for (int i = 0; i < availableMonsters.size(); i++) {
            Monster monster = availableMonsters.get(i);
            System.out.println((i + 1) + ". " + monster.getName() + 
                             " - HP:" + monster.getHealth() + 
                             " ATK:" + monster.getPower() + 
                             " DEF:" + monster.getDefense() + 
                             " SPD:" + monster.getSpeed() +
                             " (" + monster.getAttacks().size() + " attaques)");
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
                    player.getMonsters().add(monsterCopy);
                    System.out.println("✓ " + monsterCopy.getName() + " ajouté avec " + 
                                     monsterCopy.getAttacks().size() + " attaques !");
                    nbMonstersChoosen++;
                }
            }
            else {
                System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    public void startBattle() {
        // Initialisation des joueurs
        Scanner scanner = new Scanner(System.in);
        System.out.print(COLOR_BLUE + "Nom du Joueur 1: " + COLOR_RESET);
        String player1Name = scanner.nextLine();
        this.player1 = new Player(player1Name);

        System.out.print(COLOR_ORANGE + "Nom du Joueur 2: " + COLOR_RESET);
        String player2Name = scanner.nextLine();
        this.player2 = new Player(player2Name);

        // Taille de l'équipe
        System.out.print("Taille de l'équipe (nombre de monstres par joueur): ");
        this.teamSize = scanner.nextInt();
        scanner.nextLine(); 

        // Sélection des monstres
        selectMonstersForPlayer(player1, parser, COLOR_BLUE);
        selectMonstersForPlayer(player2, parser, COLOR_ORANGE);

        // Boucle de combat
        while (!isOver()) {
            // Le joueur 1 choisit son action
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

            // Le joueur 2 choisit son action
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

            // Appliquer les changements de monstres
            if (action1 == ActionType.SWITCH) {
                switchMonster(player1, switchIndex1, COLOR_BLUE);
            }
            if (action2 == ActionType.SWITCH) {
                switchMonster(player2, switchIndex2, COLOR_ORANGE);
            }

            // Appliquer l'utilisation d'objets
            if (action1 == ActionType.ITEM) {
                useItem(player1, itemIndex1, COLOR_BLUE);
            }
            if (action2 == ActionType.ITEM) {
                useItem(player2, itemIndex2, COLOR_ORANGE);
            }

            // Appliquer les effets de statut
            String status1Name = player1.getCurrentMonster().getStatus().getName();
            String status2Name = player2.getCurrentMonster().getStatus().getName();
            
            HashMap<String, String> statusEffect1 = player1.getCurrentMonster().getStatus().performStatus(player1.getCurrentMonster(), ground);
            HashMap<String, String> statusEffect2 = player2.getCurrentMonster().getStatus().performStatus(player2.getCurrentMonster(), ground);

            if (statusEffect1.containsKey("statusCured") && Boolean.parseBoolean(statusEffect1.get("statusCured"))) {
                System.out.println(COLOR_BLUE + player1.getCurrentMonster().getName() + " n'est plus " + status1Name + " !" + COLOR_RESET);
            }
            if (statusEffect2.containsKey("statusCured") && Boolean.parseBoolean(statusEffect2.get("statusCured"))) {
                System.out.println(COLOR_ORANGE + player2.getCurrentMonster().getName() + " n'est plus " + status2Name + " !" + COLOR_RESET);
            }

            if (statusEffect1.containsKey("statusEffect")) {
                System.out.println("\n" + COLOR_BLUE + statusEffect1.get("statusEffect") + COLOR_RESET);
            }
            if (statusEffect2.containsKey("statusEffect")) {
                System.out.println("\n" + COLOR_ORANGE + statusEffect2.get("statusEffect") + COLOR_RESET);
            }

            // Vérifier si les monstres peuvent attaquer après les statuts
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

            // Appliquer les effets passifs
            String passiveEffect1 = player1.getCurrentMonster().applyPassiveEffect(this);
            String passiveEffect2 = player2.getCurrentMonster().applyPassiveEffect(this);
            if (!passiveEffect1.isEmpty()) {
                System.out.println(COLOR_BLUE + passiveEffect1 + COLOR_RESET);
            }
            if (!passiveEffect2.isEmpty()) {
                System.out.println(COLOR_ORANGE + passiveEffect2 + COLOR_RESET);
            }

            // Appliquer les effets du terrain
            HashMap<String, String> groundEffect = ground.applyGroundEffect(player1.getCurrentMonster(), player2.getCurrentMonster(), this);
            if (groundEffect.containsKey("monster1_statusEffect")) {
                System.out.println(COLOR_BLUE + groundEffect.get("monster1_statusEffect") + COLOR_RESET);
            }
            if (groundEffect.containsKey("monster2_statusEffect")) {
                System.out.println(COLOR_ORANGE + groundEffect.get("monster2_statusEffect") + COLOR_RESET);
            }
            // Vérifier si un monstre peut attaquer après les effets du terrain
            if (attack1 != null && groundEffect.containsKey("monster1_attackAble") && Boolean.parseBoolean(groundEffect.get("monster1_attackAble")) == false) {
                System.out.println(COLOR_BLUE + player1.getCurrentMonster().getName() + " ne peut pas attaquer à cause du terrain !" + COLOR_RESET);
                attack1 = null;
            }
            if (attack2 != null && groundEffect.containsKey("monster2_attackAble") && Boolean.parseBoolean(groundEffect.get("monster2_attackAble")) == false) {
                System.out.println(COLOR_ORANGE + player2.getCurrentMonster().getName() + " ne peut pas attaquer à cause du terrain !" + COLOR_RESET);
                attack2 = null;
            }
            // Vérifier si le terrain a disparu
            if (groundEffect.containsKey("groundCured") && Boolean.parseBoolean(groundEffect.get("groundCured"))) {
                System.out.println("\nLe terrain " + ground.getName() + " a disparu !");
            }

            // Vérifier si un monstre est K.O. avant d'attaquer
            if (player1.getCurrentMonster().getCurrentHealth() <= 0) {
                displayMonsterKO(player1.getCurrentMonster(), COLOR_BLUE);
                attack1 = null;
            }
            if (player2.getCurrentMonster().getCurrentHealth() <= 0) {
                displayMonsterKO(player2.getCurrentMonster(), COLOR_ORANGE);
                attack2 = null;
            }

            // Appliquer les actions d'attaque
            if (attack1 != null && attack2 != null) {
                // Les deux joueurs ont choisi d'attaquer
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
                // Seul le joueur 1 attaque
                displayAttackAction(attack1.performAttack(player1.getCurrentMonster(), player2.getCurrentMonster(), this));
                if (player2.getCurrentMonster().getCurrentHealth() <= 0) {
                    displayMonsterKO(player2.getCurrentMonster(), COLOR_ORANGE);
                }
            } else if (attack2 != null) {
                // Seul le joueur 2 attaque
                displayAttackAction(attack2.performAttack(player2.getCurrentMonster(), player1.getCurrentMonster(), this));
                if (player1.getCurrentMonster().getCurrentHealth() <= 0) {
                    displayMonsterKO(player1.getCurrentMonster(), COLOR_BLUE);
                }
            }
            displayCurrentStatus();

            // Vérifier les K.O. et effectuer un changement si nécessaire
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

    private int chooseMonster(Player player, String color) {
        Scanner scanner = new Scanner(System.in);

        HashMap<Integer, Integer> indexMap = new HashMap<>();
        System.out.println("\n" + color + player.getName() + ", choisissez un monstre à envoyer au combat:" + COLOR_RESET);
        int displayIndex = 1;
        for (int i = 0; i < player.getMonsters().size(); i++) {
            Monster monster = player.getMonsters().get(i);
            if (monster.getCurrentHealth() > 0 && i != player.getCurrentMonsterIndex()) {
                System.out.println(displayIndex + ". " + monster.getName() + " (HP: " + monster.getCurrentHealth() + ")");
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
        String message = item.use(player.getCurrentMonster(), this);
        System.out.println(color + "\n" + player.getName() + " utilise " + item.getName() + ". " + message + COLOR_RESET);
        player.getInventory().remove(itemIndex);
    }

    private void displayMonsterKO(Monster monster, String color) {
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

    private Attack chooseAttack(Player player, String color) {
        Scanner scanner = new Scanner(System.in);
        
        // Get current monster and its attacks
        var currentMonster = player.getCurrentMonster();
        var attacks = currentMonster.getAttacks();

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

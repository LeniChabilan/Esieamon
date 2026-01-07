package com.esiea.pootp.Bot;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Attack.Attack;
import com.esiea.pootp.Attack.AttackMonster;
import com.esiea.pootp.Battle.ActionType;
import com.esiea.pootp.Object.ObjectMonster;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe Bot pour gérer l'intelligence artificielle du jeu
 * Peut être configurée avec différents niveaux de difficulté
 */

public class Bot {
    private Player botPlayer;
    private BotDifficulty difficulty;
    
    public enum BotDifficulty {
        EASY,    // Aléatoire
        MEDIUM,  // Stratégie basique
        HARD     // Stratégie avancée
    }
    
    public Bot(Player player, BotDifficulty difficulty) {
        this.botPlayer = player;
        this.difficulty = difficulty;
    }
    
    /**
     * Le bot choisit une action en fonction de sa difficulté
     */
    public ActionType chooseAction(Player opponent) {
        switch (difficulty) {
            case EASY:
                return chooseActionEasy();
            case MEDIUM:
                return chooseActionMedium(opponent);
            case HARD:
                return chooseActionHard(opponent);
            default:
                return ActionType.ATTACK;
        }
    }
    
    /**
     * EASY: Choix complètement aléatoire
     */
    private ActionType chooseActionEasy() {
        boolean canSwitch = botPlayer.getAvailableMonstersMap().size() > 1;
        boolean hasItems = !botPlayer.getInventory().isEmpty();
        
        List<ActionType> availableActions = new ArrayList<>();
        availableActions.add(ActionType.ATTACK);
        if (canSwitch) availableActions.add(ActionType.SWITCH);
        if (hasItems) availableActions.add(ActionType.ITEM);
        
        int randomIndex = (int) (Math.random() * availableActions.size());
        return availableActions.get(randomIndex);
    }
    
    /**
     * MEDIUM: Stratégie basique
     * - Change si le monstre a moins de 30% HP
     * - Utilise des objets si HP < 40%
     * - Sinon attaque
     */
    private ActionType chooseActionMedium(Player opponent) {
        Monster currentMonster = botPlayer.getCurrentMonster();
        double hpPercentage = (double) currentMonster.getCurrentHealth() / currentMonster.getHealth();
        
        // Change de monstre si HP très bas et qu'il y a un autre monstre
        if (hpPercentage < 0.3 && botPlayer.getAvailableMonstersMap().size() > 1) {
            return ActionType.SWITCH;
        }
        
        // Utilise un objet si HP moyen et qu'il en a
        if (hpPercentage < 0.4 && !botPlayer.getInventory().isEmpty()) {
            return ActionType.ITEM;
        }
        
        // Sinon attaque
        return ActionType.ATTACK;
    }
    
    /**
     * HARD: Stratégie avancée
     * - Analyse les types pour les matchups
     * - Gère les changements stratégiques
     * - Optimise l'utilisation des objets
     */
    private ActionType chooseActionHard(Player opponent) {
        Monster currentMonster = botPlayer.getCurrentMonster();
        Monster opponentMonster = opponent.getCurrentMonster();
        double hpPercentage = (double) currentMonster.getCurrentHealth() / currentMonster.getHealth();
        
        // Analyse le matchup de types
        boolean badMatchup = isBadMatchup(currentMonster, opponentMonster);
        
        // Change si mauvais matchup et qu'on a un meilleur monstre
        if (badMatchup && botPlayer.getAvailableMonstersMap().size() > 1) {
            return ActionType.SWITCH;
        }
        
        // Change si HP critique (< 25%)
        if (hpPercentage < 0.25 && botPlayer.getAvailableMonstersMap().size() > 1) {
            return ActionType.SWITCH;
        }
        
        // Utilise un objet si HP < 50% et qu'on a des potions
        if (hpPercentage < 0.5 && hasHealingItems()) {
            return ActionType.ITEM;
        }
        
        return ActionType.ATTACK;
    }
    
    /**
     * Le bot choisit une attaque
     */
    public Attack chooseAttack(Player opponent) {
        switch (difficulty) {
            case EASY:
                return chooseAttackEasy();
            case MEDIUM:
                return chooseAttackMedium(opponent);
            case HARD:
                return chooseAttackHard(opponent);
            default:
                return chooseAttackEasy();
        }
    }
    
    /**
     * EASY: Attaque aléatoire parmi celles disponibles
     */
    private Attack chooseAttackEasy() {
        List<AttackMonster> availableAttacks = getAvailableAttacks();
        if (availableAttacks.isEmpty()) {
            return null; // Utilisera Lutte
        }
        int randomIndex = (int) (Math.random() * availableAttacks.size());
        return availableAttacks.get(randomIndex);
    }
    
    /**
     * MEDIUM: Privilégie les attaques puissantes
     */
    private Attack chooseAttackMedium(Player opponent) {
        List<AttackMonster> availableAttacks = getAvailableAttacks();
        if (availableAttacks.isEmpty()) {
            return null;
        }
        
        // Choisit l'attaque la plus puissante
        AttackMonster bestAttack = availableAttacks.get(0);
        for (AttackMonster attack : availableAttacks) {
            if (attack.getPower() > bestAttack.getPower()) {
                bestAttack = attack;
            }
        }
        return bestAttack;
    }
    
    /**
     * HARD: Analyse les types et choisit la meilleure attaque
     */
    private Attack chooseAttackHard(Player opponent) {
        List<AttackMonster> availableAttacks = getAvailableAttacks();
        if (availableAttacks.isEmpty()) {
            return null;
        }
        
        Monster opponentMonster = opponent.getCurrentMonster();
        AttackMonster bestAttack = availableAttacks.get(0);
        double bestScore = evaluateAttack(bestAttack, opponentMonster);
        
        for (AttackMonster attack : availableAttacks) {
            double score = evaluateAttack(attack, opponentMonster);
            if (score > bestScore) {
                bestScore = score;
                bestAttack = attack;
            }
        }
        
        return bestAttack;
    }
    
    /**
     * Le bot choisit un monstre à envoyer
     */
    public int chooseMonster(Player opponent) {
        switch (difficulty) {
            case EASY:
                return chooseMonsterEasy();
            case MEDIUM:
                return chooseMonsterMedium(opponent);
            case HARD:
                return chooseMonsterHard(opponent);
            default:
                return chooseMonsterEasy();
        }
    }
    
    /**
     * EASY: Monstre aléatoire
     */
    private int chooseMonsterEasy() {
        HashMap<Integer, Monster> availableMonsters = botPlayer.getAvailableMonstersMap();
        List<Integer> indices = new ArrayList<>(availableMonsters.keySet());
        
        // Exclut le monstre actuel
        indices.remove(Integer.valueOf(botPlayer.currentMonsterIndex));
        
        if (indices.isEmpty()) {
            return botPlayer.currentMonsterIndex;
        }
        
        int randomIndex = (int) (Math.random() * indices.size());
        return indices.get(randomIndex);
    }
    
    /**
     * MEDIUM: Choisit le monstre avec le plus de HP
     */
    private int chooseMonsterMedium(Player opponent) {
        int bestIndex = -1;
        int bestHP = -1;
        
        for (int i = 0; i < botPlayer.monsters.size(); i++) {
            Monster monster = botPlayer.monsters.get(i);
            if (monster.getCurrentHealth() > 0 && i != botPlayer.currentMonsterIndex) {
                if (monster.getCurrentHealth() > bestHP) {
                    bestHP = monster.getCurrentHealth();
                    bestIndex = i;
                }
            }
        }
        
        return bestIndex != -1 ? bestIndex : botPlayer.currentMonsterIndex;
    }
    
    /**
     * HARD: Choisit le monstre avec le meilleur matchup
     */
    private int chooseMonsterHard(Player opponent) {
        Monster opponentMonster = opponent.getCurrentMonster();
        int bestIndex = -1;
        double bestScore = -1000;
        
        for (int i = 0; i < botPlayer.monsters.size(); i++) {
            Monster monster = botPlayer.monsters.get(i);
            if (monster.getCurrentHealth() > 0 && i != botPlayer.currentMonsterIndex) {
                double score = evaluateMatchup(monster, opponentMonster);
                if (score > bestScore) {
                    bestScore = score;
                    bestIndex = i;
                }
            }
        }
        
        return bestIndex != -1 ? bestIndex : botPlayer.currentMonsterIndex;
    }
    
    /**
     * Le bot choisit un objet à utiliser
     */
    public int chooseItem() {
        List<ObjectMonster> inventory = botPlayer.getInventory();
        if (inventory.isEmpty()) {
            return -1;
        }
        
        // Privilégie les potions de soin
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getName().contains("HP")) {
                return i;
            }
        }
        
        // Sinon prend le premier objet
        return 0;
    }
    
    // ============ MÉTHODES UTILITAIRES ============
    
    private List<AttackMonster> getAvailableAttacks() {
        List<AttackMonster> available = new ArrayList<>();
        for (AttackMonster attack : botPlayer.getCurrentMonster().attacks) {
            if (attack.getNbUses() > 0) {
                available.add(attack);
            }
        }
        return available;
    }
    
    private boolean hasHealingItems() {
        for (ObjectMonster item : botPlayer.getInventory()) {
            if (item.getName().contains("HP") || item.getName().contains("Potion")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Évalue si le matchup actuel est défavorable
     */
    private boolean isBadMatchup(Monster myMonster, Monster opponentMonster) {
        // Analyse basique des types (à adapter selon ton système de types)
        String myType = myMonster.getClass().getSimpleName();
        String oppType = opponentMonster.getClass().getSimpleName();
        
        // Feu faible contre Eau
        if (myType.contains("Fire") && oppType.contains("Water")) return true;
        // Eau faible contre Électrique
        if (myType.contains("Water") && oppType.contains("Electric")) return true;
        // Nature faible contre Feu
        if (myType.contains("Nature") && oppType.contains("Fire")) return true;
        // Électrique faible contre Terre
        if (myType.contains("Electric") && oppType.contains("Earth")) return true;
        
        return false;
    }
    
    /**
     * Évalue la qualité d'une attaque contre un adversaire
     */
    private double evaluateAttack(AttackMonster attack, Monster opponent) {
        double score = attack.getPower();
        
        // Bonus pour les attaques efficaces (simulation simplifiée)
        // Tu peux améliorer ça en utilisant ta vraie logique de types
        
        // Pénalité pour les attaques avec peu de PP
        if (attack.getNbUses() <= 2) {
            score *= 0.5;
        }
        
        // Pénalité pour le taux d'échec
        score *= (1 - attack.getFailureRate());
        
        return score;
    }
    
    /**
     * Évalue le matchup entre deux monstres
     */
    private double evaluateMatchup(Monster myMonster, Monster opponent) {
        double score = 0;
        
        // Score basé sur les HP restants
        score += myMonster.getCurrentHealth() * 0.5;
        
        // Score basé sur les stats
        score += myMonster.getPower() * 0.3;
        score += myMonster.getSpeed() * 0.2;
        
        // Bonus si bon matchup de type
        if (!isBadMatchup(myMonster, opponent)) {
            score += 100;
        }
        
        return score;
    }
    
    public Player getBotPlayer() {
        return botPlayer;
    }
    
    public BotDifficulty getDifficulty() {
        return difficulty;
    }
}
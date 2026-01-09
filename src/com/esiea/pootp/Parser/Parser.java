package com.esiea.pootp.Parser;

import com.esiea.pootp.Attack.AttackMonster;
import com.esiea.pootp.Attack.AttackType;
import com.esiea.pootp.Monster.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analyseur du fichier de données du jeu (game_data.txt).
 *
 * Format attendu (sections en majuscules terminées par End...):
 * - Section "Monster" ... "EndMonster" avec lignes clés/valeurs:
 *   Name <nom>
 *   Type <FIRE|WATER|GRASS|ELECTRIC|EARTH|INSECT>
 *   HP Max <valeur>
 *   Speed Value <valeur>
 *   Attack Value <valeur>
 *   Defense Value <valeur>
 *   Chances spécifiques selon le type (Paralysis, Burn, HealChance, BurrowChance,
 *   FloodChance, FallChance) sous forme "<Clé> <valeurDouble>".
 * - Section "Attack" ... "EndAttack":
 *   Name <nom>
 *   Type <NORMAL|FIRE|WATER|GRASS|ELECTRIC|EARTH|INSECT>
 *   Power <int>
 *   NbUse <int>
 *   Fail <double 0..1>
 * - Section "AttackMonster" ... "EndAttackMonster":
 *   Monster <nomMonstre>
 *   Attack <nomAttaque> (répétable)
 *
 * La lecture s'effectue en deux passes: on lit d'abord toutes les données
 * (monstres, attaques et associations), puis on instancie les objets finaux.
 */
public class Parser {
    
    private Map<String, AttackMonster> attacksMap;
    private Map<String, Monster> monstersMap;
    
    /**
     * Initialise les tables d'attaques et de monstres.
     */
    public Parser() {
        this.attacksMap = new HashMap<>();
        this.monstersMap = new HashMap<>();
    }
    
    /**
     * Parse le fichier de données et peuple les structures internes.
     * @param filePath chemin du fichier (ex: ./src/.../game_data.txt)
     */
    public void parseFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        
        Map<String, MonsterData> monsterDataMap = new HashMap<>();
        Map<String, List<String>> monsterAttacksMap = new HashMap<>();
        
        // Première passe : lire toutes les données
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.equals("Monster")) {
                MonsterData monsterData = parseMonster(reader);
                monsterDataMap.put(monsterData.name, monsterData);
            } else if (line.equals("Attack")) {
                AttackMonster attack = parseAttack(reader);
                attacksMap.put(attack.getName(), attack);
            } else if (line.equals("AttackMonster")) {
                parseAttackMonster(reader, monsterAttacksMap);
            }
        }
        
        reader.close();
        
        // Deuxième passe : créer les monstres avec leurs attaques
        for (String monsterName : monsterDataMap.keySet()) {
            MonsterData data = monsterDataMap.get(monsterName);
            Monster monster = createMonster(data);
            
            // Ajouter les attaques au monstre
            List<String> attackNames = monsterAttacksMap.get(monsterName);
            if (attackNames != null) {
                for (String attackName : attackNames) {
                    AttackMonster attack = attacksMap.get(attackName);
                    if (attack != null) {
                        monster.getAttacks().add(attack);
                    }
                }
            }
            
            monstersMap.put(monsterName, monster);
        }
    }
    
    /**
     * @return la liste des monstres disponibles (instances modèles, non destinées
     *         à être utilisées directement en combat)
     */
    public List<Monster> getAvailableMonsters() {
        return new ArrayList<>(monstersMap.values());
    }
    
    /**
     * Crée une copie indépendante d'un monstre nommé (statistiques + attaques),
     * pour être attribuée à un joueur sans partager l'état avec le modèle.
     * @param name nom du monstre
     * @return une nouvelle instance ou null si inconnue
     */
    public Monster getMonsterCopy(String name) {
        Monster original = monstersMap.get(name);
        if (original == null) {
            return null;
        }
        
        // Créer une copie du monstre pour que chaque joueur ait son propre monstre
        Monster copy = createMonsterCopy(original);
        
        // Copier les attaques
        for (AttackMonster attack : original.getAttacks()) {
            copy.getAttacks().add(attack);
        }
        
        return copy;
    }
    
    /**
     * Parse une section Monster jusqu'à EndMonster.
     */
    private MonsterData parseMonster(BufferedReader reader) throws IOException {
        String name = null;
        String type = null;
        int hp = 0;
        int speed = 0;
        int attack = 0;
        int defense = 0;
        double specialChance1 = 0.0;
        double specialChance2 = 0.0;
        
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.equals("EndMonster")) {
                break;
            }
            
            String[] parts = line.split("\\s+");
            if (parts.length < 2) continue;
            
            String key = parts[0];
            
            switch (key) {
                case "Name":
                    name = line.substring(5).trim();
                    break;
                case "Type":
                    type = parts[1];
                    break;
                case "HP":
                    hp = Integer.parseInt(parts[2]); // Valeur max
                    break;
                case "Speed":
                    speed = Integer.parseInt(parts[2]);
                    break;
                case "Attack":
                    attack = Integer.parseInt(parts[2]);
                    break;
                case "Defense":
                    defense = Integer.parseInt(parts[2]);
                    break;
                case "Paralysis":
                case "Burn":
                case "HealChance":
                case "BurrowChance":
                    specialChance1 = Double.parseDouble(parts[1]);
                    break;
                case "FloodChance":
                    specialChance1 = Double.parseDouble(parts[1]);
                    break;
                case "FallChance":
                    specialChance2 = Double.parseDouble(parts[1]);
                    break;
                }
        }
        
        return new MonsterData(name, type, hp, speed, attack, defense, specialChance1, specialChance2);
    }
    
    /**
     * Parse une section Attack jusqu'à EndAttack.
     */
    private AttackMonster parseAttack(BufferedReader reader) throws IOException {
        String name = null;
        AttackType type = null;
        int power = 0;
        int nbUse = 0;
        double fail = 0.0;
        
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.equals("EndAttack")) {
                break;
            }
            
            String[] parts = line.split("\\s+", 2);
            if (parts.length < 2) continue;
            
            String key = parts[0];
            String value = parts[1];
            
            switch (key) {
                case "Name":
                    name = value;
                    break;
                case "Type":
                    type = AttackType.valueOf(value.toUpperCase());
                    break;
                case "Power":
                    power = Integer.parseInt(value);
                    break;
                case "NbUse":
                    nbUse = Integer.parseInt(value);
                    break;
                case "Fail":
                    fail = Double.parseDouble(value);
                    break;
            }
        }
        
        return new AttackMonster(name, power, nbUse, fail, type);
    }
    
    /**
     * Parse une section AttackMonster jusqu'à EndAttackMonster et enregistre
     * les associations monstre->liste d'attaques.
     */
    private void parseAttackMonster(BufferedReader reader, Map<String, List<String>> monsterAttacksMap) throws IOException {
        String monsterName = null;
        List<String> attacks = new ArrayList<>();
        
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.equals("EndAttackMonster")) {
                break;
            }
            
            String[] parts = line.split("\\s+", 2);
            if (parts.length < 2) continue;
            
            String key = parts[0];
            String value = parts[1];
            
            if (key.equals("Monster")) {
                monsterName = value;
            } else if (key.equals("Attack")) {
                attacks.add(value);
            }
        }
        
        if (monsterName != null) {
            monsterAttacksMap.put(monsterName, attacks);
        }
    }
    
    /**
     * Crée une instance de monstre du bon type à partir des données lues.
     */
    private Monster createMonster(MonsterData data) {
        switch (data.type.toUpperCase()) {
            case "FIRE":
                return new FireMonster(data.name, data.hp, data.attack, 
                                      data.defense, data.speed, data.specialChance1);
            case "ELECTRIC":
                return new ElectricMonster(data.name, data.hp, data.attack, 
                                      data.defense, data.speed, data.specialChance1);
            case "WATER":
                return new WaterMonster(data.name, data.hp, data.attack, 
                                      data.defense, data.speed, data.specialChance1, data.specialChance2);
            case "GRASS":
                return new GrassMonster(data.name, data.hp, data.attack, 
                                      data.defense, data.speed, data.specialChance1);
            case "EARTH":
                return new EarthMonster(data.name, data.hp, data.attack, 
                                      data.defense, data.speed, data.specialChance1);
            case "INSECT":
                return new InsectMonster(data.name, data.hp, data.attack, 
                                      data.defense, data.speed);
            default:
                throw new IllegalArgumentException("Type de monstre inconnu: " + data.type);
        }
    }
    
    /**
     * Crée une copie indépendante du monstre donné, conservant le type exact et
     * les paramètres spécifiques (chances, etc.).
     */
    private Monster createMonsterCopy(Monster original) {
        if (original instanceof FireMonster) {
            return new FireMonster(original.getName(), original.getHealth(), 
                                  original.getPower(), original.getDefense(), 
                                  original.getSpeed(), ((FireMonster) original).getBurnChance()
                                );
        }
        else if (original instanceof ElectricMonster) {
            return new ElectricMonster(original.getName(), original.getHealth(), 
                                  original.getPower(), original.getDefense(), 
                                  original.getSpeed(), ((ElectricMonster) original).getParalysisChance());
        }
        else if (original instanceof WaterMonster) {
            return new WaterMonster(original.getName(), original.getHealth(), 
                                  original.getPower(), original.getDefense(), 
                                  original.getSpeed(), ((WaterMonster) original).getFloodChance(),
                                  ((WaterMonster) original).getFallChance());
        }
        else if (original instanceof GrassMonster) {
            return new GrassMonster(original.getName(), original.getHealth(), 
                                  original.getPower(), original.getDefense(), 
                                  original.getSpeed(), ((GrassMonster) original).getHealChance());
        }
        else if (original instanceof EarthMonster) {
            return new EarthMonster(original.getName(), original.getHealth(), 
                                  original.getPower(), original.getDefense(), 
                                  original.getSpeed(), ((EarthMonster) original).getBurrowChance());
        }
        else if (original instanceof InsectMonster) {
            return new InsectMonster(original.getName(), original.getHealth(), 
                                  original.getPower(), original.getDefense(), 
                                  original.getSpeed());
        }
        return null;
    }
    
    // Classe interne pour stocker les données du monstre temporairement
    private static class MonsterData {
        String name;
        String type;
        int hp;
        int speed;
        int attack;
        int defense;
        double specialChance1 = 0.0;  
        double specialChance2 = 0.0;
        
        MonsterData(String name, String type, int hp, int speed, int attack, int defense, double specialChance1, double specialChance2) {
            this.name = name;
            this.type = type;
            this.hp = hp;
            this.speed = speed;
            this.attack = attack;
            this.defense = defense;
            this.specialChance1 = specialChance1;
            this.specialChance2 = specialChance2;
        }
    }
}
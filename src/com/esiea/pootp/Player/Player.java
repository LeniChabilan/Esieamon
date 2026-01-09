package com.esiea.pootp.Player;

import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Object.ObjectMonster;
import com.esiea.pootp.Object.Medicine.Medicine;
import com.esiea.pootp.Object.Medicine.MedecineType;
import com.esiea.pootp.Object.Potion.Potion;
import com.esiea.pootp.Object.Potion.PotionEfficiency;
import com.esiea.pootp.Object.Potion.PotionType;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Représente un joueur du jeu Esieamon.
 * Un joueur possède un nom, une équipe de monstres, un inventaire d'objets
 * et l'index de son monstre actuellement au combat.
 */
public class Player {
    public String name;
    public List<Monster> monsters;
    private List<ObjectMonster> inventory;
    public int currentMonsterIndex;

    /**
     * Crée un joueur avec le nom donné.
     * @param name Nom du joueur
     */
    public Player(String name) {
        this.name = name;
        this.monsters = new ArrayList<>();
        this.currentMonsterIndex = 0;
        this.inventory = new ArrayList<>();
        // Inventaire temporaire de départ (à adapter selon vos règles)
        this.inventory.add(new Potion(PotionEfficiency.SUPER, PotionType.HP));
        this.inventory.add(new Potion(PotionEfficiency.NORMAL, PotionType.ATTACK));
        this.inventory.add(new Potion(PotionEfficiency.HYPER, PotionType.DEFENSE));
        this.inventory.add(new Medicine(3, MedecineType.SPONGE_GROUND));

    }

    /**
     * Indique si le joueur possède au moins un monstre encore en état de se battre.
     * @return true si au moins un monstre a des PV > 0, false sinon
     */
    public boolean hasUsableMonsters() {
        for (Monster monster : monsters) {
            if (monster.currentHealth > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne le monstre actuellement envoyé au combat.
     * ATTENTION: suppose qu'au moins un monstre est présent dans la liste.
     * @return Monstre courant
     */
    public Monster getCurrentMonster() {
        return monsters.get(currentMonsterIndex);
    }

    /**
     * Retourne le nom du joueur.
     * @return nom
     */
    public String getName() {
        return name;
    }

    /**
     * Construit une map indexée des monstres encore utilisables (PV > 0),
     * pratique pour l'affichage et la sélection.
     * @return une table (index d'affichage -> monstre)
     */
    public HashMap<Integer, Monster> getAvailableMonstersMap() {
        HashMap<Integer, Monster> available = new HashMap<>();
        int index = 0;
        for (Monster monster : monsters) {
            if (monster.currentHealth > 0) {
                available.put(index, monster);
                index++;
            }
        }
        return available;
    }

    /**
     * Ajoute un objet à l'inventaire du joueur.
     * @param item objet à ajouter
     */
    public void addToInventory(ObjectMonster item) {
        if (inventory == null) {
            inventory = new ArrayList<>();
        }
        inventory.add(item);
    }

    /**
     * Retourne la liste des objets détenus par le joueur.
     * @return inventaire (liste mutable)
     */
    public List<ObjectMonster> getInventory() {
        return inventory;
    }
}

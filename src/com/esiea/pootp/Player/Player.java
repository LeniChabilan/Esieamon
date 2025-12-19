package com.esiea.pootp.Player;

import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Object.ObjectMonster;
import com.esiea.pootp.Object.Potion.Potion;
import com.esiea.pootp.Object.Potion.PotionEfficiency;
import com.esiea.pootp.Object.Potion.PotionType;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    public String name;
    public List<Monster> monsters;
    private List<ObjectMonster> inventory;
    public int currentMonsterIndex;

    public Player(String name) {
        this.name = name;
        this.monsters = new ArrayList<>();
        this.currentMonsterIndex = 0;
        this.inventory = new ArrayList<>();

        //temp inventory
        this.inventory.add(new Potion(PotionEfficiency.SUPER, PotionType.HP));
        this.inventory.add(new Potion(PotionEfficiency.NORMAL, PotionType.ATTACK));
        this.inventory.add(new Potion(PotionEfficiency.HYPER, PotionType.DEFENSE));

    }

    public boolean hasUsableMonsters() {
        for (Monster monster : monsters) {
            if (monster.currentHealth > 0) {
                return true;
            }
        }
        return false;
    }

    public Monster getCurrentMonster() {
        return monsters.get(currentMonsterIndex);
    }

    public String getName() {
        return name;
    }

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

    public void addToInventory(ObjectMonster item) {
        if (inventory == null) {
            inventory = new ArrayList<>();
        }
        inventory.add(item);
    }

    public List<ObjectMonster> getInventory() {
        return inventory;
    }
}

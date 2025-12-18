package com.esiea.pootp.Player;

import com.esiea.pootp.Monster.Monster;
import java.util.List;
import java.util.ArrayList;

public class Player {
    public String name;
    public List<Monster> monsters;
    public int currentMonsterIndex;

    public Player(String name) {
        this.name = name;
        this.monsters = new ArrayList<>();
        this.currentMonsterIndex = 0;
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
}

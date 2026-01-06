package com.esiea.pootp.Battle;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Attack.Attack;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Ground.*;
import com.esiea.pootp.Parser.Parser;

import java.util.HashMap;

public abstract class Battle {
    public Player player1;
    public Player player2;
    protected Parser parser;
    protected int teamSize = 3;
    protected Ground ground = new NormalGround();

    protected static final String COLOR_BLUE = "\u001B[94m";
    protected static final String COLOR_ORANGE = "\u001B[33m";
    protected static final String COLOR_RESET = "\u001B[0m";

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

    protected void switchMonster(Player player, int monsterIndex, String color) {
        player.currentMonsterIndex = monsterIndex;
        displayMonsterSwitch(player, color);
    }

    // Méthodes abstraites pour l'affichage et l'interaction
    public abstract void displayWinner();
    
    public abstract void displayCurrentStatus();
    
    public abstract void displayMonsterKO(Monster monster, String color);
    
    protected abstract void displayAttackAction(HashMap<String, String> attackResult);
    
    protected abstract void displayMonsterSwitch(Player player, String color);
    
    protected abstract ActionType chooseAction(Player player, String color);
    
    protected abstract Attack chooseAttack(Player player, String color);
    
    protected abstract int chooseMonster(Player player, String color);
    
    protected abstract Integer chooseItem(Player player, String color);
    
    protected abstract void selectMonstersForPlayer(Player player, Parser parser, String color);
    
    protected abstract void useItem(Player player, int itemIndex, String color);

    // Méthode template pour démarrer la bataille
    public abstract void startBattle();
}

package com.esiea.pootp.Battle;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Ground.*;
import com.esiea.pootp.Parser.Parser;

public abstract class Battle {
    public Player player1;
    public Player player2;
    protected Parser parser;
    protected int teamSize = 3;
    protected Ground ground = new NormalGround();

    // Couleurs pour le terminal
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

    // Méthode abstraite pour démarrer la bataille
    public abstract void startBattle();
}

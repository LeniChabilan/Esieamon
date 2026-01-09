package com.esiea.pootp.Battle;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Ground.*;
import com.esiea.pootp.Parser.Parser;

/**
 * Base abstraite d'une bataille Esieamon (Terminal ou GUI).
 * Contient le contexte commun: joueurs, parser, taille d'équipe et terrain.
 */
public abstract class Battle {
    protected Player player1;
    protected Player player2;
    protected Parser parser;
    protected int teamSize = 3;
    protected Ground ground = new NormalGround();

    // Couleurs pour le terminal
    protected static final String COLOR_BLUE = "\u001B[94m";
    protected static final String COLOR_ORANGE = "\u001B[33m";
    protected static final String COLOR_RESET = "\u001B[0m";

    /**
     * Construit une bataille en initialisant le parser et en chargeant les données.
     */
    public Battle() {
        this.parser = new Parser();
        try {
            parser.parseFile("./src/com/esiea/pootp/Parser/game_data.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** @return le terrain actuel de la bataille */
    public void setGround(Ground ground) {
        this.ground = ground;
    }

    /** @return le terrain actuel de la bataille */
    public Ground getGround() {
        return this.ground;
    }

    /**
     * Méthode abstraite pour démarrer la bataille (à implémenter selon le mode d'IHM).
     */
    public abstract void startBattle();
}

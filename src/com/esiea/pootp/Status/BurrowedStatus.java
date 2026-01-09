package com.esiea.pootp.Status;

import java.util.HashMap;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Ground.Ground;

/**
 * Statut Enterré: le monstre double sa défense pendant une durée aléatoire
 * de 1 à 3 tours, puis revient à sa valeur initiale.
 */
public class BurrowedStatus extends Status {
    private int nbTurnsBurrowed = 0;
    private int durationBurrowed = 0;
    private int originalDefense = 0;

    public BurrowedStatus() {
        super("Enterré");
        this.durationBurrowed = (int)(Math.random() * 3) + 1; // Durée aléatoire de 1 à 3 tours
    }

    @Override
    /**
     * Au premier tour, double la défense; au dernier, restaure la valeur initiale
     * et guérit le statut.
     */
    public HashMap<String, String> performStatus(Monster monster, Ground ground) {
        HashMap<String, String> result = new HashMap<>();
        
        if (nbTurnsBurrowed == 0) {
            originalDefense = monster.getDefense();
            monster.setDefense(monster.getDefense() * 2);
            result.put("statusEffect", monster.getName() + " est enterré et double sa défense !");
        }
        
        if (nbTurnsBurrowed >= durationBurrowed) {
            monster.setDefense(originalDefense);
            result.put("statusCured", "true");
            monster.setStatus(new NormalStatus());
            return result;
        }

        nbTurnsBurrowed++;

        result.put("attackAble", "true");
        return result;
    }
    
}

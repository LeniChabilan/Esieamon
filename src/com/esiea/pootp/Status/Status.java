package com.esiea.pootp.Status;

import java.util.HashMap;

import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Ground.Ground;

/**
 * Statut appliqué à un monstre (ex: Normal, Brûlé, Paralysé...).
 * Un statut peut influencer la capacité à attaquer et infliger des effets à chaque tour.
 */
public class Status {
    private String name;
    private int duration; 

    /**
     * @param name nom lisible du statut
     */
    public Status(String name) {
        this.name = name;
        this.duration = -1;
    }

    /** @return nom du statut */
    public String getName() {
        return name;
    }

    /** @return durée du statut en tours (-1 si non borné) */
    public int getDuration() {
        return duration;
    }

    /**
     * Applique les effets du statut pour le tour courant.
     * Clés possibles dans le résultat:
     * - attackAble: "true"/"false" selon la possibilité d'attaquer
     * - statusCured: "true" si le statut est guéri durant ce tour
     * - statusEffect: message descriptif de l'effet du statut
     * @param monster monstre affecté
     * @param ground terrain actuel
     * @return effets du tour sous forme de clés/valeurs
     */
    public HashMap<String, String> performStatus(Monster monster, Ground ground) {
        HashMap<String, String> result = new HashMap<>();
        result.put("attackAble", "true");
        return result;
    }
}
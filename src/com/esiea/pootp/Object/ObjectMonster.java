package com.esiea.pootp.Object;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Monster.Monster;

/**
 * Représente un objet utilisable pendant le combat.
 * Un objet possède un poids (utilisé pour limiter l'inventaire) et définit un effet
 * via la méthode {@link #use(Monster, Battle)}.
 */
public abstract class ObjectMonster{
    protected int weight; 

    /**
     * Construit un objet avec un poids donné.
     * @param weight poids de l'objet (sert au calcul de la limite d'inventaire)
     */
    public ObjectMonster(int weight){
        this.weight = weight; 
    }

    /**
     * @return le poids de l'objet
     */
    public int getWeight(){
        return weight;
    }

    /**
     * Applique l'effet de l'objet sur un monstre et/ou la bataille.
     * @param monster monstre ciblé par l'objet
     * @param battle contexte de bataille (peut être modifié)
     * @return message descriptif de l'effet appliqué (peut être vide si aucun),
     *         ou null si l'objet n'a eu aucun effet utile
     */
    public String use(Monster monster, Battle battle){
        return "";
    }

    /**
     * @return le nom lisible de l'objet
     */
    public String getName(){
        return "ObjectMonster";
    }

}

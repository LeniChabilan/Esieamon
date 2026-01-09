package com.esiea.pootp.Battle;

/**
 * Type d'action qu'un joueur peut effectuer pendant un tour de combat.
 */
public enum ActionType {
    /**
     * Utiliser une attaque sélectionnée par le monstre actif.
     */
    ATTACK,
    /**
     * Changer de monstre actif (switch) avant la résolution du tour.
     */
    SWITCH,
    /**
     * Utiliser un objet de l'inventaire (potion, médecine, etc.).
     */
    ITEM,
}

package com.esiea.pootp.Attack;

/**
 * Types d'attaques disponibles, déterminant la couverture de type contre différents monstres.
 * Chaque type représente une catégorie d'attaque avec ses forces et faiblesses relatives.
 */
public enum AttackType {
    /**
     * Type normal : pas de bonus ou malus de type particulier.
     */
    NORMAL,
    /**
     * Type feu : efficace contre les monstres herbe, insecte et nature.
     */
    FIRE,
    /**
     * Type eau : efficace contre les monstres feu et terre.
     */
    WATER,
    /**
     * Type électrique : efficace contre les monstres eau.
     */
    ELECTRIC,
    /**
     * Type terre : efficace contre les monstres électrique, feu et poison.
     */
    EARTH,
    /**
     * Type herbe : efficace contre les monstres eau, terre et nature.
     */
    GRASS,
    /**
     * Type insecte : efficace contre les monstres herbe, nature et poison.
     */
    INSECT,
}
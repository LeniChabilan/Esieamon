package com.esiea.pootp;

import com.esiea.pootp.Battle.Battle;
import com.esiea.pootp.Parser.Parser;
import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Monster.Monster;

import java.util.List;
import java.util.Scanner;

public class EsieamonExecutable {
    
    public static void main(String[] args) {
        try {
            // 1. Parser charge toutes les données
            System.out.println("Chargement des données...");
            Parser parser = new Parser();
            parser.parseFile("./src/com/esiea/pootp/Parser/game_data.txt");
            System.out.println("✓ Données chargées avec succès!\n");
            
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des données: " + e.getMessage());
            e.printStackTrace();
        }
           
    }
}
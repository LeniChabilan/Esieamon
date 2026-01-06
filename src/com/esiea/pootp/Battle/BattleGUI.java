package com.esiea.pootp.Battle;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Parser.Parser;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BattleGUI extends Battle {
    
    private static Stage primaryStage;
    private static BattleGUI battleInstance;
    
    public BattleGUI() {
        super();
        battleInstance = this;
    }
    
    public void launch(String[] args) {
        BattleGUIApp.launchApp(args);
    }
    
    public static class BattleGUIApp extends Application {
        private static String[] appArgs;
        
        public static void launchApp(String[] args) {
            appArgs = args;
            Application.launch(BattleGUIApp.class, args);
        }
        
        @Override
        public void start(Stage stage) {
            primaryStage = stage;
            battleInstance.showPlayerSelection();
        }
    }
    
    private void showPlayerSelection() {
        primaryStage.setTitle("Esieamon - Battle");
        primaryStage.setWidth(600);
        primaryStage.setHeight(500);
        primaryStage.centerOnScreen();
        
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e28;");
        
        // Titre
        Label titleLabel = new Label("Esieamon");
        titleLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 36; -fx-font-weight: bold;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(20, 0, 0, 0));
        root.setTop(titleLabel);
        
        // Contenu central
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(30, 30, 30, 30));
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setStyle("-fx-background-color: #1e1e28;");
        
        Label subtitleLabel = new Label("Entrez les noms des joueurs");
        subtitleLabel.setStyle("-fx-text-fill: #4682b4; -fx-font-size: 16;");
        
        GridPane playerGrid = new GridPane();
        playerGrid.setHgap(20);
        playerGrid.setVgap(0);
        playerGrid.setPadding(new Insets(20));
        playerGrid.setStyle("-fx-background-color: #1e1e28;");
        
        // Joueur 1 (Bleu)
        TextField player1Field = new TextField();
        player1Field.setPromptText("Joueur 1");
        player1Field.setStyle(
            "-fx-control-inner-background: #323245;" +
            "-fx-text-fill: #dcdcdc;" +
            "-fx-prompt-text-fill: #888888;" +
            "-fx-font-size: 14;" +
            "-fx-padding: 8;" +
            "-fx-border-color: #4682b4;" +
            "-fx-border-width: 0 0 2 0;" +
            "-fx-focus-color: #4682b4;"
        );
        player1Field.setPrefHeight(35);
        
        VBox player1Box = new VBox(8);
        player1Box.setStyle("-fx-background-color: #2d2d3c; -fx-padding: 15; -fx-border-color: #4682b4; -fx-border-width: 2; -fx-border-radius: 5;");
        Label player1Label = new Label("Joueur 1");
        player1Label.setStyle("-fx-text-fill: #4682b4; -fx-font-size: 14; -fx-font-weight: bold;");
        player1Box.getChildren().addAll(player1Label, player1Field);
        
        // Joueur 2 (Orange)
        TextField player2Field = new TextField();
        player2Field.setPromptText("Joueur 2");
        player2Field.setStyle(
            "-fx-control-inner-background: #323245;" +
            "-fx-text-fill: #dcdcdc;" +
            "-fx-prompt-text-fill: #888888;" +
            "-fx-font-size: 14;" +
            "-fx-padding: 8;" +
            "-fx-border-color: #ff8c00;" +
            "-fx-border-width: 0 0 2 0;" +
            "-fx-focus-color: #ff8c00;"
        );
        player2Field.setPrefHeight(35);
        
        VBox player2Box = new VBox(8);
        player2Box.setStyle("-fx-background-color: #2d2d3c; -fx-padding: 15; -fx-border-color: #ff8c00; -fx-border-width: 2; -fx-border-radius: 5;");
        Label player2Label = new Label("Joueur 2");
        player2Label.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 14; -fx-font-weight: bold;");
        player2Box.getChildren().addAll(player2Label, player2Field);
        
        playerGrid.add(player1Box, 0, 0);
        playerGrid.add(player2Box, 1, 0);
        
        // Boutons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setStyle("-fx-background-color: #1e1e28;");
        
        Button startButton = new Button("Commencer");
        styleButton(startButton, "#3c6496", "#5078aa");
        startButton.setOnAction(e -> {
            String player1Name = player1Field.getText().trim();
            String player2Name = player2Field.getText().trim();
            
            if (player1Name.isEmpty() || player2Name.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer les noms des deux joueurs!", AlertType.ERROR);
            } else {
                this.player1 = new Player(player1Name);
                this.player2 = new Player(player2Name);
                showAlert("Prêts?", "Bienvenue " + player1Name + " et " + player2Name + "!\n\nProchaine étape: Sélection des monstres", AlertType.INFORMATION);
            }
        });
        
        Button exitButton = new Button("Quitter");
        styleButton(exitButton, "#3c6496", "#5078aa");
        exitButton.setOnAction(e -> System.exit(0));
        
        buttonBox.getChildren().addAll(startButton, exitButton);
        
        centerContent.getChildren().addAll(subtitleLabel, playerGrid, buttonBox);
        root.setCenter(centerContent);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void styleButton(Button button, String normalColor, String hoverColor) {
        button.setPrefWidth(120);
        button.setPrefHeight(40);
        button.setStyle(
            "-fx-background-color: " + normalColor + ";" +
            "-fx-text-fill: #dcdcdc;" +
            "-fx-font-size: 14;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: " + hoverColor + ";" +
            "-fx-text-fill: #dcdcdc;" +
            "-fx-font-size: 14;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;" +
            "-fx-cursor: hand;"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + normalColor + ";" +
            "-fx-text-fill: #dcdcdc;" +
            "-fx-font-size: 14;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;" +
            "-fx-cursor: hand;"
        ));
    }
    
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @Override
    public void startBattle() {
        // TODO: Implémenter la logique de bataille en GUI
    }
}

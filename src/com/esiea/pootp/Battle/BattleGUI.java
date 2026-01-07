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
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
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
        primaryStage.setTitle("Esieamon");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(850);
        primaryStage.centerOnScreen();
        
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e28;");
        root.setPadding(new Insets(30, 40, 30, 40));
        
        // Titre
        Label titleLabel = new Label("Esieamon");
        titleLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 42; -fx-font-weight: bold;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(20, 0, 0, 0));
        root.setTop(titleLabel);
        
        // Contenu central
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(30, 30, 30, 30));
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setStyle("-fx-background-color: #1e1e28;");
        centerContent.setMaxWidth(1100);
        
        Label subtitleLabel = new Label("Entrez les noms des joueurs");
        subtitleLabel.setStyle("-fx-text-fill: #4682b4; -fx-font-size: 20;");
        
        GridPane playerGrid = new GridPane();
        playerGrid.setHgap(20);
        playerGrid.setVgap(0);
        playerGrid.setPadding(new Insets(20));
        playerGrid.setStyle("-fx-background-color: #1e1e28;");
        playerGrid.setAlignment(Pos.CENTER);
        playerGrid.setMaxWidth(900);
        
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
        player1Field.setPrefHeight(42);
        player1Field.setPrefWidth(340);
        player1Field.setMaxWidth(420);
        
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
        player2Field.setPrefHeight(42);
        player2Field.setPrefWidth(340);
        player2Field.setMaxWidth(420);
        
        VBox player2Box = new VBox(8);
        player2Box.setStyle("-fx-background-color: #2d2d3c; -fx-padding: 15; -fx-border-color: #ff8c00; -fx-border-width: 2; -fx-border-radius: 5;");
        Label player2Label = new Label("Joueur 2");
        player2Label.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 14; -fx-font-weight: bold;");
        player2Box.getChildren().addAll(player2Label, player2Field);
        
        // Taille de l'équipe
        TextField teamSizeField = new TextField();
        teamSizeField.setPromptText("Taille de l'équipe (par défaut: 3)");
        teamSizeField.setStyle(
            "-fx-control-inner-background: #323245;" +
            "-fx-text-fill: #dcdcdc;" +
            "-fx-prompt-text-fill: #888888;" +
            "-fx-font-size: 14;" +
            "-fx-padding: 8;" +
            "-fx-border-color: #6a5acd;" +
            "-fx-border-width: 0 0 2 0;" +
            "-fx-focus-color: #6a5acd;"
        );
        teamSizeField.setPrefHeight(42);
        teamSizeField.setPrefWidth(300);
        teamSizeField.setMaxWidth(380);

        VBox teamSizeBox = new VBox(8);
        teamSizeBox.setStyle("-fx-background-color: #2d2d3c; -fx-padding: 15; -fx-border-color: #6a5acd; -fx-border-width: 2; -fx-border-radius: 5;");
        Label teamSizeLabel = new Label("Taille de l'équipe");
        teamSizeLabel.setStyle("-fx-text-fill: #6a5acd; -fx-font-size: 14; -fx-font-weight: bold;");
        teamSizeBox.getChildren().addAll(teamSizeLabel, teamSizeField);

        playerGrid.add(player1Box, 0, 0);
        playerGrid.add(player2Box, 1, 0);
        playerGrid.add(teamSizeBox, 0, 1, 2, 1);
        
        // Boutons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setStyle("-fx-background-color: #1e1e28;");
        
        Button startButton = new Button("Commencer");
        styleButton(startButton, "#3c6496", "#5078aa");
        startButton.setOnAction(e -> {
            String player1Name = player1Field.getText().trim();
            String player2Name = player2Field.getText().trim();
            String teamSizeInput = teamSizeField.getText().trim();
            
            if (player1Name.isEmpty() || player2Name.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer les noms des deux joueurs!", AlertType.ERROR);
            } else {
                // Parse team size, default to 3 if empty
                int chosenTeamSize = 3;
                if (!teamSizeInput.isEmpty()) {
                    try {
                        int parsed = Integer.parseInt(teamSizeInput);
                        if (parsed <= 0) {
                            showAlert("Erreur", "La taille d'équipe doit être un entier positif.", AlertType.ERROR);
                            return;
                        }
                        chosenTeamSize = parsed;
                    } catch (NumberFormatException ex) {
                        showAlert("Erreur", "Veuillez saisir un nombre valide pour la taille d'équipe.", AlertType.ERROR);
                        return;
                    }
                }
                this.teamSize = chosenTeamSize;
                this.player1 = new Player(player1Name);
                this.player2 = new Player(player2Name);
                showAlert("Prêts?", "Bienvenue " + player1Name + " et " + player2Name + "!\n\nProchaine étape: Sélection des monstres", AlertType.INFORMATION);
                showMonsterSelection();
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
        button.setPrefWidth(160);
        button.setPrefHeight(50);
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

    private void showMonsterSelection() {
        primaryStage.setTitle("Sélection des monstres");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(850);
        primaryStage.centerOnScreen();
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e28;");
        root.setPadding(new Insets(30, 40, 30, 40));

        Label titleLabel = new Label("Sélection des monstres pour " + currentSelectingPlayerName());
        titleLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 32; -fx-font-weight: bold;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(20, 0, 10, 0));
        root.setTop(titleLabel);

        VBox center = new VBox(18);
        center.setPadding(new Insets(10));
        center.setAlignment(Pos.TOP_CENTER);
        center.setFillWidth(true);

        Label instruction = new Label("Sélectionnez exactement " + teamSize + " monstres");
        instruction.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 18;");

        VBox checkboxContainer = new VBox(8);
        checkboxContainer.setStyle("-fx-background-color: #2d2d3c; -fx-padding: 18; -fx-border-color: #3c6496; -fx-border-width: 2; -fx-border-radius: 6;");
        checkboxContainer.setFillWidth(true);

        var monsters = parser.getAvailableMonsters();
        for (var m : monsters) {
            CheckBox cb = new CheckBox(m.getName() + "  |  HP:" + m.getHealth() + " ATK:" + m.getPower() + " DEF:" + m.getDefense() + " SPD:" + m.getSpeed() +
                                       "  (" + m.attacks.size() + " attaques)");
            cb.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 14;");
            cb.setUserData(m.getName());
            checkboxContainer.getChildren().add(cb);
        }

        HBox bottom = new HBox(20);
        bottom.setPadding(new Insets(15));
        bottom.setAlignment(Pos.CENTER);
        Button validateButton = new Button("Valider la sélection");
        styleButton(validateButton, "#3c6496", "#5078aa");
        bottom.getChildren().add(validateButton);

        center.getChildren().addAll(instruction, checkboxContainer);
        root.setCenter(center);
        root.setBottom(bottom);

        validateButton.setOnAction(ev -> {
            ObservableList<String> chosenNames = FXCollections.observableArrayList();
            for (var node : checkboxContainer.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox cb = (CheckBox) node;
                    if (cb.isSelected()) {
                        chosenNames.add((String) cb.getUserData());
                    }
                }
            }
            if (chosenNames.size() != teamSize) {
                showAlert("Sélection incomplète", "Veuillez choisir exactement " + teamSize + " monstres.", AlertType.WARNING);
                return;
            }
            Player target = isSelectingPlayer1 ? player1 : player2;
            target.monsters.clear();
            for (String name : chosenNames) {
                var copy = parser.getMonsterCopy(name);
                if (copy != null) {
                    target.monsters.add(copy);
                }
            }
            target.currentMonsterIndex = 0;

            if (isSelectingPlayer1) {
                isSelectingPlayer1 = false;
                showMonsterSelection();
            } else {
                showAlert("Sélection terminée", "Les équipes sont prêtes. La logique de bataille GUI reste à implémenter.", AlertType.INFORMATION);
            }
        });

        Scene scene = new Scene(root, 1200, 850);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean isSelectingPlayer1 = true;

    private String currentSelectingPlayerName() {
        return isSelectingPlayer1 ? player1.getName() : player2.getName();
    }
    
    @Override
    public void startBattle() {
        // TODO: Implémenter la logique de bataille en GUI
    }
}

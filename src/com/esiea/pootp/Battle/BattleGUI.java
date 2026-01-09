package com.esiea.pootp.Battle;
import java.io.File;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Object.ObjectMonster;
import com.esiea.pootp.Attack.Attack;
import com.esiea.pootp.Attack.AttackMonster;
import com.esiea.pootp.Attack.AttackStruggle;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Object.Potion.Potion;
import com.esiea.pootp.Object.Potion.PotionEfficiency;
import com.esiea.pootp.Object.Potion.PotionType;
import com.esiea.pootp.Object.Medicine.Medicine;
import com.esiea.pootp.Object.Medicine.MedicineType;
import com.esiea.pootp.Ground.FloodedGround;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BattleGUI extends Battle {
    
    private static Stage primaryStage;
    private static BattleGUI battleInstance;

    // État du jeu
    private enum Phase { P1_CHOOSE, P2_CHOOSE, RESOLVE }
    private Phase phase = Phase.P1_CHOOSE;
    private Attack pendingAttackP1;
    private Attack pendingAttackP2;
    private ObjectMonster pendingItemP1;
    private ObjectMonster pendingItemP2;
    private boolean switchedP1 = false;
    private boolean switchedP2 = false;

    // Conteneurs et éléments UI
    private VBox bottomContainer;
    private Label hintLabel;
    private TextArea logArea;
    private BorderPane battlefieldArea;
    private Label battleTitle;

    private MonsterView viewP1;
    private MonsterView viewP2;
    private MusicPlayer musicPlayer;
    
    // Structures de données pour la vue des monstres
    private static class MonsterView {
        VBox container;
        Label hpText;
        ProgressBar hpBar;
        Label name;
        ImageView sprite;
    }
    
    public BattleGUI() {
        super();
        battleInstance = this;
        musicPlayer = new MusicPlayer();
    }
    
    public void launch(String[] args) {
        BattleGUIApp.launchApp(args);
    }
    
    /**
     * Charge l'image du sprite d'un Pokémon basée sur son nom
     * Mappe les noms de Pokémon aux fichiers PNG disponibles
     */
    private ImageView loadMonsterSprite(Monster monster) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(320);
        imageView.setFitHeight(320);
        imageView.setPreserveRatio(true);
        
        try {
            String monsterName = monster.getName();
            String imagePath = "file:src/com/esiea/pootp/assets/sprite/" + monsterName + ".png";
            
            Image image = new Image(imagePath, 320, 320, true, true);
            
            // Vérifier si l'image s'est chargée correctement
            if (image.isError()) {
                System.err.println("Erreur de chargement du sprite pour: " + monsterName);
                // Fallback: image par défaut
                imageView.setStyle("-fx-border-color: #3c6496; -fx-border-width: 2;");
            } else {
                imageView.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("Exception lors du chargement du sprite: " + e.getMessage());
            imageView.setStyle("-fx-border-color: #3c6496; -fx-border-width: 2;");
        }
        
        return imageView;
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
            
            // Arrêter la musique quand on ferme la fenêtre
            primaryStage.setOnCloseRequest(e -> {
                battleInstance.stopBackgroundMusic();
                System.exit(0);
            });
            
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
        teamSizeBox.setAlignment(Pos.CENTER);
        Label teamSizeLabel = new Label("Taille de l'équipe");
        teamSizeLabel.setStyle("-fx-text-fill: #6a5acd; -fx-font-size: 14; -fx-font-weight: bold;");
        teamSizeBox.getChildren().addAll(teamSizeLabel, teamSizeField);

        playerGrid.add(player1Box, 0, 0);
        playerGrid.add(player2Box, 1, 0);
        playerGrid.add(teamSizeBox, 0, 1, 2, 1);
        GridPane.setHalignment(teamSizeBox, HPos.CENTER);
        
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
                // Définir la taille de l'équipe
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

    private void showGameOverAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnCloseRequest(e -> {
            stopBackgroundMusic();
            System.exit(0);
        });
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
                                       "  (" + m.getAttacks().size() + " attaques)");
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
            target.getMonsters().clear();
            for (String name : chosenNames) {
                var copy = parser.getMonsterCopy(name);
                if (copy != null) {
                    target.getMonsters().add(copy);
                }
            }
            target.setCurrentMonsterIndex(0);

            if (isSelectingPlayer1) {
                isSelectingPlayer1 = false;
                showMonsterSelection();
            } else {
                isSelectingPlayer1 = true; // reset for items
                showItemSelection();
            }
        });

        Scene scene = new Scene(root, 1200, 850);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean isSelectingPlayer1 = true;

    // Sélection d'objets
    private final int itemWeightLimit = 5;

    private String currentSelectingPlayerName() {
        return isSelectingPlayer1 ? player1.getName() : player2.getName();
    }

    private java.util.List<ObjectMonster> getAvailableItems() {
        java.util.List<ObjectMonster> items = new java.util.ArrayList<>();
        items.add(new Potion(PotionEfficiency.NORMAL, PotionType.HP));
        items.add(new Potion(PotionEfficiency.SUPER, PotionType.HP));
        items.add(new Potion(PotionEfficiency.HYPER, PotionType.HP));
        items.add(new Potion(PotionEfficiency.NORMAL, PotionType.ATTACK));
        items.add(new Potion(PotionEfficiency.SUPER, PotionType.ATTACK));
        items.add(new Potion(PotionEfficiency.HYPER, PotionType.ATTACK));
        items.add(new Potion(PotionEfficiency.NORMAL, PotionType.DEFENSE));
        items.add(new Potion(PotionEfficiency.SUPER, PotionType.DEFENSE));
        items.add(new Potion(PotionEfficiency.HYPER, PotionType.DEFENSE));
        items.add(new Potion(PotionEfficiency.NORMAL, PotionType.SPEED));
        items.add(new Potion(PotionEfficiency.SUPER, PotionType.SPEED));
        items.add(new Potion(PotionEfficiency.HYPER, PotionType.SPEED));
        items.add(new Medicine(1, MedicineType.BURN_HEAL));
        items.add(new Medicine(1, MedicineType.PARALYZE_HEAL));
        items.add(new Medicine(1, MedicineType.POISON_HEAL));
        items.add(new Medicine(2, MedicineType.SPONGE_GROUND));
        return items;
    }

    private void showItemSelection() {
        primaryStage.setTitle("Sélection des objets");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(850);
        primaryStage.centerOnScreen();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e28;");
        root.setPadding(new Insets(30, 40, 30, 40));

        Label titleLabel = new Label("Objets pour " + currentSelectingPlayerName());
        titleLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 32; -fx-font-weight: bold;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(20, 0, 10, 0));
        root.setTop(titleLabel);

        VBox center = new VBox(18);
        center.setPadding(new Insets(10));
        center.setAlignment(Pos.TOP_CENTER);
        center.setFillWidth(true);

        Label instruction = new Label("Sélectionnez des objets (poids total max: " + itemWeightLimit + ")");
        instruction.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 18;");

        VBox checkboxContainer = new VBox(8);
        checkboxContainer.setStyle("-fx-background-color: #2d2d3c; -fx-padding: 18; -fx-border-color: #3c6496; -fx-border-width: 2; -fx-border-radius: 6;");
        checkboxContainer.setFillWidth(true);

        var items = getAvailableItems();
        for (ObjectMonster item : items) {
            String label = item.getName() + " (Poids: " + item.getWeight() + ")";
            CheckBox cb = new CheckBox(label);
            cb.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 14;");
            cb.setUserData(item);
            checkboxContainer.getChildren().add(cb);
        }

        HBox bottom = new HBox(20);
        bottom.setPadding(new Insets(15));
        bottom.setAlignment(Pos.CENTER);
        Button validateButton = new Button("Valider les objets");
        styleButton(validateButton, "#3c6496", "#5078aa");
        bottom.getChildren().add(validateButton);

        center.getChildren().addAll(instruction, checkboxContainer);
        root.setCenter(center);
        root.setBottom(bottom);

        validateButton.setOnAction(ev -> {
            java.util.List<ObjectMonster> chosen = new java.util.ArrayList<>();
            int totalWeight = 0;
            for (var node : checkboxContainer.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox cb = (CheckBox) node;
                    if (cb.isSelected()) {
                        ObjectMonster item = (ObjectMonster) cb.getUserData();
                        totalWeight += item.getWeight();
                        chosen.add(item);
                    }
                }
            }

            if (totalWeight > itemWeightLimit) {
                showAlert("Limite dépassée", "Poids total " + totalWeight + " > " + itemWeightLimit, AlertType.ERROR);
                return;
            }

            Player target = isSelectingPlayer1 ? player1 : player2;
            target.getInventory().clear();
            target.getInventory().addAll(chosen);

            if (isSelectingPlayer1) {
                isSelectingPlayer1 = false;
                showItemSelection();
            } else {
                showAlert("Sélection terminée", "Les équipes et objets sont prêts. Lancement du combat.", AlertType.INFORMATION);
                startBattle();
            }
        });

        Scene scene = new Scene(root, 1200, 850);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void applyBattleBackground(BorderPane root) {
        try {
            // Choisir l'image de fond en fonction du terrain
            String imagePath;
            if (ground instanceof FloodedGround) {
                imagePath = "file:src/com/esiea/pootp/assets/flooded_ground.png";
            } else {
                imagePath = "file:src/com/esiea/pootp/assets/normal_ground.png";
            }
            
            Image bgImage = new Image(imagePath);
            BackgroundSize bgSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
            BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bgSize
            );
            root.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: linear-gradient(#1e1e28, #0f1622);");
        }
        
        // Mettre à jour le titre avec le terrain actuel
        if (battleTitle != null) {
            battleTitle.setText("Bataille - Terrain : " + ground.getName());
        }
    }
    
    
    private MonsterView createMonsterDisplay(Player player, String accentColor) {
        var monster = player.getCurrentMonster();
        if (monster == null && !player.getMonsters().isEmpty()) {
            monster = player.getMonsters().get(0);
            player.setCurrentMonsterIndex(0);
        }

        MonsterView view = new MonsterView();
        VBox monsterDisplay = new VBox(10);
        monsterDisplay.setAlignment(Pos.CENTER);

        int maxHp = (monster != null) ? monster.getHealth() : 1;
        int currentHp = (monster != null) ? monster.getCurrentHealth() : 0;
        double hpRatio = Math.max(0.0, Math.min(1.0, (double) currentHp / maxHp));

        view.hpText = new Label(currentHp + " / " + maxHp + " HP");
        view.hpText.setStyle("-fx-text-fill: black; -fx-font-size: 12;");

        view.hpBar = new ProgressBar(hpRatio);
        view.hpBar.setPrefWidth(140);
        view.hpBar.setPrefHeight(20);
        view.hpBar.setStyle("-fx-accent: " + accentColor + "; -fx-padding: 2; -fx-border-color: black; -fx-border-width: 1; -fx-control-inner-background: white;");

        // Créer une HBox pour mettre HP et barre côte à côte
        HBox hpContainer = new HBox(8);
        hpContainer.setAlignment(Pos.CENTER);
        hpContainer.getChildren().addAll(view.hpText, view.hpBar);

        // Charger le sprite du Pokémon
        if (monster != null) {
            view.sprite = loadMonsterSprite(monster);
        } else {
            view.sprite = new ImageView();
            view.sprite.setFitWidth(320);
            view.sprite.setFitHeight(320);
            view.sprite.setStyle("-fx-border-color: " + accentColor + "; -fx-border-width: 2;");
        }
        
        VBox spriteContainer = new VBox();
        spriteContainer.setAlignment(Pos.CENTER);
        spriteContainer.getChildren().add(view.sprite);

        String monsterDisplayName = "Aucun monstre";
        if (monster != null) {
            monsterDisplayName = monster.getName();
            String status = monster.getStatus().getName();
            if (!status.equals("Normal")) {
                monsterDisplayName += " (" + status + ")";
            }
        }
        view.name = new Label(monsterDisplayName);
        view.name.setStyle("-fx-text-fill: black; -fx-font-size: 16; -fx-font-weight: bold;");

        monsterDisplay.getChildren().addAll(view.name, hpContainer, spriteContainer);
        view.container = monsterDisplay;
        return view;
    }

    private void updateMonsterView(MonsterView view, Player player) {
        Monster monster = player.getCurrentMonster();
        if (monster == null && !player.getMonsters().isEmpty()) {
            monster = player.getMonsters().get(0);
            player.setCurrentMonsterIndex(0);
        }
        if (monster == null) {
            view.name.setText("Aucun monstre");
            view.hpText.setText("0 / 0 HP");
            view.hpBar.setProgress(0);
            return;
        }
        int maxHp = monster.getHealth();
        int currentHp = monster.getCurrentHealth();
        double hpRatio = Math.max(0.0, Math.min(1.0, (double) currentHp / Math.max(1, maxHp)));
        String displayName = monster.getName();
        String status = monster.getStatus().getName();
        if (!status.equals("Normal")) {
            displayName += " (" + status + ")";
        }
        view.name.setText(displayName);
        view.hpText.setText(currentHp + " / " + maxHp + " HP");
        view.hpBar.setProgress(hpRatio);

        // Rafraîchir le sprite lorsque le monstre actif change
        if (view.sprite != null) {
            ImageView newSprite = loadMonsterSprite(monster);
            view.sprite.setImage(newSprite.getImage());
            view.sprite.setStyle(newSprite.getStyle());
        }
    }

    private void appendLog(String text) {
        logArea.appendText(text + "\n");
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    private Player currentPlayer() {
        return phase == Phase.P1_CHOOSE ? player1 : player2;
    }

    private void showActionButtons() {
        bottomContainer.getChildren().clear();
        hintLabel.setText("Tour de " + currentPlayer().getName() + " : choisissez une action.");
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);

        Button attackButton = new Button("Attaquer");
        styleButton(attackButton, "#3c6496", "#5078aa");
        attackButton.setOnAction(e -> showAttackChoices());

        Button switchButton = new Button("Changer de monstre");
        styleButton(switchButton, "#3c6496", "#5078aa");
        
        // Vérifier si le joueur peut switcher
        Player p = currentPlayer();
        boolean canSwitch = false;
        for (int i = 0; i < p.getMonsters().size(); i++) {
            if (p.getMonsters().get(i).getCurrentHealth() > 0 && i != p.getCurrentMonsterIndex()) {
                canSwitch = true;
                break;
            }
        }
        switchButton.setDisable(!canSwitch);
        switchButton.setOnAction(e -> showMonsterSwitchOptions());

        Button itemButton = new Button("Utiliser un objet");
        styleButton(itemButton, "#3c6496", "#5078aa");
        boolean hasItems = !p.getInventory().isEmpty();
        itemButton.setDisable(!hasItems);
        itemButton.setOnAction(e -> showItemChoices());

        actions.getChildren().addAll(attackButton, switchButton, itemButton);
        bottomContainer.getChildren().addAll(hintLabel, actions);
    }

    private void showMonsterSwitchOptions() {
        bottomContainer.getChildren().clear();
        Player p = currentPlayer();

        Label title = new Label("Choisissez un monstre à envoyer au combat:");
        title.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 14;");

        HBox monsterList = new HBox(10);
        monsterList.setAlignment(Pos.CENTER);

        for (int i = 0; i < p.getMonsters().size(); i++) {
            Monster m = p.getMonsters().get(i);
            if (m.getCurrentHealth() > 0 && i != p.getCurrentMonsterIndex()) {
                final int idx = i;

                Button b = new Button(m.getName() + "\n(" + m.getCurrentHealth() + "/" + m.getHealth() + " HP)");
                styleButton(b, "#3c6496", "#5078aa");
                b.setOnAction(e -> {
                    selectSwitch(idx);
                });
                monsterList.getChildren().add(b);
            }
        }

        Button back = new Button("Retour");
        styleButton(back, "#555555", "#666666");
        back.setOnAction(e -> showActionButtons());

        monsterList.getChildren().add(back);

        bottomContainer.getChildren().addAll(title, monsterList);
    }

    private void selectSwitch(int monsterIndex) {
        Player p = currentPlayer();
        p.setCurrentMonsterIndex(monsterIndex);
        appendLog(p.getName() + " envoie " + p.getCurrentMonster().getName());

        if (phase == Phase.P1_CHOOSE) {
            switchedP1 = true;
            phase = Phase.P2_CHOOSE;
            showActionButtons();
        } else {
            switchedP2 = true;
            resolveTurn();
        }
    }

    private void showItemChoices() {
        bottomContainer.getChildren().clear();
        Player p = currentPlayer();

        Label title = new Label("Choisissez un objet à utiliser:");
        title.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 14;");

        HBox itemList = new HBox(10);
        itemList.setAlignment(Pos.CENTER);

        java.util.List<ObjectMonster> inventory = p.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            ObjectMonster item = inventory.get(i);
            Button b = new Button(item.getName());
            b.setPrefWidth(120);
            styleButton(b, "#3c6496", "#5078aa");
            b.setOnAction(e -> selectItem(item));
            itemList.getChildren().add(b);
        }

        Button back = new Button("Retour");
        styleButton(back, "#555555", "#666666");
        back.setOnAction(e -> showActionButtons());

        itemList.getChildren().add(back);

        bottomContainer.getChildren().addAll(title, itemList);
    }

    private void selectItem(ObjectMonster item) {
        if (phase == Phase.P1_CHOOSE) {
            pendingItemP1 = item;
            appendLog(currentPlayer().getName() + " choisit " + item.getName());
            phase = Phase.P2_CHOOSE;
            showActionButtons();
        } else {
            pendingItemP2 = item;
            appendLog(currentPlayer().getName() + " choisit " + item.getName());
            resolveTurn();
        }
    }

    private void showAttackChoices() {
        bottomContainer.getChildren().clear();
        Player p = currentPlayer();
        Monster monster = p.getCurrentMonster();

        Label title = new Label("Choisissez une attaque pour " + p.getName() + " / " + monster.getName());
        title.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 14;");

        HBox attackList = new HBox(10);
        attackList.setAlignment(Pos.CENTER);

        java.util.List<Attack> available = new java.util.ArrayList<>();
        for (AttackMonster atk : monster.getAttacks()) {
            if (atk.getNbUses() > 0) {
                available.add(atk);
            }
        }

        if (available.isEmpty()) {
            available.add(new AttackStruggle());
        }

        for (Attack atk : available) {
            String ppInfo = "";
            if (atk instanceof AttackMonster) {
                ppInfo = " (PP: " + ((AttackMonster) atk).getNbUses() + ")";
            }
            Button b = new Button(atk.getName() + ppInfo);
            styleButton(b, "#3c6496", "#5078aa");
            b.setOnAction(e -> {
                selectAttack(atk);
            });
            attackList.getChildren().add(b);
        }

        Button back = new Button("Retour");
        styleButton(back, "#555555", "#666666");
        back.setOnAction(e -> showActionButtons());
        
        attackList.getChildren().add(back);

        bottomContainer.getChildren().addAll(title, attackList);
    }

    private void selectAttack(Attack attack) {
        if (phase == Phase.P1_CHOOSE) {
            pendingAttackP1 = attack;
            appendLog(currentPlayer().getName() + " choisit " + attack.getName());
            phase = Phase.P2_CHOOSE;
            showActionButtons();
        } else {
            pendingAttackP2 = attack;
            appendLog(currentPlayer().getName() + " choisit " + attack.getName());
            resolveTurn();
        }
    }

    private void resolveTurn() {
        phase = Phase.RESOLVE;
        
        // Changement des monstres 
        if (switchedP1 || switchedP2) {
            updateMonsterView(viewP1, player1);
            updateMonsterView(viewP2, player2);
            switchedP1 = false;
            switchedP2 = false;
        }
        
        Monster m1 = player1.getCurrentMonster();
        Monster m2 = player2.getCurrentMonster();

        // Résoudre les objets avant les attaques
        if (pendingItemP1 != null) {
            useItemAndLog(player1, pendingItemP1);
            player1.getInventory().remove(pendingItemP1);
            pendingItemP1 = null;
        }

        if (pendingItemP2 != null) {
            useItemAndLog(player2, pendingItemP2);
            player2.getInventory().remove(pendingItemP2);
            pendingItemP2 = null;
        }
        
        // Mettre à jour le fond en fonction du terrain actuel
        if (battlefieldArea != null) {
            applyBattleBackground(battlefieldArea);
        }

        Attack a1 = pendingAttackP1;
        Attack a2 = pendingAttackP2;

        if (a1 == null && (m1 != null && m1.hasAvailableAttacks() == false)) {
            a1 = new AttackStruggle();
        }
        if (a2 == null && (m2 != null && m2.hasAvailableAttacks() == false)) {
            a2 = new AttackStruggle();
        }
        
        // Appliquer les effets de statut
        String status1Name = m1 != null ? m1.getStatus().getName() : "Normal";
        String status2Name = m2 != null ? m2.getStatus().getName() : "Normal";

        java.util.HashMap<String, String> statusEffect1 = new java.util.HashMap<>();
        java.util.HashMap<String, String> statusEffect2 = new java.util.HashMap<>();

        if (m1 != null) {
            statusEffect1 = m1.getStatus().performStatus(m1, ground);
            if (statusEffect1.containsKey("statusCured") && Boolean.parseBoolean(statusEffect1.get("statusCured"))) {
                appendLog(m1.getName() + " n'est plus " + status1Name + " !");
            }
            if (statusEffect1.containsKey("statusEffect")) {
                appendLog(statusEffect1.get("statusEffect"));
            }
        }

        if (m2 != null) {
            statusEffect2 = m2.getStatus().performStatus(m2, ground);
            if (statusEffect2.containsKey("statusCured") && Boolean.parseBoolean(statusEffect2.get("statusCured"))) {
                appendLog(m2.getName() + " n'est plus " + status2Name + " !");
            }
            if (statusEffect2.containsKey("statusEffect")) {
                appendLog(statusEffect2.get("statusEffect"));
            }
        }

        // Vérifier si les monstres peuvent attaquer après statut
        boolean canAttack1 = !statusEffect1.containsKey("attackAble") || Boolean.parseBoolean(statusEffect1.get("attackAble"));
        boolean canAttack2 = !statusEffect2.containsKey("attackAble") || Boolean.parseBoolean(statusEffect2.get("attackAble"));
        
        if (a1 != null && !canAttack1) {
            appendLog(m1.getName() + " est " + m1.getStatus().getName() + " et ne peut pas attaquer !");
            a1 = null;
        }
        if (a2 != null && !canAttack2) {
            appendLog(m2.getName() + " est " + m2.getStatus().getName() + " et ne peut pas attaquer !");
            a2 = null;
        }

        // Appliquer les effets passifs
        if (m1 != null) {
            String passiveEffect1 = m1.applyPassiveEffect(this);
            if (!passiveEffect1.isEmpty()) {
                appendLog(passiveEffect1);
            }
        }
        if (m2 != null) {
            String passiveEffect2 = m2.applyPassiveEffect(this);
            if (!passiveEffect2.isEmpty()) {
                appendLog(passiveEffect2);
            }
        }

        // Appliquer les effets du terrain
        java.util.HashMap<String, String> groundEffect = ground.applyGroundEffect(m1, m2, this);
        if (groundEffect.containsKey("monster1_statusEffect")) {
            appendLog(groundEffect.get("monster1_statusEffect"));
        }
        if (groundEffect.containsKey("monster2_statusEffect")) {
            appendLog(groundEffect.get("monster2_statusEffect"));
        }
        if (groundEffect.containsKey("groundCured") && Boolean.parseBoolean(groundEffect.get("groundCured"))) {
            appendLog("Le terrain " + ground.getName() + " a disparu!");
        }
        
        // Mettre à jour le fond en fonction du terrain actuel
        if (battlefieldArea != null) {
            applyBattleBackground(battlefieldArea);
        }

        // Vérifier si les monstres peuvent attaquer après terrain
        if (groundEffect.containsKey("monster1_attackAble") && !Boolean.parseBoolean(groundEffect.get("monster1_attackAble"))) {
            appendLog(m1.getName() + " ne peut pas attaquer à cause du terrain!");
            canAttack1 = false;
            a1 = null;
        }
        if (groundEffect.containsKey("monster2_attackAble") && !Boolean.parseBoolean(groundEffect.get("monster2_attackAble"))) {
            appendLog(m2.getName() + " ne peut pas attaquer à cause du terrain!");
            canAttack2 = false;
            a2 = null;
        }

        // Vérifier si les monstres sont K.O. avant les attaques
        if (m1 != null && m1.getCurrentHealth() <= 0) {
            appendLog(m1.getName() + " est K.O.");
            canAttack1 = false;
            a1 = null;
        }
        if (m2 != null && m2.getCurrentHealth() <= 0) {
            appendLog(m2.getName() + " est K.O.");
            canAttack2 = false;
            a2 = null;
        }

        // Effectuer les attaques
        if (a1 != null && a2 != null) {
            // Les deux joueurs attaquent - ordre selon la vitesse
            if (m1.getSpeed() >= m2.getSpeed()) {
                performAttackAndLog(player1, player2, a1);
                if (m2.getCurrentHealth() > 0) {
                    performAttackAndLog(player2, player1, a2);
                } else {
                    appendLog(m2.getName() + " est K.O. !");
                }
            } else {
                performAttackAndLog(player2, player1, a2);
                if (m1.getCurrentHealth() > 0) {
                    performAttackAndLog(player1, player2, a1);
                } else {
                    appendLog(m1.getName() + " est K.O. !");
                }
            }
        } else if (a1 != null) {
            // Seulement le joueur 1 attaque
            performAttackAndLog(player1, player2, a1);
            if (m2.getCurrentHealth() <= 0) {
                appendLog(m2.getName() + " est K.O. !");
            }
        } else if (a2 != null) {
            // Seulement le joueur 2 attaque
            performAttackAndLog(player2, player1, a2);
            if (m1.getCurrentHealth() <= 0) {
                appendLog(m1.getName() + " est K.O. !");
            }
        }
        
        // Mettre à jour le fond si une attaque a changé le terrain
        if (battlefieldArea != null) {
            applyBattleBackground(battlefieldArea);
        }

        pendingAttackP1 = null;
        pendingAttackP2 = null;

        updateMonsterView(viewP1, player1);
        updateMonsterView(viewP2, player2);

        // Vérifier d'abord la fin du combat
        if (!player1.hasUsableMonsters() || !player2.hasUsableMonsters()) {
            String winner = player1.hasUsableMonsters() ? player1.getName() : player2.getName();
            appendLog("Le combat est terminé. Vainqueur: " + winner);
            stopBackgroundMusic();
            showGameOverAlert("Fin du combat", winner + " a gagné!");
            return;
        }

        // Vérifier les K.O. et faire sélectionner un nouveau monstre si nécessaire
        boolean p1NeedsSwitch = player1.getCurrentMonster().getCurrentHealth() <= 0 && player1.hasUsableMonsters();
        boolean p2NeedsSwitch = player2.getCurrentMonster().getCurrentHealth() <= 0 && player2.hasUsableMonsters();

        if (p1NeedsSwitch || p2NeedsSwitch) {
            handlePostKOSwitches(p1NeedsSwitch, p2NeedsSwitch);
            return;
        }

        phase = Phase.P1_CHOOSE;
        appendLog("Nouveau tour. " + player1.getName() + " commence.");
        showActionButtons();
    }

    private void handlePostKOSwitches(boolean p1Needs, boolean p2Needs) {
        if (p1Needs && !p2Needs) {
            showMonsterSwitchChoices(player1, () -> {
                updateMonsterView(viewP1, player1);
                checkGameOver();
            });
        } else if (p2Needs && !p1Needs) {
            showMonsterSwitchChoices(player2, () -> {
                updateMonsterView(viewP2, player2);
                checkGameOver();
            });
        } else {
            // Les deux doivent changer
            showMonsterSwitchChoices(player1, () -> {
                updateMonsterView(viewP1, player1);
                showMonsterSwitchChoices(player2, () -> {
                    updateMonsterView(viewP2, player2);
                    checkGameOver();
                });
            });
        }
    }

    private void checkGameOver() {
        if (!player1.hasUsableMonsters() || !player2.hasUsableMonsters()) {
            String winner = player1.hasUsableMonsters() ? player1.getName() : player2.getName();
            appendLog("Le combat est terminé. Vainqueur: " + winner);
            showGameOverAlert("Fin du combat", winner + " a gagné!");
            return;
        }

        phase = Phase.P1_CHOOSE;
        appendLog("Nouveau tour. " + player1.getName() + " commence.");
        showActionButtons();
    }

    private void showMonsterSwitchChoices(Player player, Runnable onDone) {
        bottomContainer.getChildren().clear();

        Label title = new Label(player.getName() + ", choisissez un monstre:");
        title.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 14;");

        HBox monsterList = new HBox(10);
        monsterList.setAlignment(Pos.CENTER);

        java.util.HashMap<Integer, Integer> indexMap = new java.util.HashMap<>();
        int displayIndex = 0;
        for (int i = 0; i < player.getMonsters().size(); i++) {
            Monster m = player.getMonsters().get(i);
            if (m.getCurrentHealth() > 0 && i != player.getCurrentMonsterIndex()) {
                displayIndex++;
                final int mapIdx = displayIndex;
                final int realIdx = i;

                Button b = new Button(m.getName() + "\n(" + m.getCurrentHealth() + "/" + m.getHealth() + " HP)");
                styleButton(b, "#3c6496", "#5078aa");
                b.setOnAction(e -> {
                    player.setCurrentMonsterIndex(realIdx);
                    appendLog(player.getName() + " envoie " + m.getName());
                    onDone.run();
                });
                monsterList.getChildren().add(b);
                indexMap.put(mapIdx, realIdx);
            }
        }

        bottomContainer.getChildren().addAll(title, monsterList);
    }

    private void performAttackAndLog(Player attacker, Player defender, Attack attack) {
        Monster mAtt = attacker.getCurrentMonster();
        Monster mDef = defender.getCurrentMonster();

        if (mAtt == null || mDef == null || mAtt.getCurrentHealth() <= 0) {
            return;
        }

        java.util.HashMap<String, String> result = attack.performAttack(mAtt, mDef, this);
        if (result.isEmpty()) {
            appendLog(attacker.getName() + " rate son action.");
            return;
        }

        String line = mAtt.getName() + " utilise " + attack.getName() + " sur " + mDef.getName();
        appendLog(line);
        if (result.containsKey("damage")) {
            appendLog("Dégâts: " + result.get("damage") + " (" + result.getOrDefault("effectiveness", "") + ")");
        }
        if (result.get("status") != null) {
            appendLog(result.get("status"));
        }
        if (result.get("ground") != null) {
            appendLog(result.get("ground"));
        }

        if (mDef.getCurrentHealth() <= 0) {
            appendLog(mDef.getName() + " est K.O.");
        }
    }

    private void useItemAndLog(Player player, ObjectMonster item) {
        Monster m = player.getCurrentMonster();
        if (m == null) return;

        String message = item.use(m, this);
        appendLog(player.getName() + " utilise " + item.getName() + ". " + message);
    }

    @Override
    public void startBattle() {
        primaryStage.setTitle("Bataille");
        primaryStage.setWidth(1600);
        primaryStage.setHeight(900);
        primaryStage.centerOnScreen();

        // Lancer la musique au début du combat
        playBackgroundMusic();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e28;");

        battleTitle = new Label("Bataille - Terrain : " + ground.getName());
        battleTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 30; -fx-font-weight: bold;");
        BorderPane.setAlignment(battleTitle, Pos.CENTER);
        BorderPane.setMargin(battleTitle, new Insets(20, 0, 12, 0));
        root.setTop(battleTitle);

        // Centre avec l'image de fond et log à droite
        HBox centerContainer = new HBox(12);
        centerContainer.setPadding(new Insets(12));
        centerContainer.setStyle("-fx-background-color: #1e1e28;");

        battlefieldArea = new BorderPane();
        battlefieldArea.setPadding(new Insets(12));
        applyBattleBackground(battlefieldArea);

        StackPane monsterLayer = new StackPane();
        monsterLayer.setPadding(new Insets(20));

        viewP1 = createMonsterDisplay(player1, "#4682b4");
        viewP2 = createMonsterDisplay(player2, "#ff8c00");

        StackPane.setAlignment(viewP1.container, Pos.CENTER_LEFT);
        StackPane.setAlignment(viewP2.container, Pos.CENTER_RIGHT);
        StackPane.setMargin(viewP1.container, new Insets(150, 600, 0, 0));
        StackPane.setMargin(viewP2.container, new Insets(0, 0, 150, 600));

        monsterLayer.getChildren().addAll(viewP1.container, viewP2.container);

        battlefieldArea.setCenter(monsterLayer);
        HBox.setHgrow(battlefieldArea, Priority.ALWAYS);
        centerContainer.getChildren().add(battlefieldArea);

        // Panneau d'informations à droite
        VBox infoPanel = new VBox(8);
        infoPanel.setPrefWidth(300);
        infoPanel.setStyle("-fx-background-color: #2d2d3c; -fx-padding: 12; -fx-border-color: #4682b4; -fx-border-width: 2; -fx-background-radius: 8;");
        
        Label logTitle = new Label("Historique du combat");
        logTitle.setStyle("-fx-text-fill: #4682b4; -fx-font-size: 16; -fx-font-weight: bold;");
        
        logArea = new javafx.scene.control.TextArea();
        logArea.setStyle("-fx-control-inner-background: #1e1e28; -fx-text-fill: #dcdcdc; -fx-font-size: 11; -fx-font-family: 'Courier New';");
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefRowCount(20);
        logArea.setText("Bienvenue dans le combat!\nLes actions seront affichées ici...\n");
        VBox.setVgrow(logArea, Priority.ALWAYS);
        
        infoPanel.getChildren().addAll(logTitle, logArea);
        centerContainer.getChildren().add(infoPanel);

        root.setCenter(centerContainer);

        // Actions en bas (séparé de l'image)
        bottomContainer = new VBox(10);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setPadding(new Insets(20));
        bottomContainer.setStyle("-fx-background-color: #2d2d3c;");
        hintLabel = new Label();
        hintLabel.setStyle("-fx-text-fill: #dcdcdc; -fx-font-size: 14;");
        root.setBottom(bottomContainer);
        showActionButtons();

        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setScene(scene);
        primaryStage.show();

        phase = Phase.P1_CHOOSE;
        appendLog("Tour de " + player1.getName() + " : choisir une action.");
    }

    private void playBackgroundMusic() {
        try {
            // Chercher le fichier WAV
            File audioFile = null;
            
            File classesFile = new File("classes/com/esiea/pootp/assets/sound/background_sound.wav");
            if (classesFile.exists()) {
                audioFile = classesFile;
            } else {
                File srcFile = new File("src/com/esiea/pootp/assets/sound/background_sound.wav");
                if (srcFile.exists()) {
                    audioFile = srcFile;
                }
            }
            
            if (audioFile == null) {
                System.out.println("[AUDIO] Fichier WAV non trouvé");
                return;
            }
            
            musicPlayer.play(audioFile);
        } catch (Exception e) {
            System.out.println("[AUDIO] Erreur: " + e.getMessage());
        }
    }

    private void stopBackgroundMusic() {
        musicPlayer.stop();
    }
}

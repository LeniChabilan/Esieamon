package com.esiea.pootp.Battle;

import com.esiea.pootp.Player.Player;
import com.esiea.pootp.Attack.Attack;
import com.esiea.pootp.Attack.AttackMonster;
import com.esiea.pootp.Attack.AttackStruggle;
import com.esiea.pootp.Monster.Monster;
import com.esiea.pootp.Object.ObjectMonster;
import com.esiea.pootp.Parser.Parser;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class BattleGUI extends Application {
    private Player player1;
    private Player player2;
    private Parser parser;
    private int teamSize = 3;
    
    // UI Components
    private Stage primaryStage;
    private Label battleLog;
    private ProgressBar hp1Bar;
    private ProgressBar hp2Bar;
    private Label monster1Info;
    private Label monster2Info;
    private VBox actionPanel;
    private ScrollPane logScrollPane;
    private TextArea logArea;
    
    private boolean waitingForAction = false;
    private ActionType selectedAction1 = null;
    private ActionType selectedAction2 = null;
    private Attack selectedAttack1 = null;
    private Attack selectedAttack2 = null;
    private Integer selectedSwitch1 = null;
    private Integer selectedSwitch2 = null;
    private Integer selectedItem1 = null;
    private Integer selectedItem2 = null;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.parser = new Parser();
        
        try {
            parser.parseFile("./src/com/esiea/pootp/Parser/game_data.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        primaryStage.setTitle("Esieamon - Combat de Monstres");
        showSetupScreen();
    }
    
    private void showSetupScreen() {
        VBox setupBox = new VBox(15);
        setupBox.setPadding(new Insets(20));
        setupBox.setAlignment(Pos.CENTER);
        
        Label title = new Label("⚔️ ESIEAMON ⚔️");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.DARKBLUE);
        
        TextField player1Field = new TextField();
        player1Field.setPromptText("Nom du Joueur 1");
        player1Field.setMaxWidth(300);
        
        TextField player2Field = new TextField();
        player2Field.setPromptText("Nom du Joueur 2");
        player2Field.setMaxWidth(300);
        
        Spinner<Integer> teamSizeSpinner = new Spinner<>(1, 6, 3);
        teamSizeSpinner.setMaxWidth(300);
        Label teamSizeLabel = new Label("Taille de l'équipe:");
        
        Button startButton = new Button("Commencer la Bataille");
        startButton.setPrefWidth(200);
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        
        startButton.setOnAction(e -> {
            String name1 = player1Field.getText().trim();
            String name2 = player2Field.getText().trim();
            
            if (name1.isEmpty() || name2.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer les noms des deux joueurs !");
                return;
            }
            
            this.teamSize = teamSizeSpinner.getValue();
            this.player1 = new Player(name1);
            this.player2 = new Player(name2);
            
            showMonsterSelection();
        });
        
        setupBox.getChildren().addAll(title, 
            new Label("Bienvenue dans Esieamon !"), 
            player1Field, player2Field, 
            teamSizeLabel, teamSizeSpinner, 
            startButton);
        
        Scene scene = new Scene(setupBox, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void showMonsterSelection() {
        selectMonstersForPlayer(player1, () -> {
            selectMonstersForPlayer(player2, () -> {
                showBattleScreen();
                startBattleLoop();
            });
        });
    }
    
    private void selectMonstersForPlayer(Player player, Runnable onComplete) {
        VBox selectionBox = new VBox(10);
        selectionBox.setPadding(new Insets(20));
        
        Label title = new Label("Sélection pour " + player.getName());
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        ListView<Monster> monsterList = new ListView<>();
        monsterList.getItems().addAll(parser.getAvailableMonsters());
        monsterList.setCellFactory(lv -> new ListCell<Monster>() {
            @Override
            protected void updateItem(Monster monster, boolean empty) {
                super.updateItem(monster, empty);
                if (empty || monster == null) {
                    setText(null);
                } else {
                    setText(String.format("%s - HP:%d ATK:%d DEF:%d SPD:%d (%d attaques)",
                        monster.getName(), monster.getHealth(), monster.getPower(),
                        monster.getDefense(), monster.getSpeed(), monster.attacks.size()));
                }
            }
        });
        
        Label selectedLabel = new Label("Monstres sélectionnés: 0/" + teamSize);
        Button confirmButton = new Button("Confirmer la sélection");
        confirmButton.setDisable(true);
        
        List<Monster> selectedMonsters = new ArrayList<>();
        
        monsterList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && selectedMonsters.size() < teamSize) {
                Monster selected = monsterList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    Monster copy = parser.getMonsterCopy(selected.getName());
                    if (copy != null) {
                        selectedMonsters.add(copy);
                        selectedLabel.setText("Monstres sélectionnés: " + selectedMonsters.size() + "/" + teamSize);
                        
                        if (selectedMonsters.size() == teamSize) {
                            confirmButton.setDisable(false);
                        }
                    }
                }
            }
        });
        
        confirmButton.setOnAction(e -> {
            player.monsters.addAll(selectedMonsters);
            onComplete.run();
        });
        
        selectionBox.getChildren().addAll(title, 
            new Label("Double-cliquez pour sélectionner un monstre"),
            monsterList, selectedLabel, confirmButton);
        
        Scene scene = new Scene(selectionBox, 700, 500);
        primaryStage.setScene(scene);
    }
    
    private void showBattleScreen() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Top: Monster info
        HBox topBox = createMonsterInfoPanel();
        root.setTop(topBox);
        
        // Center: Battle log
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefRowCount(10);
        logScrollPane = new ScrollPane(logArea);
        logScrollPane.setFitToWidth(true);
        root.setCenter(logScrollPane);
        
        // Bottom: Action panel
        actionPanel = new VBox(10);
        actionPanel.setPadding(new Insets(10));
        root.setBottom(actionPanel);
        
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
    }
    
    private HBox createMonsterInfoPanel() {
        HBox topBox = new HBox(30);
        topBox.setPadding(new Insets(10));
        topBox.setAlignment(Pos.CENTER);
        
        // Player 1 monster
        VBox p1Box = new VBox(5);
        p1Box.setStyle("-fx-background-color: #E3F2FD; -fx-padding: 10; -fx-border-color: #2196F3; -fx-border-width: 2;");
        monster1Info = new Label();
        hp1Bar = new ProgressBar(1.0);
        hp1Bar.setPrefWidth(200);
        hp1Bar.setStyle("-fx-accent: #4CAF50;");
        p1Box.getChildren().addAll(monster1Info, hp1Bar);
        
        Label vsLabel = new Label("⚔️ VS ⚔️");
        vsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        // Player 2 monster
        VBox p2Box = new VBox(5);
        p2Box.setStyle("-fx-background-color: #FFF3E0; -fx-padding: 10; -fx-border-color: #FF9800; -fx-border-width: 2;");
        monster2Info = new Label();
        hp2Bar = new ProgressBar(1.0);
        hp2Bar.setPrefWidth(200);
        hp2Bar.setStyle("-fx-accent: #F44336;");
        p2Box.getChildren().addAll(monster2Info, hp2Bar);
        
        topBox.getChildren().addAll(p1Box, vsLabel, p2Box);
        return topBox;
    }
    
    private void updateMonsterDisplay() {
        Monster m1 = player1.getCurrentMonster();
        Monster m2 = player2.getCurrentMonster();
        
        monster1Info.setText(String.format("%s's %s\nHP: %d/%d", 
            player1.getName(), m1.getName(), m1.getCurrentHealth(), m1.getHealth()));
        hp1Bar.setProgress((double) m1.getCurrentHealth() / m1.getHealth());
        
        monster2Info.setText(String.format("%s's %s\nHP: %d/%d", 
            player2.getName(), m2.getName(), m2.getCurrentHealth(), m2.getHealth()));
        hp2Bar.setProgress((double) m2.getCurrentHealth() / m2.getHealth());
    }
    
    private void addLog(String message) {
        logArea.appendText(message + "\n");
        logArea.setScrollTop(Double.MAX_VALUE);
    }
    
    private void startBattleLoop() {
        updateMonsterDisplay();
        addLog("=== La bataille commence ! ===\n");
        processTurn();
    }
    
    private void processTurn() {
        if (isOver()) {
            displayWinner();
            return;
        }
        
        updateMonsterDisplay();
        
        // Reset selections
        selectedAction1 = null;
        selectedAction2 = null;
        selectedAttack1 = null;
        selectedAttack2 = null;
        selectedSwitch1 = null;
        selectedSwitch2 = null;
        selectedItem1 = null;
        selectedItem2 = null;
        
        // Player 1 chooses action
        showActionChoice(player1, (action, attack, switchIdx, itemIdx) -> {
            selectedAction1 = action;
            selectedAttack1 = attack;
            selectedSwitch1 = switchIdx;
            selectedItem1 = itemIdx;
            
            // Player 2 chooses action
            showActionChoice(player2, (action2, attack2, switchIdx2, itemIdx2) -> {
                selectedAction2 = action2;
                selectedAttack2 = attack2;
                selectedSwitch2 = switchIdx2;
                selectedItem2 = itemIdx2;
                
                // Execute turn
                executeTurn();
            });
        });
    }
    
    private void showActionChoice(Player player, ActionCallback callback) {
        actionPanel.getChildren().clear();
        
        Label prompt = new Label(player.getName() + ", choisissez une action:");
        prompt.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Button attackBtn = new Button("⚔️ Attaquer");
        attackBtn.setPrefWidth(150);
        attackBtn.setOnAction(e -> {
            showAttackChoice(player, attack -> callback.onAction(ActionType.ATTACK, attack, null, null));
        });
        
        Button switchBtn = new Button("🔄 Changer de monstre");
        switchBtn.setPrefWidth(150);
        switchBtn.setOnAction(e -> {
            showMonsterChoice(player, idx -> callback.onAction(ActionType.SWITCH, null, idx, null));
        });
        
        Button itemBtn = new Button("🎒 Utiliser un objet");
        itemBtn.setPrefWidth(150);
        itemBtn.setOnAction(e -> {
            showItemChoice(player, idx -> callback.onAction(ActionType.ITEM, null, null, idx));
        });
        
        HBox buttonBox = new HBox(10, attackBtn, switchBtn, itemBtn);
        buttonBox.setAlignment(Pos.CENTER);
        
        actionPanel.getChildren().addAll(prompt, buttonBox);
    }
    
    private void showAttackChoice(Player player, AttackChoiceCallback callback) {
        actionPanel.getChildren().clear();
        
        Monster currentMonster = player.getCurrentMonster();
        Label prompt = new Label("Choisissez une attaque pour " + currentMonster.getName() + ":");
        prompt.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        VBox attackBox = new VBox(5);
        
        if (!currentMonster.hasAvailableAttacks()) {
            addLog(player.getName() + " n'a plus d'attaques disponibles ! Utilise Lutte.");
            callback.onAttackChosen(new AttackStruggle());
            return;
        }
        
        for (AttackMonster attack : currentMonster.attacks) {
            Button attackBtn = new Button(String.format("%s (Puissance: %d, PP: %d/%d)",
                attack.getName(), attack.getPower(), attack.getNbUses(), attack.getMaxUses()));
            attackBtn.setPrefWidth(350);
            attackBtn.setOnAction(e -> callback.onAttackChosen(attack));
            attackBox.getChildren().add(attackBtn);
        }
        
        actionPanel.getChildren().addAll(prompt, attackBox);
    }
    
    private void showMonsterChoice(Player player, MonsterChoiceCallback callback) {
        actionPanel.getChildren().clear();
        
        Label prompt = new Label("Choisissez un monstre à envoyer au combat:");
        prompt.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        VBox monsterBox = new VBox(5);
        
        for (int i = 0; i < player.monsters.size(); i++) {
            Monster monster = player.monsters.get(i);
            if (monster.getCurrentHealth() > 0 && i != player.currentMonsterIndex) {
                final int index = i;
                Button monsterBtn = new Button(String.format("%s (HP: %d/%d)",
                    monster.getName(), monster.getCurrentHealth(), monster.getHealth()));
                monsterBtn.setPrefWidth(300);
                monsterBtn.setOnAction(e -> callback.onMonsterChosen(index));
                monsterBox.getChildren().add(monsterBtn);
            }
        }
        
        actionPanel.getChildren().addAll(prompt, monsterBox);
    }
    
    private void showItemChoice(Player player, ItemChoiceCallback callback) {
        actionPanel.getChildren().clear();
        
        List<ObjectMonster> inventory = player.getInventory();
        
        if (inventory.isEmpty()) {
            addLog(player.getName() + " n'a pas d'objets !");
            callback.onItemChosen(null);
            return;
        }
        
        Label prompt = new Label("Choisissez un objet à utiliser:");
        prompt.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        VBox itemBox = new VBox(5);
        
        for (int i = 0; i < inventory.size(); i++) {
            final int index = i;
            ObjectMonster item = inventory.get(i);
            Button itemBtn = new Button(item.getName());
            itemBtn.setPrefWidth(300);
            itemBtn.setOnAction(e -> callback.onItemChosen(index));
            itemBox.getChildren().add(itemBtn);
        }
        
        actionPanel.getChildren().addAll(prompt, itemBox);
    }
    
    private void executeTurn() {
        actionPanel.getChildren().clear();
        
        // Perform switches
        if (selectedAction1 == ActionType.SWITCH && selectedSwitch1 != null) {
            switchMonster(player1, selectedSwitch1);
        }
        if (selectedAction2 == ActionType.SWITCH && selectedSwitch2 != null) {
            switchMonster(player2, selectedSwitch2);
        }
        
        // Perform items
        if (selectedAction1 == ActionType.ITEM && selectedItem1 != null) {
            useItem(player1, selectedItem1);
        }
        if (selectedAction2 == ActionType.ITEM && selectedItem2 != null) {
            useItem(player2, selectedItem2);
        }
        
        // Perform attacks
        if (selectedAttack1 != null && selectedAttack2 != null) {
            // Both attack - speed determines order
            if (player1.getCurrentMonster().getSpeed() >= player2.getCurrentMonster().getSpeed()) {
                performAttack(selectedAttack1, player1, player2);
                if (player2.getCurrentMonster().getCurrentHealth() > 0) {
                    performAttack(selectedAttack2, player2, player1);
                }
            } else {
                performAttack(selectedAttack2, player2, player1);
                if (player1.getCurrentMonster().getCurrentHealth() > 0) {
                    performAttack(selectedAttack1, player1, player2);
                }
            }
        } else if (selectedAttack1 != null) {
            performAttack(selectedAttack1, player1, player2);
        } else if (selectedAttack2 != null) {
            performAttack(selectedAttack2, player2, player1);
        }
        
        updateMonsterDisplay();
        
        // Check for KO
        if (player1.getCurrentMonster().getCurrentHealth() <= 0 && player1.hasUsableMonsters()) {
            addLog("\n" + player1.getCurrentMonster().getName() + " est K.O. !");
            showMonsterChoice(player1, idx -> {
                switchMonster(player1, idx);
                checkAndContinue();
            });
        } else if (player2.getCurrentMonster().getCurrentHealth() <= 0 && player2.hasUsableMonsters()) {
            addLog("\n" + player2.getCurrentMonster().getName() + " est K.O. !");
            showMonsterChoice(player2, idx -> {
                switchMonster(player2, idx);
                checkAndContinue();
            });
        } else {
            checkAndContinue();
        }
    }
    
    private void checkAndContinue() {
        Platform.runLater(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            processTurn();
        });
    }
    
    private void performAttack(Attack attack, Player attacker, Player defender) {
        HashMap<String, String> result = attack.performAttack(
            attacker.getCurrentMonster(), defender.getCurrentMonster());
        
        addLog(String.format("\n%s utilise %s sur %s !",
            result.get("attackerName"), result.get("attackName"), result.get("defenderName")));
        addLog(String.format("Cela inflige %s points de dégâts. %s",
            result.get("damage"), result.get("effectiveness")));
        
        updateMonsterDisplay();
    }
    
    private void switchMonster(Player player, int index) {
        player.currentMonsterIndex = index;
        addLog("\n" + player.getName() + " envoie " + player.getCurrentMonster().getName() + " !");
        updateMonsterDisplay();
    }
    
    private void useItem(Player player, int itemIndex) {
        ObjectMonster item = player.getInventory().get(itemIndex);
        addLog("\n" + player.getName() + " utilise " + item.getName() + " !");
        item.use(player.getCurrentMonster());
        player.getInventory().remove(itemIndex);
        updateMonsterDisplay();
    }
    
    private boolean isOver() {
        return !player1.hasUsableMonsters() || !player2.hasUsableMonsters();
    }
    
    private void displayWinner() {
        actionPanel.getChildren().clear();
        
        String winner;
        if (player1.hasUsableMonsters()) {
            winner = player1.getName() + " a gagné la bataille ! 🏆";
        } else if (player2.hasUsableMonsters()) {
            winner = player2.getName() + " a gagné la bataille ! 🏆";
        } else {
            winner = "Match nul !";
        }
        
        addLog("\n=== FIN DE LA BATAILLE ===");
        addLog(winner);
        
        Label winnerLabel = new Label(winner);
        winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        winnerLabel.setTextFill(Color.DARKGREEN);
        
        Button restartBtn = new Button("Nouvelle Bataille");
        restartBtn.setOnAction(e -> {
            showSetupScreen();
        });
        
        Button quitBtn = new Button("Quitter");
        quitBtn.setOnAction(e -> Platform.exit());
        
        HBox buttonBox = new HBox(10, restartBtn, quitBtn);
        buttonBox.setAlignment(Pos.CENTER);
        
        VBox endBox = new VBox(20, winnerLabel, buttonBox);
        endBox.setAlignment(Pos.CENTER);
        
        actionPanel.getChildren().add(endBox);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Callback interfaces
    @FunctionalInterface
    interface ActionCallback {
        void onAction(ActionType action, Attack attack, Integer switchIdx, Integer itemIdx);
    }
    
    @FunctionalInterface
    interface AttackChoiceCallback {
        void onAttackChosen(Attack attack);
    }
    
    @FunctionalInterface
    interface MonsterChoiceCallback {
        void onMonsterChosen(int index);
    }
    
    @FunctionalInterface
    interface ItemChoiceCallback {
        void onItemChosen(Integer index);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

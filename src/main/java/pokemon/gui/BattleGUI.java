package pokemon.gui;

import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import pokemon.datastore.PokemonDataStore;
import pokemon.datastore.PokemonTransformer;
import pokemon.datastore.WinStatsStore;
import pokemon.domain.BattleUtils;
import pokemon.domain.Inventory;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonSpeciesLoader;
import pokemon.domain.Usable;
import pokemon.domain.items.Item;
import pokemon.domain.moves.Move;

/**
 * GUI koji omogućava odvijanje toka borbe (uključujući korisničku interakciju) između korisnikovog i "AI"-jevog Pokemona.
 */
public class BattleGUI extends Application {

    private final Pokemon player;
    private final Inventory inventory;
    private final Pokemon enemy;
    private final PokemonSpeciesLoader speciesLoader;
    private final PokemonDataStore pokemonDataStore;
    private final WinStatsStore winStatsStore;
    private final PokemonTransformer pokemonTransformer;
    private final StringBuilder battleLog;
    private final Text text;
    private ProgressBar playerHpBar, enemyHpBar;
    private Stage primaryStage;
    private final Random random;


    public BattleGUI(Pokemon player, PokemonSpeciesLoader speciesLoader, PokemonDataStore pokemonDataStore, WinStatsStore winStatsStore, PokemonTransformer pokemonTransformer) {
        this.player = player;
        this.speciesLoader = speciesLoader;
        this.pokemonDataStore = pokemonDataStore;
        this.winStatsStore = winStatsStore;
        this.pokemonTransformer = pokemonTransformer;
        this.enemy = BattleUtils.setUpEnemyPokemon(this.speciesLoader);
        this.inventory = new Inventory();
        this.inventory.generateInventory();
        this.random = new Random();
        this.battleLog = new StringBuilder("BATTLE START! \n");
        this.text = new Text(battleLog.toString());
    }

    /**
     * Kreira komponente za aplikaciju: <ul>
     * <li>dva ImageView-a za pregled igračevog i neprijateljskog Pokemona, zajedno sa Labelima i
     * ProgressBarovima (prikaz HP-a)</li>
     * <li>TextFlow, sa logom toka borbe, koji se ažurira po svakom potezu.</li>
     * <li>MenuButton, koji omogućava korisniku odabir vještine ili predmeta za trenutni potez.</li>
     * </ul>
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        /*
         --------------
           UI ELEMENTI
         --------------
        */

        ImageView playerImageView = new ImageView();
        GUIUtils.setImageFromURL(playerImageView, player.getImageUrl());
        Label playerLabel = new Label(player.getNickname());
        playerHpBar = new ProgressBar(player.getHealth());
        playerHpBar.prefWidth(90);

        ImageView enemyImageView = new ImageView();
        GUIUtils.setImageFromURL(enemyImageView, enemy.getImageUrl());
        Label enemyLabel = new Label(enemy.getNickname());
        enemyHpBar = new ProgressBar(enemy.getHealth());
        enemyHpBar.prefWidth(90);

        TextFlow textFlow = new TextFlow(text);
        textFlow.setPrefHeight(100);
        textFlow.setPrefWidth(400);
        textFlow.setLineSpacing(1.15);

        MenuButton menuButton = new MenuButton("Choose Action...");

        // MenuButton se popunjava mogućim korisničkim odabirima vještina i (ako su generirani) predmeta.
        for (Move move : player.getMoves()) {
            final MenuItem menuItem = new MenuItem(move.getName());
            menuButton.getItems().add(menuItem);
            menuItem.setOnAction(e -> playTurn(move));
        }
        menuButton.getItems().add(new SeparatorMenuItem());
        for (Item item : inventory.getItems()) {
            final MenuItem menuItem = new MenuItem(item.getName());
            menuButton.getItems().add(menuItem);
            menuItem.setOnAction(e -> {
                playTurn(item);
                if (item.getQuantity() == 0) {
                    menuItem.setDisable(true);
                }
            });
        }

        updateHealthBars();

        /*
         --------------
             LAYOUT
         --------------
        */

        VBox playerImageViewVbox = new VBox(10, playerImageView, playerLabel, playerHpBar);
        VBox enemyImageViewVbox = new VBox(10, enemyImageView, enemyLabel, enemyHpBar);

        playerImageViewVbox.setAlignment(Pos.CENTER);
        enemyImageViewVbox.setAlignment(Pos.CENTER);

        playerImageViewVbox.setPrefHeight(150);
        enemyImageViewVbox.setPrefHeight(150);

        SplitPane splitPane = new SplitPane(playerImageViewVbox, enemyImageViewVbox);
        splitPane.setDividerPosition(0, 0.5);
        splitPane.setBorder(new Border(new BorderStroke(Color.GRAY,
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // https://stackoverflow.com/a/28039740
        ScrollPane scrollPane = new ScrollPane(textFlow);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setBorder(new Border(new BorderStroke(Color.GRAY,
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        BorderPane borderPane = new BorderPane(scrollPane);
        borderPane.setTop(splitPane);
        borderPane.setBottom(menuButton);
        BorderPane.setAlignment(splitPane, Pos.CENTER);
        BorderPane.setAlignment(scrollPane, Pos.CENTER);
        BorderPane.setAlignment(menuButton, Pos.CENTER);
        BorderPane.setMargin(scrollPane, new Insets(10));
        BorderPane.setMargin(menuButton, new Insets(20));

        Scene scene = new Scene(borderPane, 640, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Battle");
        primaryStage.show();

    }

    /**
     * Simulira odvijanje poteza. Oba Pokemona koriste vještinu (korisnikov može i predmet),
     * te se u skladu sa tim ažuriraju log borbe, te HP barovi.
     *
     * @param usable korisnikov odabir (vještina, predmet), koji će se koristiti u trenutnom potezu
     */
    private void playTurn(Usable usable) {
        String effectMessage = usable.use(player, enemy);
        updateBattleLog(effectMessage);
        if (endOnDefeat()) {
            return;
        }
        useEnemyMove();
        endOnDefeat();
    }

    /**
     * Metoda koja generira izbor vještine neprijateljskom Pokemonu.
     */
    private void useEnemyMove() {
        int enemyMoveChoice = random.nextInt(enemy.getMoves().size());
        updateBattleLog(enemy.useMove(enemyMoveChoice, player));
    }

    /**
     * Metoda za ažuriranje loga o toku borbe.
     *
     * @param message linija teksta, kojom će se nadopuniti log
     */
    private void updateBattleLog(String message) {
        battleLog.append(message).append("\n");
        text.setText(battleLog.toString());
    }

    /**
     * Ažurira HP barove oba Pokemona.
     */
    private void updateHealthBars() {
        playerHpBar.setProgress((double) player.getHealth() / player.getMaxHealth());
        enemyHpBar.setProgress((double) enemy.getHealth() / enemy.getMaxHealth());
    }

    /**
     * Kontrolira šta će se desiti po porazu nekog od Pokemona. Mogući scenariji su:
     * <ul>
     * <li>gube oba Pokemona</li>
     * <li>gube igračev Pokemon</li>
     * <li>gube neprijateljski Pokemon</li>
     * </ul>
     * U potonjem slučaju se ažurira vrijednost ukupnih korisnikovih pobjeda u tablici Stats.
     *
     * @return boolean vrijednost koja naznačava da li aplikacija treba završiti borbu
     */
    private boolean endOnDefeat() {
        updateHealthBars();
        if (player.isDefeated() || enemy.isDefeated()) {
            if (player.isDefeated() && enemy.isDefeated()) {
                GUIUtils.displayAlert("Both Pokemon have lost! Ending the battle...", Alert.AlertType.INFORMATION);
            } else if (enemy.isDefeated()) {
                GUIUtils.displayAlert("You've won! Ending the battle...", Alert.AlertType.INFORMATION);
                winStatsStore.incrementWinCount();
            } else if (player.isDefeated()) {
                GUIUtils.displayAlert("You've lost! Ending the battle...", Alert.AlertType.INFORMATION);
            }
            pokemonDataStore.update(pokemonTransformer.convert(player, false));
            new PokemonViewGUI(pokemonDataStore,
                    new PokemonTransformer(speciesLoader),
                    winStatsStore, speciesLoader).start(
                    new Stage());
            primaryStage.close();
            return true;
        }
        return false;
    }
}

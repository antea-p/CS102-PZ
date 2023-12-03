package pokemon.gui;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import pokemon.datastore.*;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonSpeciesLoader;

import java.util.List;

/**
 * GUI prozor koji s lijeve strane pruža pregled dosad kreiranih Pokemona (smještenih u databazi), a sa desne brojač pobjeda, te dugmad za:<ul>
 * <li>dodavanje (Adopt)</li>
 * <li>brisanje (Release)</li>
 * <li>ozdravljanje (Heal) Pokemona</li>
 * <li>započinjanje borbe (Battle)</li>
 * </ul>
 */
public class PokemonViewGUI extends Application {

    private final PokemonDataStore pokemonDataStore;
    private final PokemonTransformer pokemonTransformer;
    private final WinStatsStore winStatsStore;
    private final PokemonSpeciesLoader speciesLoader;

    public PokemonViewGUI(PokemonDataStore pokemonDataStore, PokemonTransformer pokemonTransformer,
                          WinStatsStore winStatsStore, PokemonSpeciesLoader speciesLoader) {
        this.pokemonDataStore = pokemonDataStore;
        this.pokemonTransformer = pokemonTransformer;
        this.winStatsStore = winStatsStore;
        this.speciesLoader = speciesLoader;
    }

    /**
     * Klasa za podešavanje izgleda ćelija u ListView-u.
     */
    static class PokemonListCell extends ListCell<Pokemon> {

        final ImageView imageView = new ImageView();

        /**
         * Crta sadržaj ćelije. Svaka ćelija ima sliku Pokemona i posebno
         * formatirani String oblika: IME_POKEMONA  -  TRENUTNI_HP/MAKSIMALNI_HP.
         *
         * @see <a href="https://stackoverflow.com/questions/33592308/javafx-how-to-put-imageview-inside-listview">How to put ImageView inside ListView</a>
         */
        @Override
        protected void updateItem(Pokemon item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                String pokemonViewString = String.format("%s  -  %d/%d", item.getNickname(), item.getHealth(),
                        item.getMaxHealth());
                setText(pokemonViewString);
                GUIUtils.setImageFromURL(imageView, item.getImageUrl());
                setGraphic(imageView);
            }
        }
    }

    /**
     * Kreira komponente za aplikaciju: <ul>
     * <li>ListView za pregled Pokemona; sadržaj odgovara onima koji se nalaze u databazi</li>
     * <li>dugmad: Adopt za kreiranje novog Pokemona, Release za brisanje postojećeg Pokemona, Heal
     * za njegovo ozdravljanje, i Battle za početak borbe</li>
     * <li>label Wins, sa brojem pobjeda koje je korisnik ostvario</li>
     * </ul> dugmad
     */
    @Override
    public void start(Stage primaryStage) {

    /*
      --------------
         LISTVIEW
      --------------
     */
        ListView<Pokemon> listView = new ListView<>();
        listView.setPrefWidth(450);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ObservableList<Pokemon> transformedPokemonList = getTransformedPokemonList();

        listView.setItems(transformedPokemonList);
        listView.setCellFactory(e -> new PokemonListCell()); // podešava se custom izgled ćelija

    /*
       --------------
       DUGMAD I LABEL
       --------------
    */

        Button adoptBtn = new Button("Adopt");
        Button battleBtn = new Button("Battle");
        Button releaseBtn = new Button("Release");
        Button healBtn = new Button("Heal");

        // Kako bi sva dugmad bila uniformne širine. Izvor:
        // https://docs.oracle.com/javase/8/javafx/layout-tutorial/size_align.htm
        adoptBtn.setMaxWidth(Double.MAX_VALUE);
        battleBtn.setMaxWidth(Double.MAX_VALUE);
        releaseBtn.setMaxWidth(Double.MAX_VALUE);
        healBtn.setMaxWidth(Double.MAX_VALUE);

        Label winCountsLbl = new Label();
        // Ažurira se ukupni broj korisnikovih pobjeda (playerWins u tablici Stats).
        // U slučaju neuspjeha, prikazuje se: Win Counts: ---
        try {
            winCountsLbl.setText("Win Counts: " + winStatsStore.getWinCount());
        } catch (HibernateException | IllegalArgumentException winEx) {
            GUIUtils.displayAlert("An error has occured while reading player's win count from database!");
            winEx.printStackTrace();
            winCountsLbl.setText("Win Counts: ---");
        }

    /*
     --------------
     EVENT HANDLERI
     --------------
     */

        // Zatvara trenutni prozor i otvara prozor za posvajanje novog Pokemona.
        adoptBtn.setOnAction(e -> {
                    new AdoptionGUI(pokemonDataStore, winStatsStore).start(new Stage());
                    primaryStage.close();
                }
        );

        // Ozdravlja se Pokemon trenutno izabran u ListView-u, do punog HP-a.
        healBtn.setOnAction(e -> {
            Pokemon pokemon = listView.getSelectionModel().getSelectedItem();
            if (pokemon != null) {
                try {
                    pokemonDataStore.heal(pokemon.getId());
                    pokemon.setHealth(pokemon.getMaxHealth());
                    listView.setItems(getTransformedPokemonList()); // ažuriranje ListView-a
                } catch (HibernateException healEx) {
                    GUIUtils.displayAlert("Could not heal Pokemon!");
                    healEx.printStackTrace();
                }
            }
        });

        // Briše zapis u tablicama Pokemon i PokemonMove, koji odgovara Pokemonu trenutno izabranom u ListView-u.
        releaseBtn.setOnAction(e -> {
            Pokemon pokemon = listView.getSelectionModel().getSelectedItem();
            if (pokemon != null) {
                try {
                    PokemonData pokemonData = pokemonTransformer.convert(pokemon, true);
                    // Zbog ograničenja u vidu foreign key-a, prvo se brišu vještine iz PokemonMove tablice.
                    for (PokemonMoveData moveData : pokemonData.getMovesList()) {
                        pokemonDataStore.delete(moveData.getPokemonId());
                    }
                    pokemonDataStore.delete(pokemonData);
                    listView.setItems(getTransformedPokemonList()); // ažuriranje ListView-a
                } catch (HibernateException deleteEx) {
                    GUIUtils.displayAlert("Failed to delete the Pokemon!");
                    deleteEx.printStackTrace();
                }
            }
        });

        // Otvara prozor za borbu.

        battleBtn.setOnAction(e -> {
            Pokemon player = listView.getSelectionModel().getSelectedItem();
            if (player != null) {
                if (player.getHealth() > 0) {
                    new BattleGUI(player, speciesLoader, pokemonDataStore, winStatsStore, pokemonTransformer).start(new Stage());
                    primaryStage.close();
                } else {
                    GUIUtils.displayAlert("Your Pokemon's health has 0 HP!", Alert.AlertType.WARNING);
                }
            }
        });


    /*
     --------------
         LAYOUT
     --------------
    */
        VBox vbox = new VBox(10, adoptBtn, battleBtn, releaseBtn, healBtn, winCountsLbl);
        VBox.setMargin(winCountsLbl, new Insets(10, 0, 0, 0));
        vbox.setAlignment(Pos.BASELINE_CENTER);

        HBox hbox = new HBox(20, listView, vbox);

        BorderPane borderPane = new BorderPane(hbox);
        // Margine oko HBox objekta, radi ljepšeg izgleda
        BorderPane.setMargin(hbox, new Insets(20));

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pokemon View");
        primaryStage.show();
    }

    /**
     * Pomoćna metoda za dobivanje liste objekata mapiranih iz PokemonData u Pokemon. Korisna za
     * podešavanje sadržaja ListView-a.
     *
     * @return ObservableList Pokemon objekata.
     */
    private ObservableList<Pokemon> getTransformedPokemonList() {
        ObservableList<Pokemon> transformedPokemonList = FXCollections.observableArrayList();
        try {
            List<PokemonData> pokemonDataList = pokemonDataStore.getPokemonData();
            transformedPokemonList = FXCollections.observableArrayList();
            for (PokemonData pokemonData : pokemonDataList) {
                transformedPokemonList.add(pokemonTransformer.convert(pokemonData));
            }
        } catch (HibernateException getPokemonDataEx) {
            GUIUtils.displayAlert("Failed to retrieve Pokemon objects from the database!");
        }
        return transformedPokemonList;
    }
}

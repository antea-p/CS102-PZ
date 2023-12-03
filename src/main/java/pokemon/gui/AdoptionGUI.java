package pokemon.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.hibernate.HibernateException;
import org.jsoup.Jsoup;
import pokemon.datastore.PokemonData;
import pokemon.datastore.PokemonDataStore;
import pokemon.datastore.PokemonTransformer;
import pokemon.datastore.WinStatsStore;
import pokemon.domain.BattleUtils;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonSpecies;
import pokemon.domain.PokemonSpeciesLoader;
import pokemon.domain.PokemonType;
import pokemon.domain.moves.Move;
import pokemon.web.WebSpeciesLoader;

/**
 * GUI aplikacija, preko koje se pokreće Pokemon igra. Omogućava korisniku da posvoji, tj. kreira
 * Pokemona, tako što prođe kroz sljedeće korake:
 * <ol>
 * <li>odabere tip vrste (FIRE, NORMAL, WATER, GRASS, DARK, PSYCHIC... puna lista se nalazi u enumu {@link pokemon.domain.PokemonType PokemonType}</li>
 * <li>odabere vrstu Pokemona - lista dostupnih odabira bazirana je na tipu vrste</li>
 * <li>(opcionalno) promijeni defaultno ime Pokemona (koje je jednako nazivu vrste)
 * <li>pritisne Play</li>
 * </ol>
 */

public class AdoptionGUI extends Application {

    private final PokemonSpeciesLoader speciesLoader = new WebSpeciesLoader(
            () -> Jsoup.connect("https://pokemondb.net/pokedex/all").get());
    private final PokemonTransformer transformer = new PokemonTransformer(speciesLoader);
    private final PokemonDataStore pokemonDataStore;
    private final WinStatsStore winStatsStore;

    public AdoptionGUI(PokemonDataStore pokemonDataStore, WinStatsStore winStatsStore) {
        this.pokemonDataStore = pokemonDataStore;
        this.winStatsStore = winStatsStore;
    }

    /**
     * Kreira komponente za aplikaciju: <ul>
     * <li>ImageView za prikaz slike Pokemona (kao pixel sprite); slika se pojavljuje tek po odabiru
     * vrste</li>
     * <li>TextField za prikaz i unos imena Pokemona; po defaultu poprima vrijednost naziva trenutno
     * odabrane vrste Pokemona</li>
     * <li>Type ComboBox za odabir tipa vrste (npr. NORMAL) </li>
     * <li>Species ComboBox za odabir vrste Pokemona (npr. Meowth)</li>
     * <li>Play dugme za pokretanje PokemonViewGUI prozora; može se kliknuti tek kada program
     * raspolaže
     * svim informacijama potrebnim za dodavanje Pokemona u databazu!</li>
     * </ul>
     */
    @Override
    public void start(Stage primaryStage) {
        TreeMap<Integer, PokemonSpecies> pokemonSpecies = speciesLoader.getPokemonSpecies();

        ImageView imageView = new ImageView();

        Label nickLabel = new Label("Nickname");
        TextField tf = new TextField();

    /*
      -----------------
        TYPE COMBOBOX
      -----------------
     */
        Label typeLabel = new Label("Type");
        ComboBox<PokemonType> typeComboBox = new ComboBox<>();
        typeComboBox.setPrefWidth(150);
        // Alfabetsko sortiranje tipova Pokemona, radi lakše navigacije korinika.
        List<PokemonType> sortedPokemonTypes = Stream.of(PokemonType.values())
                .sorted(Comparator.comparing(Enum::toString))
                .collect(Collectors.toList());

        typeComboBox.getItems().setAll(sortedPokemonTypes);

        // Kontrolira na koji će se način korisniku prikazivati PokemonType objekti.
        typeComboBox.setConverter(new StringConverter<PokemonType>() {
            @Override
            // Veliko početno slovo, sva ostala slova mala. Tako će izgledati ljepše u odnosu
            // na uobičajenu reprezentaciju enum vrijednosti.
            public String toString(PokemonType object) {
                String typeName = object.toString();
                return typeName.charAt(0) + typeName.substring(1).toLowerCase();
            }

            @Override
            public PokemonType fromString(String string) {
                return null;
            }
        });

    /*
      -----------------
      SPECIES COMBOBOX
      -----------------
     */
        Label speciesLabel = new Label("Species");
        ComboBox<PokemonSpecies> speciesComboBox = new ComboBox<>();
        speciesComboBox.setDisable(true);
        speciesComboBox.setPrefWidth(150);

        // https://stackoverflow.com/questions/19242747/javafx-editable-combobox-showing-tostring-on-item-selection
        speciesComboBox.setConverter(new StringConverter<PokemonSpecies>() {

            @Override
            public String toString(PokemonSpecies object) {
                return object.getName();
            }

            @Override
            public PokemonSpecies fromString(String string) {
                return null;
            }
        });

        // Isprva je Play dugme onemogućeno, dok ne budu dostupni svi podaci koje su potrebni za kreiranje Pokemona.
        Button playButton = new Button("Play!");
        playButton.setDisable(true);

    /*
      -----------------
        EVENT HANDLERI
      -----------------
    */

        /*
        Kad korisnik odabere tip, kreira se filtrirana lista Pokemon vrsta tog tipa. Npr. ako je tip
        GRASS, lista će sadržavati SAMO vrste Pokemona tipa GRASS (Bulbasaur, Chikorita...). Isto tako,
        lista se alfabetski sortira po imenu vrste.
        */
        typeComboBox.setOnAction(event -> {
            PokemonType selectedType = typeComboBox.getSelectionModel().getSelectedItem();

            ObservableList<PokemonSpecies> filteredSpecies = pokemonSpecies.values().stream()
                    .filter(species -> species.getType() == selectedType)
                    .sorted(Comparator.comparing(PokemonSpecies::getName))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            speciesComboBox.getItems().setAll(filteredSpecies);

          /*
          Odabire se prva vrsta tog tipa; time se sprečava i NullPointException u slučaju da korisnik:
          1) odabere tip, 2) odabere vrstu, 3) promijeni tip, 4) klikne Play bez prethodne promjene vrste
           */
            speciesComboBox.getSelectionModel().selectFirst();
            speciesComboBox.setDisable(false);
        });

        /*
        Kad korisnik odabere vrstu, njen naziv se podešava kao defaultno ime Pokemona u okviru TextField-a. Isto tako,
        dohvaća se URL slike date vrste, i podešava se kao slika za ImageView. Konačno, omogućuje se Play dugme, tj.
        može se kliknuti na njega.
         */
        speciesComboBox.setOnAction(event -> {
            PokemonSpecies selectedSpecies = speciesComboBox.getSelectionModel().getSelectedItem();
            tf.setText(selectedSpecies.getName());
            typeComboBox.getSelectionModel().select(selectedSpecies.getType());
            GUIUtils.setImageFromURL(imageView, selectedSpecies.getImageUrl());
            playButton.setDisable(false);
        });

        /*
        Kreira Pokemona od datih vrijednosti u GUI komponentama (imena, vrste, tipa vrste, slike, HP-a vrste), i smješta ga u tablicu Pokemon (kao PokemonData objekat).
        Zatim u PokemonMove tablicu dodaje listu njegovih vještina, koje se nasumično generiraju. Na kraju, trenutni prozor se zatvara, a otvara novi, za pregled svih Pokemona
        koji su dotad dodani u databazu.
         */
        playButton.setOnAction(event -> {
            PokemonSpecies species = speciesComboBox.getSelectionModel().getSelectedItem();
            // Provjerava da li je null kako bi se spriječio NullPointerException u slučaju da
            // korisnik: 1) odabere tip, 2) odabere vrstu, 3) promijeni tip, 4) NE promijeni vrstu
            if (species != null) {
                try {
                    ArrayList<Move> movesList = BattleUtils.generateMoves();
                    PokemonData pokemonData = transformer.convert(new Pokemon(tf.getText().trim(), species.getHp(),
                            species.getHp(), species.getType(), species, new ArrayList<>()), false);
                    // add metoda vraća id dodanog PokemonData objekta
                    int pokemonId = pokemonDataStore.add(pokemonData);

                    for (Move move : movesList) {
                        pokemonDataStore.add(transformer.convert(pokemonId, move));
                    }

                    new PokemonViewGUI(pokemonDataStore, new PokemonTransformer(speciesLoader),
                        winStatsStore, speciesLoader).start(
                        new Stage());
                    primaryStage.close();
                } catch (HibernateException addEx) {
                    GUIUtils.displayAlert("Could not add Pokemon and/or its moves to database!");
                    addEx.printStackTrace();
                } catch (IllegalArgumentException nicknameTooShortOrTooLongEx) {
                    GUIUtils.displayAlert(
                        "Pokemon nickname should contain between 3 and 64 characters!",
                        Alert.AlertType.WARNING);
                    nicknameTooShortOrTooLongEx.printStackTrace();
                }

            }
        });

    /*
      -----------------
           LAYOUT
      -----------------
     */

        HBox hbox = new HBox(playButton);
        hbox.setPadding(new Insets(15));
        hbox.setAlignment(Pos.CENTER);

        HBox imageViewHBox = new HBox(15, imageView);
        imageViewHBox.setPadding(new Insets(15));
        imageViewHBox.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        gridPane.add(imageViewHBox, 1, 0);
        gridPane.add(nickLabel, 0, 1);
        gridPane.add(tf, 1, 1);
        gridPane.add(typeLabel, 0, 2);
        gridPane.add(typeComboBox, 1, 2);
        gridPane.add(speciesLabel, 0, 3);
        gridPane.add(speciesComboBox, 1, 3);
        gridPane.add(hbox, 1, 5);

        imageViewHBox.setPrefHeight(128);

        gridPane.setAlignment(Pos.CENTER);
        // gridPane.setGridLinesVisible(true);
        BorderPane borderPane = new BorderPane(gridPane);

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setTitle("Pokemon Adoption");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

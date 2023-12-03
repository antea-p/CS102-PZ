package pokemon.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jsoup.Jsoup;
import pokemon.datastore.PokemonData;
import pokemon.datastore.PokemonDataStore;
import pokemon.datastore.PokemonMoveData;
import pokemon.datastore.PokemonTransformer;
import pokemon.datastore.StatsData;
import pokemon.datastore.WinStatsStore;
import pokemon.domain.PokemonSpeciesLoader;
import pokemon.web.WebSpeciesLoader;

/**
 * Main klasa Pokemon projekta. Pokreće inicijalizaciju i konfiguraciju databaze, te starta instancu PokemonViewGUI klase.
 */
public class MainGUI extends Application {

    private PokemonDataStore pokemonDataStore;
    private WinStatsStore winStatsStore;

    /**
     * Metoda za inicijalizaciju i konfiguraciju Hibernate databaze.
     */
    private void initDatabase() {
        try {
            Configuration config = new Configuration();
            config.addAnnotatedClass(PokemonData.class);
            config.addAnnotatedClass(PokemonMoveData.class);
            config.addAnnotatedClass(StatsData.class);
            SessionFactory factory = config.buildSessionFactory();

            pokemonDataStore = new PokemonDataStore(factory);
            winStatsStore = new WinStatsStore(factory);
        } catch (HibernateException initEx) {
            GUIUtils.displayAlert("Failed to configure the database!");
            initEx.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starta PokemonViewGUI.
     */
    @Override
    public void start(Stage primaryStage) {
        initDatabase();
        PokemonSpeciesLoader speciesLoader = new WebSpeciesLoader(
                () -> Jsoup.connect("https://pokemondb.net/pokedex/all").get());
        new PokemonViewGUI(pokemonDataStore, new PokemonTransformer(speciesLoader),
                winStatsStore, speciesLoader).start(new Stage());

    }
}

package pokemon.domain;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import pokemon.domain.moves.Bubble;
import pokemon.domain.moves.Ember;
import pokemon.domain.moves.Move;
import pokemon.domain.moves.Splash;
import pokemon.domain.moves.Tackle;
import pokemon.domain.moves.TakeDown;
import pokemon.domain.moves.VineWhip;

/**
 * Klasa sa pomoćnim metodama koje se mogu koristiti za pripremu borbe.
 */
public class BattleUtils {

    private static final Random random = new Random();

    private static final Move[] RANDOM_MOVES = new Move[]{
        new Bubble(), new Ember(), new Splash(),
        new Tackle(), new TakeDown(), new VineWhip()
    };

    /**
     * Pomoćna metoda za generiranje liste nasumičnih vještina. Lista nema duplikate, i sadrži
     * <b>do</b> 4 vještine.
     *
     * @return ArrayList Move objekata, pročišćena od duplikata
     */
    public static ArrayList<Move> generateMoves() {
        int choice;
        ArrayList<Move> moves = new ArrayList<>();
        Move generatedMove;
        for (int i = 0; i < 4; i++) {
            choice = random.nextInt(RANDOM_MOVES.length);
            generatedMove = RANDOM_MOVES[choice];
            moves.add(generatedMove);
        }
        return (ArrayList<Move>) moves.stream().distinct().collect(Collectors.toList());
    }

    public static Pokemon setUpEnemyPokemon(PokemonSpeciesLoader speciesLoader) {
        ArrayList<PokemonSpecies> pokemonSpecies = new ArrayList<>(speciesLoader.getPokemonSpecies().values());
        int speciesChoice = random.nextInt(pokemonSpecies.size());
        return setUpPokemon(pokemonSpecies.get(speciesChoice));
    }

    private static Pokemon setUpPokemon(PokemonSpecies pokemonSpecies) {

        ArrayList<Move> moves = BattleUtils.generateMoves();
        return new Pokemon(pokemonSpecies.getName(), pokemonSpecies.getHp(), pokemonSpecies.getHp(),
                pokemonSpecies.getType(), pokemonSpecies, moves);
    }
}

package pokemon.datastore;

import pokemon.domain.Pokemon;
import pokemon.domain.PokemonSpecies;
import pokemon.domain.PokemonSpeciesLoader;
import pokemon.domain.moves.*;

import java.util.*;

/**
 * <b>PokemonTransformer</b> predstavlja klasu koja sadrži metode za pretvaranje objekata
 * entitetskih, kao i POJO klasa. Podržane su sljedeće konverzije:
 * <ul>
 *   <li>PokemonData &rarr; Pokemon</li>
 *   <li>Pokemon &rarr; PokemonData</li>
 *   <li>Move &rarr; PokemonMoveData</li>
 *   <li>PokemonMoveData &rarr; Move</li>
 * </ul>
 */
public class PokemonTransformer {
    private final TreeMap<Integer, PokemonSpecies> speciesMap;
    private final static Map<String, Move> movesMap = new HashMap<>();

    public PokemonTransformer(PokemonSpeciesLoader speciesLoader) {
        speciesMap = speciesLoader.getPokemonSpecies();

        movesMap.put("Tackle", new Tackle());
        movesMap.put("Bubble", new Bubble());
        movesMap.put("Ember", new Ember());
        movesMap.put("Splash", new Splash());
        movesMap.put("Take Down", new TakeDown());
        movesMap.put("Vine Whip", new VineWhip());
    }

    /**
     * Transformira PokemonData objekat u Pokemon. Dobiveni objekat moguće je koristiti za sve
     * operacije koje se ne oslanjaju na CRUD operacije, tj. ne pozivaju metode klase
     * PokemonDataStore.
     *
     * @param pokemonData PokemonData kojeg se treba transformirati
     * @return Pokemon objekat
     */
    public Pokemon convert(PokemonData pokemonData) {
        PokemonSpecies pokemonSpecies = speciesMap.get(pokemonData.getSpeciesNumber());

        List<PokemonMoveData> moveDataList = pokemonData.getMovesList();
        ArrayList<Move> moveList = new ArrayList<>();
        for (PokemonMoveData moveData : moveDataList) {
            moveList.add(movesMap.get(moveData.getMove()));
        }

        return new Pokemon(pokemonData.getId(), pokemonData.getNickname(),
                pokemonData.getHealth(), pokemonData.getMaxHealth(), pokemonSpecies.getType(),
                pokemonSpecies, moveList);
    }

    /**
     * Transformira Pokemon objekat u PokemonData objekat. Dobiveni objekat moguće je koristiti kao
     * argument kod različitih metoda PokemonDataStore.
     *
     * @param pokemon        Pokemon kojeg se treba transformirati
     * @param transformMoves da li transformirati vještine. Ako je false, vještine će biti nevidljive Hibernateu, što
     *                       sprječava potencijalnu duplikaciju vještina, npr. u slučaju da korisnik odluči boriti se istim
     *                       Pokemonom više puta. true opcija je korisna kod brisanja Pokemona, jer osigurava da će biti izbrisani
     *                       i Pokemon i odgovarajuće vještine.
     * @return PokemonData objekat
     */
    public PokemonData convert(Pokemon pokemon, boolean transformMoves) {
        List<PokemonMoveData> moveDataList = new ArrayList<>();
        if (transformMoves) {
            ArrayList<Move> moveList = pokemon.getMoves();
            moveDataList = new ArrayList<>();
            for (Move move : moveList) {
                moveDataList.add(convert(pokemon.getId(), move));
            }
        }

        return new PokemonData(pokemon.getId(), pokemon.getNickname(),
                pokemon.getHealth(), pokemon.getMaxHealth(),
                pokemon.getSpecies().getId(), moveDataList);
    }

    /**
     * Transformira pojedinačni objekat POJO klase Move u objekat entitetske klase PokemonMoveData.
     *
     * @param pokemonId id Pokemona kojem pripada data vještina. Odgovara vrijednosti atributa id u
     *                  tablici Pokemon.
     * @param move      Objekat koji proširuje apstraktnu klasu Move, npr. Splash i Ember.
     * @return PokemonMoveData objekat, koji sadrži ID Pokemona te naziv vještine
     */
    public PokemonMoveData convert(int pokemonId, Move move) {
        return new PokemonMoveData(pokemonId, move.getName());
    }
}

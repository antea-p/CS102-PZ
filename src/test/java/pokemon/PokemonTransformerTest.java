package pokemon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokemon.datastore.PokemonData;
import pokemon.datastore.PokemonMoveData;
import pokemon.datastore.PokemonTransformer;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonSpecies;
import pokemon.domain.PokemonSpeciesLoader;
import pokemon.domain.PokemonType;
import pokemon.domain.moves.Bubble;
import pokemon.domain.moves.Ember;
import pokemon.domain.moves.Move;
import pokemon.domain.moves.TakeDown;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PokemonTransformerTest {

  static class TestSpeciesLoader implements PokemonSpeciesLoader {

    @Override
    public TreeMap<Integer, PokemonSpecies> getPokemonSpecies() {
      TreeMap<Integer, PokemonSpecies> speciesMap = new TreeMap<>();
      speciesMap.put(1, new PokemonSpecies(
              1,
          "Bulbasaur",
          PokemonType.GRASS,
          45,
          "https://img.pokemondb.net/sprites/sword-shield/icon/bulbasaur.png")
      );
      speciesMap.put(2, new PokemonSpecies(
          2,
          "Ivysaur",
          PokemonType.GRASS,
          60,
          "https://img.pokemondb.net/sprites/sword-shield/icon/ivysaur.png")
      );
      speciesMap.put(3, new PokemonSpecies(
          3,
          "Venusaur",
          PokemonType.GRASS,
          80,
          "https://img.pokemondb.net/sprites/sword-shield/icon/venusaur.png")
      );
      speciesMap.put(4, new PokemonSpecies(
          4,
          "Charmander",
          PokemonType.FIRE,
          39,
          "https://img.pokemondb.net/sprites/sword-shield/icon/charmander.png")
      );
      speciesMap.put(5, new PokemonSpecies(
          5,
          "Charmeleon",
          PokemonType.FIRE,
          58,
          "https://img.pokemondb.net/sprites/sword-shield/icon/charmeleon.png")
      );
      return speciesMap;
    }
  }

  private PokemonTransformer pokemonTransformer;
  private PokemonData pokemonData;
  private Pokemon pokemon;
  private Pokemon actualPokemon;
  private PokemonData actualPokemonData;


  @BeforeEach
  void setUp() {
    TestSpeciesLoader testSpeciesLoader = new TestSpeciesLoader();
    pokemonTransformer = new PokemonTransformer(testSpeciesLoader);
  }


  @Test
  @DisplayName("Testiranje valjanosti atributa name, health i maxHealth dobivenog Pokemon objekta")
  public void testReturnedPokemonHasEqualNameHealthMaxHealth() {
    pokemonData = new PokemonData(4, "TestPokemon", 39, 39, 4, new ArrayList<>());
    actualPokemon = pokemonTransformer.convert(pokemonData);

    assertEquals("TestPokemon", actualPokemon.getNickname());
    assertEquals(39, actualPokemon.getHealth());
    assertEquals(39, actualPokemon.getMaxHealth());
  }

  @Test
  @DisplayName("Testiranje valjanosti naziva vrste i tipa vrste dobivenog Pokemon objekta")
  public void testReturnedPokemonHasEqualSpeciesNameAndType() {
    pokemonData = new PokemonData(3, "TestPokemon", 80, 80, 3, new ArrayList<>());
    actualPokemon = pokemonTransformer.convert(pokemonData);

    assertEquals("Venusaur", actualPokemon.getSpecies().getName()); // provjeriti speciesMap (speciesNumber 3 -> Venusaur)
    assertEquals(PokemonType.GRASS, actualPokemon.getSpecies().getType());

  }

  @Test
  @DisplayName("Testiranje da se pravilno vrši konverzija PokemonMoveData objekata u listu Move objekata")
  public void testReturnedMoveList() {
    List<PokemonMoveData> moveDataList = new ArrayList<>();
    moveDataList.add(new PokemonMoveData("Ember"));
    moveDataList.add(new PokemonMoveData("Take Down"));
    pokemonData = new PokemonData(102, "Fido", 39, 39, 4, moveDataList);
    actualPokemon = pokemonTransformer.convert(pokemonData);

    ArrayList<Move> actualMovesList = actualPokemon.getMoves();
    assertTrue(actualMovesList.get(0) instanceof Ember);
    assertTrue(actualMovesList.get(1) instanceof TakeDown);

  }

  @Test
  @DisplayName("Testiranje valjanosti atributa name, health i maxHealth dobivenog PokemonData objekta")
  public void testReturnedPokemonDataHasEqualNameHealthMaxHealth() {
    pokemon = new Pokemon(102, "TestPokemon", 39, 39, PokemonType.FIRE,
            new PokemonSpecies(4, "Charmander", PokemonType.FIRE, 39,
                    "https://img.pokemondb.net/sprites/sword-shield/icon/charmander.png"),
            new ArrayList<>());
    actualPokemonData = pokemonTransformer.convert(pokemon, false);

    assertEquals("TestPokemon", actualPokemonData.getNickname());
    assertEquals(39, actualPokemonData.getHealth());
    assertEquals(39, actualPokemonData.getMaxHealth());
  }

  @Test
  @DisplayName("Testiranje valjanosti broja vrste dobivenog PokemonData objekta")
  public void testReturnedPokemonDataHasEqualSpeciesNameAndType() {
    pokemon = new Pokemon(95, "Fido", 80, 80, PokemonType.GRASS,
            new PokemonSpecies(3, "Venusaur", PokemonType.GRASS, 80,
                    "https://img.pokemondb.net/sprites/sword-shield/icon/venusaur.png"),
            new ArrayList<>());
    actualPokemonData = pokemonTransformer.convert(pokemon, false);

    assertEquals(3, actualPokemonData.getSpeciesNumber());
  }

  @Test
  @DisplayName("Testiranje da se pravilno vrši konverzija Move objekata u listu PokemonMoveData objekata")
  public void testReturnedPokemonMoveDataList() {
    ArrayList<Move> moveList = new ArrayList<>();
    moveList.add(new Ember());
    moveList.add(new TakeDown());
    moveList.add(new Bubble());

    List<PokemonMoveData> actualMoveDataList = new ArrayList<>();
    for (Move move : moveList) {
      actualMoveDataList.add(pokemonTransformer.convert(0, move));
    }

    assertEquals(0, actualMoveDataList.get(0).getPokemonId());
    assertEquals("Ember", actualMoveDataList.get(0).getMove());
    assertEquals(0, actualMoveDataList.get(1).getPokemonId());
    assertEquals("Take Down", actualMoveDataList.get(1).getMove());
    assertEquals(0, actualMoveDataList.get(2).getPokemonId());
    assertEquals("Bubble", actualMoveDataList.get(2).getMove());
  }


}

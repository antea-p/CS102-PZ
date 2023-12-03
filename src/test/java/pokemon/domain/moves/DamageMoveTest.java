package pokemon.domain.moves;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonSpecies;
import pokemon.domain.PokemonType;

class DamageMoveTest {

  private final Pokemon player = new Pokemon("Vulpix", 500, 500, PokemonType.FIRE,
      new ArrayList<>());
  private Pokemon target;
  private final DamageMove testMove = new DamageMove() {

    @Override
    protected int getDamageAmount() {
      return 20;
    }

    @Override
    protected PokemonType getDamageType() {
      return PokemonType.FIRE;
    }

    @Override
    public String getName() {
      return "testMove";
    }
  };

  @Test
  @DisplayName("Testira da li napadačka vještina čini očekivanu količinu štete (smanjenje HP-a za 20 u ovom slučaju)," +
          " i da li vraća očekivanu poruku")
  void useDamageMoveDealsCorrectDamageAmountAndReturnsCorrectString() {
    target = new Pokemon(123, "Squirtle", 600, 600, PokemonType.WATER,
        new PokemonSpecies(7, "Squirtle", PokemonType.WATER, 44,
            "https://img.pokemondb.net/sprites/sword-shield/icon/squirtle.png"),
        new ArrayList<>());

    assertEquals("Vulpix used testMove. Squirtle takes 20 damage!", testMove.use(player, target));
    assertEquals(580, target.getHealth());
  }

  @Test
  @DisplayName("Testira da li napadačka vještina čini 2x količinu štete (smanjenje HP-a) protiv osjetljivog tipa Pokemona," +
          " i da li vraća očekivanu poruku")
  void useDamageMoveDealsDoubleAgainstVulnerableTypeAndReturnsCorrectString() {
    target = new Pokemon(999, "Chikorita", 600, 600, PokemonType.GRASS,
        new PokemonSpecies(152, "Chikorita", PokemonType.GRASS, 45,
            "https://img.pokemondb.net/sprites/sword-shield/icon/chikorita.png"),
        new ArrayList<>());

    assertEquals("Vulpix used testMove. Chikorita takes 40 damage!\nIt was super effective!",
        testMove.use(player, target));
    assertEquals(560, target.getHealth());
  }
}
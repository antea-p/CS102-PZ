package pokemon.domain.moves;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonSpecies;
import pokemon.domain.PokemonType;

class TakeDownTest {

  @Test
  @DisplayName("Testira da li TakeDown vještina (pored smanjenja HP-a drugog Pokemona), izaziva recoil štetu " +
          "kod Pokemonu koji napada, te da li vraća očekivanu poruku")
  void useDealsRecoilDamageAndReturnsCorrectString() {
    Pokemon player = new Pokemon("Eevee", 500, 500, PokemonType.NORMAL, new ArrayList<>());
    Pokemon target = new Pokemon(123, "Meowth", 400, 400, PokemonType.NORMAL,
        new PokemonSpecies(52, "Meowth", PokemonType.NORMAL, 40,
            "https://img.pokemondb.net/sprites/sword-shield/icon/meowth.png"),
        new ArrayList<>());

    assertEquals("Eevee used Take Down. Meowth takes 60 damage!\nEevee takes 30 recoil damage!",
        new TakeDown().use(player, target));
    assertEquals((470), player.getHealth());
  }

}
package pokemon.domain.moves;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonType;

class SplashTest {

  @Test
  @DisplayName("Testira da li kozmetička Splash vještina vraća očekivanu poruku (bez popratnih učinaka)")
  void useReturnsCorrectString() {
    Pokemon squirtle = new Pokemon("Squirtle", 500,500, PokemonType.WATER, new ArrayList<>());

    assertEquals("Squirtle splashed about.", new Splash().use(squirtle, squirtle));
  }
}
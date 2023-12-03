package pokemon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonSpecies;
import pokemon.domain.PokemonType;
import pokemon.domain.moves.Move;
import pokemon.domain.moves.VineWhip;

class PokemonTest {

  private Pokemon player = new Pokemon("Treecko", 300, 300, PokemonType.GRASS, new ArrayList<Move>(
      Collections.singletonList(new VineWhip())));
  private Pokemon target = new Pokemon(123, "Bellossom", 450, 450, PokemonType.GRASS,
      new PokemonSpecies(182, "Bellossom", PokemonType.GRASS, 75,
          "https://img.pokemondb.net/sprites/sword-shield/icon/bellossom.png"),
      new ArrayList<>());

  @Test
  @DisplayName("Testiranje napadačke vještine, tj. da je upotrijebljena zadata vještina, " +
          "i da je vraćena očekivana poruka")
  void useMove() {
    assertEquals("Treecko used Vine Whip. Bellossom takes 20 damage!", player.useMove(0, target));
  }

}
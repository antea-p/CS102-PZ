package pokemon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonType;
import pokemon.domain.items.Bomb;

class BombTest {

  @Test
  @DisplayName("Testira da li bomba smanjuje HP drugog Pokemona (za 40), i da li se vraća očekivana poruka")
  void useInternalDamagesTargetAndReturnsCorrectString() {
    Pokemon player = new Pokemon("Vulpix", 500, 500, PokemonType.FIRE, new ArrayList<>());
    Pokemon target = new Pokemon("Meowth", 400, 400, PokemonType.NORMAL, new ArrayList<>());

    Bomb bomb = new Bomb(1);
    assertEquals("Vulpix throws a bomb at Meowth. It takes 40 damage!", bomb.use(player, target));
    assertEquals(360, target.getHealth());
  }
}
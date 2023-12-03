package pokemon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonType;
import pokemon.domain.items.Potion;

class PotionTest {

  private Pokemon player;
  private Potion potion;

  @BeforeEach
  void setUp() {
    player = new Pokemon("Eevee", 300, 300, PokemonType.NORMAL, new ArrayList<>());
    potion = new Potion(1);
  }


  @Test
  @DisplayName("Testira da li korištenje napitka vraća očekivanu poruku")
  void useInternalReturnsCorrectString() {
    assertEquals("Eevee has restored 50 HP!", potion.use(player, player));
  }

  @Test
  @DisplayName("Testira da li korištenje napitka ozdravlja Pokemona (postavljanjem nove vrijednosti HP-a)")
  void useInternalHealsPokemon() {
    player.setHealth(240);
    potion.use(player, player);
    assertEquals(290, player.getHealth());

  }

  @Test
  @DisplayName("Testira da li korištenje napitka NE povisuje HP Pokemona koji je već pri punom HP-u")
  void useInternalDoesNotOverhealPokemon() {
    potion.use(player, player);
    assertEquals(300, player.getHealth());
  }
}
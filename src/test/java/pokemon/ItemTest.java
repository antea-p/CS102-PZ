package pokemon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonType;
import pokemon.domain.items.Item;

class ItemTest {

  private final Item someItem = new Item(2) {
    @Override
    public String getName() {
      return "someItem";
    }

    @Override
    protected String useInternal(Pokemon user, Pokemon target) {
      return "someStuff";
    }
  };

  private Pokemon player = new Pokemon("Treecko", 300, 300, PokemonType.GRASS, new ArrayList<>());
  private Pokemon target = new Pokemon("Bellossom", 450, 450, PokemonType.GRASS, new ArrayList<>());

  @Test
  @DisplayName("Testira da li korištenje predmeta smanjuje njegovu količinu za 1")
  void useCorrectlyDecrementsQuantity() {
    someItem.use(player, target);
    assertEquals(1, someItem.getQuantity());

    someItem.use(player, target);
    assertEquals(0, someItem.getQuantity());
  }

  @Test
  @DisplayName("Testira da li korištenje predmeta sa quantity 0 dovodi do izbacivanja izuzetka IllegalStateException")
  void useDoesntDecrementNonexistentItem() {
    someItem.setQuantity(0);
    assertThrows(IllegalStateException.class, () -> someItem.use(player, target));
  }

}
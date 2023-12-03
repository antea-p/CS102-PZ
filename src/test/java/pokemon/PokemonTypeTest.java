package pokemon;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokemon.domain.PokemonType;

class PokemonTypeTest {

  @Test
  @DisplayName("Testira jesu li Water vještine posebno učinkovite protiv Fire tipa Pokemona")
  void isSuperEffectiveWaterVsFire() {
    assertTrue(PokemonType.WATER.isSuperEffective(PokemonType.FIRE));
  }

  @Test
  @DisplayName("Testira jesu li Fire vještine posebno učinkovite protiv Grass tipa Pokemona")
  void isSuperEffectiveFireVsGrass() {
    assertTrue(PokemonType.FIRE.isSuperEffective(PokemonType.GRASS));
  }

  @Test
  @DisplayName("Testira jesu li Grass vještine posebno učinkovite protiv Water tipa Pokemona")
  void isSuperEffectiveGrassVsWater() {
    assertTrue(PokemonType.GRASS.isSuperEffective(PokemonType.WATER));
  }

  @Test
  @DisplayName("Testira da Normal tip vještine NIJE posebno učinkovit protiv Grass, Fire, Water ili Normal tipa Pokemona")
  void isNOTSuperEffectiveNormalVsAnything() {
    assertFalse(PokemonType.NORMAL.isSuperEffective(PokemonType.GRASS));
    assertFalse(PokemonType.NORMAL.isSuperEffective(PokemonType.FIRE));
    assertFalse(PokemonType.NORMAL.isSuperEffective(PokemonType.WATER));
    assertFalse(PokemonType.NORMAL.isSuperEffective(PokemonType.NORMAL));
  }
}
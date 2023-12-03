package pokemon.domain.moves;

import pokemon.domain.PokemonType;

/**
 * Napadačka vještina tipa WATER.
 */
public class Bubble extends DamageMove {

  @Override
  protected int getDamageAmount() {
    return 20;
  }

  @Override
  protected PokemonType getDamageType() {
    return PokemonType.WATER;
  }

  @Override
  public String getName() {
    return "Bubble";
  }

}

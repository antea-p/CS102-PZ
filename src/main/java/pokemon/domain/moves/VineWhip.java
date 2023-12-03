package pokemon.domain.moves;

import pokemon.domain.PokemonType;

/**
 * Napadačka vještina tipa GRASS.
 */
public class VineWhip extends DamageMove {

  @Override
  protected int getDamageAmount() {
    return 20;
  }

  @Override
  protected PokemonType getDamageType() {
    return PokemonType.GRASS;
  }

  @Override
  public String getName() {
    return "Vine Whip";
  }

}

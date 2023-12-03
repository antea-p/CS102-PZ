package pokemon.domain.moves;

import pokemon.domain.PokemonType;

/**
 * Napadačka vještina tipa FIRE.
 */
public class Ember extends DamageMove {

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
    return "Ember";
  }

}

package pokemon.domain.moves;

import pokemon.domain.PokemonType;

/**
 * Napadačka vještina tipa NORMAL.
 */
public class Tackle extends DamageMove {

  @Override
  protected int getDamageAmount() {
    return 30;
  }

  @Override
  protected PokemonType getDamageType() {
    return PokemonType.NORMAL;
  }

  @Override
  public String getName() {
    return "Tackle";
  }

}

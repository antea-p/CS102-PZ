package pokemon.domain.items;

import pokemon.domain.Pokemon;

/**
 * Bomb predstavlja jednokratnu bombu, koja smanjuje HP drugog Pokemona za fiksnih 40 HP.
 */
public class Bomb extends Item {

  public Bomb(int quantity) {
    super(quantity);
  }

  @Override
  protected String useInternal(Pokemon user, Pokemon target) {
    target.setHealth(target.getHealth() - 40);
    return String.format("%s throws a bomb at %s. It takes 40 damage!",
            user.getNickname(), target.getNickname());
  }

  @Override
  public String getName() {
    return "Bomb";
  }

}

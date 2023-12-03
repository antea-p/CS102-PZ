package pokemon.domain.items;

import pokemon.domain.Pokemon;

/**
 * Potion predstavlja jednokratni napitak, koji ozdravlja Pokemona za fiksnih 50 HP.
 */
public class Potion extends Item {

  public Potion(int quantity) {
    super(quantity);
  }

  @Override
  protected String useInternal(Pokemon user, Pokemon target) {
    int currentHealth = user.getHealth();
    int maxHealth = user.getMaxHealth();
    user.setHealth(Math.min((currentHealth + 50), maxHealth));
    return String.format("%s has restored 50 HP!", user.getNickname());
  }

  @Override
  public String getName() {
    return "Potion";
  }

}

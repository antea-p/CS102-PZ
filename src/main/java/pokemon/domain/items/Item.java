package pokemon.domain.items;

import pokemon.domain.Pokemon;
import pokemon.domain.Usable;

/**
 * Item je apstraktna klasa koja predstavlja predmet za jednokratnu upotrebu (npr. napitak, bombu i sl.). Predmet
 * ima količinu, kao i efekat - bilo na konzumenta, bilo na drugog Pokemona.
 */
public abstract class Item implements Usable {

  private int quantity;

  protected Item(int quantity) {
    this.quantity = quantity;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * Predstavlja korištenje i/ili konzumaciju predmeta. Svako korištenje dekrementira količinu predmeta
   * u inventoryju.
   *
   * @param user   Pokemon A koji koristi predmet
   * @param target Pokemon B; predmet može, ali ne mora, imati učinak na njega
   * @return poruka o efektu
   */
  @Override
  public String use(Pokemon user, Pokemon target) {
    if (quantity < 1) {
      throw new IllegalStateException("There's nothing to decrement!");
    } else {
      quantity -= 1;
      return useInternal(user, target);
    }
  }

  protected abstract String useInternal(Pokemon user, Pokemon target);

  @Override
  public String toString() {
    return "Item{" +
            "name=" + getName() +
            ", quantity=" + quantity +
            '}';
  }
}

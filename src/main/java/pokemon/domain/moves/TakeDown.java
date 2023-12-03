package pokemon.domain.moves;

import pokemon.domain.Pokemon;
import pokemon.domain.PokemonType;

/**
 * Napadačka vještina tipa NORMAL.
 */
public class TakeDown extends DamageMove {

  @Override
  protected int getDamageAmount() {
    return 60;
  }

  @Override
  protected PokemonType getDamageType() {
    return PokemonType.NORMAL;
  }

  /**
   * Predstavlja korištenje Take Down vještine. Ista ima recoil damage, tj. sami Pokemon A, koji ju
   * poziva, također će pretrpjeti štetu (upola manju od one koju vještina čini meti, Pokemonu B).
   *
   * @param user   Pokemon koji koristi vještinu
   * @param target Pokemon koji je njena meta
   * @return String poruke o efektu
   */
  @Override
  public String use(Pokemon user, Pokemon target) {
    // info sadrži String koji vraća use metoda roditeljske klase, ali usto ima dodatne informacije.
    int recoilDamage = getDamageAmount() / 2;
      String info = super.use(user, target)
              + String.format("\n%s takes %d recoil damage!", user.getNickname(), recoilDamage);
    user.setHealth(user.getHealth() - recoilDamage);
    return info;
  }

  @Override
  public String getName() {
    return "Take Down";
  }

}

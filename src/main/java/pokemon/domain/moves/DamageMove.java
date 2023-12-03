package pokemon.domain.moves;

import pokemon.domain.Pokemon;
import pokemon.domain.PokemonType;

/**
 * Apstraktna vještina koja nasljeđuje apstraktnu nadklasu Move. Predstavlja sve vještine koje se
 * mogu upotrijebiti protiv drugog Pokemona.
 */
public abstract class DamageMove extends Move {

  /**
   * Metoda za kalkuliranje štete koju vještina nanosi.
   *
   * @return količina prouzrokovane štete
   */
  protected abstract int getDamageAmount();

  /**
   * Metoda koja vraća tip napadačke vještine. Ovo je relevantno kod provjera da li je napad posebno
   * efektivan protiv nekog tipa Pokemona. Npr. nadapačka vještina tipa FIRE radit će 2x štete
   * specifično protiv GRASS tipa Pokemona.
   *
   * @return tip napadačke vještine
   */
  protected abstract PokemonType getDamageType();

  /**
   * Metoda koja predstavlja korištenje vještine. Kada Pokemon A upotrijebi vještinu, desit će se
   * sljedeće:
   * <ol>
   *   <li>Pokemonu B će se odrediti tip.</li>
   *   <li>Izračunat će količina štete koju je vještina uzrokovala. U slučaju
   *   da je vještina tog tipa koji je posebno efektivan protiv tipa kojem pripada Pokemon B (npr.
   *   vještina je tipa WATER, a B je tipa FIRE), šteta se računa kao 2x. </li>
   *   <li>Ažurira se, tj. oduzima trenutni HP Pokemona B.</li>
   *   <li>Vraća se poruka o statusu korištenja vještine. U slučaju posebne učinkovitosti, dodaje se napomena
   *   "It was super effective!".</li>
   * </ol>
   *
   * @param user   Pokemon koji koristi vještinu
   * @param target Pokemon koji je njena meta
   * @return poruka o učinku vještine.
   */
  @Override
  public String use(Pokemon user, Pokemon target) {
    PokemonType targetType = target.getType();
    boolean superEffective = false;
    int damage = getDamageAmount();
    if (getDamageType().isSuperEffective(targetType)) {
      damage *= 2;
      superEffective = true;
    }
    target.setHealth(target.getHealth() - damage);
    String info = String.format("%s used %s. %s takes %d damage!",
            user.getNickname(), getName(), target.getNickname(), damage);
    // Ako je napad bio posebno efektivan, dodat će se i ta informacija.
    return superEffective ? info + "\nIt was super effective!" : info;
  }

  @Override
  public String toString() {
    return "DamageMove{" +
            "name=" + getName() +
            ", damage=" + getDamageAmount() +
            ", damageType=" + getDamageType() +
            "}";
  }
}

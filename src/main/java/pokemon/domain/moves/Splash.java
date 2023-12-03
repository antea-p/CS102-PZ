package pokemon.domain.moves;

import pokemon.domain.Pokemon;

/**
 * Kozmetička vještina, koja ne radi nikakvu štetu drugom Pokemonu.
 */
public class Splash extends Move {

  @Override
  public String getName() {
    return "Splash";
  }

  /**
   * Vraća poruku o upotrebi Splash vještine. Ne radi ništa drugo.
   *
   * @param user   Pokemon A koji koristi vještinu
   * @param target Pokemon B; vještina nema utjecaj na njega
   * @return String poruke: "IME_POKEMONA splashed about.",
   */
  @Override
  public String use(Pokemon user, Pokemon target) {
    return String.format("%s splashed about.", user.getNickname());
  }
}

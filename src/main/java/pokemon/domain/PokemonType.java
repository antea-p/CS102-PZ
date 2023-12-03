package pokemon.domain;

/**
 * Predstavlja tip Pokemon vrste, npr. FIRE, WATER, POISON...
 */
public enum PokemonType {

  NORMAL,
  FIRE,
  WATER,
  ICE,
  GRASS,
  BUG,
  DRAGON,
  FLYING,
  POISON,
  ELECTRIC,
  GROUND,
  ROCK,
  STEEL,
  FIGHTING,
  PSYCHIC,
  GHOST,
  FAIRY,
  DARK;

  /**
   * Provjerava učinkovitost dvaju tipova. Koristi se provjere tipa napadačke vještine vs. tipa
   * Pokemona koji se napada.
   *
   * @param targetType tip B, sa kojim će se porediti tip A
   * @return true ako je ovaj tip posebno efektivan protiv targetType-a, false inače
   */
  public boolean isSuperEffective(PokemonType targetType) {
    switch (this) {
      case NORMAL:
        return false; // NORMAL nikad nije posebno efektivan
      case FIRE:
        if (targetType == GRASS) {
          return true;
        }
        break;
      case WATER:
        if (targetType == FIRE) {
          return true;
        }
        break;
      case GRASS:
        if (targetType == WATER) {
          return true;
        }
        break;
    }
    return false;
  }
}

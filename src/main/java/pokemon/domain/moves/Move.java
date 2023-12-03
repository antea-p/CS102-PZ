package pokemon.domain.moves;

import pokemon.domain.Usable;

/**
 * Apstraktna klasa koja predstavlja vještine. Sve Pokemon vještine ju trebaju proširivati.
 */
public abstract class Move implements Usable {

  @Override
  public String toString() {
    return "Move{" +
            "name=" + getName() +
            "}";
  }
}

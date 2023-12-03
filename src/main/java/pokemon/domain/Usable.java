package pokemon.domain;

/**
 * Definira nešto što Pokemon može upotrijebiti, i što ima određeni učinak kod upotrebe. To može
 * biti vještina, hrana, napitak, ...
 */
public interface Usable {

  /**
   * Vraća naziv upotrebljivog objekta.
   *
   * @return naziv objekta
   */
  String getName();

  /**
   * Predstavlja korištenje i/ili konzumaciju nečega upotrebljivog. Ima određene efekte, koji se
   * mogu odnositi na samog korisnika, Pokemona A, i/ili na drugog Pokemona B.
   *
   * @param user   Pokemon A koji koristi objekat
   * @param target Pokemon B; objekat može, ali ne mora utjecati na njega
   * @return String poruke koja opisuje ishod upotrebe
   */
  String use(Pokemon user, Pokemon target);

}

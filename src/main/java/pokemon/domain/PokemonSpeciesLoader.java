package pokemon.domain;

import java.util.TreeMap;

/**
 * Interfejs namijenjen klasama koje učitavaju Pokemon vrste koje su smještene na nekoj lokaciji
 * (disk, web, itd.).
 */
public interface PokemonSpeciesLoader {

  /**
   * Vraća mapu PokemonSpecies objekata, koja je sortirana po ID-u dobivenih Pokemon vrsta. Za više
   * detalja o atributima vrste, vidjeti domain.PokemonSpecies.
   *
   * @return TreeMap objekat
   */
  TreeMap<Integer, PokemonSpecies> getPokemonSpecies();
}

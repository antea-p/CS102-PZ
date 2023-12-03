package pokemon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.TreeMap;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokemon.domain.PokemonSpecies;
import pokemon.domain.PokemonSpeciesLoader;
import pokemon.domain.PokemonType;
import pokemon.web.WebSpeciesLoader;

/**
 * Testira PokemonSpeciesLoader na testnom HTML stringu. HTML je iste strukture kao onaj koji
 * se dohvaća u okviru parsiranja PokeDex stranice.
 */
public class PokemonSpeciesLoaderTest {

  static final String TEST_HTML = "<html>\n"
      + "<body>\n"
      + "<main>\n"
      + "  <div>\n"
      + "    <table id=\"pokedex\">\n"
      + "      <thead>\n"
      + "      <tr>\n"
      + "        <th>#</th>\n"
      + "        <th>Name</th>\n"
      + "        <th>Type</th>\n"
      + "        <th>Total</th>\n"
      + "        <th>HP</th>\n"
      + "      </tr>\n"
      + "      </thead>\n"
      + "      <tbody>\n"
      + "      <tr>\n"
      + "        <td>\n"
      + "\t\t\t\t<span>\n"
      + "          <span data-src=\"https://img.pokemondb.net/sprites/sword-shield/icon/bulbasaur.png\"></span>\n"
      + "\t\t\t\t</span>\n"
      + "          <span>\n"
      + "\t\t\t\t  001\n"
      + "\t\t\t\t</span>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          <a>Bulbasaur</a>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          <a>Grass</a>\n"
      + "          <a>Poison</a>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          318\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          45\n"
      + "        </td>\n"
      + "      </tr>\n"
      + "      <tr>\n"
      + "        <td>\n"
      + "\t\t\t\t<span>\n"
      + "          <span data-src=\"https://img.pokemondb.net/sprites/sword-shield/icon/ivysaur.png\"></span>\n"
      + "\t\t\t\t</span>\n"
      + "          <span>\n"
      + "\t\t\t\t  002\n"
      + "\t\t\t\t</span>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          <a>Ivysaur</a>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          <a>Grass</a>\n"
      + "          <a>Poison</a>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          405\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          60\n"
      + "        </td>\n"
      + "      </tr>\n"
      + "      <tr>\n"
      + "        <td>\n"
      + "\t\t\t\t<span>\n"
      + "          <span data-src=\"https://img.pokemondb.net/sprites/sword-shield/icon/venusaur.png\"></span>\n"
      + "\t\t\t\t</span>\n"
      + "          <span>\n"
      + "\t\t\t\t  003\n"
      + "\t\t\t\t</span>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          <a>Venusaur</a>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          <a>Grass</a>\n"
      + "          <a>Poison</a>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          525\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          80\n"
      + "        </td>\n"
      + "      </tr>\n"
      + "      <tr>\n"
      + "        <td>\n"
      + "\t\t\t\t<span>\n"
      + "          <span data-src=\"https://img.pokemondb.net/sprites/sword-shield/icon/venusaur-mega.png\"></span>\n"
      + "\t\t\t\t</span>\n"
      + "          <span>\n"
      + "\t\t\t\t  003\n"
      + "\t\t\t\t</span>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          <a>Venusaur</a>\n"
      + "          <small>Mega Venusaur</small>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          <a>Grass</a>\n"
      + "          <a>Poison</a>\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          625\n"
      + "        </td>\n"
      + "        <td>\n"
      + "          80\n"
      + "        </td>\n"
      + "      </tr>\n"
      + "      </tbody>\n"
      + "    </table>\n"
      + "  </div>\n"
      + "</main>\n"
      + "</body>\n"
      + "</html>";

  @Test
  @DisplayName("Testiranje pravilnog učitavanja Pokemon vrsta iz testnog HTML dokumenta")
  public void assertLoaderCreatedTheSpeciesCorrectly() {
    PokemonSpeciesLoader loader = new WebSpeciesLoader(() -> Jsoup.parse(TEST_HTML));
    TreeMap<Integer, PokemonSpecies> pokemonSpecies = loader.getPokemonSpecies();

    TreeMap<Integer, PokemonSpecies> expectedSpecies = new TreeMap<>();
    expectedSpecies.put(1, new PokemonSpecies(
        1,
        "Bulbasaur",
        PokemonType.GRASS,
        45,
        "https://img.pokemondb.net/sprites/sword-shield/icon/bulbasaur.png")
    );
    expectedSpecies.put(2, new PokemonSpecies(
        2,
        "Ivysaur",
        PokemonType.GRASS,
        60,
        "https://img.pokemondb.net/sprites/sword-shield/icon/ivysaur.png")
    );
    expectedSpecies.put(3, new PokemonSpecies(
        3,
        "Venusaur",
        PokemonType.GRASS,
        80,
        "https://img.pokemondb.net/sprites/sword-shield/icon/venusaur.png")
    );
    assertEquals(expectedSpecies, pokemonSpecies);
  }
}

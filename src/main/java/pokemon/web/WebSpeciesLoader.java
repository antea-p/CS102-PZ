package pokemon.web;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pokemon.domain.PokemonSpecies;
import pokemon.domain.PokemonSpeciesLoader;
import pokemon.domain.PokemonType;
import pokemon.gui.GUIUtils;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;


/**
 * Klasa koja se koristi za dohvaćanje različitih podataka potrebnih za kreiranje objekata
 * PokemonSpecies i Pokemon klase (npr. ID vrste, tip vrste).
 */
public class WebSpeciesLoader implements PokemonSpeciesLoader {

    private final TreeMap<Integer, PokemonSpecies> pokemonSpecies;
    private final DocumentSource documentSource;

    public WebSpeciesLoader(DocumentSource documentSource) {
        pokemonSpecies = new TreeMap<>();
        this.documentSource = documentSource;
    }

    /**
     * Koristi se kao funkcionalni interfejs. Kreiran je kako bi se ova klasa mogla lakše testirati
     * standardnim JUnit testovima.
     */
    public interface DocumentSource {

        Document getDocument() throws IOException;
    }

    /**
     * Uzima sadržaj sljedećih ćelija sa <a href="https://pokemondb.net/pokedex/all">PokeDex
     * kataloga</a>:
     * <ul>
     *   <li># (ID, URL slike)</li>
   *   <li>Name (naziv vrste)</li>
   *   <li>Type (tip vrste)</li>
   *   <li>HP (defaultni max HP vrste)</li>
   * </ul>
   * Podatke smješta u TreeMap objekat, koji na kraju i vraća.
   *
   * @return TreeMap&lt;Integer, PokemonSpecies&gt; mapa Pokemon vrsta u formatu {id=PokemonSpecies(id,
   * name, type, hp, imageUrl)}
   */
  @Override
  public TreeMap<Integer, PokemonSpecies> getPokemonSpecies() {
    try {
      Document doc = documentSource.getDocument();
      Elements pokemonRows = doc.select("#pokedex > tbody:nth-child(2) > tr");
      for (Element row : pokemonRows) {
        List<String> cellsTextVals = row.select("td:lt(5)").eachText();
        int speciesID = Integer.parseInt(cellsTextVals.get(0));
        cellsTextVals.remove(3); // Uklanja se tekst (barem zasad) suvišne ćelije
        if (!pokemonSpecies.containsKey(speciesID)) {
          String imageLink = row.select("td:eq(0) > span > span[data-src]").attr("data-src");
          pokemonSpecies.put(speciesID,
                  new PokemonSpecies(
                          speciesID,
                          cellsTextVals.get(1),
                          PokemonType.valueOf(cellsTextVals.get(2).split(" ")[0].toUpperCase()),
                          Integer.parseInt(cellsTextVals.get(3)),
                          imageLink)
          );
        }
      }
    } catch (IOException e) {
      GUIUtils.displayAlert("Could not read the HTML document!");
      e.printStackTrace();
    }
    return pokemonSpecies;
  }

}

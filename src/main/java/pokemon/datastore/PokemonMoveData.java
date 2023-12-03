package pokemon.datastore;

import javax.persistence.*;

/**
 * <b>PokemonMove</b> predstavlja podatke o vještinama, koji se pohranjuju u tablici PokemonMove.
 * Svaki objekat ima jedinstveni ID. Značenje atributa:
 * <ul>
 *   <li>id: ID vještine u databazi; primarni ključ, autoincrement</li>
 *   <li>pokemonId: ID Pokemona koji zna tu vještinu. Referencira id nekog zapisa u tablici Pokemon.</li>
 *   <li>move: naziv vještine. U tablici PokemonMove može postojati više zapisa koji dijele istu
 *   vrijednost ovog atributa. Drugim riječima, vrijednost ovog atributa ne mora biti jedinstvena,
 *   pa može npr. postojati više zapisa sa nazivom Splash.</li>
 * </ul>
 */
@Entity
@Table(name = "PokemonMove", schema = "pokemon")
public class PokemonMoveData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private int id;

  @Column(name = "pokemonId", nullable = false)
  private int pokemonId;

  @Column(name = "move", nullable = false, length = 20)
  private String move;


  public PokemonMoveData() {
  }

  public PokemonMoveData(String move) {
    checkMoveNameLength(move);
    this.move = move;
  }

  public PokemonMoveData(int pokemonId, String move) {
    this.pokemonId = pokemonId;
    checkMoveNameLength(move);
    this.move = move;
  }

  public PokemonMoveData(int id, int pokemonId, String move) {
    this.id = id;
    this.pokemonId = pokemonId;
    checkMoveNameLength(move);
    this.move = move;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPokemonId() {
    return pokemonId;
  }

  public void setPokemonId(int pokemonId) {
    this.pokemonId = pokemonId;
  }

  public String getMove() {
    return move;
  }

  public void setMove(String move) {
    this.move = move;
  }

  @Override
  public String toString() {
    return "PokemonMoveData{" +
            "id=" + id +
            ", pokemonId=" + pokemonId +
            ", move=" + move +
            '}';
  }

  /**
   * Provjerava da li naziv vještine premašuje 20 karaktera.
   *
   * @param move String move koji se provjerava
   */
  private void checkMoveNameLength(String move) {
    if (move.length() > 20) {
      throw new IllegalArgumentException("Move name shouldn't exceed 20 characters!");
    }
  }

}

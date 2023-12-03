package pokemon.datastore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <b>StatsData</b> predstavlja single-row tablicu Stats. Sadrži atribute:
 * <ul>
 *   <li>id: dummy polje, stavljeno zato što je obvezno u svakoj Entity klasi.</li>
 *   <li>playerWins: broj korisničkih pobjeda. Nije bitno sa kojim su Pokemonom ostvarene,
 *   ni da li je svaka ostvarena sa drugačijim Pokemonom.</li>
 * </ul>
 */
@Entity
@Table(name = "Stats", schema = "pokemon")
public class StatsData {

  @Id
  @Column(name = "id", nullable = false)
  private int id;

  @Column(name = "playerWins", nullable = false)
  private int playerWins;

  public StatsData() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPlayerWins() {
    return playerWins;
  }

  public void setPlayerWins(int wins) {
    this.playerWins = wins;
  }
}

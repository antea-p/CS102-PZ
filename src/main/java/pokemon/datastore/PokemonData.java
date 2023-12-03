package pokemon.datastore;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * <b>PokemonData</b> predstavlja podatke onako kako su pohranjeni u databazi, u tablici Pokemon.
 * Predstavlja individualnog Pokemona, i sadržava slične podatke kao {@link pokemon.domain.Pokemon Pokemon}
 * klasa, s nekim razlikama. Značenje atributa:<ul>
 * <li>id: ID Pokemona u databazi; primarni ključ, autoincrement</li>
 * <li>name: ime/nadimak Pokemona; po defaultu je to ujedno i naziv njegove vrste</li>
 * <li>health: trenutni HP Pokemona, manji od ili jednak maxHealth</li>
 * <li>maxHealth: maksimalni HP pokemona, po defaultu jednak health atributu njegove vrste</li>
 * <li>speciesNumber: broj vrste, identičan # na https://pokemondb.net/pokedex/all</li>
 * </ul>
 */
@Entity
@Table(name = "Pokemon", schema = "pokemon")
public class PokemonData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "nickname")
  private String nickname;

  @Column(name = "health")
  private int health;

  @Column(name = "maxHealth")
  private int maxHealth;

  @Column(name = "speciesNumber")
  private int speciesNumber;

  // Automatski left join kako bi se dobile sve vještine nekog pojedinog Pokemona.
  // FetchType.EAGER sprečava bacanje exceptiona koji se može javiti nakon klika na Play dugme unutar AdoptionGUI-ja.
  @OneToMany(targetEntity = PokemonMoveData.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "pokemonId", insertable = false, updatable = false, referencedColumnName = "id")
  private List<PokemonMoveData> movesList;

  public PokemonData() {
  }

  public PokemonData(String nickname, int health, int maxHealth, int speciesNumber,
                     List<PokemonMoveData> movesList) {
    checkNicknameLength(nickname);
    this.nickname = nickname;
    this.health = health;
    this.maxHealth = maxHealth;
    this.speciesNumber = speciesNumber;
    this.movesList = movesList;
  }

  public PokemonData(int id, String nickname, int health, int maxHealth, int speciesNumber,
                     List<PokemonMoveData> movesList) {
    this.id = id;
    this.movesList = movesList;
    checkNicknameLength(nickname);
    this.nickname = nickname;
    this.health = health;
    this.maxHealth = maxHealth;
    this.speciesNumber = speciesNumber;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String name) {
    this.nickname = name;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
  }

  public int getSpeciesNumber() {
    return speciesNumber;
  }

  public void setSpeciesNumber(int speciesNumber) {
    this.speciesNumber = speciesNumber;
  }

  public List<PokemonMoveData> getMovesList() {
    return movesList;
  }

  public void setMovesList(List<PokemonMoveData> movesList) {
    this.movesList = movesList;
  }

  @Override
  public String toString() {
    return "PokemonData{" +
            "id=" + id +
            ", name=" + nickname +
            ", health=" + health +
            ", maxHealth=" + maxHealth +
            ", speciesNumber=" + speciesNumber +
            ", movesList=" + movesList +
            '}';
  }

  /**
   * Provjerava da li je ime Pokemona prekratko (< 3 karaktera) ili predugo (> 64 karaktera).
   *
   * @param nickname String nickname koji se provjerava
   */
  private void checkNicknameLength(String nickname) {
    if (nickname.length() < 3) {
      throw new IllegalArgumentException("Pokemon name should have at least 3 characters!");
    } else if (nickname.length() > 64) {
      throw new IllegalArgumentException("Pokemon name shouldn't exceed 64 characters!");
    }
  }
}

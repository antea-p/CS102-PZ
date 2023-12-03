package pokemon.domain;

import java.util.ArrayList;
import pokemon.domain.moves.Move;

/**
 * <b>Pokemon</b> predstavlja individualnog Pokemona (ne njegovu vrstu). Atributi:
 * <ul>
 *   <li>id: ID Pokemona</li>
 *   <li>nickname: ime Pokemona; po defaultu jednako nazivu vrste kojoj pripada</li>
 *   <li>health: trenutni HP</li>
 *   <li>maxHealth: maksimalni HP; po defaultu jednak HP-u vrste</li>
 *   <li>type: tip Pokemona; uvijek jednaka tipu njegove vrste</li>
 *   <li>species: vrsta Pokemona, sa svim podacima o njoj</li>
 *   <li>moves: lista vještina koje Pokemon posjeduje (do 4)</li>
 * </ul>
 */
public class Pokemon {

  private int id;
  private final String nickname;
  private int health;
  private int maxHealth;
  private PokemonType type;
  private PokemonSpecies species;
  private final ArrayList<Move> moves;

  public Pokemon(String nickname, int health, int maxHealth, PokemonType type,
                 ArrayList<Move> moves) {
    this.nickname = nickname;
    this.health = health;
    this.maxHealth = maxHealth;
    this.type = type;
    this.moves = moves;
  }

  public Pokemon(String nickname, int health, int maxHealth, PokemonType type,
                 PokemonSpecies species, ArrayList<Move> moves) {
    this.nickname = nickname;
    this.health = health;
    this.maxHealth = maxHealth;
    this.type = type;
    this.species = species;
    this.moves = moves;
  }

  public Pokemon(int id, String nickname, int health, int maxHealth, PokemonType type,
                 PokemonSpecies species, ArrayList<Move> moves) {
    this.id = id;
    this.nickname = nickname;
    this.health = health;
    this.maxHealth = maxHealth;
    this.type = type;
    this.species = species;
    this.moves = moves;
  }

  public int getId() {
    return id;
  }

  public String getNickname() {
    return nickname;
  }

  public int getHealth() {
    return health;
  }

  /**
   * Postavlja HP Pokemona. Ako je novi HP ;lt; 0, postavlja vrijednost na 0.
   *
   * @param health novi HP
   */
  public void setHealth(int health) {
    this.health = Math.max(health, 0);
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  // Premda se ovaj setter ne rabi, maksimalni HP Pokemona bi tehnički imalo smisla mijenjati kod, npr. level up-a.
  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
  }

  public PokemonType getType() {
    return species.getType();
  }

  public void setType(PokemonType type) {
    this.type = type;
  }

  public PokemonSpecies getSpecies() {
    return species;
  }

  public ArrayList<Move> getMoves() {
    return moves;
  }

  @Override
  public String toString() {
    return "Pokemon{" +
            "id=" + id +
            ", nickname=" + nickname +
            ", health=" + health +
            ", maxHealth=" + maxHealth +
            ", type=" + type +
            ", species=" + species +
            ", moves=" + moves +
            ", defeated=" + isDefeated() +
            '}';
  }

  public String getImageUrl() {
    return species.getImageUrl();
  }

  /**
   * Naznačuje da trenutni Pokemon priziva, tj. koristi vještinu. Za više informacija, provjeriti
   * dokumentaciju za domain.Useable.use, te dokumentaciju use metode bilo koje klase koja proširuje
   * apstraktnu klasu Move.
   *
   * @param moveNumber broj vještine koja se rabi. Odgovara njenom "slotu", tj. indeksu u listi
   *                   vještina trenutnog Pokemona.
   * @param target     Pokemon B, nad/protiv kojim se vještina rabi. Vještina može imati efekat na
   *                   njega, ali i ne mora (npr. ako je riječ o kozmetičkoj vještini Splash).
   * @return poruka o rezultatu upotrebe vještine. Neke vještine imaju jedinstvenu poruku.
   */
  public String useMove(int moveNumber, Pokemon target) {
    Move usedMove = moves.get(moveNumber);
    return usedMove.use(this, target);
  }

  /**
   * Vraća boolean vrijednost koja naznačava da li je Pokemon pobijeđen (HP &le; 0).
   *
   * @return true ako je Pokemon izgubio, false inače
   */
  public boolean isDefeated() {
    return health <= 0;
  }
}

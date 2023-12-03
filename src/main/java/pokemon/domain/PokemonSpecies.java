package pokemon.domain;

import java.util.Objects;

/**
 * PokemonSpecies predstavlja vrstu Pokemona (ne individualnog Pokemona). Atributi koje sadržava
 * odgovaraju podacima koje ekstrahira metoda klase WebSpeciesLoader:
 * <ul>
 *   <li>id: odgovara # na <a href="https://pokemondb.net/pokedex/all">PokeDex katalogu</a></li>
 *   <li>name: naziv vrste</li>
 *   <li>type: tip vrste (npr. NORMAL)</li>
 *   <li>hp: defaultni maksimalni HP za vrstu</li>
 *   <li>imageUrl: URL sa slikom Pokemona</li>
 * </ul>
 */
public class PokemonSpecies {

  private final int id;
  private final String name;
  private final PokemonType type;
  private final int hp;
  private final String imageUrl;

  public PokemonSpecies(int id, String name, PokemonType type, int hp, String imageUrl) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.hp = hp;
    this.imageUrl = imageUrl;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public PokemonType getType() {
    return type;
  }

  public int getHp() {
    return hp;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  @Override
  public String toString() {
    return "PokemonSpecies{" +
        "id=" + id +
        ", name=" + name +
        ", type=" + type +
        ", hp=" + hp +
        ", imageUrl=" + imageUrl +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PokemonSpecies that = (PokemonSpecies) o;
    return id == that.id && hp == that.hp && Objects.equals(name, that.name)
        && type == that.type && Objects.equals(imageUrl, that.imageUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, type, hp, imageUrl);
  }
}

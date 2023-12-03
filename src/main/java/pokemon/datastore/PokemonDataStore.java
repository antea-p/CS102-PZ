package pokemon.datastore;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <b>PokemonDataStore</b> je klasa koja sadrži metode za dodavanje, brisanje i ažuriranje zapisa u
 * tablicama Pokemon i PokemonMove.
 */
public class PokemonDataStore {

  private final SessionFactory factory;

  public PokemonDataStore(SessionFactory factory) {
    this.factory = factory;
  }

  // Privatne metode koje koriste funkcionalne interfejse. Koriste se u javnim
  // metodama radi smanjenja repetitivnosti koda.

  private Integer saveWithTransaction(Function<Session, Serializable> action) {
    try (Session session = factory.openSession()) {
      Transaction transaction = session.beginTransaction();
      Integer result = (Integer) action.apply(session);
      transaction.commit();
      return result;
    }
  }

  private void executeWithTransaction(Consumer<Session> action) {
    try (Session session = factory.openSession()) {
      Transaction transaction = session.beginTransaction();
      action.accept(session);
      transaction.commit();
    }
  }

  private <T> T queryWithSession(Function<Session, T> action) {
    try (Session session = factory.openSession()) {
      return action.apply(session);
    }
  }

  /**
   * Selektira sve zapise u Pokemon tablici te ih vraća kao listu. Svaki zapis sadrži:
   * <ul>
   *   <li>id: ID Pokemona u databazi</li>
   *   <li>name: ime</li>
   *   <li>health: trenutni HP</li>
   *   <li>maxhealth: maksimalni HP</li>
   *   <li>speciesNumber: ID Pokemon vrste kojoj dati Pokemon pripada</li>
   * </ul>
   *
   * @return lista svih PokemonData objekata
   */
  public List<PokemonData> getPokemonData() {
    return queryWithSession(session -> session.createQuery("FROM PokemonData").list());
  }


  /**
   * Dodaje zapis o Pokemonu, u tablicu Pokemon. PokemonData objekat predstavlja podatke o
   * individualnom Pokemonu. Metoda vraća id Pokemona, koji odgovara vrijednosti atributa id u
   * tablici.
   *
   * @param pokemon PokemonData objekat
   * @return id dodatog Pokemona. Koristan kad god je potrebno kreirati PokemonMoveData objekte koji
   * su povezani sa tim Pokemonom (tj. kad se kreira lista vještina koje posjeduje).
   */
  public int add(PokemonData pokemon) {
    return saveWithTransaction(session -> session.save(pokemon));
  }

  /**
   * Dodaje zapis o Pokemonovoj vještini, u tablicu PokemonMove. PokemonMove objekat predstavlja
   * podatke o pojedinačnoj vještini nekog Pokemona. Prilikom specifikacije pokemonId parametra za
   * PokemonMove, trebalo bi koristiti onaj id koji vraća metoda add(PokemonData pokemon)!
   *
   * @param pokemonMove PokemonMoveData objekat
   */
  public void add(PokemonMoveData pokemonMove) {
    executeWithTransaction(session -> session.save(pokemonMove));
  }

  /**
   * Ažurira Pokemona u tablici Pokemon.
   *
   * @param pokemon PokemonData objekat koji će zamijeniti stari zapis
   */
  public void update(PokemonData pokemon) {
    executeWithTransaction(session -> session.update(pokemon));
  }

  /**
   * Briše Pokemona iz tablice Pokemon.
   *
   * @param pokemon PokemonData objekat koji se briše
   */
  public void delete(PokemonData pokemon) {
    executeWithTransaction(session -> session.delete(pokemon));
  }

  /**
   * Briše vještinu iz tablice PokemonMove, ako pripada Pokemonu sa datim ID-jem.
   *
   * @param pokemonId pokemonId nekog PokemonData objekta
   */
  public void delete(int pokemonId) {
    executeWithTransaction(session -> {
      Query query = session.createQuery("DELETE FROM PokemonMoveData WHERE pokemonId = :pokemonId");
      query.setParameter("pokemonId", pokemonId);
      query.executeUpdate();
    });
  }

  /**
   * U potpunosti ozdravlja Pokemona čiji se dati ID nalazi u Pokemon tablici.
   *
   * @param id id nekog PokemonData objekta
   */
  public void heal(int id) {
    executeWithTransaction(session -> {
      Query query = session.createQuery("UPDATE PokemonData SET health = maxHealth WHERE id = :id");
      query.setParameter("id", id);
      query.executeUpdate();
    });
  }
}


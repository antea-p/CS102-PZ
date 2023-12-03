package pokemon.datastore;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * <b>WinsStatsStore</b> je klasa koja sadrži metode za dobivanje trenutnog broja korisnikovih
 * pobjeda, te za njihovo inkrementiranje. Radi sa tablicom Stats.
 */
public class WinStatsStore {

  private final SessionFactory factory;

  public WinStatsStore(SessionFactory factory) {
    this.factory = factory;
  }

  /**
   * Vraća trenutni broj korisnikovih pobjeda, tj. vrijednost atributa playerWins.
   *
   * @return broj pobjeda
   */
  public int getWinCount() {
    try (Session session = factory.openSession()) {
      Query query = session.createQuery("SELECT playerWins FROM StatsData");
      return (int) query.uniqueResult();
    }
  }

  /**
   * Za jedan inkrementira broj pobjeda u tablici Stats.
   */
  public void incrementWinCount() {
    try (Session session = factory.openSession()) {
      Transaction transaction = session.beginTransaction();
      Query query = session.createQuery("UPDATE StatsData SET playerWins = playerWins + 1");
      query.executeUpdate();
      transaction.commit();
    }
  }
}

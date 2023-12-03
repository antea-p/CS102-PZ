package pokemon.domain;

import pokemon.domain.items.Bomb;
import pokemon.domain.items.Item;
import pokemon.domain.items.Potion;

import java.util.ArrayList;
import java.util.Random;

/**
 * Inventory predstavlja korisnički inventar. Sadrži objekte koji proširuju Item klasu.
 *
 * @author Antea Primorac
 */

public class Inventory {

    private final ArrayList<Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "items=" + items +
                '}';
    }

    /**
     * Metoda za generiranje (0-2) predmeta, konkretno Potion i Bomb objekata.
     * Isti se potom dodaju u trenutni inventar.
     */

    public void generateInventory() {
        Random random = new Random();
        int randomQuantity = random.nextInt(3);
        if (randomQuantity > 0) {  // dodaje predmete u inventar
            items.add(new Potion(randomQuantity));
            items.add(new Bomb(randomQuantity));
        }
    }
}

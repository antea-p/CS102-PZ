package pokemon.gui;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Klasa sa pomoćnim metodama korisne programima u sklopu gui paketa.
 */
public class GUIUtils {

    /**
     * Podešava sliku za dati ImageView objekat. Slika je veličine 96x96 piksela.
     *
     * @param imageView ImageView objekat koji će biti ažuriran
     * @param url       URL do slike
     */
    public static void setImageFromURL(ImageView imageView, String url) {
        try {
            // Potrebno je specificirati User-Agent, inače se slika neće učitati.
            // https://stackoverflow.com/a/55077004
            URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            imageView.setImage(new Image(connection.getInputStream(), 96, 96, true, true));
        } catch (IOException e) {
            displayAlert("Failed to retrieve the image!");
            e.printStackTrace();
        }
    }

    /**
     * Kreira alert obavijesti. Za zatvaranje prozora, dovoljno je pritisnuti OK.
     *
     * @param message   poruka koji će se prikazati korisniku
     * @param alertType vrsta obavijesti (AlertType.ERROR, AlertType.WARNING, AlertType.INFORMATION, AlertType.CONFIRMATION)
     */
    public static void displayAlert(String message, AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);

        // Workaround za premalen alert: https://stackoverflow.com/a/55200733
        alert.setResizable(true);
        alert.onShownProperty()
            .addListener(e -> Platform.runLater(() -> alert.setResizable(false)));

        // Wraparound opcija: https://stackoverflow.com/a/46395543
        StringBuilder sb = new StringBuilder(message);
        for (int i = 0; i < message.length(); i += 200) {
            sb.insert(i, "\n");
        }

        Label messageLbl = new Label(sb.toString());
        alert.getDialogPane().setContent(messageLbl);

        // On-top opcija: https://stackoverflow.com/a/43007782
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> alert.close());
    }

    /**
     * Kreira alert greške.
     *
     * @param errorMessage obavijest o grešci
     */
    public static void displayAlert(String errorMessage) {
        displayAlert(errorMessage, AlertType.ERROR);
    }
}

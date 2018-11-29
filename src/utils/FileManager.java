package utils;

import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileManager {
    /**
     * Provide the user with an interface to choose an Image to load.
     *
     * @return the file chosen.
     */
    public File imageFileOpen() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "JPEG Files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("./img/"));
        return fileChooser.showOpenDialog(null);
    }

    /**
     * Provide the user with an interface to save its work on an image.
     *
     * @param img the image to save
     */
    public void imageFileSave(BufferedImage img) {

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "PNG Files (*.jpg)", "*.jpg");

        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.getExtensionFilters().add(extFilter);
        saveFileChooser.setInitialDirectory(new File("./img/"));
        saveFileChooser.setInitialFileName("AfterMyModifications");
        File file = saveFileChooser.showSaveDialog(null);
        try {
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            System.out.println("Save of your image was unsuccessful.");
        } catch (IllegalArgumentException e) {
            System.out.println("You decided to cancel the save process.");
        }
    }
}

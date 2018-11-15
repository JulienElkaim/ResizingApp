package operation;

import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class SimpleOperation {

    /**
     * Allow to create a copy of your BufferedImage.
     *
     * @param bImage the image source to clone.
     * @return an exact copy of the source.
     */
    public static BufferedImage cloningBufferedImage(BufferedImage bImage) {

        ColorModel cm = bImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bImage.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);

    }

    /**
     * Provide the user with an interface to choose an Image to load.
     *
     * @return the file choosed.
     */
    public static File imageFileOpen() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPEG Files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("./img/"));
        return fileChooser.showOpenDialog(null);


    }

    /**
     * Provide the user with an interface to save its work on an image.
     *
     * @param img the image to save
     * @throws IOException if it fails to write the file on your hard disk.
     */
    public static void imageFileSave(BufferedImage img) throws IOException {

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG Files (*.jpg)", "*.jpg");

        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.getExtensionFilters().add(extFilter);
        saveFileChooser.setInitialDirectory(new File("./img/"));
        saveFileChooser.setInitialFileName("AfterMyModifications");
        File file = saveFileChooser.showSaveDialog(null);

        ImageIO.write(img, "jpg", file);
    }

    /**
     * Retrieve interesting content of a specific string format.
     *
     * @param src is the String where the interesting text is.
     * @return a string containing the name of the item generator of this string.
     */
    public static String getText(String src) {
        StringBuilder myStr = new StringBuilder();
        char currentChar;
        for (int i = src.length() - 2; i > -1; i--) {

            currentChar = src.charAt(i);
            if (currentChar == '\'') {
                i = -1;
                continue;
            }
            myStr.insert(0, currentChar);

        }
        return myStr.toString();
    }


}

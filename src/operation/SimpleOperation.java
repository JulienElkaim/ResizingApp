package operation;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class SimpleOperation {

    public static BufferedImage cloningBufferedImage(BufferedImage bImage){
        ColorModel cm = bImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bImage.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}

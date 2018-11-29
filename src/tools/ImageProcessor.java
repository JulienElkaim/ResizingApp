package tools;

import java.awt.image.BufferedImage;

public interface ImageProcessor {
    /**
     * Process the image using the tool's current settings.
     *
     * @param inputImage the image to be processed
     * @return the image after processing
     */
    BufferedImage process(BufferedImage inputImage);
}

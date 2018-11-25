package tools;

import utils.Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static java.lang.Math.abs;

/**
 * Converts the image into another image which uses gradients of a given color.
 */
public class GradientPainter implements ImageProcessor {

    private ColorEnum gradientColor = null;

    /**
     * Sets the color gradient. This is a prerequisite before applying the gradient.
     *
     * @param color
     */
    public void setGradientColor(Color color) {
        this.gradientColor = ColorEnum.findColor(color);
    }

    /**
     * The gradient is the difference between the intensity of the same colour in
     * the left and in the right pixel
     *
     * @param myBufferedImage
     *            is the BufferedImage to apply the gradient.
     * @return a power-gradiented image resulting of the Gradient application.
     */
    @Override
    public BufferedImage process(BufferedImage myBufferedImage) {
        BufferedImage gradientBImage = Utils.clone(myBufferedImage);
        if (this.gradientColor != null) {
            int maxX = gradientBImage.getWidth();
            int maxY = gradientBImage.getHeight();

            for (int x = 2; x < maxX - 1; x++) {
                for (int y = 1; y < maxY; y++) {

                    Color myRightPixelColor = new Color(myBufferedImage.getRGB(x + 1, y));
                    Color myLeftPixelColor = new Color(myBufferedImage.getRGB(x - 1, y));

                    int colorChange = 0;
                    int Left = 0;
                    int Right = 0;
                    // Color change requires on operation on gradient: 8 for green, 16 for red,
                    // nothing for blue
                    switch (this.gradientColor) {
                        case PROCESS_RED:
                            Left = myLeftPixelColor.getRed();
                            Right = myRightPixelColor.getRed();
                            colorChange = 16;
                            break;
                        case PROCESS_GREEN:
                            Left = myLeftPixelColor.getGreen();
                            Right = myRightPixelColor.getGreen();
                            colorChange = 8;
                            break;
                        case PROCESS_BLUE:
                            Left = myLeftPixelColor.getBlue();
                            Right = myRightPixelColor.getBlue();
                            break;
                        default:
                            System.err.println("Unexpected color: " + this.gradientColor);
                    }
                    int gradient = abs(Left - Right);
                    if (gradient > 255) {
                        gradient = 255;
                    }
                    gradient = (gradient << colorChange);
                    gradientBImage.setRGB(x, y, gradient);
                }
            }
        } else {
            System.err.println("Gradient color is not set!");
        }
        return gradientBImage;
    }
}
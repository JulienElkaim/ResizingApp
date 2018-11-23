package tools;


import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.Color.*;
import static java.lang.Math.abs;


public class GradientPainter implements ImageProcessor {
    enum GradientColor {
        GRADIENT_RED(RED, 16),
        GRADIENT_GREEN(GREEN, 8),
        GRADIENT_BLUE(BLUE, 0);

        Color color;
        int offset;

        GradientColor(Color color, int offset) {
            this.color = color;
            this.offset = offset;
        }
    }

    private GradientColor gradientColor = null;

    // methode de paramétrage du traitement ou de l'outil
    public void setGradientColor(Color color) {
        for (GradientColor c : GradientColor.values()) {
            if (c.color == color) {
                this.gradientColor = c;
                break;
            }
        }
    }

    /**
     * The gradient is the difference between the intensity of the same colour in the left and in the right pixel
     *
     * @param myBufferedImage is the BufferedImage to apply the gradient.
     * @return a power-gradiented image resulting of the GradientPainter application.
     */
    @Override
    public BufferedImage process(BufferedImage myBufferedImage) {
        BufferedImage gradientBImage = SimpleOperation.cloningBufferedImage(myBufferedImage);
        int maxX = gradientBImage.getWidth();
        int maxY = gradientBImage.getHeight();


        for (int x = 2; x < maxX - 1; x++) {
            for (int y = 1; y < maxY; y++) {

                Color myRightPixelColor = new Color(myBufferedImage.getRGB(x + 1, y));
                Color myLeftPixelColor = new Color(myBufferedImage.getRGB(x - 1, y));


                int colorChange = this.gradientColor.offset;
                int Left = 0;
                int Right = 0;
                //Color change requires on tools on gradient: 8 for green, 16 for red, nothing for blue
                switch (this.gradientColor) {
                    case GRADIENT_RED:
                        Left = myLeftPixelColor.getRed();
                        Right = myRightPixelColor.getRed();
                        break;
                    case GRADIENT_GREEN:
                        Left = myLeftPixelColor.getGreen();
                        Right = myRightPixelColor.getGreen();
                        break;
                    case GRADIENT_BLUE:
                        Left = myLeftPixelColor.getBlue();
                        Right = myRightPixelColor.getBlue();
                        break;
                    default:
                        System.err.println("Error on color: " + this.gradientColor);
                }
                int gradient = abs(Left - Right);
                if (gradient > 255) {
                    gradient = 255;
                }
                gradient = (gradient << colorChange);
                gradientBImage.setRGB(x, y, gradient);

            }
        }
        return gradientBImage;
    }
}

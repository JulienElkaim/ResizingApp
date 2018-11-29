package tools;

import java.awt.image.BufferedImage;

import static java.lang.Math.abs;

public class Cropper implements ImageProcessor {
    // the coefficient to apply to width
    private double coef = 1.0;
    private String direction = "H";

    public void setCoef(double coef) {
        this.coef = coef;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Provide the user with useful method to crop an image.
     *
     * @param myBufferedImage the image source to crop.
     * @return a cropped image.
     */
    public BufferedImage process(BufferedImage myBufferedImage) {

        double coef = (0.01 < this.coef / 100) ? abs(this.coef) / 100 : 0.01;
        int width = myBufferedImage.getWidth();
        int height = myBufferedImage.getHeight();

        if (this.direction.equals("H"))
            width = (int) (coef * width);
        else
            height = (int) (coef * height);

        return myBufferedImage.getSubimage(0, 0, width, height);
    }
}

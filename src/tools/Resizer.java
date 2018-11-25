package tools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static java.lang.Math.abs;

public class Resizer implements ImageProcessor{

    private double coef = 0.0;
    private String direction = "V"; //default value

    public void setCoef(double coef){
        this.coef = coef;
    }

    public void setDirection(String direction){
        this.direction = direction;
    }
    /**
     * Provide method to resize the width of your image.
     * @param myBufferedImage is the image to resize.
     * @return the image resized.
     */
    @Override
    public  BufferedImage process(BufferedImage myBufferedImage){
        double coef = (0.01 < this.coef / 100) ? abs(this.coef) / 100 : 0.01;
        int width =myBufferedImage.getWidth();
        int height = myBufferedImage.getHeight();
        if (this.direction.equals("H"))
            width = (int) (coef * width);
        else
            height = (int) (coef * height);
        return (this.scale(myBufferedImage, width, height));
    }

    /**
     * Provide useful method to scale an image with new width and height.
     *
     * @param myBufferedImage is the image to rescale.
     * @param width           is the new width to obtain.
     * @param height          is the new height to obtain.
     * @return the BufferedImage scaled.
     */
    private  BufferedImage scale(BufferedImage myBufferedImage, int width, int height) {
        BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dest.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
                (double) width / myBufferedImage.getWidth(),
                (double) height / myBufferedImage.getHeight());
        g.drawRenderedImage(myBufferedImage, at);
        return dest;
    }

    /**
     * Provide the user with useful method to crop an image.
     *
     * @param myBufferedImage the image source to crop.
     * @param cropCoef        the coefficient to apply to width.
     * @return a cropped image.
     */
    public  BufferedImage cropping(BufferedImage myBufferedImage, double cropCoef, String direction) {
        double coef = (0.01 < cropCoef / 100) ? abs(cropCoef) / 100 : 0.01;
        int width = myBufferedImage.getWidth();
        int height = myBufferedImage.getHeight();

        if (direction.equals("H"))
            width = (int) (coef * width);
        else
            height = (int) (coef * height);

        return myBufferedImage.getSubimage(0, 0, width, height);
    }

    /**
     * Provide the user with useful method to resize its image with a Seam Carving algorithm.
     *
     * @param nbOfSeamToWithdraw number of seam to retrieve on our image.
     * @param img                the image where the seams are withdrawn.
     * @return the image without the seams.
     */
    public  BufferedImage SeamCarving(int nbOfSeamToWithdraw, BufferedImage img, String direction) {

        for (int i = 0; i < nbOfSeamToWithdraw; i++) {
            BufferedImage energyBImage = SeamCarver.EnergizedImage(img);
            int[] seamToWithdraw = SeamCarver.bestSeam(energyBImage, direction);

            BufferedImage bImageWithOutSeam = SeamCarver.seamVerticalDestroyer(img, seamToWithdraw, direction);
            img = SimpleOperation.cloningBufferedImage(bImageWithOutSeam);
        }

        return img;
    }
}

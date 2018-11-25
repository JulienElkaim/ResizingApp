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
}

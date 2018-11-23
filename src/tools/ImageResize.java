package tools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static java.lang.Math.abs;

public class ImageResize {

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
     * Provide useful method to resize the width of your image.
     *
     * @param myBufferedImage is the image to resize.
     * @param resizeCoef      coefficient to apply to width.
     * @return the image resized.
     */
    public  BufferedImage resizing(BufferedImage myBufferedImage, double resizeCoef, String direction) {
        double coef = (0.01 < resizeCoef / 100) ? abs(resizeCoef) / 100 : 0.01;
        int width =myBufferedImage.getWidth();
        int height = myBufferedImage.getHeight();

        if (direction.equals("H"))
            width = (int) (coef * width);
        else
            height = (int) (coef * height);


        return (this.scale(myBufferedImage, width, height));
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
            BufferedImage energyBImage = ImageSeamComputation.EnergizedImage(img);
            int[] seamToWithdraw = ImageSeamComputation.bestSeam(energyBImage, direction);

            BufferedImage bImageWithOutSeam = ImageSeamComputation.seamVerticalDestroyer(img, seamToWithdraw, direction);
            img = SimpleOperation.cloningBufferedImage(bImageWithOutSeam);
        }

        return img;
    }

    /**
     * Provide the user with useful method to zoom on its image.
     *
     * @param myBufferedImage The image where to zoom happen.
     * @param viewSize        the displayed image's size , different of the BufferedImage used.
     * @param direction       the size direction provided, H for Height and W for width. BOTH is an already computed coef View / Real
     * @param X               the View x-coordinate where the mouse pointed out.
     * @param Y               the View y-coordinate where the mouse pointed out.
     * @param zoomCoef        the strength of the zoom. High coef mean high zoom.
     * @return the BufferedImage resulting the zoom tools.
     */
    public  BufferedImage zoom(BufferedImage myBufferedImage, double viewSize, String direction, double X, double Y, double zoomCoef) {

        double zoomingCoef = zoomCoef / 100 * (0.1 - 1) + 1;// 1* -0.9 +1
        double initWidth = myBufferedImage.getWidth();
        double initHeight = myBufferedImage.getHeight();
        double coefViewReal = 1;

        if (direction.equals("H"))
            coefViewReal = viewSize / initHeight;
        if (direction.equals("V"))
            coefViewReal = viewSize / initWidth;


        double XReal = X / coefViewReal;
        double YReal = Y / coefViewReal;
        double x = Math.max(0, XReal - (zoomingCoef / 2) * initWidth);
        double y = Math.max(0, YReal - (zoomingCoef / 2) * initHeight);
        int width = (int) (zoomingCoef * initWidth);
        int height = (int) (zoomingCoef * initHeight);

        while (x + width > myBufferedImage.getWidth())
            x -= 1;
        while (y + height > myBufferedImage.getHeight())
            y -= 1;
        return myBufferedImage.getSubimage((int) x, (int) y, width, height);
    }

}

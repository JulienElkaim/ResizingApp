package operation;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import imageChange.*;
import javafx.embed.swing.SwingFXUtils;

import static java.lang.Math.abs;

public class ImageResize {
    private static BufferedImage scale(BufferedImage myBufferedImage, int width, int height){
        BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dest.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
                (double)width/myBufferedImage.getWidth(),
                (double)height/myBufferedImage.getHeight());
        g.drawRenderedImage(myBufferedImage,at);
        return dest;
    }
    public static BufferedImage resizingWidth(BufferedImage myBufferedImage, double resizeCoef){
        double coef = (0.01<resizeCoef/100)? abs(resizeCoef)/100:0.01 ;
        int width = (int) (coef*myBufferedImage.getWidth());
        int height = myBufferedImage.getHeight();
        return (ImageResize.scale(myBufferedImage,width, height));
    }





}

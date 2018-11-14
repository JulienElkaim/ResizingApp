package operation;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import imageChange.*;
import javafx.embed.swing.SwingFXUtils;

import javafx.scene.image.ImageView;

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


    public static BufferedImage croppingWidth(BufferedImage myBufferedImage, double cropCoef) {
        double coef = (0.01 < cropCoef / 100) ? abs(cropCoef) / 100 : 0.01;
        int width = myBufferedImage.getWidth();
        int height = myBufferedImage.getHeight();
        int newWidth = (int) (coef * width);
        BufferedImage dest = myBufferedImage.getSubimage(0, 0, newWidth, height);
        return dest;
    }


    public static BufferedImage zoom(BufferedImage myBufferedImage, double viewSize, String direction , double X, double Y, double zoomCoef){

        //BufferedImage myBufferedImage = SwingFXUtils.fromFXImage(myImage.getImage(),null);

        double zoomingCoef = zoomCoef/100  *(0.1-1) +1;// 1* -0.9 +1

        double initWidth = myBufferedImage.getWidth();
        double initHeight = myBufferedImage.getHeight();

        double coefViewReal = 1;
        if (direction.equals("H"))
            coefViewReal = viewSize / initHeight;
         if(direction.equals("W"))
            coefViewReal = viewSize / initWidth;
        if(direction.equals("BOTH"))
            coefViewReal = viewSize;


        double XReal = X/coefViewReal;
        double YReal = Y/coefViewReal;

        double x = Math.max(0, XReal - (zoomingCoef/2)* initWidth);
        double y = Math.max(0 , YReal - (zoomingCoef/2)* initHeight);

        int width =(int)(zoomingCoef*initWidth) ;
        int height = (int)(zoomingCoef*initHeight);

        System.out.println("x: " + x +" , y : " + y + "   width: " + width + " , height : " + height);


        while (x+width >= myBufferedImage.getWidth()){
            x-=1;
        }
        while (y+height >= myBufferedImage.getHeight()){
            y-=1;
        }

        return myBufferedImage.getSubimage((int)x,(int)y,width,height);
    }



}

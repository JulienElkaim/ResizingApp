package tools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SimpleOperation {
    /** Modify color composition of a bufferedImage.
     *
     * @param myColor is the color to increase/decrease.
     * @param myBufferedImage is the bufferedImage to color.
     * @param ratio is the ratio to apply at the color concerned.
     * @return a bufferedImage with rgb values modified.
     */
    public static BufferedImage imageColoring(String myColor, BufferedImage myBufferedImage, double ratio) {
        int decalage;
        int colorToChange;
        int secondColor;
        int thirdColor;
        int maxX = myBufferedImage.getWidth();
        int maxY = myBufferedImage.getHeight();

        for (int x = 0; x < maxX; x++){
            for(int y = 0; y < maxY; y++) {

                Color myPixelColor = new Color(myBufferedImage.getRGB(x, y));

                switch (myColor) {
                    case "R":
                        decalage = 16;
                        colorToChange = myPixelColor.getRed();
                        secondColor = myPixelColor.getGreen()<<8;
                        thirdColor = myPixelColor.getBlue();
                        break;
                    case "G":
                        decalage = 8;
                        colorToChange = myPixelColor.getGreen();
                        secondColor = myPixelColor.getRed()<<16;
                        thirdColor = myPixelColor.getBlue();
                        break;
                    default:
                        decalage = 0;
                        colorToChange = myPixelColor.getBlue();
                        secondColor = myPixelColor.getGreen()<<8;
                        thirdColor = myPixelColor.getRed()<<16;
                        break;
                }
                colorToChange =( Math.min((int)(colorToChange*ratio),255) )<< decalage;
                myBufferedImage.setRGB( x , y ,( colorToChange + secondColor + thirdColor ) );
            }

        }
        return myBufferedImage;
    }

    /**
     * Create a copy of BufferedImage.
     *
     * @param bImage the image source to clone.
     * @return an exact copy of the source.
     */
    public static BufferedImage cloningBufferedImage(BufferedImage bImage) {
        int maxX = bImage.getWidth();
        int maxY = bImage.getHeight();
        BufferedImage bImageNew = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x<maxX; x++){
            for(int y = 0; y<maxY; y++){
                bImageNew.setRGB(x,y,bImage.getRGB(x,y));
            }
        }
        return bImageNew;

    }

    /**
     * Retrieve interesting content of a specific string format.
     *
     * @param src is the String where the interesting text is.
     * @return a string containing the name of the item generator of this string.
     */
    public static String getButtonText(String src) {
        StringBuilder myStr = new StringBuilder();
        char currentChar;
        for (int i = src.length() - 2; i > -1; i--) {

            currentChar = src.charAt(i);
            if (currentChar == '\'') {
                i = -1;
                continue;
            }
            myStr.insert(0, currentChar);

        }
        return myStr.toString();
    }

}

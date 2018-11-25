package tools;

import java.awt.image.BufferedImage;

public class SimpleOperation {
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

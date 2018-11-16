package operation;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import static java.lang.Math.abs;

public class ImageSeamComputation {

    /**
     * Print energy negative of the image.
     *
     * @param img the energy BufferedImage to gray.
     * @return BufferedImage that represent energized Image passed on a gray scale filter.
     */
    public static BufferedImage EnergizedImage(BufferedImage img) {

        return ImageSeamComputation.grayOut(ImageSeamComputation.determineEnergy(img));

    }

    /**
     * Transform image with a gray filter.
     *
     * @param img is the BufferedImage to filter.
     * @return a copy of BufferedImage in a gray Scale.
     */
    private static BufferedImage grayOut(BufferedImage img) {
        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(img, img);
        return img;
    }

    /**
     * BUG BUG BUG: Need to compute energy for border pixel (x=2, y=2...)
     *
     * @param imgToCompute is the bufferedImage on which to run energy computation.
     * @return BufferedImage that represent energy of the initial image.
     */
    private static BufferedImage determineEnergy(BufferedImage imgToCompute) {
        int maxX = imgToCompute.getWidth();
        int maxY = imgToCompute.getHeight();
        BufferedImage newBImage = SimpleOperation.cloningBufferedImage(imgToCompute);

        for (int x = 2; x < maxX - 1; x++) {
            for (int y = 2; y < maxY - 1; y++) {

                Color myLeftPixelColor = new Color(imgToCompute.getRGB(x - 1, y));
                Color myRightPixelColor = new Color(imgToCompute.getRGB(x + 1, y));
                Color myTopPixelColor = new Color(imgToCompute.getRGB(x, y - 1));
                Color myBottomPixelColor = new Color(imgToCompute.getRGB(x, y + 1));

                int energyRed;
                int energyGreen;
                int energyBlue;
                int energy;

                energyRed = abs(myLeftPixelColor.getRed() - myRightPixelColor.getRed()) + abs(myTopPixelColor.getRed() - myBottomPixelColor.getRed());
                energyGreen = abs(myLeftPixelColor.getGreen() - myRightPixelColor.getGreen()) + abs(myTopPixelColor.getGreen() - myBottomPixelColor.getGreen());
                energyBlue = abs(myLeftPixelColor.getBlue() - myRightPixelColor.getBlue()) + abs(myTopPixelColor.getBlue() - myBottomPixelColor.getBlue());


                energy = (energyRed << 16) + (energyGreen << 8) + energyBlue;
                newBImage.setRGB(x, y, energy);
            }
        }
        return newBImage;
    }

    /**
     * Provide Dynamic structure to find seams available on your Gray energy BufferedImage.
     *
     * @param imgToCompute is the energy BufferedImage source to retrieve seams pattern.
     * @return a matrix of long that represent a dynamic seam matrix
     */
    private static long[][] dynamicSeam(BufferedImage imgToCompute, String direction) {
        int maxX = imgToCompute.getWidth();
        int maxY = imgToCompute.getHeight();
        long[][] dynamicSeamTable = new long[maxX][maxY];

        //seam computing
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (y==0){
                    if (direction.equals("H"))
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x, y);
                    if (direction.equals("V")) {
                        if (x == 0)
                            dynamicSeamTable[x][y] = imgToCompute.getRGB(x, y);
                        else
                            dynamicSeamTable[x][y] = imgToCompute.getRGB(x, y) + Math.min(dynamicSeamTable[x - 1][y], dynamicSeamTable[x - 1][y + 1]);
                    }
                }else if(x==0){
                    if (direction.equals("H"))
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x, y) + Math.min(dynamicSeamTable[x][y - 1], dynamicSeamTable[x + 1][y - 1]);
                    if (direction.equals("V"))
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x, y);

                }else if (x == (maxX - 1)){
                    if (direction.equals("H"))
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x, y) + Math.min(dynamicSeamTable[x][y - 1], dynamicSeamTable[x - 1][y - 1]);

                }else if (y == (maxY-1)){
                    if(direction.equals("H"))
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x, y) + Math.min(dynamicSeamTable[x-1][y], dynamicSeamTable[x - 1][y - 1]);

                }else{
                    if(direction.equals("H"))
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x, y) + Math.min(dynamicSeamTable[x][y - 1], Math.min(dynamicSeamTable[x - 1][y - 1], dynamicSeamTable[x + 1][y - 1]));
                    if (direction.equals("V"))
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x, y) + Math.min(dynamicSeamTable[x - 1][y], Math.min(dynamicSeamTable[x - 1][y - 1], dynamicSeamTable[x -1][y +1]));
                }

            }
        }
        return dynamicSeamTable;
    }

    /** Compute the best seam last pixel coordinate (x or y, depending on direction).
     *
     * @param seamTable The seamTable providing all the seam available.
     * @param maxSize The size of the seam.
     * @param maxOtherSize The other direction's size of the initial image.
     * @param direction direction of resizing occuring.
     * @return the coordinate (x or y, depending on direction) of the best seam's last pixel.
     */
    private static int lowestSeamFirstCoord( long[][] seamTable,int maxSize, int maxOtherSize, String direction){
        int firstCoord = 0;

        for (int coor = 1; coor < maxOtherSize; coor++) {
            if (direction.equals("H")) {
                if (seamTable[firstCoord][maxSize - 1] > seamTable[coor][maxSize - 1]) {
                    firstCoord = coor;
                }
            } else { // direction "V"
                if (seamTable[maxSize - 1][firstCoord] > seamTable[maxSize - 1][coor]) {
                    firstCoord = coor;
                }

            }
        }
        //seam[maxSize - 1] = firstcoord;
        return firstCoord;
    }

    /** Compute following pixels coordinate (x or y, depending on direction) of the best seam.
     *
     * @param seamTable The seamTable providing all the seam available.
     * @param firstCoord the coordinate (x or y, depending on direction) of the best seam's last pixel.
     * @param maxSize The size of the seam.
     * @param maxOtherSize The other direction's size of the initial image.
     * @param direction direction of resizing occuring.
     * @return the best seam of the seamTable.
     */
    private static int[] lowestSeamNextLines( long[][] seamTable,int firstCoord, int maxSize, int maxOtherSize, String direction){
        int[] seam = new int[maxSize];
        seam[maxSize-1] = firstCoord;
        for (int i = (maxSize - 2); i > -1; i--){


                if (seam[i + 1] == 0)
                    //If on border-left, 2 pixels above him
                    if(direction.equals("H"))
                        seam[i] = (seamTable[0][i] <= seamTable[1][i]) ? 0 : 1;
                    else // direction "V"
                        seam[i] = (seamTable[i][0] <= seamTable[i][1]) ? 0 : 1;

                else if (seam[i + 1] == (maxOtherSize - 1))
                    //If on border-right, 2 pixels above him
                    if(direction.equals("H"))
                        seam[i] = (seamTable[maxOtherSize - 2][i] <= seamTable[maxOtherSize - 1][i]) ? maxOtherSize - 2 : maxOtherSize - 1;
                    else // direction "V"
                        seam[i] = (seamTable[i][maxOtherSize - 2] <= seamTable[i][maxOtherSize - 1]) ? maxOtherSize - 2 : maxOtherSize - 1;
                else {
                    //If not on a border, 3 pixels above him
                    if(direction.equals("H")) {
                        seam[i] = (seamTable[seam[i + 1] - 1][i] <= seamTable[seam[i + 1]][i]) ? (seam[i + 1] - 1) : seam[i + 1];
                        seam[i] = (seamTable[seam[i]][i] <= seamTable[seam[i + 1] + 1][i]) ? seam[i] : (seam[i + 1] + 1);
                    }else{ // direction "V"
                        seam[i] = (seamTable[i][seam[i + 1] - 1] <= seamTable[i][seam[i + 1]]) ? (seam[i + 1] - 1) : seam[i + 1];
                        seam[i] = (seamTable[i][seam[i]] <= seamTable[i][seam[i + 1] + 1]) ? seam[i] : (seam[i + 1] + 1);
                    }
                }

        }
        return seam;
    }

    /**
     * Isolate the best seam over the image.
     *
     * @param seamTable dynamic seam matrix.
     * @return a seam of seam.length elements of X coordinates (Seam is vertical, useful for width resizing)
     */
    private static int[] bestSeamFinder(long[][] seamTable, String direction) {
        int maxY = seamTable[0].length;
        int maxX = seamTable.length;
        int firstCoord;

        //Search in last line of energy table which one is the best
        //For every other pixel, we are looking for next X coordinates between pixels above the previous pixel found.

        if(direction.equals("H")) {

            firstCoord = lowestSeamFirstCoord(seamTable, maxY, maxX, direction);
            return lowestSeamNextLines(seamTable, firstCoord, maxY, maxX, direction);

        }else { // direction "V"

            firstCoord = lowestSeamFirstCoord(seamTable, maxX, maxY, direction);
            return lowestSeamNextLines(seamTable, firstCoord, maxX, maxY, direction);
        }

    }

    /**
     * Provide possibility to know the best seam in an image
     *
     * @param bImageEnergized is the bufferedImage energized to retrieve seams et find the best one.
     * @return seam of seam.length elements of X coordinates (Seam is vertical, useful for width resizing)
     */
    public static int[] bestSeam(BufferedImage bImageEnergized, String direction) {
        return bestSeamFinder(dynamicSeam(bImageEnergized, direction),direction);
    }

    /**
     * Retrieve the most relevant seam of your image.
     *
     * @param img  the normal BufferedImage where the seam is to retrieve
     * @param seam the seam to retrieve.
     * @return the image without the seam provided.
     */
    static BufferedImage seamVerticalDestroyer(BufferedImage img, int[] seam, String direction) {
        int maxX = img.getWidth();
        int maxY = img.getHeight();
        BufferedImage newBImage;
        if (direction.equals("H")) {
            newBImage = new BufferedImage(maxX - 1, maxY, BufferedImage.TYPE_INT_RGB);
            maxX = maxX - 1;
        }else{ //direction "V"
            newBImage = new BufferedImage(maxX, maxY - 1, BufferedImage.TYPE_INT_RGB);
            maxY = maxY-1;
        }

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x<maxX; x++) {



                    if (direction.equals("H")) {
                        if (seam[y] <= x) {
                            newBImage.setRGB(x, y, img.getRGB(x + 1, y));
                        } else {
                        newBImage.setRGB(x, y, img.getRGB(x, y));
                    }
                    } else { // direction V
                        if (seam[x] <= y) {
                            //System.out.println("("+x+","+y+")");
                            newBImage.setRGB(x, y, img.getRGB(x, y + 1));
                        } else {
                            newBImage.setRGB(x, y, img.getRGB(x, y));
                        }
                    }

            }
        }

        return newBImage;
    }


}

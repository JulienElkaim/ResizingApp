package operation;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import static java.lang.Math.abs;

public class ImageSeamComputation {

    /** This function provide public possibility to obtain an energy negative of their images.
     *
     * @param img the energy BufferedImage to gray.
     * @return BufferedImage that represent energized Image passed on a gray scale filter.
     */
    public static BufferedImage EnergizedImage(BufferedImage img){

        return ImageSeamComputation.grayOut(ImageSeamComputation.determineEnergy(img));

    }

    /** Transform your images with a gray filter.
     *
     * @param img is the BufferedImage to filter.
     * @return a copy of your BufferedImage in a gray Scale.
     */
    private static BufferedImage grayOut(BufferedImage img) {

        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(img, img);
        return img;
    }

    /** BUG BUG BUG: Need to compute energy for border pixel (x=2, y=2...)
     *
     * @param imgToCompute is the bufferedImage bas to compute the energy.
     * @return BufferedImage that represent energy of the initial image.
     */
    private static BufferedImage determineEnergy(BufferedImage imgToCompute){
        int maxX = imgToCompute.getWidth();
        int maxY = imgToCompute.getHeight();
        BufferedImage newBImage = SimpleOperation.cloningBufferedImage(imgToCompute);

        for (int x = 2; x < maxX-1; x++) {
            for (int y = 2; y < maxY-1; y++) {

                Color myLeftPixelColor = new Color(imgToCompute.getRGB(x - 1, y));
                Color myRightPixelColor = new Color(imgToCompute.getRGB(x + 1, y));
                Color myTopPixelColor = new Color(imgToCompute.getRGB(x, y - 1));
                Color myBottomPixelColor = new Color(imgToCompute.getRGB(x, y + 1));

                int energyRed;
                int energyGreen;
                int energyBlue;
                int energy;

                energyRed = abs(myLeftPixelColor.getRed()-myRightPixelColor.getRed())+abs(myTopPixelColor.getRed()-myBottomPixelColor.getRed());
                energyGreen = abs(myLeftPixelColor.getGreen()-myRightPixelColor.getGreen())+abs(myTopPixelColor.getGreen()-myBottomPixelColor.getGreen());
                energyBlue = abs(myLeftPixelColor.getBlue()-myRightPixelColor.getBlue())+abs(myTopPixelColor.getBlue()-myBottomPixelColor.getBlue());


                energy = (energyRed<<16) + (energyGreen<<8) + energyBlue;
                newBImage.setRGB(x,y,energy);
            }
        }
        return newBImage;
    }

    /** Provide Dynamic structure to find seams available on your Gray energy BufferedImage.
     *
     * @param imgToCompute is the energy BufferedImage source to retrieve seams pattern.
     * @return a matrix of long that represent a dynamic seam matrix
     */
    private static long[][] dynamicVerticalSeam(BufferedImage imgToCompute){
        int maxX = imgToCompute.getWidth();
        int maxY = imgToCompute.getHeight();
        long [][] dynamicSeamTable = new long [maxX][maxY];

        //seam computing
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (y == 0) {
                    // la toute premiere ligne
                    dynamicSeamTable[x][y] = imgToCompute.getRGB(x,y);
                }else{
                    // les autres lignes

                    // le pixel border-left de l'image
                    if (x==0){
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x,y) + Math.min(dynamicSeamTable[x][y-1], dynamicSeamTable[x+1][y-1]);

                    }else if(x==(maxX-1)){ // le pixel border-right de l'image
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x,y) + Math.min(dynamicSeamTable[x][y-1], dynamicSeamTable[x-1][y-1]);

                    }else{ // les pixels au milieu de la ligne
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x,y) + Math.min(dynamicSeamTable[x][y-1], Math.min(dynamicSeamTable[x-1][y-1],dynamicSeamTable[x+1][y-1] ) );
                    }
                }
            }
        }
        return dynamicSeamTable;
    }

    /** Isolate the best seam over the image.
     *
     * @param seamTable dynamic seam matrix.
     * @return a seam of seam.length elements of X coordinates (Seam is vertical, useful for width resizing)
     */
    private static int[] bestVerticalSeam(long[][] seamTable){
        int maxY = seamTable[0].length;
        int maxX = seamTable.length;
        int[] lowestSeamXCoordinates = new int [maxY];

        //Search in last line of energy table which one is the best
        for (int x=0 ; x < maxX;x++)
            if (x == 0)
                lowestSeamXCoordinates[maxY-1] = 0;
            else
                if(seamTable[lowestSeamXCoordinates[maxY-1] ][maxY-1] > seamTable[x][maxY-1])
                    lowestSeamXCoordinates[maxY-1] = x;

        //For every other pixel, we are looking for next X coordinates between pixels above the previous pixel found.
        for (int y=(maxY-2);y >-1; y--)
            if(lowestSeamXCoordinates[y+1]==0)
                //If on border-left, 2 pixels above him
                lowestSeamXCoordinates[y] = (seamTable[0][y] <= seamTable[1][y])? 0: 1 ;

            else if (lowestSeamXCoordinates[y+1]==(maxX-1) )
                //If on border-right, 2 pixels above him
                lowestSeamXCoordinates[y] = (seamTable[maxX-2][y] <= seamTable[maxX-1][y])? maxX-2: maxX-1 ;
            else{
                //If not on a border, 3 pixels above him
                lowestSeamXCoordinates[y] = (seamTable[lowestSeamXCoordinates[y+1]-1][y] <= seamTable[lowestSeamXCoordinates[y+1]][y])? (lowestSeamXCoordinates[y+1]-1): lowestSeamXCoordinates[y+1] ;
                lowestSeamXCoordinates[y] = (seamTable[lowestSeamXCoordinates[y]][y] <= seamTable[lowestSeamXCoordinates[y+1]+1][y])? lowestSeamXCoordinates[y]: (lowestSeamXCoordinates[y+1]+1) ;
            }

        return lowestSeamXCoordinates;
    }

    /** Provide possibility to know the best seam in an image
     *
     * @param bImageEnergized is the bufferedImage energized to retrieve seams et find the best one.
     * @return seam of seam.length elements of X coordinates (Seam is vertical, useful for width resizing)
     */
    public static int[]  bestSeam(BufferedImage bImageEnergized){
        return bestVerticalSeam(dynamicVerticalSeam(bImageEnergized));
    }

    /** Retrieve the most relevant seam of your image.
     *
     * @param img the normal BufferedImage where the seam is to retrieve
     * @param seam the seam to retrieve.
     * @return the image without the seam provided.
     */
    static BufferedImage seamVerticalDestroyer(BufferedImage img, int[] seam){
        int maxX = img.getWidth();
        int maxY = img.getHeight();
        BufferedImage newBImage = new BufferedImage(maxX-1, maxY, BufferedImage.TYPE_INT_RGB);

        for (int y=0; y < maxY; y++)
            for (int x=0; x < maxX-1; x++)
                if( seam[y] <=x )
                    newBImage.setRGB(x, y, img.getRGB(x+1, y));
                else
                    newBImage.setRGB(x, y, img.getRGB(x, y));

        return newBImage;
    }


}

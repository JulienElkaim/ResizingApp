package sample;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Slider;

import static java.lang.Math.abs;

public class SampleController {
    public Slider mySlider;
    public ImageView myImage;
    public Label helloWorld;
    public BufferedImage myBufferedImage;
    public BufferedImage myBufferedImageSTOCKED;



    /**
     * Provide an example of how manipulating the bufferedImage, COULD BE AN ALTERNATIVE FOR RESIZING
     */
    public void julienZoomingExample(ActionEvent actionEvent){
        int x =(int)(0.25*this.myBufferedImage.getWidth()) ;
        int y = (int)(0.25*this.myBufferedImage.getHeight());
        int width =(int)(0.5*this.myBufferedImage.getWidth()) ;
        int height = (int)(0.5*this.myBufferedImage.getHeight());
        this.myBufferedImage = this.myBufferedImage.getSubimage(x,y,width,height);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    /**
     * Provide an example of how resizing regarding the slider value
     */
    public void resizing(){
        double coef = (0.01<this.mySlider.getValue()/100)? abs(this.mySlider.getValue())/100:0.01 ;
        this.myImage.setScaleY(1);
        this.myImage.setScaleX(coef);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage,null));
    }

    public void ChooseAFile(ActionEvent actionEvent) throws IOException {

        // Choosing the file to input
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPEG Files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("./img/"));
        File file = fileChooser.showOpenDialog(null);

        //Displaying the image chosen.
        this.myBufferedImageSTOCKED = ImageIO.read(file);
        this.myBufferedImage = this.myBufferedImageSTOCKED;
        Image image = SwingFXUtils.toFXImage(this.myBufferedImage, null);
        this.myImage.setImage(image);

    }

    /**
     * The gradient is the difference between the intensity of the same colour in the left and in the right pixel
     */

    public BufferedImage createGradient(String couleur){
        int maxX = this.myBufferedImage.getWidth();
        int maxY = this.myBufferedImage.getHeight();
        this.myBufferedImageSTOCKED = this.myBufferedImage;

        for (int x = 2; x < maxX-1; x++) {
            for (int y = 1; y < maxY; y++) {

                Color myPixelColor = new Color(this.myBufferedImageSTOCKED.getRGB(x, y));

                //pixel à gauche
                Color myLeftPixelColor = new Color(this.myBufferedImageSTOCKED.getRGB(x - 1, y));
                //pixel à droite
                Color myRightPixelColor = new Color(this.myBufferedImageSTOCKED.getRGB(x + 1, y));

                int decalage = 0;
                int Left;
                int Right;
                //changement de couleur  //8 pour green, 16 pour rouge, rien pour bleu
                if (couleur == "red") { //rouge
                    Left = myLeftPixelColor.getRed();
                    Right = myRightPixelColor.getRed();
                    decalage = 16;
                    //gradient = (gradient << 16);
                } else if (couleur == "green") { //vert
                    Left = myLeftPixelColor.getGreen();
                    Right = myRightPixelColor.getGreen();
                    decalage = 8;
                    //gradient = (gradient << 8);
                } else {
                    Left = myLeftPixelColor.getBlue();
                    Right = myRightPixelColor.getBlue();
                }
                int gradient = abs(Left-Right);
                if (gradient > 255) {
                    gradient = 255;
                }
                gradient = (gradient << decalage);
                this.myBufferedImage.setRGB(x,y,gradient);
            }
        }
        return this.myBufferedImage;
    }

    public void redGradient(ActionEvent actionEvent){
        this.myBufferedImage = createGradient("red");
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

    public void greenGradient(ActionEvent actionEvent){
        this.myBufferedImage = createGradient("green");
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

    public void blueGradient(ActionEvent actionEvent){
        this.myBufferedImage = createGradient("blue");
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

    public void createCropImage() {
        double coef = (0.01<this.mySlider.getValue()/100)? abs(this.mySlider.getValue())/100:0.01 ;
        int width = this.myBufferedImageSTOCKED.getWidth() ;
        int height = this.myBufferedImageSTOCKED.getHeight();
        BufferedImage dest = this.myBufferedImage.getSubimage(0, 0, (int) (coef*width), height);
        this.myImage.setImage(SwingFXUtils.toFXImage(dest, null));

    }
    private BufferedImage cloningBufferedImage(BufferedImage bImage) {
        ColorModel cm = bImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bImage.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * This method convert the color image into grayscale image
     */
    public BufferedImage grayOut(BufferedImage img) {
        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace
                .getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(img, img);

        return img;
    }

    public BufferedImage determineEnergy(){
        int maxX = this.myBufferedImage.getWidth();
        int maxY = this.myBufferedImage.getHeight();
        this.myBufferedImage = cloningBufferedImage(this.myBufferedImageSTOCKED);
        for (int x = 2; x < maxX-1; x++) {
            for (int y = 2; y < maxY-1; y++) {

                //pixel à gauche
                Color myLeftPixelColor = new Color(this.myBufferedImageSTOCKED.getRGB(x - 1, y));
                //pixel à droite
                Color myRightPixelColor = new Color(this.myBufferedImageSTOCKED.getRGB(x + 1, y));
                //top pixel
                Color myTopPixelColor = new Color(this.myBufferedImageSTOCKED.getRGB(x, y - 1));
                //bottom pixel
                Color myBottomPixelColor = new Color(this.myBufferedImageSTOCKED.getRGB(x, y + 1));

                int energyRed;
                int energyGreen;
                int energyBlue;
                int energy;

                energyRed = abs(myLeftPixelColor.getRed()-myRightPixelColor.getRed())+abs(myTopPixelColor.getRed()-myBottomPixelColor.getRed());
                energyGreen = abs(myLeftPixelColor.getGreen()-myRightPixelColor.getGreen())+abs(myTopPixelColor.getGreen()-myBottomPixelColor.getGreen());
                energyBlue = abs(myLeftPixelColor.getBlue()-myRightPixelColor.getBlue())+abs(myTopPixelColor.getBlue()-myBottomPixelColor.getBlue());


                energy = (energyRed<<16) + (energyGreen<<8) + energyBlue;
                this.myBufferedImage.setRGB(x,y,energy);
            }
        }
        return this.myBufferedImage;
    }

    public void energyMap(ActionEvent actionEvent){
        this.myBufferedImage = grayOut(determineEnergy());
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

    /**
     * This method gets the index of min element in input array
     * @return int  --> minIndex
     */
    public int getMinIndex(double[] numbers){
        double minValue = numbers[0];
        int minIndex = 0;
        for(int i=0;i<numbers.length;i++){
            if(numbers[i] < minValue){
                minValue = numbers[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * This method gets the min value in input array
     * @return double  --> minValue
     */
    public double getMinValue(double[] numbers){
        double minValue = numbers[0];
        for(int i=0;i<numbers.length;i++){
            if(numbers[i] < minValue){
                minValue = numbers[i];
            }
        }
        return minValue;
    }

    /**
     * This method calculates the cumulative energy array
     * @return double[][]  --> cumulative_energy_array
     */
    public double[][] getCumulativeEnergyArray (BufferedImage img){
        int width = img.getWidth();
        int height = img.getHeight();
        double[][] cumulativeEnergyArray = new double[height][width];

        for (int y = 1; y < height; ++y){
            for (int x = 1; x < width-1; ++x){
                cumulativeEnergyArray[y][x] = (double)img.getRaster().getSample(x,y,0);
            }
        }

        for (int y = 1; y < height; ++y){
            for (int x = 1; x < width-1; ++x){
                double temp = 0.0;
                double tempArray3[] = new double[3];
                tempArray3[0] = cumulativeEnergyArray[y-1][x-1];
                tempArray3[1] = cumulativeEnergyArray[y-1][x];
                tempArray3[2] = cumulativeEnergyArray[y-1][x+1];
                temp = getMinValue(tempArray3) + (double)img.getRaster().getSample(x,y,0);
                cumulativeEnergyArray[y][x] = temp;
            }
        }
        return cumulativeEnergyArray;
    }

    /**
     * This method finds the minimum cost path from
     * cumulative energy array
     * @return int[]  --> path
     */
    public int[] findPath (double[][] cumulativeEnergyArray) {
        int width = cumulativeEnergyArray[0].length;
        int height = cumulativeEnergyArray.length;
        int[] path = new int[height];

        double[] tempArray = new double[width - 10];
        int y = height - 1;
        for (int x = 5; x < width - 5; ++x) {
            tempArray[x - 5] = cumulativeEnergyArray[y][x];
        }

        int ind_bot = getMinIndex(tempArray) + 5;
        //System.out.println("\nThe bottom index is: " + ind_bot);
        path[height - 1] = ind_bot;

        int ind_temp = 0;
        double[] tempArray2 = new double[3];
        for (int i = height - 1; i > 0; --i) {
            tempArray2[0] = cumulativeEnergyArray[i - 1][path[i] - 1];
            tempArray2[1] = cumulativeEnergyArray[i - 1][path[i]];
            tempArray2[2] = cumulativeEnergyArray[i - 1][path[i] + 1];
            ind_temp = getMinIndex(tempArray2);
            path[i - 1] = path[i] + ind_temp - 1;
            if (path[i - 1] <= 0) {
                path[i - 1] = 1;
            } else if (path[i - 1] >= width - 1) {
                path[i - 1] = width - 2;
            }
        }
        return path;
    }

    /**
     * This method remove the path from input image
     * @return BufferedImage  --> removePathImg
     */
    public BufferedImage removePathFromImage(BufferedImage img, int[] path){
        int type = img.getType();
        int width = img.getWidth();
        int height = img.getHeight();
        int band = 3;
        BufferedImage removePathImg = new BufferedImage(width-1, height, type);
        WritableRaster raster = removePathImg.getRaster();

        for (int b = 0; b < band; ++b){
            for (int y = 0; y < height; ++y){
                for (int x = 0; x <= path[y]-2; ++x){
                    double temp = 0.0;
                    temp = img.getRaster().getSample(x, y, b);
                    raster.setSample(x, y, b, Math.round(temp));
                }
                for (int x = path[y]-1; x < width-1; ++x){
                    double temp = 0.0;
                    temp = img.getRaster().getSample(x+1, y, b);
                    raster.setSample(x, y, b, Math.round(temp));
                }
            }
        }
        return removePathImg;
    }
    /**
     * This method remove the path from energy array
     * @return double[][]  --> new_cumulativeEnergyArray
     */
    public double[][] removePathEnergyArray(double[][] cumulativeEnergyArray, int[] path){
        int width = cumulativeEnergyArray[0].length;
        int height = cumulativeEnergyArray.length;
        double[][] new_cumulativeEnergyArray = new double[height][width-1];
        for (int y = 0; y < height; ++y){
            for (int x = 0; x <= path[y]-1; ++x){
                new_cumulativeEnergyArray[y][x] = cumulativeEnergyArray[y][x];
            }
            for (int x = path[y]; x < width-1; ++x){
                new_cumulativeEnergyArray[y][x] = cumulativeEnergyArray[y][x+1];
            }
        }
        return new_cumulativeEnergyArray;
    }

    public void seamCarving(){
        double[][] cumulativeEnergyArray = getCumulativeEnergyArray(determineEnergy());
        double zoomingCoef = this.mySlider.getValue()/100  *(0.1-1) +1;
        while (cumulativeEnergyArray[0].length>zoomingCoef*this.myBufferedImageSTOCKED.getWidth()){
            int[] path = findPath(cumulativeEnergyArray);
            this.myBufferedImage = removePathFromImage(this.myBufferedImage, path);
            cumulativeEnergyArray = removePathEnergyArray(cumulativeEnergyArray, path);
        }
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

}



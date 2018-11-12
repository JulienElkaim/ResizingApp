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

}



package sample;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    public void julienResizingExample(){
       double coef = (0.01<this.mySlider.getValue()/100)? this.mySlider.getValue()/100:0.01 ;
       int widthInitial = this.myBufferedImageSTOCKED.getWidth();
       int heightInitial = this.myBufferedImageSTOCKED.getHeight();
       this.myImage.maxHeight(500);
       this.myImage.maxWidth(500);
       this.myImage.setFitHeight(coef*heightInitial);
       this.myImage.setFitWidth(coef*widthInitial);

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
                int redLeft = myLeftPixelColor.getRed();

                //pixel à droite
                Color myRightPixelColor = new Color(this.myBufferedImageSTOCKED.getRGB(x + 1, y));
                int redRight = myRightPixelColor.getRed();

                int gradient = abs(redLeft - redRight);
                if (gradient > 255) { //8 pour transparent, 16 pour rouge, 24 pour green, rien pour bleu
                    gradient = 255;
                }

                //changement de couleur  //8 pour transparent, 16 pour rouge, 24 pour green, rien pour bleu
                if (couleur == "red") { //rouge
                    gradient = (gradient << 16);
                } else if (couleur == "green") { //vert
                    gradient = (gradient << 24);
                }
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

}



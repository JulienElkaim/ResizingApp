package sample;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.activation.ActivationInstantiator;

import javafx.scene.control.Slider;

import javax.imageio.ImageIO;

import static java.lang.Math.abs;

public class SampleController {
    //private KeyCode keyPressed;
    private String sliderListener="";
    public Slider mySlider;
    public ImageView myImage;
    public Label helloWorld;
    public BufferedImage myBufferedImage;
    public BufferedImage myBufferedImageSTOCKED;

    public void initialize(){
        System.out.println("lol");

        //Reaction when  the Slider value is changed
        this.mySlider.valueProperty().addListener((ov, old_val, new_val) -> this.listenSliderChange());


    }

    private void listenSliderChange() {

        //The function called will change accordingly to the last listener activated
        switch(this.sliderListener){
            case "Resizing":
                this.resizing();
                break;
            case "Zooming Example":
                this.zoom();
                break;
        }

    }

    /**
     * Provide an example of how manipulating the bufferedImage, COULD BE AN ALTERNATIVE FOR RESIZING
     */
    public void zoom(){
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

    public void saveAFile(ActionEvent actionEvent) throws IOException{

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG Files (*.jpg)", "*.jpg");

        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.getExtensionFilters().add(extFilter);
        saveFileChooser.setInitialDirectory(new File("./img/"));
        saveFileChooser.setInitialFileName("AfterMyModifications");
        File file = saveFileChooser.showSaveDialog(null);

        ImageIO.write(this.myBufferedImage, "jpg", file);

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
                int Left = 0;
                int Right = 0;
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


    public void Switcher(ActionEvent actionEvent) {
       this.sliderListener = this.getText(actionEvent.getSource().toString());
    }

    private String getText (String src){
        String myStr="";
        char currentChar;
        for (int i=src.length()-2; i> -1; i--){

            currentChar = src.charAt(i);
            if (currentChar=='\''){
                i = -1;
                continue;
            }
            myStr = currentChar + myStr;

        }
        return myStr;
    }

}



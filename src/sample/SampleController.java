package sample;
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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javafx.scene.control.Slider;

import static java.lang.Math.abs;

public class SampleController {
    private KeyCode keyPressed;
    private String sliderListener="";
    public Slider mySlider;
    public ImageView myImage;
    public Label helloWorld;

    public BufferedImage myBufferedImage;
    public BufferedImage myBufferedImageSTOCKED;

    public void initialize(){



        //Reaction when  the Slider value is changed
        this.mySlider.valueProperty().addListener((ov, old_val, new_val) -> this.listenSliderChange(ov, old_val, new_val));


    }

    private void listenSliderChange(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

        //The function called will change accordingly to the last listener activated
        switch(this.sliderListener){
            case "Resizing":
                this.resizing();
                break;
            case "Zooming Example":
                this.unZoom();

                double coefViewReal = this.myImage.getFitWidth()/this.myBufferedImage.getWidth();
                double widthView = this.myBufferedImage.getWidth()*coefViewReal;
                double heightView = this.myBufferedImage.getHeight()*coefViewReal;
                this.zoom( 0.5*widthView, 0.5*heightView );
                break;
        }

    }
    private BufferedImage cloningBufferedImage(BufferedImage bImage){
        ColorModel cm = bImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bImage.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);

    }
    private void unZoom(){

        this.myBufferedImage = cloningBufferedImage(this.myBufferedImageSTOCKED);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    private void zoom(double X, double Y){
        //zooming power is 30% of the initial image

        double zoomingCoef = this.mySlider.getValue()/100  *(0.1-1) +1;// 1* -0.9 +1
        //double zoomingCoef = this.mySlider.getValue()/100;
        double initWidth = this.myBufferedImage.getWidth();
        double initHeight = this.myBufferedImage.getHeight();

        double coefViewReal = this.myImage.getFitWidth()/initWidth;

        double XReal = X/coefViewReal;
        double YReal = Y/coefViewReal;

        double x = Math.max(0, XReal - (zoomingCoef/2)* initWidth);
        double y = Math.max(0 , YReal - (zoomingCoef/2)* initHeight);

        int width =(int)(zoomingCoef*this.myBufferedImage.getWidth()) ;
        int height = (int)(zoomingCoef*this.myBufferedImage.getHeight());

        while (x+width >= this.myBufferedImage.getWidth()){
            x-=1;
        }
        while (y+height >= this.myBufferedImage.getHeight()){
            y-=1;
        }

        this.myBufferedImage = this.myBufferedImage.getSubimage((int)x,(int)y,width,height);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }


    /**
     * Provide an example of how resizing regarding the slider value
     */
    public void resizing(){
        double coef = (0.01<this.mySlider.getValue()/100)? abs(this.mySlider.getValue())/100:0.01 ;
        int width = (int) (coef*this.myBufferedImage.getWidth());
        int height = this.myBufferedImage.getHeight();
        BufferedImage dest = scale(this.myBufferedImage,width, height);
        this.myImage.setImage(SwingFXUtils.toFXImage(dest,null));
    }

    public static BufferedImage scale(BufferedImage src, int width, int height){
        BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dest.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
                (double)width/src.getWidth(),
                (double)height/src.getHeight());
        g.drawRenderedImage(src,at);
        return dest;
    }

    public void ChooseAFile() throws IOException {

        // Choosing the file to input
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPEG Files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("./img/"));
        File file = fileChooser.showOpenDialog(null);

        //Displaying the image chosen.
        this.myBufferedImageSTOCKED = ImageIO.read(file);

        this.myBufferedImage = cloningBufferedImage(this.myBufferedImageSTOCKED);

        Image image = SwingFXUtils.toFXImage(this.myBufferedImage, null);
        this.myImage.setImage(image);

    }

    public void saveAFile() throws IOException{

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

    public void createGradient(String couleur){
        int maxX = this.myBufferedImage.getWidth();
        int maxY = this.myBufferedImage.getHeight();


        for (int x = 2; x < maxX-1; x++) {
            for (int y = 1; y < maxY; y++) {

                //pixel à droite
                Color myRightPixelColor = new Color(this.myBufferedImageSTOCKED.getRGB(x + 1, y));
                //pixel à gauche
                Color myLeftPixelColor = new Color(this.myBufferedImageSTOCKED.getRGB(x - 1, y));


                int decalage = 0;
                int Left;
                int Right;
                //changement de couleur  //8 pour green, 16 pour rouge, rien pour bleu
                switch (couleur) {
                    case "red":  //rouge
                        Left = myLeftPixelColor.getRed();
                        Right = myRightPixelColor.getRed();
                        decalage = 16;
                        //gradient = (gradient << 16);
                        break;
                    case "green":  //vert
                        Left = myLeftPixelColor.getGreen();
                        Right = myRightPixelColor.getGreen();
                        decalage = 8;
                        //gradient = (gradient << 8);
                        break;
                    default:
                        Left = myLeftPixelColor.getBlue();
                        Right = myRightPixelColor.getBlue();
                        break;
                }
                int gradient = abs(Left-Right);
                if (gradient > 255) {
                    gradient = 255;
                }
                gradient = (gradient << decalage);
                this.myBufferedImage.setRGB(x,y,gradient);

            }
        }

    }

    public void redGradient(){
        createGradient("red");
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

    public void greenGradient(){
        createGradient("green");
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

    public void blueGradient(){
        createGradient("blue");
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }


    public void Switcher(ActionEvent actionEvent) {
       this.sliderListener = this.getText(actionEvent.getSource().toString());
    }

    private String getText (String src){
        StringBuilder myStr= new StringBuilder();
        char currentChar;
        for (int i=src.length()-2; i> -1; i--){

            currentChar = src.charAt(i);
            if (currentChar=='\''){
                i = -1;
                continue;
            }
            myStr.insert(0, currentChar);

        }
        return myStr.toString();
    }


    public void keyPressedRemove() {
        this.keyPressed = null;
    }

    public void keyPressedAdd(KeyEvent keyEvent) {
        this.keyPressed = keyEvent.getCode();
    }

    public void imageClicked(MouseEvent mouseEvent) {

        if (this.keyPressed == KeyCode.SHIFT)
            this.unZoom();
        else
            this.zoom(mouseEvent.getX() - this.myImage.getX(), mouseEvent.getY() - this.myImage.getY());

    }

    public void createCropImage() {
        double coef = (0.01<this.mySlider.getValue()/100)? abs(this.mySlider.getValue())/100:0.01 ;
        this.myBufferedImage = cloningBufferedImage(this.myBufferedImageSTOCKED);
        int width = this.myBufferedImage.getWidth() ;
        int height = this.myBufferedImage.getHeight();
        int newWidth = (int) (coef*width);
        BufferedImage dest = this.myBufferedImageSTOCKED.getSubimage(0, 0, newWidth, height);
        System.out.println("Height is equal to:" + height);
        System.out.println("New width is equal to:" + newWidth);
        this.myImage.setImage(SwingFXUtils.toFXImage(dest, null));

    }

}



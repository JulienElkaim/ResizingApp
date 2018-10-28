package sample;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Slider;

import static java.awt.Color.*;

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
     * Provide an example of how coloring an image based on its RGB value
     */
    public void julienRedColoringExample(ActionEvent actionEvent) {
        int maxX = this.myBufferedImage.getWidth();
        int maxY = this.myBufferedImage.getHeight();

        for (int x = 1; x < maxX; x++){
            for(int y = 1; y < maxY; y++){

                Color myPixelColor = new Color(this.myBufferedImage.getRGB(x,y));
                int red = myPixelColor.getRed();

                double coef = (this.mySlider.getValue()+50)/100 ;
                red = (255 < (int)(red*coef))? 255: (int)(red*coef);

                int blue = myPixelColor.getBlue();
                int green = myPixelColor.getGreen();
                int pixel =(red << 16) + (green << 8) + blue;
                this.myBufferedImage.setRGB(x,y,pixel);
            }
        }
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
     * Laurence Gradient way
     * */
    /*
    public  BufferedImage createGradientMask(int width, int height, int orientation, Color color) {
        BufferedImage gradient = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = this.myBufferedImage.createGraphics();
        GradientPaint paint = new GradientPaint(0.0f, 0.0f, BLACK, orientation == SwingConstants.HORIZONTAL? width : 0.0f,
                orientation == SwingConstants.VERTICAL? height : 0.0f, color);
        g.setPaint(paint);
        g.fill(new Rectangle2D.Double(0, 0, width, height));

        g.dispose();
        this.myBufferedImage.flush();

        return this.myBufferedImage;
    }

    public void greenGradient(ActionEvent actionEvent) {
        //Displaying the gradient.
        BufferedImage bufferedImage;
        bufferedImage = this.createGradientMask(900, 1200, 0, GREEN);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        this.myImage.setImage(image);
    }*/


}



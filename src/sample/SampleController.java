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

import static java.awt.Color.*;

public class SampleController {
    private int count=0;
    public ImageView myImage;
    public Label helloWorld;


    /** Say Hello to the world and increment a counter.
     *  Now used by 3 buttons and a slider.
     * @param actionEvent When button is pressed
     */
    public void sayHelloWorld(ActionEvent actionEvent) {

        helloWorld.setFont(new Font("Arial",10)); //seems to do not work.
        count++;
        helloWorld.setText("Hello the world ! " + count);

    }


    public void ChooseAFile(ActionEvent actionEvent) throws IOException {

        // Choosing the file to input
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPEG Files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);

        //Displaying the image chosen.
        BufferedImage bufferedImage;
        bufferedImage = ImageIO.read(file);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        this.myImage.setImage(image);

    }

    public void performTaskRequested(ActionEvent actionEvent) {

        // ============= BASE DE TEST pour resizing, possibilite; de l'effacer

        //Image img = this.boximage.getImage();

        //double w = img.getWidth();
        //double h = img.getHeight();

        //BufferedImage dimg = new BufferedImage((int)w+1, (int)h+1, BufferedImage.TYPE_INT_ARGB);
        //Image image = SwingFXUtils.toFXImage(dimg, null);
        //this.myImage.setImage(image);


        //Just to show the slider is in use.
        this.sayHelloWorld(new ActionEvent());
        }

    public static BufferedImage createGradientMask(int width, int height, int orientation, Color color) {
        BufferedImage gradient = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = gradient.createGraphics();
        GradientPaint paint = new GradientPaint(0.0f, 0.0f, BLACK, orientation == SwingConstants.HORIZONTAL? width : 0.0f,
                orientation == SwingConstants.VERTICAL? height : 0.0f, color);
        g.setPaint(paint);
        g.fill(new Rectangle2D.Double(0, 0, width, height));

        g.dispose();
        gradient.flush();

        return gradient;
    }

    public void greenGradient(ActionEvent actionEvent) {
        //Displaying the gradient.
        BufferedImage bufferedImage;
        bufferedImage = this.createGradientMask(900, 1200, 0, GREEN);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        this.myImage.setImage(image);
    }

    public void redGradient(ActionEvent actionEvent) {
        //Displaying the gradient.
        BufferedImage bufferedImage;
        bufferedImage = this.createGradientMask(900, 1200, 0, RED);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        this.myImage.setImage(image);
    }

    public void blueGradient(ActionEvent actionEvent) {
        //Displaying the gradient.
        BufferedImage bufferedImage;
        bufferedImage = this.createGradientMask(900, 1200, 0, BLUE);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        this.myImage.setImage(image);
    }
}



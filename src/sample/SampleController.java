package sample;
import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


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

    public void performTaskRequested(MouseEvent mouseEvent) {

        // ============= BASE DE TEST pour resizing, possibilite; de l'efacer

        //Image img = this.boximage.getImage();

        //double w = img.getWidth();
        //double h = img.getHeight();

        //BufferedImage dimg = new BufferedImage((int)w+1, (int)h+1, BufferedImage.TYPE_INT_ARGB);
        //Image image = SwingFXUtils.toFXImage(dimg, null);
        //this.myImage.setImage(image);


        //Just to show the slider is in use.
        this.sayHelloWorld(new ActionEvent());
    }

}


package imageChange;

import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import java.io.IOException;
import javafx.scene.control.Slider;
import operation.*;

import static java.lang.Math.abs;
import javafx.scene.shape.Rectangle;

public class Controller {

    //Slider objects
    public Slider mySlider;
    public Label sliderListenerLabel;
    private String sliderListener="";

    //Hoovered objects
    public Rectangle redG;
    public Rectangle greenG;
    public Rectangle blueG;
    public Label seamPrintingLabel;
    public Label energyPrintingLabel;

    //Image objects
    public ImageView myImage;
    private BufferedImage myBufferedImage;
    private BufferedImage myBufferedImageSTOCKED;

    //Conceptual objects
    private KeyCode keyPressed;
    private String tempImg;



    public void initialize(){

        this.tempImg ="null";
        this.initializeGradientItems();
        this.initializeSliderItems();


    }
    private void initializeSliderItems(){

        this.sliderListener = "Zoom";
        this.sliderListenerLabel.setText(this.labelDisplayActualListner(this.sliderListener));
        this.mySlider.valueProperty().addListener(this::ListenSlider);

    }

    private void initializeGradientItems(){

        this.redG.setFill(javafx.scene.paint.Color.RED);
        this.greenG.setFill(javafx.scene.paint.Color.GREEN);
        this.blueG.setFill(javafx.scene.paint.Color.BLUE);

    }

    private void ListenSlider(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

        switch(this.sliderListener){
            case "Resize":
                this.resizeDisplayedImage(new_val.doubleValue());
                break;

            case "Zoom":
                //Just for visualisation, the use of ZOOM with the slider is done by clicking on the imageView
                break;

            case "Seam Carving":
                this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);
                this.seamCarveDisplayedImage(new_val.intValue());
                break;

            case"Crop":
                this.cropDisplayedImage(new_val.doubleValue());
        }
    }



    private void resetViewModifications(){

        this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    /**
     * Provide an example of how resizing regarding the slider value
     */
    private void resizeDisplayedImage( double sliderValue ){

        this.myBufferedImage = SimpleOperation.cloningBufferedImage( ImageResize.resizingWidth( this.myBufferedImageSTOCKED,sliderValue ) );
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage,null));

    }

    private void cropDisplayedImage( double sliderValue ){

        this.myBufferedImage = ImageResize.croppingWidth( this.myBufferedImageSTOCKED , sliderValue );
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage,null));

    }

    private void zoomDisplayedImage(double X, double Y, double sliderValue){

        double viewHeight = this.myImage.getFitHeight();
        this.myBufferedImage = ImageResize.zoom(this.myBufferedImage, viewHeight, "H", X, Y, sliderValue);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage,null));

    }

    private void seamCarveDisplayedImage(int nbOfSeam){
        BufferedImage img =  ImageResize.SeamCarving(nbOfSeam, this.myBufferedImage);

        this.myBufferedImage = SimpleOperation.cloningBufferedImage(img);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

    public void ChooseAFile()throws IOException {

        this.myBufferedImageSTOCKED = ImageIO.read(operation.SimpleOperation.imageFileOpen());
        this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
        this.labelDisplayActualListner(this.sliderListener);

    }

    public void saveAFile() throws IOException{
        SimpleOperation.imageFileSave(this.myBufferedImage);
    }

    public void Switcher(ActionEvent actionEvent) {
       this.sliderListener = SimpleOperation.getText(actionEvent.getSource().toString());
       this.sliderListenerLabel.setText(
               this.labelDisplayActualListner(this.sliderListener));
    }

    public void keyPressedRemove() {
        this.keyPressed = null;
    }

    public void keyPressedAdd(KeyEvent keyEvent) {
        this.keyPressed = keyEvent.getCode();
    }

    public void imageClicked(MouseEvent mouseEvent) {

        if (this.keyPressed == KeyCode.SHIFT)
            this.resetViewModifications();
        else if (this.sliderListener.equals("Zoom"))
            this.zoomDisplayedImage(mouseEvent.getX() - this.myImage.getX(), mouseEvent.getY() - this.myImage.getY(), this.mySlider.getValue());

    }

    private void energyImageDisplay (BufferedImage imgToEnergize,String label, boolean doWePrintSeam ){

        this.seamPrintingLabel.setTextFill(javafx.scene.paint.Color.BLACK);

        if (this.tempImg.equals("Energy computation") || this.tempImg.equals("Show next seam")){
            this.seamPrintingLabel.setTextFill(javafx.scene.paint.Color.PALEVIOLETRED);
            this.myImage.setImage(SwingFXUtils.toFXImage(imgToEnergize, null));
            this.tempImg = "null";

        }else{

            if (this.myBufferedImage!=null)
                this.energyPrintingLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            assert this.myBufferedImage != null;
            BufferedImage bImageEnergized = ImageSeamComputation.EnergizedImage(this.myBufferedImage);

            if (doWePrintSeam) {
                int totalRedRGB = 255 << 16;
                int[] seamToPrint = ImageSeamComputation.bestSeam(bImageEnergized);
                for (int y = 0; y < seamToPrint.length; y++)
                    bImageEnergized.setRGB(seamToPrint[y], y, totalRedRGB);
            }

            this.myImage.setImage(SwingFXUtils.toFXImage(bImageEnergized, null));
            this.tempImg = label;
        }
    }

    public void imageVerticalSeamDisplay(){
        this.energyImageDisplay(this.myBufferedImage,"Show next seam", true);
    }

    public void imageEnergyDisplay(){
        this.energyImageDisplay(this.myBufferedImage, "Energy computation", false);
    }



    public void redGSwitcher (){
        gradientSwitcher("redG");
    }
    public void greenGSwitcher (){
        gradientSwitcher("greenG");
    }
    public void blueGSwitcher (){
        gradientSwitcher("blueG");
    }

    private void redGradient(){
        BufferedImage img = Gradient.createGradient("red",this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    private void greenGradient(){
        BufferedImage img = Gradient.createGradient("green",this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    private void blueGradient(){
        BufferedImage img = Gradient.createGradient("blue",this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }


    private void gradientSwitcher(String colorGradient) {

        if(this.tempImg.equals(colorGradient)){
            this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
            this.tempImg="null";
        }else{

            switch (colorGradient) {
                case "redG":
                    this.redGradient();
                    break;
                case "greenG":
                    this.greenGradient();
                    break;
                case "blueG":
                    this.blueGradient();
                    break;
            }
            this.tempImg = colorGradient;
        }
    }

    public void makeNewOperationPersistant() {
        int maxX = this.myBufferedImage.getWidth();
        int maxY = this.myBufferedImage.getHeight();
        this.myBufferedImageSTOCKED = new BufferedImage(maxX, maxY,BufferedImage.TYPE_INT_RGB);

        for (int y=0; y < maxY; y++)
            for (int x=0; x < maxX; x++)
                this.myBufferedImageSTOCKED.setRGB(x,y, this.myBufferedImage.getRGB(x,y));
    }

    private String labelDisplayActualListner(String functionName){
        if (this.myBufferedImage==null)
            return " Please load an Image to process ! ";
        else
            return "Actually Using : " + functionName;
    }
}


package application;

import java.awt.*;
import java.awt.image.BufferedImage;

import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import tools.*;
import utils.Utils;

import static java.lang.Math.abs;

class View {

    private String direction = "H";
    public String tempImg = "null";
    String sliderListener = "Zoom";
    private double initFitHeight = 0.0;

    private GradientPainter gradientPainter = new GradientPainter();
    private SeamCarver seamCarver = new SeamCarver();
    private Resizer resizer = new Resizer();
    private Cropper cropper = new Cropper();
    private Zoomer zoomer = new Zoomer();
    private Colorizer colorizer = new Colorizer();

    public String getDirection() {
        return this.direction;
    }

    private double setInitFitHeight(ImageView myImage){
        return myImage.getFitHeight();
    }

    void switchDirection(MenuItem menu, Label directionLabel, Label sliderLabel, ImageView image,
                         BufferedImage buffer) {
        if (this.direction.equals("H")) { // actually in width, go in height mode

            menu.setText("Switch direction -> Width   CRTL+D");
            directionLabel.setText("Actual direction: Height");
            double idealFitWidth = (image.getFitHeight() / buffer.getHeight()) * buffer.getWidth();
            image.setFitWidth(idealFitWidth);
            image.fitHeightProperty().setValue(null);
            this.direction = "V";

        } else { // actually in height, go in width mode

            menu.setText("Switch direction -> Height  CTRL+D");
            directionLabel.setText("Actual direction: Width");
            double idealFitHeight = (image.getFitWidth() / buffer.getWidth()) * buffer.getHeight();
            image.setFitHeight(idealFitHeight);
            image.fitWidthProperty().setValue(null);
            this.direction = "H";
        }
        sliderLabel.setText(this.updateSliderLabel());

    }

    /** Actualize the label displayed to notify the direction we are working on.
     *
     * @return the label to display on the application window relative to the slider
     */
    String updateSliderLabel(){
        if (this.direction.equals("H"))
            return "Percentage of Width : ";
        if (this.direction.equals("V"))
            return "Percentage of Height : ";
        return "/!\\ Direction issue! Please relaunch the App /!\\";
    }


    /**
     * Apply a red GradientPainter computation on the displayed image.
     */
    private void redGradient(BufferedImage myBufferedImage, ImageView myImage) {
        gradientPainter.setGradientColor(Color.RED);
        BufferedImage img = gradientPainter.process(myBufferedImage);
        myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    /**
     * Apply a green GradientPainter computation on the displayed image.
     */
    private void greenGradient(BufferedImage myBufferedImage, ImageView myImage) {
        gradientPainter.setGradientColor(Color.GREEN);
        BufferedImage img = gradientPainter.process(myBufferedImage);
        myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    /**
     * Apply a blue GradientPainter computation on the displayed image.
     */
    private void blueGradient(BufferedImage myBufferedImage, ImageView myImage) {
        gradientPainter.setGradientColor(Color.BLUE);
        BufferedImage img = gradientPainter.process(myBufferedImage);
        myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    /**
     * Trigger a GradientPainter computation based on the color choice provided.
     *
     * @param colorGradient the color choice (RGB) to determine the GradientPainter used.
     */
    void gradientSwitcher(String colorGradient, ImageView myImage, BufferedImage bufferedImage) {

        if (this.tempImg.equals(colorGradient)) {
            myImage.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
            this.tempImg = "null";
        } else {

            switch (colorGradient) {
                case "redG":
                    this.redGradient(bufferedImage, myImage);
                    break;
                case "greenG":
                    this.greenGradient(bufferedImage, myImage);
                    break;
                case "blueG":
                    this.blueGradient(bufferedImage, myImage);
                    break;
            }
            this.tempImg = colorGradient;
        }
    }

    /**
     * Method to display an energy computation on an image, with the most relevant seam if necessary.
     *
     * @param imgToEnergize the actual displayed image.
     * @param label         the label hoovered that trigger the function.
     * @param doWePrintSeam only if necessary to display the seam.
     */
    void energyImageDisplay(BufferedImage imgToEnergize, String label, boolean doWePrintSeam, ImageView myImage,
                            Label seamPrintingLabel, Label energyPrintingLabel) {
        seamPrintingLabel.setTextFill(javafx.scene.paint.Color.BLACK);
        if (this.tempImg.equals("Energy computation") || this.tempImg.equals("Show next seam")) {
            seamPrintingLabel.setTextFill(javafx.scene.paint.Color.PALEVIOLETRED);
            myImage.setImage(SwingFXUtils.toFXImage(imgToEnergize, null));
            this.tempImg = "null";
        } else {
            if (imgToEnergize != null)
                energyPrintingLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            assert imgToEnergize != null;
            BufferedImage bImageEnergized = SeamCarver.EnergizedImage(imgToEnergize);
            if (doWePrintSeam) {
                int totalRedRGB = 255 << 16;
                int[] seamToPrint = SeamCarver.bestSeam(bImageEnergized, this.getDirection());
                if (this.getDirection().equals("H")) {
                    for (int y = 0; y < seamToPrint.length; y++)
                        bImageEnergized.setRGB(seamToPrint[y], y, totalRedRGB);
                }else{
                    for (int x = 0; x < seamToPrint.length; x++)
                        bImageEnergized.setRGB( x,seamToPrint[x], totalRedRGB);
                }
            }
            myImage.setImage(SwingFXUtils.toFXImage(bImageEnergized, null));
            this.tempImg = label;
        }
    }

    /**
     * Trigger zooming process.
     * @param x           is the x-coordinate of the mouse pointer when click occurred.
     * @param y           is the y-coordinate of the mouse pointer when click occurred.
     * @param sliderValue is the actual value of the slider
     */
    void zoomDisplayedImage(double x, double y, double sliderValue, BufferedImage myBufferedImage, ImageView myImage) {
        this.zoomer.setCoef(sliderValue);
        this.zoomer.setDirection(this.getDirection());
        this.zoomer.setX(x);
        this.zoomer.setY(y);
        //to set viewSize, direction needs to be chosen before
        this.zoomer.setViewSize(myImage);
        myBufferedImage = this.zoomer.process(myBufferedImage);
        myImage.setImage(SwingFXUtils.toFXImage(myBufferedImage, null));
    }

    /**
     * Trigger the resizing process.
     * @param sliderValue is the actual value of the slider.
     */
    void resizeDisplayedImage(double sliderValue, BufferedImage myBufferedImageSTOCKED, ImageView myImage) {
        this.resizer.setCoef(sliderValue);
        this.resizer.setDirection(this.getDirection());
        BufferedImage myBufferedImage = Utils.clone(this.resizer.process(myBufferedImageSTOCKED));
        myImage.setImage(SwingFXUtils.toFXImage(myBufferedImage, null));
    }

    /**
     * Trigger the cropping process.
     * @param sliderValue is the actual value of the slider.
     */
    void cropDisplayedImage(double sliderValue, BufferedImage myBufferedImageSTOCKED, ImageView myImage) {
        this.cropper.setCoef(sliderValue);
        this.cropper.setDirection(this.getDirection());
        BufferedImage myBufferedImage = this.cropper.process(myBufferedImageSTOCKED);
        myImage.setImage(SwingFXUtils.toFXImage(myBufferedImage, null));
    }

    /** Display X and Y position of the pointer.
     * @param X is the x-coordinate on the imageView.
     * @param Y is the y-coordinate on the imageView.
     */
    void updatePointerPositionLabel(double X, double Y, BufferedImage myBufferedImage, ImageView myImage,
                                    Label pointerPositionLabel){
        double coefViewReal;
        if (this.getDirection().equals("H"))
            coefViewReal = myImage.getFitHeight()/myBufferedImage.getHeight();
        else // direction "V"
            coefViewReal = myImage.getFitWidth()/myBufferedImage.getWidth();
        pointerPositionLabel.setText("| x : "+(int)(X/coefViewReal)+" y : "+(int)(Y/coefViewReal));
    }


    /**
     * This method initialize the GradientItems used to display GradientPainter computation.
     */
    void initializeGradientItems(Rectangle redG, Rectangle greenG, Rectangle blueG) {
        redG.setFill(javafx.scene.paint.Color.RED);
        greenG.setFill(javafx.scene.paint.Color.GREEN);
        blueG.setFill(javafx.scene.paint.Color.BLUE);
    }

    /** Increase/Decrease the color selected (basic graphical change).
     *
     * @param ov      is the observable value of the slider.
     * @param old_val is the previous value of the slider.
     * @param new_val is the actual value of the slider.
     * @param color is the color R/G/B used for coloring process.
     */
    void Colorize(ObservableValue<? extends Number> ov, Number old_val, Number new_val, Color color,
                  BufferedImage myBufferedImage, ImageView myImage){
        double coefColor = (new_val.doubleValue()/100 +0.5) / (old_val.doubleValue()/100+0.5);
        this.colorizer.setChangeColor(color);
        this.colorizer.setRatio(coefColor);
        myBufferedImage= Utils.clone( this.colorizer.process(myBufferedImage));
        myImage.setImage(SwingFXUtils.toFXImage(myBufferedImage,null));
    }
}

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
import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.scene.control.Slider;
import operation.*;
import javafx.scene.shape.Rectangle;

public class Controller {

    //Slider objects
    public Slider mySlider;
    public Label sliderListenerLabel;
    private String sliderListener = "";

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


    /**
     * INITIALIZE method is called after the fxml creation. Useful to set environment variables.
     */
    public void initialize() {

        this.tempImg = "null";
        this.initializeGradientItems();
        this.initializeSliderItems();


    }

    /**
     * This method initializes the sliderItems used in the application
     */
    private void initializeSliderItems() {

        this.sliderListener = "Zoom";
        this.sliderListenerLabel.setText(this.labelDisplayActualListner(this.sliderListener));
        this.mySlider.valueProperty().addListener(this::ListenSlider);

    }

    /**
     * This method initialize the GradientItems used to display Gradient computation.
     */
    private void initializeGradientItems() {

        this.redG.setFill(javafx.scene.paint.Color.RED);
        this.greenG.setFill(javafx.scene.paint.Color.GREEN);
        this.blueG.setFill(javafx.scene.paint.Color.BLUE);

    }

    /**
     * Function called when the Slider's value changes, notifications are not used because we chose a "toggle framework".
     *
     * @param ov      is the observable value of the slider.
     * @param old_val is the previous value of the slider.
     * @param new_val is the actual value of the slider.
     */
    private void ListenSlider(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

        switch (this.sliderListener) {
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

            case "Crop":
                this.cropDisplayedImage(new_val.doubleValue());
        }
    }


    /**
     * Reset actual modification that occurred on the image.
     * The last persistent change saved is displayed.
     * only callable by SHIFT + LEFT CLICK
     */
    private void resetViewModifications() {

        this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    /**
     * Trigger the resizing process.
     *
     * @param sliderValue is the actual value of the slider.
     */
    private void resizeDisplayedImage(double sliderValue) {

        this.myBufferedImage = SimpleOperation.cloningBufferedImage(ImageResizeWidth.resizing(this.myBufferedImageSTOCKED, sliderValue));
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    /**
     * Trigger the cropping process.
     *
     * @param sliderValue is the actual value of the slider.
     */
    private void cropDisplayedImage(double sliderValue) {

        this.myBufferedImage = ImageResizeWidth.cropping(this.myBufferedImageSTOCKED, sliderValue);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    /**
     * Trigger zooming process.
     *
     * @param X           is the x-coordinate of the mouse pointer when click occurred.
     * @param Y           is the y-coordinate of the mouse pointer when click occurred.
     * @param sliderValue is the actual value of the slider
     */
    private void zoomDisplayedImage(double X, double Y, double sliderValue) {

        double viewHeight = this.myImage.getFitHeight();
        this.myBufferedImage = ImageResizeWidth.zoom(this.myBufferedImage, viewHeight, "H", X, Y, sliderValue);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    /**
     * Trigger Seam Carving process.
     *
     * @param nbOfSeam is the number of occurrence of the process.
     */
    private void seamCarveDisplayedImage(int nbOfSeam) {
        BufferedImage img = ImageResizeWidth.SeamCarving(nbOfSeam, this.myBufferedImage);

        this.myBufferedImage = SimpleOperation.cloningBufferedImage(img);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

    /**
     * Trigger Opening file process.
     *
     * @throws IOException when it fails to read the file path provided.
     */
    public void ChooseAFile() throws IOException {

        this.myBufferedImageSTOCKED = ImageIO.read(operation.SimpleOperation.imageFileOpen());
        this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
        this.sliderListenerLabel.setText(this.labelDisplayActualListner(this.sliderListener));

    }

    /**
     * Trigger Saving image process.
     *
     * @throws IOException when it fails to write a file from myBufferedImage.
     */
    public void saveAFile() throws IOException {
        SimpleOperation.imageFileSave(this.myBufferedImage);
    }

    /**
     * Allow to switch the listener of the slider.
     *
     * @param actionEvent is the Button action, with all the button relative information.
     */
    public void Switcher(ActionEvent actionEvent) {
        this.sliderListener = SimpleOperation.getText(actionEvent.getSource().toString());
        this.sliderListenerLabel.setText(this.labelDisplayActualListner(this.sliderListener));
    }

    /**
     * Called when a key is released.
     * Erase memory of this key.
     */
    public void keyPressedRemove() {
        this.keyPressed = null;
    }

    /**
     * Called when a key is pressed.
     * Set memory of this key.
     *
     * @param keyEvent is the action of the key pressed, with all the key relative information.
     */
    public void keyPressedAdd(KeyEvent keyEvent) {
        this.keyPressed = keyEvent.getCode();
    }

    /**
     * Called when the image displayed is clicked.
     *
     * @param mouseEvent is the click mouse event, with all the mouse-click relative information.
     */
    public void imageClicked(MouseEvent mouseEvent) {

        if (this.keyPressed == KeyCode.SHIFT)
            this.resetViewModifications();
        else if (this.sliderListener.equals("Zoom"))
            this.zoomDisplayedImage(mouseEvent.getX() - this.myImage.getX(), mouseEvent.getY() - this.myImage.getY(), this.mySlider.getValue());

    }

    /**
     * Method to display an energy computation on an image, with the most relevant seam if necessary.
     *
     * @param imgToEnergize the actual displayed image.
     * @param label         the label hoovered that trigger the function.
     * @param doWePrintSeam only if necessary to display the seam.
     */
    private void energyImageDisplay(BufferedImage imgToEnergize, String label, boolean doWePrintSeam) {

        this.seamPrintingLabel.setTextFill(javafx.scene.paint.Color.BLACK);

        if (this.tempImg.equals("Energy computation") || this.tempImg.equals("Show next seam")) {
            this.seamPrintingLabel.setTextFill(javafx.scene.paint.Color.PALEVIOLETRED);
            this.myImage.setImage(SwingFXUtils.toFXImage(imgToEnergize, null));
            this.tempImg = "null";

        } else {

            if (this.myBufferedImage != null)
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

    /**
     * Display the energy map of the displayed image and the most relevant seam.
     */
    public void imageVerticalSeamDisplay() {
        this.energyImageDisplay(this.myBufferedImage, "Show next seam", true);
    }

    /**
     * Display the energy map of the displayed image.
     */
    public void imageEnergyDisplay() {
        this.energyImageDisplay(this.myBufferedImage, "Energy computation", false);
    }


    /**
     * Triggered when red square is hoovered.
     */
    public void redGSwitcher() {
        gradientSwitcher("redG");
    }

    /**
     * Triggered when green square is hoovered.
     */
    public void greenGSwitcher() {
        gradientSwitcher("greenG");
    }

    /**
     * Triggered when blue square is hoovered.
     */
    public void blueGSwitcher() {
        gradientSwitcher("blueG");
    }


    /**
     * Apply a red Gradient computation on the displayed image.
     */
    private void redGradient() {
        BufferedImage img = Gradient.createGradient("red", this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    /**
     * Apply a green Gradient computation on the displayed image.
     */
    private void greenGradient() {
        BufferedImage img = Gradient.createGradient("green", this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    /**
     * Apply a blue Gradient computation on the displayed image.
     */
    private void blueGradient() {
        BufferedImage img = Gradient.createGradient("blue", this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }


    /**
     * Trigger a Gradient computation based on the color choice provided.
     *
     * @param colorGradient the color choice (RGB) to determine the Gradient used.
     */
    private void gradientSwitcher(String colorGradient) {

        if (this.tempImg.equals(colorGradient)) {
            this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
            this.tempImg = "null";
        } else {

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

    /**
     * Save the last modification on the displayed image in order to chain modifications
     * without removing previous ones.
     */
    public void makeNewOperationPersistent() {
        int maxX = this.myBufferedImage.getWidth();
        int maxY = this.myBufferedImage.getHeight();
        this.myBufferedImageSTOCKED = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < maxY; y++)
            for (int x = 0; x < maxX; x++)
                this.myBufferedImageSTOCKED.setRGB(x, y, this.myBufferedImage.getRGB(x, y));
    }

    /**
     * Actualize the label displayed to notify users of the actual active function.
     *
     * @param functionName is the Function actually listening the slider.
     * @return the label to display on the application window.
     */
    private String labelDisplayActualListner(String functionName) {
        if (this.myBufferedImage == null)
            return " Please load an Image to process ! ";
        else
            return "Actually Using : " + functionName;
    }
}


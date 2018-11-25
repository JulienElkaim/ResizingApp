package application;

import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Slider;
import tools.*;
import javafx.scene.shape.Rectangle;

import static java.lang.Math.abs;

public class Controller {

    /** MENU:
     * 1. Attributes.
     * 2. Initialization methods.
     * 3. Listener and special-event-related methods.
     * 4. Switch methods.
     * 5. Image modification methods.
     * 6. Open & Save images methods..
     * 7. Reset & Save modifications methods.
     * 8. Seam Carving vizualization methods.
     * 9. Label actualization methods.
     */

    @FXML private MenuItem directionMenu;
    @FXML private Rectangle redG;
    @FXML private Rectangle greenG;
    @FXML private Rectangle blueG;
    @FXML private Label sliderLabel;
    @FXML private Slider mySlider;
    @FXML private Label sliderListenerLabel;
    @FXML private Label energyPrintingLabel;
    @FXML private Label seamPrintingLabel;
    @FXML private Slider redSlider;
    @FXML private Slider greenSlider;
    @FXML private Slider blueSlider;
    @FXML private ImageView myImage;
    @FXML private Label directionLabel;
    @FXML private Label pointerPositionLabel;

    //Slider objects
    private String direction;
    private String sliderListener = "";

    //Image objects
    private BufferedImage myBufferedImage;
    private BufferedImage myBufferedImageSTOCKED;

    //Conceptual objects
    private KeyCode keyPressed;
    private String tempImg;
    private Resizer resizer = new Resizer();
    private double initFitHeight;

    private GradientPainter gradientPainter = new GradientPainter();
    private Colorizer colorizer = new Colorizer();
    private FileManager fileManager = new FileManager();


    /**
     * INITIALIZE method is called after the fxml creation. Useful to set environment variables.
     */
    public void initialize() {
        this.initFitHeight = this.myImage.getFitHeight();
        directionMenu.setText("Switch direction -> Height   CTRL+D");
        this.direction="H";
        this.tempImg = "null";
        this.initializeGradientItems();
        this.initializeSliderItems();
        this.initializeColorSlidersItems();

        this.myImage.setOnMouseMoved(t -> updatePointerPositionLabel(t.getX() - this.myImage.getX(), t.getY() - this.myImage.getX()));

    }

    /**
     * This method initializes the color sliders items used in the application.
     */
    private void initializeColorSlidersItems() {
        this.redSlider.valueProperty().addListener(this::onColorizedRedChanged);

        this.greenSlider.valueProperty().addListener(this::onColorizedGreenChanged);

        this.blueSlider.valueProperty().addListener(this::onColorizedBlueChanged);

    }


    /**
     * This method initializes the sliderItems used in the application
     */
    private void initializeSliderItems() {

        this.sliderListener = "Zoom";
        this.sliderLabel.setText(this.updateSliderLabel());
        this.sliderListenerLabel.setText(this.updateListnerLabel(this.sliderListener));
        this.mySlider.valueProperty().addListener(this::ListenSlider);

    }

    /**
     * This method initialize the GradientItems used to display GradientPainter computation.
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
                this.seamCarveDisplayedImage(new_val.doubleValue());
                break;

            case "Crop":
                this.cropDisplayedImage(new_val.doubleValue());
        }
    }

    /** Listen the blue slider.
     *
     * @param ov      is the observable value of the slider.
     * @param old_val is the previous value of the slider.
     * @param new_val is the actual value of the slider.
     */
    private void onColorizedBlueChanged(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
        Colorize(ov, old_val, new_val, Color.BLUE);
    }

    /** Listen the green slider.
     *
     ** @param ov      is the observable value of the slider.
     *      * @param old_val is the previous value of the slider.
     *      * @param new_val is the actual value of the slider.
     */
    private void onColorizedGreenChanged(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
        Colorize(ov, old_val, new_val, Color.GREEN);
    }

    /** Listen the red slider.
     *
     * @param ov      is the observable value of the slider.
     * @param old_val is the previous value of the slider.
     * @param new_val is the actual value of the slider.
     */
    private void onColorizedRedChanged(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
        Colorize(ov, old_val, new_val, Color.RED);
    }

    /** Increase/Decrease the color selected (basic graphical change).
     *
     * @param ov      is the observable value of the slider.
     * @param old_val is the previous value of the slider.
     * @param new_val is the actual value of the slider.
     * @param color is the color R/G/B used for coloring process.
     */
    private void Colorize(ObservableValue<? extends Number> ov, Number old_val, Number new_val, Color color){
        double coefColor = (new_val.doubleValue()/100 +0.5) / (old_val.doubleValue()/100+0.5);

        this.colorizer.setChangeColor(color);
        this.colorizer.setRatio(coefColor);

        this.myBufferedImage= SimpleOperation.cloningBufferedImage( this.colorizer.process(this.myBufferedImage));
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage,null));
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
     * Called when a key is released.
     * Erase memory of this key.
     */
    public void keyPressedRemove() {
        this.keyPressed = null;
    }

    /**
     * Called when a key is pressed.
     * Set memory of this key, and react for special key combination.
     *
     * @param keyEvent is the action of the key pressed, with all the key relative information.
     */
    public void keyPressed(KeyEvent keyEvent) {
        this.keyPressed = keyEvent.getCode();
        //(CMD)CTRL+W will switch the current direction used
        if (keyEvent.getCode() == KeyCode.D && (keyEvent.isControlDown()  || keyEvent.isShortcutDown()))
            this.directionSwitch();
        //(CMD)CTRL+C save modifications to continue working on it
        if (keyEvent.getCode() == KeyCode.C && (keyEvent.isControlDown() || keyEvent.isShortcutDown()) )
            this.saveViewModifications();
        //(CMD)CTRL+N propose to open a new image
        if (keyEvent.getCode() == KeyCode.N && (keyEvent.isControlDown() || keyEvent.isShortcutDown()) )
            this.openFile();
        //(CMD)CTRL+SHIFT+S propose to save your image.
        if (keyEvent.getCode() == KeyCode.S && keyEvent.isShiftDown() && (keyEvent.isControlDown() || keyEvent.isShortcutDown()) ){
            this.saveFile();

        }

    }


    /**
     * Allow to switch the listener of the slider.
     *
     * @param actionEvent is the Button action, with all the button relative information.
     */
    public void switcherListener(ActionEvent actionEvent) {
        this.sliderListener = SimpleOperation.getButtonText(actionEvent.getSource().toString());
        this.sliderListenerLabel.setText(this.updateListnerLabel(this.sliderListener));
    }

    /** Actualize all the parameters tied to the direction.
     * myImage.fit(Height?Width?) -> We fix the direction not modified.
     * direction -> string showing the direction choosed. "V" is Height, "H" is width.
     * directionButto.text -> As indication, display the direction that will be modify if we click it.
     */
    public void directionSwitch() {
        if (this.direction.equals("H")) { //actually in width, go in height mode

            this.directionMenu.setText("Switch direction -> Width   CRTL+D");
            this.directionLabel.setText("Actual direction: Height");
            double idealFitWidth = (this.myImage.getFitHeight()/this.myBufferedImage.getHeight()) * this.myBufferedImage.getWidth();
            this.myImage.setFitWidth(idealFitWidth);
            this.myImage.fitHeightProperty().setValue(null);
            this.direction = "V";

        }else{ // actually in height, go in width mode

            this.directionMenu.setText("Switch direction -> Height  CTRL+D");
            this.directionLabel.setText("Actual direction: Width");
            double idealFitHeight = (this.myImage.getFitWidth()/this.myBufferedImage.getWidth()) * this.myBufferedImage.getHeight();
            this.myImage.setFitHeight(idealFitHeight);
            this.myImage.fitWidthProperty().setValue(null);
            this.direction = "H";
        }
        this.sliderLabel.setText(this.updateSliderLabel());
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
     * Trigger a GradientPainter computation based on the color choice provided.
     *
     * @param colorGradient the color choice (RGB) to determine the GradientPainter used.
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
     * Apply a red GradientPainter computation on the displayed image.
     */
    private void redGradient() {
        gradientPainter.setGradientColor(Color.RED);
        BufferedImage img = gradientPainter.process(this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    /**
     * Apply a green GradientPainter computation on the displayed image.
     */
    private void greenGradient() {
        gradientPainter.setGradientColor(Color.GREEN);
        BufferedImage img = gradientPainter.process(this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    /**
     * Apply a blue GradientPainter computation on the displayed image.
     */
    private void blueGradient() {
        gradientPainter.setGradientColor(Color.BLUE);
        BufferedImage img = gradientPainter.process(this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    /**
     * Trigger the resizing process.
     *
     * @param sliderValue is the actual value of the slider.
     */
    private void resizeDisplayedImage(double sliderValue) {

        this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.resizer.resizing(this.myBufferedImageSTOCKED, sliderValue, this.direction));
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    /**
     * Trigger the cropping process.
     *
     * @param sliderValue is the actual value of the slider.
     */
    private void cropDisplayedImage(double sliderValue) {

        this.myBufferedImage = this.resizer.cropping(this.myBufferedImageSTOCKED, sliderValue, this.direction);
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
        double viewSize;
        if(this.direction.equals("H"))
            viewSize = this.myImage.getFitHeight();
        else
            viewSize = this.myImage.getFitWidth();
        this.myBufferedImage = this.resizer.zoom(this.myBufferedImage, viewSize, this.direction, X, Y, sliderValue);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    /**
     * Trigger Seam Carving process.
     *
     * @param sliderValue is the percentage of width to display.
     */
    private void seamCarveDisplayedImage(double sliderValue) {
        double coef =  (0.01 < sliderValue / 100) ? abs(sliderValue) / 100 : 0.01; // Slider a 100: 100%, Slider a 0: 10%
        int actualReferenceSize;
        if(this.direction.equals("H"))
            actualReferenceSize= this.myBufferedImage.getWidth();
        else // direction "V"
            actualReferenceSize = this.myBufferedImage.getHeight();

        int nbOfSeamToDestroy = actualReferenceSize - (int)(coef*actualReferenceSize);
        BufferedImage img = this.resizer.SeamCarving(nbOfSeamToDestroy, this.myBufferedImage, this.direction);

        this.myBufferedImage = SimpleOperation.cloningBufferedImage(img);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

    /**
     * Trigger Opening file process.
     */
    @FXML
    private void openFile(){
        try{ this.myBufferedImageSTOCKED = ImageIO.read(this.fileManager.imageFileOpen());}
        catch(IOException e){System.out.println("Error occured during the openning process!");}
        catch(IllegalArgumentException e){System.out.println("No file choosed !");}


        this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);
        this.myImage.setFitHeight(initFitHeight);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
        this.sliderListenerLabel.setText(this.updateListnerLabel(this.sliderListener));

    }

    /**
     * Trigger Saving image process.
     */
    @FXML
    private void saveFile(){
        this.fileManager.imageFileSave(this.myBufferedImage);
    }
    /**
     * Reset actual modification that occurred on the image.
     * The last persistent change saved is displayed.
     * only callable by SHIFT + LEFT CLICK
     */
    public void resetViewModifications() {

        this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    /**
     * Save the last modification on the displayed image in order to chain modifications
     * without removing previous ones.
     */
    public void saveViewModifications() {
        int maxX = this.myBufferedImage.getWidth();
        int maxY = this.myBufferedImage.getHeight();
        this.myBufferedImageSTOCKED = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < maxY; y++)
            for (int x = 0; x < maxX; x++)
                this.myBufferedImageSTOCKED.setRGB(x, y, this.myBufferedImage.getRGB(x, y));
    }

    /**
     * Display the energy map of the displayed image and the most relevant seam.
     */
    public void imageSeamDisplay() {
        this.energyImageDisplay(this.myBufferedImage, "Show next seam", true);
    }

    /**
     * Display the energy map of the displayed image.
     */
    public void imageEnergyDisplay() {
        this.energyImageDisplay(this.myBufferedImage, "Energy computation", false);
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
            BufferedImage bImageEnergized = SeamCarver.EnergizedImage(this.myBufferedImage);

            if (doWePrintSeam) {
                int totalRedRGB = 255 << 16;
                int[] seamToPrint = SeamCarver.bestSeam(bImageEnergized, this.direction);
                if (this.direction.equals("H")) {
                    for (int y = 0; y < seamToPrint.length; y++)
                        bImageEnergized.setRGB(seamToPrint[y], y, totalRedRGB);
                }else{
                    for (int x = 0; x < seamToPrint.length; x++)
                        bImageEnergized.setRGB( x,seamToPrint[x], totalRedRGB);
                }
            }

            this.myImage.setImage(SwingFXUtils.toFXImage(bImageEnergized, null));
            this.tempImg = label;
        }
    }

    /**
     * Actualize the label displayed to notify users of the actual active function.
     *
     * @param functionName is the Function actually listening the slider.
     * @return the label to display on the application window.
     */
    private String updateListnerLabel(String functionName) {
        if (this.myBufferedImage == null)
            return " Please load an Image to process ! ";
        else
            return "Actually Using : " + functionName;
    }

    /** Actualize the label displayed to notify the direction we are working on.
     *
     * @return the label to display on the application window relative to the slider
     */
    private String updateSliderLabel(){

        if (this.direction.equals("H"))
            return "Percentage of Width : ";
        if (this.direction.equals("V"))
            return "Percentage of Height : ";
        return "/!\\ Direction issue ! Please relaunch the App /!\\";

    }

    /** Open the readme file to help user.
     *
     * @throws IOException when README is not available.
     */
    public void helpMe()throws IOException{
        File file = new File("./README.md");

        if(!Desktop.isDesktopSupported()){
            System.out.println("Your computer have restricted access. Please open README.md manually.");
        }else {
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) desktop.open(file);
        }


    }

    /** Display X and Y position of the pointer.
     *
     * @param X is the x-coordinate on the imageView.
     * @param Y is the y-coordinate on the imageView.
     */
    private void updatePointerPositionLabel(double X, double Y){
        double coefViewReal;
        if (this.direction.equals("H"))
            coefViewReal = this.myImage.getFitHeight()/this.myBufferedImage.getHeight();
        else // direction "V"
            coefViewReal = this.myImage.getFitWidth()/this.myBufferedImage.getWidth();
        this.pointerPositionLabel.setText("| x : "+(int)(X/coefViewReal)+" y : "+(int)(Y/coefViewReal));
    }

    /** Reset the pointer label when the mouse exit the image area.
     *
     */
    public void pointerLabelReset() {
        this.pointerPositionLabel.setText("| x : - y : -");
    }


}


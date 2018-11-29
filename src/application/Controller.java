package application;

import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import tools.SeamCarver;
import utils.FileManager;
import utils.UserHelper;
import utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Controller {

    /**
     * MENU: 1. Attributes. 2. Initialization methods. 3. Listener and
     * special-event-related methods. 4. Switch methods. 5. Image modification
     * methods. 6. Open & Save images methods.. 7. Reset & Save modifications
     * methods. 8. Seam Carving visualization methods. 9. Label actualization
     * methods.
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

    // Image objects
    private BufferedImage myBufferedImage;
    private BufferedImage myBufferedImageStocked;

    private View view = new View();

    private FileManager fileManager = new FileManager();
    private UserHelper userHelper = new UserHelper();
    private SeamCarver seamCarver = new SeamCarver();

    /**
     * INITIALIZE method is called after the fxml creation. Useful to set
     * environment variables.
     */
    public void initialize() {
        directionMenu.setText("Switch direction -> Height");
        view.initializeGradientItems(redG, greenG, blueG);
        this.initializeSliderItems();
        this.initializeColorSlidersItems();
        this.myImage.setOnMouseMoved(t -> view.updatePointerPositionLabel(
                t.getX() - this.myImage.getX(), t.getY() - this.myImage.getX(),
                this.myBufferedImage, this.myImage, pointerPositionLabel));
    }

    /**
     * This method initializes the color sliders items used in the application.
     */
    private void initializeColorSlidersItems() {
        this.redSlider.valueProperty().addListener(this::onColorizedRedChanged);
        this.greenSlider.valueProperty()
                .addListener(this::onColorizedGreenChanged);
        this.blueSlider.valueProperty()
                .addListener(this::onColorizedBlueChanged);
    }

    /**
     * This method initializes the sliderItems used in the application.
     */
    private void initializeSliderItems() {
        this.sliderLabel.setText(this.view.updateSliderLabel());
        this.sliderListenerLabel
                .setText(this.updateListenerLabel(view.currentTool));
        this.mySlider.valueProperty().addListener(this::onSliderChanged);
    }

    /**
     * Function called when the Slider's value changes, notifications are not
     * used because we chose a "toggle framework".
     *
     * @param ov      is the observable value of the slider.
     * @param old_val is the previous value of the slider.
     * @param new_val is the actual value of the slider.
     */
    private void onSliderChanged(ObservableValue<? extends Number> ov,
                                 Number old_val, Number new_val) {
        switch (view.currentTool) {
            case "Resize":
                this.myBufferedImage = Utils.clone(view.resizeDisplayedImage(new_val.doubleValue(),
                        this.myBufferedImageStocked, this.myImage));
                break;
            case "Zoom":
                // Just for visualisation, the use of ZOOM with the slider is
                // done by clicking on the imageView
                break;
            case "Seam carving":
                this.myBufferedImage = Utils.clone(this.view.carveSeams(
                        this.seamCarver, new_val.doubleValue(),
                        this.myBufferedImageStocked, this.myImage));
                break;
            case "Crop":
                this.myBufferedImage = Utils.clone(view.cropDisplayedImage(new_val.doubleValue(),
                        this.myBufferedImageStocked, this.myImage));
                break;
            default:
                System.err.println("Unexpected tool name: " + view.currentTool);
        }
    }

    /**
     * Listen the blue slider.
     *
     * @param ov      is the observable value of the slider.
     * @param old_val is the previous value of the slider.
     * @param new_val is the actual value of the slider.
     */
    private void onColorizedBlueChanged(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {
        view.colorize(ov, old_val, new_val, Color.BLUE, this.myBufferedImage,
                this.myImage);
    }

    /**
     * Listen the green slider.
     *
     * @param ov      is the observable value of the slider.
     * @param old_val is the previous value of the slider.
     * @param new_val is the actual value of the slider.
     */
    private void onColorizedGreenChanged(ObservableValue<? extends Number> ov,
                                         Number old_val, Number new_val) {
        view.colorize(ov, old_val, new_val, Color.GREEN, this.myBufferedImage,
                this.myImage);
    }

    /**
     * Listen the red slider.
     *
     * @param ov      is the observable value of the slider.
     * @param old_val is the previous value of the slider.
     * @param new_val is the actual value of the slider.
     */
    private void onColorizedRedChanged(ObservableValue<? extends Number> ov,
                                       Number old_val, Number new_val) {
        view.colorize(ov, old_val, new_val, Color.RED, this.myBufferedImage,
                this.myImage);
    }

    /**
     * Called when the image displayed is clicked.
     *
     * @param mouseEvent is the click mouse event, with all the mouse-click relative
     *                   information.
     */
    @FXML
    private void imageClicked(MouseEvent mouseEvent) {
        if (view.currentTool.equals("Zoom"))
            view.zoomDisplayedImage(mouseEvent.getX() - this.myImage.getX(),
                    mouseEvent.getY() - this.myImage.getY(),
                    this.mySlider.getValue(), this.myBufferedImage,
                    this.myImage);
    }

    /**
     * Allow to switch the listener of the slider.
     *
     * @param actionEvent is the Button action, with all the button relative
     *                    information.
     */
    @FXML
    private void switcherListener(ActionEvent actionEvent) {
        view.currentTool = Utils
                .getButtonText(actionEvent.getSource().toString());
        this.sliderListenerLabel
                .setText(this.updateListenerLabel(view.currentTool));
    }

    /**
     * Actualize all the parameters tied to the direction.
     * myImage.fit(Height?Width?) -> We fix the direction not modified.
     * direction -> string showing the direction chosen. "V" is Height, "H" is
     * width. directionButton.text -> As indication, display the direction that
     * will be modify if we click it.
     */
    @FXML
    private void directionSwitch() {
        this.view.switchDirection(this.directionMenu, this.directionLabel,
                this.sliderLabel, this.myImage, this.myBufferedImage);
    }

    /**
     * Triggered when red square is hovered.
     */
    @FXML
    private void redGSwitcher() {
        view.gradientSwitcher("redG", this.myImage, this.myBufferedImage);
    }

    /**
     * Triggered when green square is hovered.
     */
    @FXML
    private void greenGSwitcher() {
        view.gradientSwitcher("greenG", this.myImage, this.myBufferedImage);
    }

    /**
     * Triggered when blue square is hoovered.
     */
    @FXML
    private void blueGSwitcher() {
        view.gradientSwitcher("blueG", this.myImage, this.myBufferedImage);
    }

    /**
     * Trigger Opening file process.
     */
    @FXML
    private void openFile() {
        try {
            this.myBufferedImageStocked = ImageIO
                    .read(this.fileManager.imageFileOpen());
        } catch (IOException e) {
            System.out.println("Error occurred during the opening process!");
        } catch (IllegalArgumentException e) {
            System.out.println("No file chosen!");
        }

        this.myBufferedImage = Utils.clone(this.myBufferedImageStocked);
        this.myImage
                .setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
        this.sliderListenerLabel
                .setText(this.updateListenerLabel(view.currentTool));
    }

    /**
     * Trigger Saving image process.
     */
    @FXML
    private void saveFile() {
        this.fileManager.imageFileSave(this.myBufferedImage);
    }

    /**
     * Reset actual modification that occurred on the image. The last persistent
     * change saved is displayed. only callable by SHIFT + LEFT CLICK
     */
    @FXML
    private void resetViewModifications() {
        this.myBufferedImage = Utils.clone(this.myBufferedImageStocked);
        this.myImage
                .setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }

    /**
     * Save the last modification on the displayed image in order to chain
     * modifications without removing previous ones.
     */
    @FXML
    private void saveViewModifications() {
        int maxX = this.myBufferedImage.getWidth();
        int maxY = this.myBufferedImage.getHeight();
        BufferedImage img = new BufferedImage(maxX, maxY,
                BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < maxY; y++)
            for (int x = 0; x < maxX; x++){
                img.setRGB(x, y,
                        this.myBufferedImage.getRGB(x, y));}
        this.myBufferedImage = Utils.clone(img);
        this.myBufferedImageStocked = Utils.clone(img);
    }

    /**
     * Display the energy map of the displayed image and the most relevant seam.
     */
    @FXML
    private void imageSeamDisplay() {
        if (this.myBufferedImage != null) {
            this.view.energyImageDisplay(this.myBufferedImage, "Show next seam",
                    true, this.myImage, this.seamPrintingLabel,
                    this.energyPrintingLabel);
        }
    }

    /**
     * Display the energy map of the displayed image.
     */
    @FXML
    private void imageEnergyDisplay() {
        if (this.myBufferedImage != null) {
            this.view.energyImageDisplay(this.myBufferedImage,
                    "Energy computation", false, this.myImage,
                    this.seamPrintingLabel, this.energyPrintingLabel);
        }
    }

    /**
     * Actualize the label displayed to notify users of the actual active
     * function.
     *
     * @param functionName is the Function actually listening the slider.
     * @return the label to display on the application window.
     */
    @FXML
    private String updateListenerLabel(String functionName) {
        if (this.myBufferedImage == null)
            return "Please load an image!";
        else
            return "Current tool: " + functionName;
    }

    @FXML
    private void openHelpFile() throws IOException {
        this.userHelper.helpMe();
    }

    /**
     * Reset the pointer label when the mouse exit the image area.
     */
    @FXML
    private void pointerLabelReset() {
        this.pointerPositionLabel.setText("| x : - y : -");
    }
}
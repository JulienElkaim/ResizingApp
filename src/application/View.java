package application;

import java.awt.*;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import tools.GradientPainter;
import tools.SeamCarver;

class View {

	private String direction = "H";
    private String tempImg = "null";

    private GradientPainter gradientPainter = new GradientPainter();


    String getDirection() {
		return this.direction;
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

}

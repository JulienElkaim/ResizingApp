package application;

import java.awt.image.BufferedImage;

import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

class View {

	private String direction = "H";

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

}

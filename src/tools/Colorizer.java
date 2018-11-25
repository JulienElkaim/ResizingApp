package tools;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Change the color composition of a bufferedImage, changing a given color with
 * a given ratio. Other colors are not changed.
 */
public class Colorizer implements ImageProcessor {

	// not private to be seen by test
	ColorEnum changeColor = null;
	double ratio = .5;

	/**
	 * Set the color to change during process.
	 * 
	 * @param color
	 *            color to change
	 */
	public void setChangeColor(Color color) {
		this.changeColor = ColorEnum.findColor(color);
	}

	/**
	 * Sets the ratio to be applied to the color.
	 * 
	 * @param ratio
	 *            ratio to apply, in [0.; 1.]
	 */
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

//	/**
//	 * Get the color to change during process.
//	 * 
//	 * @return color
//	 *            color to change
//	 */
//	ColorEnum getChangeColor() {
//		return this.changeColor;
//	}
//
//	/**
//	 * Get the ratio applied to the change color.
//	 * 
//	 * @return color
//	 *            color to change
//	 */
//	double getRatio() {
//		return this.ratio;
//	}

	@Override
	public BufferedImage process(BufferedImage myBufferedImage) {
		int decalage = 0;
		int colorToChange = 0;
		int secondColor = 0;
		int thirdColor = 0;
		int maxX = myBufferedImage.getWidth();
		int maxY = myBufferedImage.getHeight();

		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < maxY; y++) {

				Color myPixelColor = new Color(myBufferedImage.getRGB(x, y));

				switch (this.changeColor) {
				case PROCESS_RED:
					decalage = 16;
					colorToChange = myPixelColor.getRed();
					secondColor = myPixelColor.getGreen() << 8;
					thirdColor = myPixelColor.getBlue();
					break;
				case PROCESS_GREEN:
					decalage = 8;
					colorToChange = myPixelColor.getGreen();
					secondColor = myPixelColor.getRed() << 16;
					thirdColor = myPixelColor.getBlue();
					break;
				case PROCESS_BLUE:
					decalage = 0;
					colorToChange = myPixelColor.getBlue();
					secondColor = myPixelColor.getGreen() << 8;
					thirdColor = myPixelColor.getRed() << 16;
					break;
				default:
					System.err.println("Unexpected color: " + this.changeColor);
				}
				colorToChange = (Math.min((int) (colorToChange * this.ratio), 255)) << decalage;
				myBufferedImage.setRGB(x, y, (colorToChange + secondColor + thirdColor));
			}

		}
		return myBufferedImage;
	}

}

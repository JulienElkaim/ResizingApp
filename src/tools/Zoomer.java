package tools;

import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public class Zoomer implements ImageProcessor {
    // the displayed image's size , different of the BufferedImage used.
    private double viewSize = 0.0;
    // the size direction provided, H for Height and W for width. BOTH is an
    // already
    // computed coef View / Real
    private String direction = "V";
    // the View x-coordinate where the mouse pointed out.
    private double X = 0.0;
    // the View y-coordinate where the mouse pointed out.
    private double Y = 0.0;
    // the strength of the zoom. High coef mean high zoom.
    private double coef = 1.0;

    /**
     * Set size according to direction.
     *
     * @param imageView image display
     */
    public void setViewSize(ImageView imageView) {
        double viewSize;
        if (this.direction.equals("H"))
            viewSize = imageView.getFitHeight();
        else
            viewSize = imageView.getFitWidth();
        this.viewSize = viewSize;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setX(double X) {
        this.X = X;
    }

    public void setY(double Y) {
        this.Y = Y;
    }

    public void setCoef(double coef) {
        this.coef = coef;
    }

    /**
     * Provide the user with useful method to zoom on its image.
     *
     * @param myBufferedImage The image where to zoom happen.
     * @return the BufferedImage resulting the zoom tools.
     */
    @Override
    public BufferedImage process(BufferedImage myBufferedImage) {

        double zoomingCoef = this.coef / 100 * (0.1 - 1) + 1;// 1* -0.9 +1
        double initWidth = myBufferedImage.getWidth();
        double initHeight = myBufferedImage.getHeight();
        double coefViewReal = 1;

        if (this.direction.equals("H"))
            coefViewReal = viewSize / initHeight;
        if (this.direction.equals("V"))
            coefViewReal = viewSize / initWidth;

        double XReal = this.X / coefViewReal;
        double YReal = this.Y / coefViewReal;
        double x = Math.max(0, XReal - (zoomingCoef / 2) * initWidth);
        double y = Math.max(0, YReal - (zoomingCoef / 2) * initHeight);
        int width = (int) (zoomingCoef * initWidth);
        int height = (int) (zoomingCoef * initHeight);

        while (x + width > myBufferedImage.getWidth())
            x -= 1;
        while (y + height > myBufferedImage.getHeight())
            y -= 1;
        return myBufferedImage.getSubimage((int) x, (int) y, width, height);
    }
}

package tools;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class ColorizerTest {

    static private double RATIO = 0.4;
    static private int afterRed = (Math.min((int) (0xff * RATIO), 255) << 16)
            + (0xff << 8) + 0xff;
    static private final BufferedImage IMAGE_AFTER_RED = createSmallImage(
            afterRed);
    static private int afterGreen = (0xff << 16)
            + (Math.min((int) (0xff * RATIO), 255) << 8) + 0xff;
    static private final BufferedImage IMAGE_AFTER_GREEN = createSmallImage(
            afterGreen);
    static private int afterBlue = (0xff << 16) + (0xff << 8)
            + Math.min((int) (0xff * RATIO), 255);
    static private final BufferedImage IMAGE_AFTER_BLUE = createSmallImage(
            afterBlue);
    static private BufferedImage IMAGE = null;
    private Colorizer colorizer = null;

    // 3x2 image to check w & h are not confused
    // pixels are almost the same color but different for test
    static private BufferedImage createSmallImage(int color) {
        BufferedImage bi = new BufferedImage(3, 2, BufferedImage.TYPE_INT_ARGB);
        bi.setRGB(0, 0, color);
        bi.setRGB(0, 1, color);
        bi.setRGB(1, 0, color);
        bi.setRGB(1, 1, color);
        bi.setRGB(2, 0, color);
        bi.setRGB(2, 1, color);
        return bi;
    }

    private static void assertImagesEqual(BufferedImage expected,
                                          BufferedImage actual) {
        for (int x = 0; x < IMAGE.getWidth(); x++) {
            for (int y = 0; y < IMAGE.getHeight(); y++) {
                assertEquals(expected.getRGB(x, y), actual.getRGB(x, y));
            }
        }
    }

    /**
     * Do before starting test.
     */
    @Before
    public void setUp() {
        assertNull(colorizer);
        this.colorizer = new Colorizer();
        assertNotNull(colorizer);

        int before = 0x00ffffff;
        IMAGE = createSmallImage(before);
    }

    @Test
    public void testChangeColor() {
        assertNull(colorizer.changeColor);

        // ok values
        colorizer.setChangeColor(Color.RED);
        assertEquals(ColorEnum.PROCESS_RED, colorizer.changeColor);
        colorizer.setChangeColor(Color.GREEN);
        assertEquals(ColorEnum.PROCESS_GREEN, colorizer.changeColor);
        colorizer.setChangeColor(Color.BLUE);
        assertEquals(ColorEnum.PROCESS_BLUE, colorizer.changeColor);

        // nok values
        try {
            colorizer.setChangeColor(Color.BLACK);
        } catch (RuntimeException ignored) {
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testSetRatio() {
        final double ratio = .6;
        final double epsilon = 1e-4;
        assertEquals(.5, colorizer.ratio, epsilon);

        colorizer.setRatio(ratio);
        assertEquals(ratio, colorizer.ratio, epsilon);
    }

    @Test
    public void testProcessUnchanged() {
        colorizer.setChangeColor(Color.RED);
        colorizer.setRatio(1.);
        assertImagesEqual(IMAGE, colorizer.process(IMAGE));

        colorizer.setChangeColor(Color.GREEN);
        colorizer.setRatio(1.);
        assertImagesEqual(IMAGE, colorizer.process(IMAGE));

        colorizer.setChangeColor(Color.BLUE);
        colorizer.setRatio(1.);
        assertImagesEqual(IMAGE, colorizer.process(IMAGE));
    }

    @Test
    public void testProcessRed() {
        colorizer.setChangeColor(Color.RED);
        colorizer.setRatio(RATIO);
        assertImagesEqual(IMAGE_AFTER_RED, colorizer.process(IMAGE));
    }

    @Test
    public void testProcessGreen() {
        colorizer.setChangeColor(Color.GREEN);
        colorizer.setRatio(RATIO);
        assertImagesEqual(IMAGE_AFTER_GREEN, colorizer.process(IMAGE));
    }

    @Test
    public void testProcessBlue() {
        colorizer.setChangeColor(Color.BLUE);
        colorizer.setRatio(RATIO);
        assertImagesEqual(IMAGE_AFTER_BLUE, colorizer.process(IMAGE));
    }

}

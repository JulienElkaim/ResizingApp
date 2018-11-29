package utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class UserHelper {
    /**
     * Open the readme file to help user.
     *
     * @throws IOException when README is not available.
     */
    public void helpMe() throws IOException {
        File file = new File("./README.md");

        if (!Desktop.isDesktopSupported()) {
            System.out.println(
                    "Your computer have restricted access. Please open README.md manually.");
        } else {
            Desktop desktop = Desktop.getDesktop();
            if (file.exists())
                desktop.open(file);
        }
    }
}

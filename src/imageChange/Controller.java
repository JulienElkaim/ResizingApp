package imageChange;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Slider;
import operation.*;

import static java.lang.Math.abs;
import javafx.scene.shape.Rectangle;

public class Controller {
    //Objets visuels
    public ImageView myImage;
    public Slider mySlider;
    public Rectangle redG;
    public Rectangle greenG;
    public Rectangle blueG;
    public Label seamPrintingLabel;
    public Label energyPrintingLabel;
    public Label sliderListenerLabel;

    //Objets conceptuels
    private KeyCode keyPressed;
    private String sliderListener="";
    private String tempImg;

    //Objets image
    private BufferedImage myBufferedImage;
    private BufferedImage myBufferedImageSTOCKED;

    private String displayActualFunction(String functionName){
        if (this.myBufferedImage==null)
            return " - ";
        else
            return "Actually Using : " + functionName;
    }

    public void initialize(){
        this.sliderListener = "Zoom";
        this.sliderListenerLabel.setText(this.displayActualFunction(this.sliderListener));
        this.tempImg ="null";
        this.redG.setFill(javafx.scene.paint.Color.RED);
        this.greenG.setFill(javafx.scene.paint.Color.GREEN);
        this.blueG.setFill(javafx.scene.paint.Color.BLUE);


        //Reaction when  the Slider value is changed
        this.mySlider.valueProperty().addListener(this::listenSliderChange);


    }


    private void listenSliderChange(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

        //The function called will change accordingly to the last listener activated
        switch(this.sliderListener){
            case "Resize":
                this.resizeButton(new_val.doubleValue());
                break;
            case "Zoom":
                //Just for visualisation, the use of ZOOM with the slider is done by clicking on the imageView
                break;

            case "Seam Carving":
                this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);
                this.seamWithdraw(new_val.intValue());
                break;
            case"Crop":
                this.createCropImage();


        }

    }

    private void reset(){

        this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));

    }

    private void zoom(double X, double Y){


        double zoomingCoef = this.mySlider.getValue()/100  *(0.1-1) +1;// 1* -0.9 +1

        double initWidth = this.myBufferedImage.getWidth();
        double initHeight = this.myBufferedImage.getHeight();

        double coefViewReal = this.myImage.getFitHeight()/initHeight;

        double XReal = X/coefViewReal;
        double YReal = Y/coefViewReal;

        double x = Math.max(0, XReal - (zoomingCoef/2)* initWidth);
        double y = Math.max(0 , YReal - (zoomingCoef/2)* initHeight);

        int width =(int)(zoomingCoef*this.myBufferedImage.getWidth()) ;
        int height = (int)(zoomingCoef*this.myBufferedImage.getHeight());

        while (x+width >= this.myBufferedImage.getWidth()){
            x-=1;
        }
        while (y+height >= this.myBufferedImage.getHeight()){
            y-=1;
        }

        this.myBufferedImage = this.myBufferedImage.getSubimage((int)x,(int)y,width,height);
        this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
    }


    /**
     * Provide an example of how resizing regarding the slider value
     */

    private void resizeButton(double sliderValue){
      this.myBufferedImage = SimpleOperation.cloningBufferedImage( ImageResize.resizingWidth( this.myBufferedImageSTOCKED,sliderValue ) );
      this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage,null));
    }



    public void ChooseAFile() throws IOException {

        // Choosing the file to input
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPEG Files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("./img/"));
        File file = fileChooser.showOpenDialog(null);

        //Displaying the image chosen.
        this.myBufferedImageSTOCKED = ImageIO.read(file);

        this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);

        Image image = SwingFXUtils.toFXImage(this.myBufferedImage, null);
        this.myImage.setImage(image);

    }

    public void saveAFile() throws IOException{

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG Files (*.jpg)", "*.jpg");

        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.getExtensionFilters().add(extFilter);
        saveFileChooser.setInitialDirectory(new File("./img/"));
        saveFileChooser.setInitialFileName("AfterMyModifications");
        File file = saveFileChooser.showSaveDialog(null);

        ImageIO.write(this.myBufferedImage, "jpg", file);

    }


    private void redGradient(){
        BufferedImage img = Gradient.createGradient("red",this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    private void greenGradient(){
        BufferedImage img = Gradient.createGradient("green",this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }

    private void blueGradient(){
        BufferedImage img = Gradient.createGradient("blue",this.myBufferedImage);
        this.myImage.setImage(SwingFXUtils.toFXImage(img, null));
    }


    public void Switcher(ActionEvent actionEvent) {
       this.sliderListener = this.getText(actionEvent.getSource().toString());
       this.sliderListenerLabel.setText(this.displayActualFunction(this.sliderListener));
    }

    private String getText (String src){
        StringBuilder myStr= new StringBuilder();
        char currentChar;
        for (int i=src.length()-2; i> -1; i--){

            currentChar = src.charAt(i);
            if (currentChar=='\''){
                i = -1;
                continue;
            }
            myStr.insert(0, currentChar);

        }
        return myStr.toString();
    }


    public void keyPressedRemove() {
        this.keyPressed = null;
    }

    public void keyPressedAdd(KeyEvent keyEvent) {
        this.keyPressed = keyEvent.getCode();
    }

    public void imageClicked(MouseEvent mouseEvent) {

        if (this.keyPressed == KeyCode.SHIFT)
            this.reset();
        else if (this.sliderListener.equals("Zoom"))
            this.zoom(mouseEvent.getX() - this.myImage.getX(), mouseEvent.getY() - this.myImage.getY());

    }


    private void createCropImage() {
        double coef = (0.01 < this.mySlider.getValue() / 100) ? abs(this.mySlider.getValue()) / 100 : 0.01;
        this.myBufferedImage = SimpleOperation.cloningBufferedImage(this.myBufferedImageSTOCKED);
        int width = this.myBufferedImage.getWidth();
        int height = this.myBufferedImage.getHeight();
        int newWidth = (int) (coef * width);
        BufferedImage dest = this.myBufferedImageSTOCKED.getSubimage(0, 0, newWidth, height);
        this.myImage.setImage(SwingFXUtils.toFXImage(dest, null));

    }
    /**
     * This method convert the color image into grayscale image
     */
    private BufferedImage grayOut(BufferedImage img) {
        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace
                .getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(img, img);

        return img;
    }

    private BufferedImage determineEnergy(BufferedImage imgToCompute){
        int maxX = imgToCompute.getWidth();
        int maxY = imgToCompute.getHeight();
        BufferedImage newBImage = SimpleOperation.cloningBufferedImage(imgToCompute);

        for (int x = 2; x < maxX-1; x++) {
            for (int y = 2; y < maxY-1; y++) {

                //pixel à gauche
                Color myLeftPixelColor = new Color(imgToCompute.getRGB(x - 1, y));
                //pixel à droite
                Color myRightPixelColor = new Color(imgToCompute.getRGB(x + 1, y));
                //top pixel
                Color myTopPixelColor = new Color(imgToCompute.getRGB(x, y - 1));
                //bottom pixel
                Color myBottomPixelColor = new Color(imgToCompute.getRGB(x, y + 1));

                int energyRed;
                int energyGreen;
                int energyBlue;
                int energy;

                energyRed = abs(myLeftPixelColor.getRed()-myRightPixelColor.getRed())+abs(myTopPixelColor.getRed()-myBottomPixelColor.getRed());
                energyGreen = abs(myLeftPixelColor.getGreen()-myRightPixelColor.getGreen())+abs(myTopPixelColor.getGreen()-myBottomPixelColor.getGreen());
                energyBlue = abs(myLeftPixelColor.getBlue()-myRightPixelColor.getBlue())+abs(myTopPixelColor.getBlue()-myBottomPixelColor.getBlue());


                energy = (energyRed<<16) + (energyGreen<<8) + energyBlue;
                newBImage.setRGB(x,y,energy);
            }
        }
        return newBImage;
    }

    public void energyMap(){
        this.energyPrintingLabel.setTextFill(javafx.scene.paint.Color.BLACK);

        if (!this.tempImg.equals("Energy computation")){
            if (this.myBufferedImage!=null)
                this.energyPrintingLabel.setTextFill(javafx.scene.paint.Color.GREEN);

            assert this.myBufferedImage != null;
            this.myImage.setImage(SwingFXUtils.toFXImage(grayOut(determineEnergy(this.myBufferedImage)), null));
            this.tempImg = "Energy computation";
        }else{
            this.energyPrintingLabel.setTextFill(javafx.scene.paint.Color.PALEVIOLETRED);
            this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
            this.tempImg = "null";
        }
    }


    private long[][] dynamicEnergySeamVertical(BufferedImage imgToCompute){
        int maxX = imgToCompute.getWidth();
        int maxY = imgToCompute.getHeight();
        long [][] dynamicSeamTable = new long [maxX][maxY];

        //seam computing
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (y == 0) {
                    // la toute premiere ligne
                    dynamicSeamTable[x][y] = imgToCompute.getRGB(x,y);
                }else{
                    // les autres lignes

                    // le pixel border-left de l'image
                    if (x==0){
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x,y) + Math.min(dynamicSeamTable[x][y-1], dynamicSeamTable[x+1][y-1]);

                    }else if(x==(maxX-1)){ // le pixel border-right de l'image
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x,y) + Math.min(dynamicSeamTable[x][y-1], dynamicSeamTable[x-1][y-1]);

                    }else{ // les pixels au milieu de la ligne
                        dynamicSeamTable[x][y] = imgToCompute.getRGB(x,y) + Math.min(dynamicSeamTable[x][y-1], Math.min(dynamicSeamTable[x-1][y-1],dynamicSeamTable[x+1][y-1] ) );
                    }
                }
            }

        }

        return dynamicSeamTable;
    }

    private int[] bestVerticalSeam(long[][] seamTable){
        int maxY = seamTable[0].length;
        int maxX = seamTable.length;
        int[] lowestSeamXCoordinates = new int [maxY];

        //recherche dans la dernière ligne du pixel etant la fin de la seam la plus petite
        for (int x=0 ; x < maxX;x++){
            if (x == 0){

                lowestSeamXCoordinates[maxY-1] = 0; // Premier pixel a etre verifier, il est champion LOW par defaut

            }else{
                if(seamTable[lowestSeamXCoordinates[maxY-1] ][maxY-1] > seamTable[x][maxY-1]) // Si notre champion LOW est battu
                    lowestSeamXCoordinates[maxY-1] = x;
            }
        }

        //Pour tous les autres lignes en decoulant

        for (int y=(maxY-2);y >-1; y--){
            //comparaison entre les pixels au dessus de  lui


            if(lowestSeamXCoordinates[y+1]==0){
                //SI le pixel davant est sur le bord gauche: 2 pixel au dessus de lui:
                //[0][y]
                //[1][y]
                lowestSeamXCoordinates[y] = (seamTable[0][y] <= seamTable[1][y])? 0: 1 ;


            }else if (lowestSeamXCoordinates[y+1]==(maxX-1) ){
                //SI le pixel est sur le bord droit: 2 pixel au dessus de lui:
                //[maxX-1][y]
                //[maxX-2][y]
                lowestSeamXCoordinates[y] = (seamTable[maxX-2][y] <= seamTable[maxX-1][y])? maxX-2: maxX-1 ;
            }else{
                //SI le pixel est sur le bord droit: 2 pixel au dessus de lui:
                //[lowestSeamXCoordinates[y-1]-1][y]
                //[lowestSeamXCoordinates[y-1]][y]
                //[lowestSeamXCoordinates[y-1]+1][y]
                lowestSeamXCoordinates[y] = (seamTable[lowestSeamXCoordinates[y+1]-1][y] <= seamTable[lowestSeamXCoordinates[y+1]][y])? (lowestSeamXCoordinates[y+1]-1): lowestSeamXCoordinates[y+1] ;
                lowestSeamXCoordinates[y] = (seamTable[lowestSeamXCoordinates[y]][y] <= seamTable[lowestSeamXCoordinates[y+1]+1][y])? lowestSeamXCoordinates[y]: (lowestSeamXCoordinates[y+1]+1) ;
                //Assigne le X du moins energique du parent gauche et face puis assigne le X du moins energique du parent droit et du gagnatprecedent
            }

        }

        return lowestSeamXCoordinates;
    }

    public void printBestSeamVertical(){
        this.seamPrintingLabel.setTextFill(javafx.scene.paint.Color.BLACK);

        if (this.tempImg.equals("Show next seam")){

            this.seamPrintingLabel.setTextFill(javafx.scene.paint.Color.PALEVIOLETRED);
            this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
            this.tempImg = "null";


        }else {
            if (this.myBufferedImage!=null){
                this.seamPrintingLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            }

            assert this.myBufferedImage != null;
            BufferedImage energyBImage = grayOut(determineEnergy(this.myBufferedImage));

            int totalRedRGB = 255 << 16;
            int[] seamToPrint = bestVerticalSeam(dynamicEnergySeamVertical(energyBImage));

            for (int y = 0; y < seamToPrint.length; y++) {
                energyBImage.setRGB(seamToPrint[y], y, totalRedRGB);
            }

            this.myImage.setImage(SwingFXUtils.toFXImage(energyBImage, null));
            this.tempImg = "Show next seam";

        }
    }
    private BufferedImage seamVerticalDestroyer(BufferedImage img, int[] seam){
        int maxX = img.getWidth();
        int maxY = img.getHeight();
        BufferedImage newBImage = new BufferedImage(maxX-1, maxY, BufferedImage.TYPE_INT_RGB);

        for (int y=0; y < maxY; y++){
            for (int x=0; x < maxX-1; x++){

                if( seam[y] <=x ) {// it is the pixel to skip
                    newBImage.setRGB(x, y, img.getRGB(x+1, y));
                }else{
                    newBImage.setRGB(x, y, img.getRGB(x, y));
                }
            }
        }
        return newBImage;
    }

    private void seamWithdraw(int nbOfSeamToWithdraw) {

        for (int i=0; i< nbOfSeamToWithdraw; i++) {
            BufferedImage energyBImage = grayOut(determineEnergy(this.myBufferedImage));
            int[] seamToWithdraw = bestVerticalSeam(dynamicEnergySeamVertical(energyBImage));

            BufferedImage bImageWithOutSeam = this.seamVerticalDestroyer(this.myBufferedImage, seamToWithdraw);

            this.myBufferedImage = SimpleOperation.cloningBufferedImage(bImageWithOutSeam);
            this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
           }
    }

    public void redGSwitcher (){
        gradientSwitcher("redG");
    }
    public void greenGSwitcher (){
        gradientSwitcher("greenG");
    }
    public void blueGSwitcher (){
        gradientSwitcher("blueG");
    }

    private void gradientSwitcher(String colorGradient) {

        if(this.tempImg.equals(colorGradient)){
            this.myImage.setImage(SwingFXUtils.toFXImage(this.myBufferedImage, null));
            this.tempImg="null";

        }else{

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

    public void loadChanges() {
        this.myBufferedImageSTOCKED = SimpleOperation.cloningBufferedImage(this.myBufferedImage);
    }
}


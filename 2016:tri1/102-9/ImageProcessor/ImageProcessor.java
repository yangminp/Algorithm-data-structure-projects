// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP 102 Assignment 9 
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/** ImageProcessor allows the user to display and edit a
 *  greyscale image in a number of ways.
 *  The program represents the image as a 2D array of integers, which must
 *   all be between 0 (black) and 255 (white).
 *  The class includes three methods (already written for you) that will
 *   - read a png or jpeg image file and store a 2D array of greyscale values
 *     into the image field.
 *   - render (display) the 2D array of greyscale values in the image field on the graphics pane
 *   - write the 2D array of greyscale values in the image field to a png file.
 *  
 *  You are to complete the methods to modify an image:
 *   - darken the image
 *   - increase the contrast
 *   - Rotate the image 180 degrees and 90 degrees
 *   - flip the image horizontally or vertically.
 *   - merge another image with the current image
 *   - zoom in on the image (in two different ways)
 *
 *
 */
public class ImageProcessor{
    // the current image (initialised to a very small 3x3 image)
    private int[][] image = new int[][]{{80,80,80},{80, 200, 80},{80,80,80}}; 

    // current selected point
    private int selectedRow = 0;
    private int selectedCol = 0;

    private final int pixelSize = 1;  // the size of the pixels as drawn on screen

    /** CORE:
     * Make all pixels in the image darker by 20 greylevels.
     * but make sure that you never go below 0
     */
    public void darkenImage(){
        for(int row=0;row<this.image.length;row++){
            for(int col=0;col<this.image[0].length;col++){
                if(this.image[row][col]>=20){
                    this.image[row][col]-=20;
                }
            }
        }

        this.redisplayImage();
    }  

    /** CORE:
     * Increase the contrast of the image -
     * make all lighter pixels in the image (above 128) even lighter (by 20%)
     * and make all darker pixels even darker (by 20%)
     * For example, a pixel value of 158 is 30 levels above 128. It should be lightened by 20% of 30 (= 6) to 164. 
     *              A pixel value of 48 is 80 levels below 128. It should be darkened by 20% of 80 (= 16) to 32. 
     */
    public void contrastImage(){

        for(int row=0;row<this.image.length;row++){
            for(int col=0;col<this.image[0].length;col++){
                if(this.image[row][col]>128){
                    int lighter=(int)((this.image[row][col]-128)*0.2);
                    int limit=this.image[row][col]+lighter;
                    if(limit<=255)
                        this.image[row][col]=limit;
                }else{
                    int darker=(int)((128-this.image[row][col])*0.2);
                    int limit=this.image[row][col]-darker;
                    if(limit>=0)
                        this.image[row][col]=limit;
                }
            }
        }

        this.redisplayImage();
    } 

    /** CORE:
     * Flip the image horizontally
     *   exchange the values on the left half of the image
     *   with the corresponding values on the right half
     */
    public void flipImageHorizontally(){
        int rows=this.image.length;
        int cols=this.image[0].length;

        int [][] temp=new int[rows][cols];
        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                temp[row][cols-1-col]=this.image[row][col];
            }
        }
        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                this.image[row][col]=temp[row][col];
            }
        }

        this.redisplayImage();
    }

    /** CORE:
     * Flip the image vertically
     *   exchange the values on the top half of the image
     *   with the corresponding values on the bottom half
     */
    public void flipImageVertically(){
        int rows=this.image.length;
        int cols=this.image[0].length;

        int [][] temp=new int[rows][cols];
        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                temp[row][col]=this.image[rows-1-row][col];
            }
        }
        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                this.image[row][col]=temp[row][col];
            }
        }
        this.redisplayImage();
    }

    /**  CORE:
     * Rotate the image 180 degrees
     * Each cell is swapped with the corresponding cell
     *  on the other side of the center of the images.
     * It is easier to make a new array, the same size as image, then
     *   copy each pixel in image to the right place in the new array
     *   and then assign the new array to the image field.
     */
    public void rotateImage180(){
        int rows=this.image.length;
        int cols=this.image[0].length;

        int [][] temp=new int[rows][cols];
        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                temp[rows-1-row][cols-1-col]=this.image[row][col];
            }
        }
        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                this.image[row][col]=temp[row][col];
            }
        }

        this.redisplayImage();
    }

    /**  COMPLETION:
     * Rotate the image 90 degrees anticlockwise
     * Note, the resulting image will have different dimensions:
     *  the width of the new image will be the height of the old image.
     *  the height of the new image will be the width of the old image.
     * You will need to make a new image array of the new dimensions,
     *  fill it with the correct pixel values from the original array, 
     *  and then set the image field to contain the new image.
     */
    public void rotateImage90(){
        int rows=this.image.length;
        int cols=this.image[0].length;

        int [][] temp=new int[cols][rows];
        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                temp[col][row]=this.image[row][cols-col-1];
            }
        }
        this.image=new int[cols][rows];
        for(int row=0;row<this.image.length;row++){
            for(int col=0;col<this.image[0].length;col++){ 
                this.image[row][col]=temp[row][col];
            }
        }
        this.redisplayImage();
    }

    /** COMPLETION:
     * Expand the top left quarter of the image to fill the whole image
     * each pixel in the top left quarter will be copied to four pixels
     * in the new image.
     * Be careful not to try to access elements past the edge of the array!
     * Hint: It is actually easier to work backwards from the bottom right corner.
     */
    public void expandImage(){

        int rows=this.image.length;
        int cols=this.image[0].length;
        int [][] temp=new int[(rows/2)][(cols)/2];
        for(int row=0;row<(rows/2);row++){
            for(int col=0;col<(cols/2);col++){
                temp[row][col]=this.image[row][col];
            }
        }
        for(int row=0;row<(rows/2);row++){
            for(int col=0;col<(cols/2);col++){
                this.image[row*2][col*2]=temp[row][col];
                this.image[row*2][col*2+1]=temp[row][col];
                this.image[row*2+1][col*2]=temp[row][col];
                this.image[row*2+1][col*2+1]=temp[row][col];

            }
        }

        
        this.redisplayImage();
    }

    /** COMPLETION:
     * Merge two images 
     * Ask the user to select another image file, and load it into another array.
     *  Work out the rows and columns shared by the images
     *  For each pixel value in the shared region, replace the current pixel value
     *  by the average of the pixel value in current image and the corresponding
     *  pixel value in the other image.
     */
    public void mergeImage(){
        int [][] other = this.loadAnImage(UIFileChooser.open());
        int rows = Math.min(this.image.length, other.length);       // rows and cols
        int cols = Math.min(this.image[0].length, other[0].length); // common to both
        //only change image in region 0..rows-1, 0..cols-1
        int [][] temp=new int[rows][cols];

        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                temp[row][col]=(other[row][col]+this.image[row][col])/2;
            }
        }
        this.image=new int[rows][cols];
        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){ 
                this.image[row][col]=temp[row][col];
            }
        }
        this.redisplayImage();
    }

    /** CHALLENGE:
     * Zoom in on the image, expanding by 133%, centered on the currently
     * selected pixel.
     * (The user can use the mouse to select a pixel which will be highlighted)
     * Hint: the selected pixel should stay where it is, and other pixels should be
     *  moved away from it by a factor or 4/3.
     *  Be careful not to try to access elements past the edge of the array!
     *  Be careful not to leave gaps in the image.
     *  Hint: It is easier to make a new array, copy the image over, expanding as you go
     *  and then assign the new array to the image field.
     */
    public void zoomImage(){
        int rows=this.image.length;
        int cols=this.image[0].length;
        int[][] temp=new int[rows][cols];
        
        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                double distanceR=row-this.selectedRow;
                double distanceC=col-this.selectedCol;
                int newR=(int)(this.selectedRow+(distanceR*(0.75)));
                int newC=(int)(this.selectedCol+(distanceC*(0.75)));      
               
                if(newR>=0&&newC>=0&&newR<rows&&newC<cols){
                     temp[row][col]=this.image[newR][newC];
                }
                
            }
        }
        
        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                this.image[row][col]=temp[row][col];
            }
        }
        
 
        
 
        this.redisplayImage();
    }

    //=========================================================================
    // Methods below here are already written for you -
    // for redisplaying the image array on the graphics pane,
    // for loading an image file into the image array,
    // for saving the image array into a file,
    // for setting the mouse position.

    // Constructors
    /**
     * Construct a new ImageProcessor object
     * and set up the GUI
     */
    public ImageProcessor(){
        UI.initialise();
        UI.setMouseListener(this::doMouse);
        UI.addButton("Load",       this::loadImage );
        UI.addButton("Save",       this::saveImage );       
        UI.addButton("Darken",     this::darkenImage );
        UI.addButton("Contrast",   this::contrastImage );    
        UI.addButton("Flip Horiz", this::flipImageHorizontally );
        UI.addButton("Flip Vert",  this::flipImageVertically );
        UI.addButton("Rotate 180", this::rotateImage180 );     
        UI.addButton("Rotate 90",  this::rotateImage90 );   
        UI.addButton("Merge",      this::mergeImage );   
        UI.addButton("Expand",     this::expandImage );
        UI.addButton("Zoom",       this::zoomImage );        
        UI.addButton("Quit", UI::quit );              

        this.computeGreyColours();
    }

    /** field and helper methods to precompute and store all the possible grey colours,
     *  so the redisplay method does not have to constantly construct new color objects
     */
    private Color[] greyColors = new Color[256];

    /** Display the image on the screen with each pixel as a square of size pixelSize.
     *  To speed it up, all the possible colours from 0 - 255 have been precalculated.
     */
    public void redisplayImage(){
        UI.clearGraphics();
        UI.setImmediateRepaint(false);
        for(int row=0; row<this.image.length; row++){
            int y = row * this.pixelSize;
            for(int col=0; col<this.image[0].length; col++){
                int x = col * this.pixelSize;
                UI.setColor(this.greyColor(this.image[row][col]));
                UI.fillRect(x, y, this.pixelSize, this.pixelSize);
            }
        }
        UI.setColor(Color.red);
        UI.drawRect(this.selectedCol*this.pixelSize,this.selectedRow*this.pixelSize,
            this.pixelSize,this.pixelSize);
        UI.repaintGraphics();
    }

    /** Get and return an image as a two-dimensional grey-scale image (from 0-255).
     *  This method will cause the image to be returned as a grey-scale image,
     *  regardless of the original colouration.
     */
    public int[][] loadAnImage(String imageName) {
        int[][] ans = null;
        if (imageName==null) return null;
        try {
            BufferedImage img = ImageIO.read(new File(imageName));
            UI.printMessage("loaded image height(rows)= " + img.getHeight() +
                "  width(cols)= " + img.getWidth());
            ans = new int[img.getHeight()][img.getWidth()];
            for (int row = 0; row < img.getHeight(); row++){
                for (int col = 0; col < img.getWidth(); col++){
                    Color c = new Color(img.getRGB(col, row), true);
                    // Use a common algorithm to move to greyscale
                    ans[row][col] = (int)Math.round((0.3 * c.getRed()) + (0.59 * c.getGreen())
                        + (0.11 * c.getBlue()));
                }
            }
        } catch(IOException e){UI.println("Image reading failed: "+e);}
        return ans;
    }

    /** Ask user for an image file, and load it into the current image */
    public void loadImage(){
        this.image = this.loadAnImage(UIFileChooser.open());
        this.redisplayImage();
    }

    /** Write the current greyscale image to the specified filename */
    public  void saveImage() {
        // For speed, we'll assume every row of the image is the same length!
        int height = this.image.length;
        int width = this.image[0].length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int greyscaleValue = this.image[row][col];
                Color c = new Color(greyscaleValue, greyscaleValue, greyscaleValue);
                img.setRGB(col, row, c.getRGB());
            }
        }
        try {
            String fname = UIFileChooser.save("save to png image file");
            if (fname==null) return;
            File imageFile = new File(fname);
            ImageIO.write(img, "png", new File(fname));
        } catch(IOException e){UI.println("Image reading failed: "+e);}
    }

    private void computeGreyColours(){
        for (int i=0; i<256; i++){
            this.greyColors[i] = new Color(i, i, i);
        }
    }

    private Color greyColor(int grey){
        if (grey < 0){
            return Color.blue;
        }
        else if (grey > 255){
            return Color.red;
        }
        else {
            return this.greyColors[grey];
        }
    }

    public void doMouse(String a, double x, double y){
        if (a.equals("released")) {
            this.setPos(x, y);}
    }

    /** Set the selected Row and Col to the pixel on the mouse position x, y */
    public void setPos(double x, double y){
        int row = (int)(y/this.pixelSize);
        int col = (int)(x/this.pixelSize);
        if (this.image != null && row < this.image.length && col < this.image[0].length){
            this.selectedRow = row;
            this.selectedCol = col;
            this.redisplayImage();
        }
    }

    // Main
    public static void main(String[] arguments){
        ImageProcessor ob = new ImageProcessor();
    }   

}

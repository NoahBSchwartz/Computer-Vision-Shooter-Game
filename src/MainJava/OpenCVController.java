package Test.MainJava.com;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;  
import java.util.ArrayList;  
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;  
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.opencv.core.Core;  
import org.opencv.core.Mat;   
import org.opencv.core.Point;  
import org.opencv.core.Scalar;  
import org.opencv.core.Size;    
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.CvType;  

class Panel extends JPanel{  
   private static final long serialVersionUID = 1L;  
   private BufferedImage image;    
   // Create a constructor method  
   public Panel(){  
     super();  
   }  
   private BufferedImage getimage(){  
     return image;  
   }  
   public void setimage(BufferedImage newimage){  
     image=newimage;  
     return;  
   }  
   public void setimagewithMat(Mat newimage){  
     image=this.matToBufferedImage(newimage);  
     return;  
   }  
   /**  
    * Converts/writes a Mat into a BufferedImage.  
    * @param matrix Mat of type CV_8UC3 or CV_8UC1  
    * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
    */  
   public BufferedImage matToBufferedImage(Mat matrix) {  
     int cols = matrix.cols();  
     int rows = matrix.rows(); 
     int elemSize = (int)matrix.elemSize();  
     byte[] data = new byte[cols * rows * elemSize];  
     int type;  
     matrix.get(0, 0, data);  
     switch (matrix.channels()) {  
       case 1:  
         type = BufferedImage.TYPE_BYTE_GRAY;  
         break;  
       case 3:  
         type = BufferedImage.TYPE_3BYTE_BGR;  
         // bgr to rgb  
         byte b;  
         for(int i=0; i<data.length; i=i+3) {  
           b = data[i];  
           data[i] = data[i+2];  
           data[i+2] = b;  
         }  
         break;  
       default:  
         return null;  
     }  
     BufferedImage image2 = new BufferedImage(cols, rows, type);  
     image2.getRaster().setDataElements(0, 0, cols, rows, data);  
     return image2;  
   }  
   @Override  
   //Start the game method 
   protected void paintComponent(Graphics g){  
      super.paintComponent(g);  
      BufferedImage temp=getimage();  
      if( temp != null)
        g.drawImage(temp,10,10,temp.getWidth(),temp.getHeight(), this);  
   }  
 }  
 public class OpenCVController {  
   public static void main(String arg[]) throws AWTException{ 
	    int x1 = 1;
	    int x2 = 1;
	    int	z = 0;
	    int	y1 = 1;
	    int	y2 = 1;
	    int	d = 0;
	    int	dist1 = 1;
	    int dist2 = 1;
	    int xdiff = 0;
	    int ydiff = 0;
	    int j = 0;
     // Load the native library.  
     System.loadLibrary("opencv_java451");  
     // It is better to group all frames together so cut and paste to  
     // create more frames is easier 
     JFrame frame1 = new JFrame("Camera");  
     frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     frame1.setSize(640,480);  
     frame1.setBounds(0, 0, frame1.getWidth(), frame1.getHeight());  
     Panel panel1 = new Panel();  
     frame1.setContentPane(panel1);  
     frame1.setVisible(true);  
     
    //1. Display camera with different color filters (commented out to help performance
     VideoCapture capture =new VideoCapture(0);  
     Mat webcam_image=new Mat();  
     Mat hsv_image=new Mat();  
     Mat thresholded=new Mat();  
     Mat thresholded2=new Mat();  
      capture.read(webcam_image);  
      frame1.setSize(webcam_image.width()+40,webcam_image.height()+60); 
     Mat array255=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
     array255.setTo(new Scalar(255));  
     Mat distance=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
     List<Mat> lhsv = new ArrayList<Mat>(3);      
     Mat circles = new Mat();  
     //code to track blue marker
     Scalar hsv_min = new Scalar(90, 50, 50, 0);  
     Scalar hsv_max = new Scalar(120, 255, 255, 0);  
     Scalar hsv_min2 = new Scalar(90, 50, 50, 0);  
     Scalar hsv_max2 = new Scalar(120, 255, 255, 0); 
     //code to track skin color
     double[] data=new double[3];  
     if( capture.isOpened())  
     { 
    	 //start mouse method for testing purposes 
    	 SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 MouseMove mm = new MouseMove();
            	 mm.createAndShowGUI();
             }
         });
      while( true )  
      {  
        capture.read(webcam_image);  
        if( !webcam_image.empty() )  
         {  
          // One way to select a range of colors by Hue  
          Imgproc.cvtColor(webcam_image, hsv_image, Imgproc.COLOR_BGR2HSV);  
          Core.inRange(hsv_image, hsv_min, hsv_max, thresholded);           
          Core.inRange(hsv_image, hsv_min2, hsv_max2, thresholded2);  
          Core.bitwise_or(thresholded, thresholded2, thresholded);   
          Core.split(hsv_image, lhsv); // We get 3 2D one channel Mats  
          Mat S = lhsv.get(1);  
          Mat V = lhsv.get(2);  
          //subtract everything that's not in the color range
          Core.subtract(array255, S, S);  
          Core.subtract(array255, V, V);  
          S.convertTo(S, CvType.CV_32F);  
          V.convertTo(V, CvType.CV_32F);  
          Core.magnitude(S, V, distance);  
          Core.inRange(distance,new Scalar(0.0), new Scalar(200.0), thresholded2);  
          Core.bitwise_and(thresholded, thresholded2, thresholded);  
          // Apply the Hough Transform to find the circles  
          Imgproc.GaussianBlur(thresholded, thresholded, new Size(9,9),0,0);  
          Imgproc.HoughCircles(thresholded, circles, Imgproc.CV_HOUGH_GRADIENT, 2, thresholded.height()/4, 500, 50, 0, 0);     
          // Add the HSV color values and tracker markers to the image  
          Imgproc.line(webcam_image, new Point(150,50), new Point(202,200), new Scalar(100,10,10), 3);  
          Imgproc.circle(webcam_image, new Point(210,210), 10, new Scalar(100,10,10),3);  
          data=webcam_image.get(210, 210);  
          Imgproc.putText(webcam_image,String.format("("+String.valueOf(data[0])+","+String.valueOf(data[1])+","+String.valueOf(data[2])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
               ,1.0,new Scalar(100,10,10,255),3);  
           int rows = circles.rows();  
           int elemSize = (int)circles.elemSize(); // Returns 12 (3 * 4bytes in a float)  
           float[] data2 = new float[rows * elemSize/4];  
           if (data2.length>0){  
             circles.get(0, 0, data2); 
             //get marker coordinates from the array 
             for(int i=0; i<data2.length; i=i+3) {  
               Point center= new Point(data2[i], data2[i+1]);  
               Robot robot = new Robot();
               //algorithm for smoothing out the marker movement
               int x = (int)data2[i];
               int y = (int)data2[i+1];
               int ex = (x - 300) * -5 + 1000;
               int ey = y * 5 - 600;
               //save a value from the past frame and then compare it to the future frame to stop micro movements 
               if (z != 1)
               {
       			x1 = ex;
       			y1 = ey;
       			z = 1;
               }
       			else {
       			x2 = ex;
       			y2 = ey;
       			z = 2;
               }
       		if (z != 1) {
       			xdiff = Math.abs(x2 - x1);
       			ydiff = Math.abs(y2 - y1);}
       		else {
       			xdiff = x1 - x2;
       			ydiff = y1 - y2;}
       		if ((xdiff + ydiff > 5) && (xdiff + ydiff < 100)) {
       			robot.mouseMove(ex, ey);
       			j = 4; 
       			}
       		else {
       			if (j != 3) {
       				//smoothing method
       				robot.mouseMove(ex, ey);
       				j = 3;  
       			}
       		}
                 
               Imgproc.ellipse( webcam_image, center, new Size((double)data2[i+2], (double)data2[i+2]), 0, 0, 360, new Scalar( 255, 0, 255 ), 4, 8, 0 );  
             } 
           }  
           //draw the necessary data onto the screen so the user knows if tracking works
           Imgproc.line(hsv_image, new Point(150,50), new Point(202,200), new Scalar(100,10,10)/*CV_BGR(100,10,10)*/, 3);  
           Imgproc.circle(hsv_image, new Point(210,210), 10, new Scalar(100,10,10),3);  
          data=hsv_image.get(210, 210);  
          Imgproc.putText(hsv_image,String.format("("+String.valueOf(data[0])+","+String.valueOf(data[1])+","+String.valueOf(data[2])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
               ,1.0,new Scalar(100,10,10,255),3);  
          distance.convertTo(distance, CvType.CV_8UC1);  
          Imgproc.line(distance, new Point(150,50), new Point(202,200), new Scalar(100)/*CV_BGR(100,10,10)*/, 3);  
          Imgproc.circle(distance, new Point(210,210), 10, new Scalar(100),3);  
          data=(double[])distance.get(210, 210);  
          Imgproc.putText(distance,String.format("("+String.valueOf(data[0])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
               ,1.0,new Scalar(100),3);   
        
          panel1.setimagewithMat(webcam_image);
            //paint every frame
            frame1.repaint();  
         }  
         else  
         {  
           System.out.println(" --(!) No captured frame -- Break!");  
           break;  
         }  
        }  
       }  
     return;  
   }  
 }   
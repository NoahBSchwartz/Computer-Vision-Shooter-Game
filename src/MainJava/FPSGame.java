package Test.MainJava.com;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class FPSGame {
	//make a new window, set dimensions, and make it visible 
	  static void createAndShowGUI() {
          JFrame f = new JFrame();
          f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          f.getContentPane().add(new ImageFollowingMousePanel());
          
          f.setPreferredSize(new Dimension(2000, 1000));
          f.setLocationRelativeTo(null);
          f.setVisible(true);
      }
    }

class ImageFollowingMousePanel extends JPanel implements MouseMotionListener {
	//setup variables for storing bullet and gun pngs
	private final BufferedImage bu;
    private final BufferedImage gu;
    //create variables for each zombie so they can all be controlled separately
    private final Image en;
    private final Image en1;
    private final Image en2;
    private final Image en3;
    private final Image en4;
    private final Image en5;
    private final Image en6;
    private final Image en7;
    private final Image en8;
    private final Image en9;
    private final Image en10;
    //track mouse movement
    private Point imagePosition = new Point(150, 150);
    private Point mousePoint;
    private boolean inGame = true;
    private static JLabel enemyComponent;
    private static JLabel gunComponent;
    //create a counter to increment (moves each zombie across the screen)
    public int a = 1;
    public int b = 1;
    public int c = 1;
    public int d = 1;
    public int e = 1;
    public int f = 1;
    public int g = 1;
    public int h = 1;
    public int i = 1;
    public int j = 1;
    private int x = 1;
    //create a couple other variables to be used in game
    private boolean increment = true;
    private int point = 0;
    private double imageAngleRad = 0;
    final static int B_WIDTH = 2000;
    final static int B_HEIGHT = 1000;
    public ImageFollowingMousePanel() {
    	//set up each png to be used in the painting
        BufferedImage gun = null;
        BufferedImage enemy = null; 
        BufferedImage bullet = null; 
        try {
        	JFrame f = new JFrame();
            enemy = ImageIO.read(new File("src/resources/Zombie.png"));
            gun = ImageIO.read(new File("src/resources/Gun.png"));
            bullet = ImageIO.read(new File("src/resources/Cross.png"));
        }
         catch (IOException e){
            e.printStackTrace();
        }
        //assign pngs to their holding variables
        bu = bullet;
        gu = gun;
        en = enemy;
        en1 = enemy;
        en2 = enemy;
        en3 = enemy;
        en4 = enemy;
        en5 = enemy;
        en6 = enemy;
        en7 = enemy;
        en8 = enemy;
        en9 = enemy;
        en10 = enemy;
        addMouseMotionListener(this);
        Timer timer = new Timer(0, new ActionListener() {
            @Override
            //not sure what this does but it breaks everything when I delete it
            public void actionPerformed(ActionEvent e) {
                if (mousePoint != null) {
                }
            }
        });
       
        timer.start();
    }
    //paint all of the data to the panel for the viewer 
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D) gr;
        g.setRenderingHint(
                        RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
        //if player hasn't lost, position gun and zombies on the screen
        if ((inGame))
        {
            //set font for the score counter
            String msg = Integer.toString(point);  
            Font small = new Font("Helvetica", Font.BOLD, 50);
            FontMetrics metr = getFontMetrics(small);
            g.setColor(Color.black);
            g.setFont(small);
            g.drawString(msg, ((B_WIDTH) / 2), (B_HEIGHT / 2) - 400);
            AffineTransform oldAT = g.getTransform();
            //position the gun and bullet marker in the frame so that they track with the mouse 
            g.drawImage(bu, -500 + imagePosition.x, -70 + imagePosition.y, null);
            g.drawImage(gu, -500 + imagePosition.x, -70 + imagePosition.y, null);
            g.setTransform(oldAT);
            //make a zombie in the top left corner of the screen and slowly move it down towards the middle
            Image image = en.getScaledInstance(a, a, Image.SCALE_DEFAULT);
            g.drawImage(image, a, a, null);
            //if the zombie gets hit, move it off the screen (ie. if its coordinates match mouse coordinates) and increase # of points
            if (((-500 + imagePosition.x) < a + a) && ((-500 + imagePosition.x) > a - 30) && ((-70 + imagePosition.y) < a + 10 + a/5) && ((-70 + imagePosition.y) > a - 10 - a/5))
            {
                a = -1000000000;
                point ++;
            }
            //spawn in a new zombie when the first one is hit and teleport the first one back to the corner
            if (point >= 1)
            {
            Image image2 = en10.getScaledInstance(b, b, Image.SCALE_DEFAULT);
            g.drawImage(image2, b + 100, b + 2, null);
            //check if this zombie is hit
            if (((-600 + imagePosition.x) < b + b) && ((-600 + imagePosition.x) > b - 30) && ((-68 + imagePosition.y) < b + 10 + b/5) && ((-68 + imagePosition.y) > b - 10 - b/5))
            {
                b = -10000000;
                point ++;
                a = 1;
            }
            }
            //continue spawning zombies in and incrementing points 
            if (point >= 2)
            {
            Image image2 = en10.getScaledInstance(c, c, Image.SCALE_DEFAULT);
            g.drawImage(image2, c, c, null);
            
            if (((-500 + imagePosition.x) < c + c) && ((-500 + imagePosition.x) > c - 30) && ((-70 + imagePosition.y) < c + 10 + c/5) && ((-70 + imagePosition.y) > c - 10 - c/5))
            {
                b = 1;
                c = -100000;
                point ++;
            }
            }
            if (point >= 3)
            {
            Image image2 = en1.getScaledInstance(d, d, Image.SCALE_DEFAULT);
            g.drawImage(image2, d + 100, d + 2, null);
            if (((-600 + imagePosition.x) < d + d) && ((-600 + imagePosition.x) > d - 30) && ((-68 + imagePosition.y) < d + 10 + d/5) && ((-68 + imagePosition.y) > d - 10 - d/5))
            {
                d = -10000000; 
                if (((-600 + imagePosition.x) < b + b) && ((-600 + imagePosition.x) > b - 30) && ((-68 + imagePosition.y) < b + 10 + b/5) && ((-68 + imagePosition.y) > b - 10 - b/5))
                {
                    b = -10000000;
                    point ++;
                    a = 1;
                }
                point ++;
                c = 1;
            }
            }
            if (point >= 4)
            {
            Image image2 = en10.getScaledInstance(e, e, Image.SCALE_DEFAULT);
            g.drawImage(image2, e+ 100, e + 2, null);
            
            if (((-600 + imagePosition.x) < e + e) && ((-600 + imagePosition.x) > e - 30) && ((-68 + imagePosition.y) < e + 10 + e/5) && ((-68 + imagePosition.y) > e - 10 - e/5))
            {
                e = -10000000;
                point ++;
                d = 1;
            }
            }
            if (point >= 5)
            {
            Image image2 = en10.getScaledInstance(f, f, Image.SCALE_DEFAULT);
            g.drawImage(image2, f, f, null);
            
            if (((-500 + imagePosition.x) < f + f) && ((-500 + imagePosition.x) > f - 30) && ((-70 + imagePosition.y) < f + 10 + f/5) && ((-70 + imagePosition.y) > f - 10 - f/5))
            {
                e = 1;
                f = -100000;
                point ++;
            }
            }
            if (point >= 6)
            {
            Image image2 = en1.getScaledInstance(h, h, Image.SCALE_DEFAULT);
            g.drawImage(image2, h + 100, h + 2, null);
            if (((-600 + imagePosition.x) < h + h) && ((-600 + imagePosition.x) > h - 30) && ((-68 + imagePosition.y) < h + 10 + h/5) && ((-68 + imagePosition.y) > h - 10 - h/5))
            {
                h = -10000000; if (((-600 + imagePosition.x) < e + e) && ((-600 + imagePosition.x) > e - 30) && ((-68 + imagePosition.y) < e + 10 + e/5) && ((-68 + imagePosition.y) > e - 10 - e/5))
                {
                    h = -10000000;
                    point ++;
                    e = 1;
                }
                point ++;
                h = 1;
            }
            }
        }
        //if the player get's enough points, display you win text and stop the painting of the game
        if ((point >= 25))
		{
	 String msg = "You Win!";
	  inGame = false;
      Font small = new Font("Helvetica", Font.BOLD, 50);
     FontMetrics metr = getFontMetrics(small);
      g.setColor(Color.black);
      g.setFont(small);
      g.drawString(msg, ((B_WIDTH) / 2) - 500, (B_HEIGHT / 2) - 200);
		}
        //if the player isn't in the game, and they haven't won, print a game over message 
        else if ((inGame == false) && (point <= 40))
        {
        	 
            String msg = "Game Over";
            Font small = new Font("Helvetica", Font.BOLD, 50);
            FontMetrics metr = getFontMetrics(small);
            g.setColor(Color.black);
            g.setFont(small);
            g.drawString(msg, ((B_WIDTH) / 2) - 500, (B_HEIGHT / 2) - 200);
        }
        //increment all of the zombies' positions 
        x += 1;
        a += 2;
        b ++;
        c ++;
        d += 2;
        e += 1;
        f ++;
        h += 2;
    }
    //call the repaint method
    public void repain()
    {
    		 repaint();
    }
    //when the mouse moves, repaint the screen and make the mouse coordinates into game coordinates
    public void mouseMoved(MouseEvent e) {
        mousePoint = e.getPoint();
        imagePosition.y = mousePoint.y;
        imagePosition.x = mousePoint.x;
        repain();
    }
	@Override
	public void mouseDragged(MouseEvent e) {
	}

}
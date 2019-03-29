package som;

import java.awt.Color;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;

public class Display extends JPanel {
   
   private int width, height;
   private BufferedImage img;
   
   private javax.swing.JButton jButton1;
   private javax.swing.JSplitPane jSplitPane1;
   private javax.swing.JButton btnRetrain;
   private javax.swing.JPanel ControlsPanel;
   private javax.swing.JPanel jPanel1;
   
   public Display(int w, int h) {
      width = w;
      height = h;
      
//      buildDisplay();
//      System.out.println("image = "+ createImage(width, height));
//      img = (BufferedImage)createImage(width, height);
//      if (img == null) System.out.println("null");
//      else System.out.println("not null");
   }
   
   public BufferedImage getImage() {
		if (img == null)
			img = (BufferedImage)createImage(width, height);
		
		return img;
	}
   
   public void paint(Graphics g) {
		if (img == null)
			super.paint(g);
		else
			g.drawImage(img, 0, 0, this);
	}
   
   public void showDisplayer(double[][][] colors) {
      
//      if (img == null) System.out.println("null");
//      else System.out.println("not null");
      Graphics2D g2d = img.createGraphics();
      int r, g, b;
      g2d.setBackground(Color.black);
      g2d.clearRect(0, 0, width, height);
      for (int i = 0; i < colors.length; i++) {
         for (int j = 0; j < colors[i].length; j++) {
            r = (int)(colors[i][j][0] * 255);
            g = (int)(colors[i][j][1] * 255);
            b = (int)(colors[i][j][2] * 255);
            
            g2d.setColor(new Color(r, g, b));
            g2d.fillRect(j*2, i*2, 2, 2);
         }
      }
      
      g2d.dispose();
      repaint();
      
   }
	
	public void setImage(BufferedImage bimg) {
		img = bimg;
	}
   
}

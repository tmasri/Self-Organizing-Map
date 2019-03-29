package som;

import java.awt.image.BufferedImage;

public class Som extends javax.swing.JFrame {

   private int ITERATIONS = 500;
   private int SIZE = 200;
   private int inputSize = 400;
   private int radius = 35;
   private int displaySize;
   private double start_learning_rate = 0.07;
   private Display display;
   
   
   private javax.swing.JButton jButton1;
   private javax.swing.JSplitPane jSplitPane1;
   private javax.swing.JButton btnRetrain;
   private javax.swing.JPanel ControlsPanel;
   private javax.swing.JPanel jPanel1;
   
   
   private double[][][] colors;
   double[][] inputColor;
   
   public Som(int s) {
      if (s != -1) {
         SIZE = s;
         inputSize = s*2;
      }
      
      colors = new double[SIZE][SIZE][3];
      inputColor = new double[inputSize][3];
      display = new Display(SIZE*2, SIZE*2);
      initComponents();
      BufferedImage img = display.getImage();
      displaySize = SIZE * 2;
      // get the colors
      
      for (int i = 0; i < SIZE; i++) {
         for (int j = 0; j < SIZE; j++) {
            for (int k = 0; k < 3; k++) {
               colors[i][j][k] = Math.random();
            }
         }
      }
      
      
      for (int i = 0; i < inputSize; i++) {
         for (int j = 0; j < 3; j++) {
            inputColor[i][j] = Math.random();
         }
      }
      
      display.showDisplayer(colors);
              
      
   }
   
   public double[][][] train(int i, double[][][] colors, double[][] inputColor) {
      
      
      
      
      double lamda = ITERATIONS / Math.log(radius);
      
      double nRadius, learningRate, dist, weightChange;
      int x, y;
      double[] color, bmuInfo, bmu, curr;
      bmu = new double[3];
//      for (int i = 0; i < ITERATIONS; i++) {
         System.out.println("iteration " + i);
         nRadius = radius * Math.exp(-i / lamda);
         System.out.println("new radius = "+ nRadius);
         learningRate = start_learning_rate * Math.exp(-i/lamda);
         
         for (int j = 0; j < inputSize; j++) {
            color = inputColor[j];
            bmuInfo = getBMU(color, colors);
            for (int k = 0; k < bmu.length; k++) {
               bmu[k] = bmuInfo[k];
            }
            x = (int)bmuInfo[3];
            y = (int)bmuInfo[4];
            
            int startX = (int)((x - nRadius < 0) ? 0 : x - nRadius);
            int startY = (int)((y - nRadius < 0) ? 0 : y - nRadius);
            int endX = (int)((x + nRadius >= 200) ? 0 : x + nRadius);
            int endY = (int)((y + nRadius >= 200) ? 0 : y + nRadius);
//            for (int k = startY; k < endY; k++) { // colors.length
//               for (int l = startX; l < endX; l++) { // colors[i].length
            for (int k = 0; k < colors.length; k++) {
               for (int l = 0; l < colors[k].length; l++) {
                  curr = colors[k][l];
                  dist = different(x, y, l, k);
//                  if (dist <= nRadius) {
                     weightChange = getWeightChange(dist, nRadius);
                     colors[k][l] = changeWeight(colors[k][l], color, learningRate, weightChange);
//                  }
               }
            }
            
//            System.out.println("here");
            display.showDisplayer(colors);
            
         }
//      }
      return colors;
      
   }
   
   private double[] changeWeight(double[] colors, double[] input, double learning, double change) {
      
      for (int i = 0; i < 3; i++) {
         colors[i] += change * learning * (input[i] - colors[i]);
      }
      
      return colors;
      
   }
   
   private double getWeightChange(double dist, double rad) {
      
      double rad2 = 2 * Math.pow(rad, 2);
      
      return Math.exp(-(dist*dist)/rad2);
      
   }
   
   private double different(int x1, int y1, int x2, int y2) {
      
      double x = Math.pow(x1 - x2, 2);
      double y = Math.pow(y1 - y2, 2);
      
      return Math.sqrt(x + y);
      
   }
   
   private double[] getBMU(double[] chosen, double[][][] color) {
      
      double[] bmu = color[0][0];
      double bestDistance = euclidianDistance(bmu, chosen);
      
      int xCoord = 0;
      int yCoord = 0;
      
      double curDistance;
      for (int i = 0; i < color.length; i++) {
         for (int j = 0; j < color[i].length; j++) {
            curDistance = euclidianDistance(color[i][j], chosen);
            
            if (curDistance < bestDistance) {
               bestDistance = curDistance;
               bmu = color[i][j];
               xCoord = j;
               yCoord = i;
            }
         }
      }
      
      return new double[]{
         bmu[0],
         bmu[1],
         bmu[2],
         xCoord,
         yCoord
      };
      
   }
   
   private double euclidianDistance(double[] bmu, double[] chosen) {
      
      double sum = 0;
      for (int i = 0; i < bmu.length; i++) {
         sum += Math.pow(bmu[i] - chosen[i], 2);
      }
      
      return Math.sqrt(sum);
      
   }
   
   private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		System.exit(0);
	}
   
   	private void btnRetrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetrainActionPerformed
//		lattice = new SOMLattice(latticeWidth, latticeHeight);
//		trainer.setTraining(lattice, inputVectors, renderPanel);
//		renderPanel.registerLattice(lattice);
//		train();
	}
   
   private void initComponents() {//GEN-BEGIN:initComponents
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        
        ControlsPanel = new javax.swing.JPanel();
        btnRetrain = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setTitle("SOM Demo");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        display.setBackground(new java.awt.Color(0, 0, 0));
        display.setFont(new java.awt.Font("Dialog", 0, 11));
        display.setMinimumSize(new java.awt.Dimension(SIZE*2, SIZE*2));
        display.setSize(new java.awt.Dimension(SIZE*2,SIZE*2));
        display.setPreferredSize(new java.awt.Dimension(SIZE*2,SIZE*2));
        
        add(display);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        //setSize(new java.awt.Dimension(550, 400));
        setLocation((screenSize.width-550)/2,(screenSize.height-400)/2);
    }//GEN-END:initComponents
   
   public static void main(String[] args) {
      int size = 200;
      int input = size*2;
      Som s = new Som(size);
      
      double[][][] colors = new double[size][size][3];
      for (int i = 0; i < size; i++) {
         for (int j = 0; j < size; j++) {
            for (int k = 0; k < 3; k++) {
               colors[i][j][k] = Math.random();
            }
         }
      }
      
      double[][] inputColor = new double[input][3];
      for (int i = 0; i < input; i++) {
         for (int j = 0; j < 3; j++) {
            inputColor[i][j] = Math.random();
         }
      }
      
      for (int i = 0; i < 500; i++) {
         s.show();
         colors = s.train(i, colors, inputColor);
      }
   }
   
}

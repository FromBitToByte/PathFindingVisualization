package pathfindingproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

public class frame3 extends javax.swing.JFrame {

    Grid canvas;
    private int WidthFrame;
    private int HeightFrame;
    private int inf;
    private int cellCountx, cellCounty, cellSizex, cellSizey;
    public frame3() {
        inf = 1000000007;
        initComponents();
        this.setResizable(false);
        WidthFrame = 1040;
        HeightFrame = 710;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int leftMargin = ((int)screenSize.getWidth()-WidthFrame)/2;
        setLocation( leftMargin, 0 );
        initGrid();
        
    }
    public void initGrid(){
        
        canvas = new Grid();
        int leftMargin = (WidthFrame-canvas.WidthGrid)/2;
        canvas.setBounds( leftMargin, 170, canvas.WidthGrid+1, canvas.HeightGrid+1  );
        this.getContentPane().add(canvas);
    }
   /*---------------------------------------------------------------------------------------------------------------*/
   //class Cell
   private class Cell{
       private int x, y, type, parx, pary, fx, gx, hx;
       Cell(){//constructor
           x = y = parx = pary = -1;
           gx = hx = inf;
           fx = gx+hx;
       }
       Cell( int tt, int xx, int yy ){//constructor
           x = xx; y = yy; parx = pary = -1;
           gx = hx = inf;
           fx = gx+hx;
       }
       public int getType(){ return type; }
       public void setType(int tt){ type = tt; }
       public void setgx( int gxx ){
            gx = gxx;
            fx = gx+hx;
       }
       public void sethx( int hxx ){
            hx = hxx;
            fx = gx+hx;
       }
   }
    // class Grid
   /*---------------------------------------------------------------------------------------------------------------*/
    private class Grid extends JPanel implements MouseListener, MouseMotionListener {// class Grid
        Cell matrix[][];
        int WidthGrid;
        int HeightGrid;
        
        public void initMatrix(){
            matrix = new Cell[cellCountx][cellCounty];
            for( int i = 0; i < cellCountx ; i++ ){
                for( int j = 0 ; j < cellCounty ; j++ ){
                    matrix[i][j] = new Cell( 3, i, j );
                }
            }
        }
        Grid(){
            addMouseListener(this);
            addMouseMotionListener(this);
            WidthGrid = 960;
            HeightGrid = 480;
            cellCountx = 40;
            cellCounty = 20;
            cellSizex = WidthGrid/(cellCountx);
            cellSizey = HeightGrid/(cellCounty);
            initMatrix();
        }
        
        /*------- PAINT COMPONENT(inside Class Grid) -------------------------------------------------------*/
        @Override
        public void paintComponent( Graphics g ){ // called to change the colour of cells according to their type
            System.out.println("YES");
            super.paintComponent(g);// calls the default function of parent class
            for( int x = 0; x < cellCountx ; x++ ){
                for( int y = 0 ; y < cellCounty ; y++ ){
                    if( matrix[x][y].getType() == 0 ){
                        g.setColor(Color.YELLOW);
                    }else if(  matrix[x][y].getType() == 1 ){
                        g.setColor(Color.RED);
                    }else if(  matrix[x][y].getType() == 2 ){
                        g.setColor(Color.BLACK);
                    }else if(  matrix[x][y].getType() == 3 ){
                        g.setColor(Color.WHITE);
                    }
                    if( matrix[x][y].getType() == 4 ){
                        g.setColor(Color.BLUE );
                        g.fillRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey);
                        g.setColor( Color.pink ); 
                        g.fillOval(x*cellSizex , y*cellSizey, cellSizex, cellSizey);
                    }else if( matrix[x][y].getType() == 5 ){
                        g.setColor(Color.BLUE );
                        g.fillRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey);
                        g.setColor( Color.cyan ); 
                        g.fillOval(x*cellSizex , y*cellSizey, cellSizex, cellSizey);
                    }
                    else
                        g.fillRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey);
                    g.setColor(Color.white);
                     g.fillRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey);
                    g.setColor(Color.black);
                    g.drawRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey );
                }
            }
        }/*------- PAINT COMPONENT END (inside Class Grid) -------------------------------------------------------*/
        
        
        /*------------------- Mouse Click Events --------------------------------------------------------*/
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
        @Override
        public void mouseDragged(MouseEvent e) {
        }
        @Override
        public void mouseMoved(MouseEvent e) {
        }
       /*------------------- Mouse Click Events --------------------------------------------------------*/
         
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pathfindingproject/wallpaper1.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 937, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 204, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}

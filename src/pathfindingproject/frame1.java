package pathfindingproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import static java.lang.System.exit;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class frame1 extends javax.swing.JFrame {

    private Grid canvas;
    private int widthFrame;
    private int heightFrame;
    int delayTime;
    Algorithm algo;
    Queue<Cell> path, path2;
    Timer timer;
    Timer timer1;
    boolean timerStarted;
    private int dbg;
    Random ran;
    private void initFrame(){
        timerStarted = false;
        path = new LinkedList();
        path2 = new LinkedList();
        algo = new Algorithm();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        delayTime = 100;
        widthFrame = 1040;
        heightFrame = 710;
        int leftMargin = ((int)screenSize.getWidth()-widthFrame)/2;
        this.setSize(widthFrame ,heightFrame);
        
        this.setLocation( leftMargin, 20 );
        this.setResizable(false);
        
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }
    private void initGrid(){
        canvas = new Grid();
        int leftMargin = (widthFrame-canvas.widthGrid)/2;
        canvas.setBounds( leftMargin-10, 170, canvas.widthGrid+1, canvas.heightGrid+1  );
    }
    private void initSliders(){
        sizeSlider.setMinimum( 0 );
        sizeSlider.setMaximum( 4 );
        sizeSlider.setValue(2);
        sizeSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                       if( sizeSlider.getValue() == 0 ){
                            canvas.cellCountx = 10;
                            canvas.cellCounty = 5;
                       }else{
                            canvas.cellCountx = sizeSlider.getValue()*20;
                            canvas.cellCounty = sizeSlider.getValue()*10;
                       }
                       canvas.initMatrix();
                       canvas.updateGrid();
                }
                
        });
        speedSlider.setMinimum( 0 );
        speedSlider.setMaximum( 50 );
        speedSlider.setValue(25);
        speedSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    delayTime = 200 - 2*speedSlider.getValue();
                    speedLabel.setText( ""+speedSlider.getValue()*2 + "%" );
                    if( timerStarted == true )   
                        timer.setDelay( delayTime );
                }
                
        });
    }
    private void initRadioButtons(){
        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(eraseRB);
        bg1.add(startRB);
        bg1.add(endRB);
        bg1.add(wallRB);
//        startRB.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                canvas.resetGrid();
//            }
//        });
  
    }
    public void showPath(){
        path.remove();//starting Element
        path2.clear();
        if( path.element() == null ){// null
            path.remove();
        }
        timer = new Timer(delayTime, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( path.isEmpty() ) {
                    timerStarted = false;
                    while( !path2.isEmpty() ){
                        path2.element().setType(5);
                        path2.remove();
                    }
                     canvas.updateGrid();
                     ((Timer) e.getSource()).stop();
                } else {
                    while( !path2.isEmpty() ){
                        path2.element().setType(5);
                        path2.remove();
                    }
                    while( true ){
                        if( path.isEmpty() )
                            break;
                        if(  path.element() == null ){
                            path.remove();
                            break;
                        }else{
                            path.element().setType(4);
                            path2.add( path.element() );
                            path.remove();
                        }
                    }
                }
                canvas.updateGrid();
            }   
           });
           timer.start();
           
    }
    public void showPathDFS(){
        path.remove();
        int x, y;
        timer = new Timer(delayTime, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( path.isEmpty() ) {
                    ((Timer) e.getSource()).stop();
                } else {
                    path.element().setType( 5 );
                    path.remove();
                }
                canvas.updateGrid();
            }   
           });
           timer.start();
           
    }
    public frame1( ){ // CONSTRUCTOR
        initComponents();
        initFrame();
        initGrid();
        this.getContentPane().add(canvas);
        initSliders();
        initRadioButtons();
        
    }
    private class Cell{
        private int x;
        private int y;
        private int type;
        private int gx;
        private int hx;
        private int fx;
        private int parx;
        private int pary;
        private int insertTime;
        Cell(){
            type = 0;
            x = -1;
            y = -1;
            parx = pary = -1;
        }
       
        Cell( int tt, int xx, int yy ){
            type = tt;
            x = xx;
            y = yy;
            parx = pary = -1;
        }
        int getType(){
            return type;
        }
        public void setType( int tt ){
            type = tt;
        }
        public boolean equals( Cell other ){
            return ( x == other.x && y == other.y );
        }
        public int compareTo( Cell other ){
            if( this.fx == other.fx ){
                return 0;
            }
            if( this.fx < other.fx )// callerObject is smaller
                return -1;
            return 1;
        }
        public void setValues(){
            if( type == 2 ){
                gx = hx = 1000000007;
                fx = gx+hx;
            }else{
                gx = 1000000007;
                int dx = Math.abs(canvas.endCellx-x); 
                int dy = Math.abs(canvas.endCelly-y);
                /*if( dx > dy ){
                    hx = 14*dx + 10*(dx-dy);
                }else{
                    hx = 14*dx + 10*(dy-dx);
                }*/
                hx = dx+dy;
//                hx = (int) Math.round((double)dx*dx+dy*dy);
                fx = hx+gx;
            }
        }
    }
    public class CellComparator implements Comparator<Cell>{

        @Override
        public int compare(Cell o1, Cell o2) {
             if( o1.fx == o2.fx ){
                return ( o1.gx <=  o2.gx ? -1 : 1 );
            }
            if( o1.fx < o2.fx )// callerObject is smaller
                return -1;
            return 1;
        }
        
    }
    private class Grid extends JPanel implements MouseListener, MouseMotionListener {// class Grid
        private int cellCountx;
        private int cellCounty;
        private int widthGrid;
        private int heightGrid;
        private int cellSizex;
        private int cellSizey;
        private Cell[][] matrix;
        int startCellx, startCelly;
        int endCellx, endCelly;
        private LinkedList<Cell> gridMaze;
        boolean firstdiv;
        public void initMatrix(){
            matrix = new Cell[cellCountx][cellCounty];
            for( int i = 0; i < cellCountx ; i++ ){
                for( int j = 0 ; j < cellCounty ; j++ ){
                    matrix[i][j] = new Cell( 3, i, j );
                }
            }
            startCellx = startCelly = -1;
            endCellx = endCelly = -1;
        }
        Grid(){
            startRB.setSelected(true);
            addMouseListener(this);
            addMouseMotionListener(this);
            cellCounty = 20;
            cellCountx = 40;
            widthGrid = 960;
            heightGrid = 480;
            cellSizex = widthGrid/(cellCountx);
            cellSizey = heightGrid/(cellCounty);
            initMatrix();
            
        }
        public void updateGrid(){
            cellSizex = widthGrid/(cellCountx);
            cellSizey = heightGrid/(cellCounty);
            canvas.repaint();
        }
        public void clearGrid(){// sets colour of all the cells to white
            path.clear();
            
            startCellx = startCelly = -1;
            endCellx = endCelly = -1;
            
            for( int i = 0, j ; i < cellCountx ; i++ ){
                for( j = 0; j < cellCounty ; j++  ){
                    matrix[i][j].setType(3);
                }
            }
            updateGrid();
        }
        public void resetGrid(){//clears all the visited nodes excepts walls, startCell, endCell
            path.clear();
            path2.clear();
            for( int i = 0 ; i < canvas.cellCountx ; i++ ){
                for( int j = 0 ; j < canvas.cellCounty ; j++ ){
                    if( i == canvas.startCellx && j == canvas.startCelly ){
                        canvas.matrix[i][j].setType(0);
                    }else if( i == canvas.endCellx && j == canvas.endCelly ){
                        canvas.matrix[i][j].setType(1);
                    }else if( canvas.matrix[i][j].getType() != 2 ){
                        canvas.matrix[i][j].setType(3);
                    }
                }
            }
            canvas.updateGrid();
        }
        public void gRM( int x, int y, int h, int w ){
            Random r = new Random();
            if( h < 2 || w < 2 || x <= 0 || y <= 0 || x >= cellCountx-1 || y >= cellCounty-1 ){
               return;
            }
            
            
            int ori;// orientation 0->Horizontal, 1->Vertical
            
            if(  h == w ){
                ori =  r.nextInt(2);
                
            }else{
                ori = (w > h ? 1 : 0);//|(r.nextInt(2)&r.nextInt(2)) ;
            }
//            if( firstdiv ){
//                ori = r.nextInt(2) & r.nextInt(2);
//                firstdiv = false;
//            }
            
//            System.out.println(""+ori);
            int gx, gy, wx, wy;
//            System.out.println( x + " " + y );
           
            if( ori == 0 ){
                wx = x + 2*r.nextInt((h+1)/2) + ((x%2 == 0) ? 0 : 1);//must be at even position
                gy = y + 2*r.nextInt((w+1)/2) + ((y%2 == 0) ? 1 : 0);// must be at odd position
                
                for( int i = 0 ; i <= w && wx != cellCountx-2  ; i++ ){
                    if( i+y != gy   ){//&& (wx+1 != cellCountx-1)
//                       matrix[wx][i+y].setType(2);
                       gridMaze.add(matrix[wx][i+y]);
                    }
                }
               
                gRM( x, y, wx-x-1, w  );
                gRM( wx+1, y, (x+h)-wx-1, w  );    
                
            }else{
                
                wy = y + 2*r.nextInt((w+1)/2) + ((y%2 == 0) ? 0 : 1);//must be at even position
                gx = x + 2*r.nextInt((h+1)/2) + ((x%2 == 0) ? 1 : 0);// must be at odd position
                
                
                for( int i = 0 ; i <= h && wy != cellCounty-2 ; i++ ){
                    if( i+x != gx  ){//&& (wy+1!=cellCounty-1)    
//                       matrix[i+x][wy].setType(2);
                       gridMaze.add(matrix[i+x][wy]);
                    }
                }
                gRM( x, y, h, wy-y-1  );
                gRM( x, wy+1, h, (y+w)-wy-1  );
               
            }
        }
        public void generateRecursiveMaze(){
            clearGrid();
            gridMaze = new LinkedList();
//            gridMaze.clear();
            
            for( int i = 0 ; i < cellCounty ; i++ ){
//                matrix[0][i].setType(2);
//                matrix[cellCountx-1][i].setType(2);
                gridMaze.add(matrix[0][i]);
            }
            for( int i = 0 ; i < cellCountx ; i++ ){
//                matrix[i][0].setType(2);
//                matrix[i][cellCounty-1].setType(2);
                gridMaze.add(matrix[i][cellCounty-1] );
            }
            for( int i = cellCounty-1 ; i >= 0 ; i-- ){
//                matrix[0][i].setType(2);
//                matrix[cellCountx-1][i].setType(2);
                gridMaze.add(matrix[cellCountx-1][i]);
                
            }
            for( int i = cellCountx-1 ; i >= 0 ; i-- ){
//                matrix[i][0].setType(2);
//                matrix[i][cellCounty-1].setType(2);
                gridMaze.add(matrix[i][0]);
            }
            firstdiv = true;
            gRM( 1, 1, cellCountx-2, cellCounty-2 );
            

            timer = new Timer(5, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( gridMaze.isEmpty() ) {
                     ((Timer) e.getSource()).stop();
                } else {
                    gridMaze.element().setType(2);
                    gridMaze.remove();
                }
                updateGrid();
            }   
           });
           timer.start();
           updateGrid();
            
        }
        public void generateRandomMaze(){
            ran = new Random();
            int n = ran.nextInt(300);
            boolean solve = true;
            dbg = 0;
            timer = new Timer(10, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   if ( dbg >= n   ) {
                       ((Timer) e.getSource()).stop();
                   } else {
                       dbg+=1;
                       int x = ran.nextInt(cellCountx);
                       int y = ran.nextInt(cellCounty);
                       matrix[x][y].setType(2);
                       canvas.updateGrid();
                   }
                   
               }   
              });
              timer.start();
        }
        public void generateMaze(){
            ran = new Random();
            
            boolean vis[][] = new boolean[cellCountx][cellCounty];
            for( int i = 0, j ; i < cellCountx ; i++ ){
                for( j = 0 ; j < cellCounty ; j++ ){
                    vis[i][j] = false;
                    matrix[i][j].setType(2);
                }
            }
                int x = ran.nextInt(cellCountx);
                int y = ran.nextInt(cellCounty);
                Queue<Cell> q = new LinkedList();
                q.add( matrix[x][y] );
                vis[x][y] = true;
                /*while( !q.isEmpty() ){
                    x = q.element().x;
                    y = q.element().y;
                    matrix[x][y].setType(2);
                    q.remove();
                    LinkedList<Cell> arr = new LinkedList();
                    if( y-1 >= 0  ){
                        if( !vis[x][y-1] ){
                            arr.add( matrix[x][y-1] );
                            vis[x][y-1] = true;
                        }
                    }//u
                    if( y+1 < cellCounty ){
                        if( !vis[x][y+1] ){
                            arr.add( matrix[x][y+1] );
                            vis[x][y+1] = true;
                        }
                    }//d

                    if( x+1 < cellCountx ){
                        if( !vis[x+1][y] ){
                            arr.add( matrix[x+1][y] );
                            vis[x+1][y] = true;
                        }
                    }//r
                    if( x-1 >= 0  ){
                        if( !vis[x-1][y] ){
                            arr.add( matrix[x-1][y] );
                            vis[x-1][y] = true;
                        }
                    }//l
                    if( arr.size() == 0 ){
                        continue;
                    }

                    int n  = ran.nextInt(arr.size());
                    int ok, val = 0;
                    if( n == 0 )
                        n++;
                    for( int p = 0 ; p < n ; p++ ){
                        ok = ran.nextInt( arr.size() );
                        if( (val&(1<<ok)) == 0 && ok < arr.size()){
                            q.add( arr.get(ok) );
                            val |= (1<<ok);
                        }
                    }
                }
                updateGrid();*/
                
                timer = new Timer(5, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   if ( q.isEmpty()  ) {
                       ((Timer) e.getSource()).stop();
                   } else {
                       int x = q.element().x;
                       int y = q.element().y;
                       matrix[x][y].setType(5);
                       q.remove();
                       LinkedList<Cell> arr = new LinkedList();
                       if( y-1 >= 0  ){
                           if( !vis[x][y-1] ){
                               arr.add( matrix[x][y-1] );
                               vis[x][y-1] = true;
                           }
                       }//u
                       if( y+1 < cellCounty ){
                           if( !vis[x][y+1] ){
                               arr.add( matrix[x][y+1] );
                               vis[x][y+1] = true;
                           }
                       }//d

                       if( x+1 < cellCountx ){
                           if( !vis[x+1][y] ){
                               arr.add( matrix[x+1][y] );
                               vis[x+1][y] = true;
                           }
                       }//r
                       if( x-1 >= 0  ){
                           if( !vis[x-1][y] ){
                               arr.add( matrix[x-1][y] );
                               vis[x-1][y] = true;
                           }
                       }//l
                       if( arr.size() != 0 ){
                           
                            int n  = ran.nextInt(arr.size());
                            int ok, val = 0;
                            if( n == 0 )
                                n = 1;
                            
                            for( int p = 0 ; p < n ; p++ ){
                                ok = ran.nextInt( arr.size() );
                                if( (val&(1<<ok)) == 0 && ok < arr.size()){
                                    q.add( arr.get(ok) );
                                    val |= (1<<ok);
                                }
                            }
                            
                       }
                       
                       canvas.updateGrid();
                       
                   }
                   
               }   
              });
              timer.start();
            
            
        }
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
                    g.setColor(Color.black);
                    g.drawRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey );
                }
            }
        }
        // why
        @Override
        public void mouseClicked(MouseEvent e) {
           
        }

        @Override
        public void mousePressed(MouseEvent e) {
            try{ 
                int x = e.getX()/cellSizex;
                int y = e.getY()/cellSizey;
                if( x == startCellx && y == startCelly ){
                    matrix[startCellx][startCelly].setType(3);
                    startCellx = startCelly = -1;
                }
                if( x == endCellx && y == endCelly ){
                    matrix[endCellx][endCelly].setType(3);
                    endCellx = endCelly = -1;
                }
                if( startRB.isSelected() ){
                    if( startCellx != -1 && startCelly != -1 ){
                         matrix[startCellx][startCelly].setType(3);
                    }
                    startCellx = x;
                    startCelly = y;
                    matrix[x][y].setType(0);
                }else if( endRB.isSelected() ){
                    if( endCellx != -1 && endCelly != -1 ){
                         matrix[endCellx][endCelly].setType(3);
                    }
                    endCellx = x;
                    endCelly = y;
                    matrix[x][y].setType(1);
                }else{
                    if( wallRB.isSelected() ){
                        matrix[x][y].setType(2);
                    }else if( eraseRB.isSelected() ){
                        matrix[x][y].setType(3);
                    }
                }
                resetGrid();
            }catch( Exception error ){
                 
            }
            
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
            try{  
                int x = e.getX()/cellSizex;
                int y = e.getY()/cellSizey;
                if( wallRB.isSelected() || eraseRB.isSelected() ){
                   if( x == startCellx && y == startCelly ){
                        matrix[startCellx][startCelly].setType(3);
                        startCellx = startCelly = -1;
                    }
                    if( x == endCellx && y == endCelly ){
                        matrix[endCellx][endCelly].setType(3);
                        endCellx = endCelly = -1;
                    } 
                    if( wallRB.isSelected() ){
                        matrix[x][y].setType(2);
                    }else if( eraseRB.isSelected() ){
                        matrix[x][y].setType(3);
                    }
                    resetGrid();
                }
            }catch( Exception error ){
                 
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }
//    public void setComponentsEnabled( boolean val){
//        startDFSbtn.setEnabled(val);
//        startBFSbtn.setEnabled(val);
        /*clearGridbtn.setEnabled(val);
        resetGridbtn.setEnabled(val);
        
        startRB.setEnabled(val);
        endRB.setEnabled(val);
        wallRB.setEnabled(val);
        eraseRB.setEnabled(val);
        
        speedSlider.setEnabled(val);
        densitySlider.setEnabled(val);
        sizeSlider.setEnabled(val);*/
        
//    }
    private class Algorithm{
        public void solve(int x){
            if( canvas.startCellx == -1 || canvas.startCelly == -1 ){
                JOptionPane.showMessageDialog( frame1.this, "STARTING CELL NOT SPECIFIED !!" );
                return;
            }
            if( canvas.endCellx == -1 || canvas.endCelly == -1 ){
                JOptionPane.showMessageDialog( frame1.this, "ENDING CELL NOT SPECIFIED !!" );
                return;
            }
            
            if( x == 0 )
                runBFS();
            else if( x==1)
                runDFS();
            else
                runAStar();
        }
        public void runAStar(){
            boolean foundEnd = false;
            boolean vis[][] = new boolean[canvas.cellCountx][canvas.cellCounty];
            for( int i = 0 ; i < canvas.cellCountx ; i++ ){
                for( int j = 0 ; j < canvas.cellCounty ; j++ ){
                    vis[i][j] = false;
                    canvas.matrix[i][j].setValues();
                }
            }
            canvas.resetGrid();
            PriorityQueue<Cell> pq = new PriorityQueue<>( new CellComparator() );
            pq.add( canvas.matrix[canvas.startCellx][canvas.startCelly] );
            vis[canvas.startCellx][canvas.startCelly] = true;
            canvas.matrix[canvas.startCellx][canvas.startCelly].gx = 0;
            canvas.matrix[canvas.startCellx][canvas.startCelly].parx = canvas.startCellx;
            canvas.matrix[canvas.startCellx][canvas.startCelly].pary = canvas.startCelly;
//            canvas.matrix[canvas.startCellx][canvas.startCelly].insertTime = 0;
            canvas.matrix[canvas.startCellx][canvas.startCelly].hx = Math.abs(canvas.endCellx-canvas.startCellx)+Math.abs(canvas.endCelly-canvas.startCelly);
            Cell curr;
            path = new LinkedList();
//            dbg = 1;
            
            while( !pq.isEmpty() ){
//                System.out.println( pq.size() );
                curr = pq.peek();
                int x = curr.x;
                int y = curr.y;
                pq.remove();
                vis[x][y] = true;
                canvas.matrix[x][y].gx = canvas.matrix[ curr.parx ][ curr.pary ].gx+1;
                if( curr.x == canvas.endCellx && curr.y == canvas.endCelly ){
                    foundEnd = true;
                    break;
                }
//                System.out.println( x+" "+y );
                path.add( canvas.matrix[x][y] );
                if( x+1 < canvas.cellCountx && (canvas.matrix[x+1][y].getType() == 3 || canvas.matrix[x+1][y].getType() == 1) ){
                    if( !vis[x+1][y] ){
//                        System.out.println( curr.gx+1 + canvas.matrix[x+1][y].hx) +" "+canvas.matrix[x+1][y].fx );
                        if( ( curr.gx+1 + canvas.matrix[x+1][y].hx) < canvas.matrix[x+1][y].fx ){
                            canvas.matrix[x+1][y].gx = curr.gx+1;
                            canvas.matrix[x+1][y].fx = canvas.matrix[x+1][y].gx + canvas.matrix[x+1][y].hx;
                            canvas.matrix[x+1][y].parx = x;
                            canvas.matrix[x+1][y].pary = y;
//                            canvas.matrix[x+1][y].insertTime = dbg;
//                            dbg++;
//                            vis[x+1][y] = true;
                            if( !pq.contains( canvas.matrix[x+1][y] ) )
                                 pq.add(canvas.matrix[x+1][y]);
                        }
                    }
                }//r
                 if( x-1 >= 0 && (canvas.matrix[x-1][y].getType() == 3 || canvas.matrix[x-1][y].getType() == 1) ){
                    if( (curr.gx+1 + canvas.matrix[x-1][y].hx) < canvas.matrix[x-1][y].fx ){
                            canvas.matrix[x-1][y].gx = curr.gx+1;
                            canvas.matrix[x-1][y].fx = canvas.matrix[x-1][y].gx + canvas.matrix[x-1][y].hx;
                            canvas.matrix[x-1][y].parx = x;
                            canvas.matrix[x-1][y].pary = y;
//                            canvas.matrix[x-1][y].insertTime = dbg;
//                            dbg++;
//                            vis[x-1][y] = true;
                             if( !pq.contains( canvas.matrix[x-1][y] ) )
                                 pq.add(canvas.matrix[x-1][y]);
                        }
                }//l
                 if( y+1 < canvas.cellCounty && (canvas.matrix[x][y+1].getType() == 3 || canvas.matrix[x][y+1].getType() == 1) ){
                    if( !vis[x][y+1] ){
                        if( (curr.gx+1 + canvas.matrix[x][y+1].hx) < canvas.matrix[x][y+1].fx ){
                            canvas.matrix[x][y+1].gx = curr.gx+1;
                            canvas.matrix[x][y+1].fx = canvas.matrix[x][y+1].gx + canvas.matrix[x][y+1].hx;
                            canvas.matrix[x][y+1].parx = x;
                            canvas.matrix[x][y+1].pary = y;
//                            canvas.matrix[x][y+1].insertTime = dbg;
//                            dbg++;
//                            vis[x][y+1] = true;
                             if( !pq.contains( canvas.matrix[x][y+1] ) )
                                 pq.add(canvas.matrix[x][y+1]);
                        }
                    }
                }//d
                if( y-1 >= 0 && (canvas.matrix[x][y-1].getType() == 3 || canvas.matrix[x][y-1].getType() == 1) ){
                    if( !vis[x][y-1] ){
                        if( (curr.gx+1 + canvas.matrix[x][y-1].hx) < canvas.matrix[x][y-1].fx ){
                            canvas.matrix[x][y-1].gx = curr.gx+1;
                            canvas.matrix[x][y-1].fx = canvas.matrix[x][y-1].gx + canvas.matrix[x][y-1].hx;
                            canvas.matrix[x][y-1].parx = x;
                            canvas.matrix[x][y-1].pary = y;
//                            canvas.matrix[x][y-1].insertTime = dbg;
//                            dbg++;
//                            vis[x][y-1] = true;
                             if( !pq.contains( canvas.matrix[x][y-1] ) )
                                 pq.add(canvas.matrix[x][y-1]);
                        }
                    }
                }//u
                
                
                if( x+1 < canvas.cellCountx && y+1 < canvas.cellCounty && (canvas.matrix[x+1][y+1].getType() == 3 || canvas.matrix[x+1][y+1].getType() == 1) ){
                    if( !vis[x+1][y+1] ){
//                        System.out.println( curr.gx+1 + canvas.matrix[x+1][y].hx) +" "+canvas.matrix[x+1][y].fx );
                        if( ( curr.gx+1 + canvas.matrix[x+1][y+1].hx) < canvas.matrix[x+1][y+1].fx ){
                            canvas.matrix[x+1][y+1].gx = curr.gx+1;
                            canvas.matrix[x+1][y+1].fx = canvas.matrix[x+1][y+1].gx + canvas.matrix[x+1][y+1].hx;
                            canvas.matrix[x+1][y+1].parx = x;
                            canvas.matrix[x+1][y+1].pary = y;
//                            canvas.matrix[x+1][y].insertTime = dbg;
//                            dbg++;
//                            vis[x+1][y] = true;
                            if( !pq.contains( canvas.matrix[x+1][y+1] ) )
                                 pq.add(canvas.matrix[x+1][y+1]);
                        }
                    }
                }//r
                 if( x-1 >= 0 && y-1 >= 0 && (canvas.matrix[x-1][y-1].getType() == 3 || canvas.matrix[x-1][y-1].getType() == 1) ){
                    if( (curr.gx+1 + canvas.matrix[x-1][y-1].hx) < canvas.matrix[x-1][y-1].fx ){
                            canvas.matrix[x-1][y-1].gx = curr.gx+1;
                            canvas.matrix[x-1][y-1].fx = canvas.matrix[x-1][y-1].gx + canvas.matrix[x-1][y-1].hx;
                            canvas.matrix[x-1][y-1].parx = x;
                            canvas.matrix[x-1][y-1].pary = y;
//                            canvas.matrix[x-1][y].insertTime = dbg;
//                            dbg++;
//                            vis[x-1][y] = true;
                             if( !pq.contains( canvas.matrix[x-1][y-1] ) )
                                 pq.add(canvas.matrix[x-1][y-1]);
                        }
                }//l
                 if( y+1 < canvas.cellCounty && x-1 >= 0 &&  (canvas.matrix[x-1][y+1].getType() == 3 || canvas.matrix[x-1][y+1].getType() == 1) ){
                    if( !vis[x-1][y+1] ){
                        if( (curr.gx+1 + canvas.matrix[x-1][y+1].hx) < canvas.matrix[x-1][y+1].fx ){
                            canvas.matrix[x-1][y+1].gx = curr.gx+1;
                            canvas.matrix[x-1][y+1].fx = canvas.matrix[x-1][y+1].gx + canvas.matrix[x-1][y+1].hx;
                            canvas.matrix[x-1][y+1].parx = x;
                            canvas.matrix[x-1][y+1].pary = y;
//                            canvas.matrix[x][y+1].insertTime = dbg;
//                            dbg++;
//                            vis[x][y+1] = true;
                             if( !pq.contains( canvas.matrix[x-1][y+1] ) )
                                 pq.add(canvas.matrix[x-1][y+1]);
                        }
                    }
                }//d
                if( y-1 >= 0 && x+1 < canvas.cellCountx && (canvas.matrix[x+1][y-1].getType() == 3 || canvas.matrix[x+1][y-1].getType() == 1) ){
                    if( !vis[x+1][y-1] ){
                        if( (curr.gx+1 + canvas.matrix[x+1][y-1].hx) < canvas.matrix[x+1][y-1].fx ){
                            canvas.matrix[x+1][y-1].gx = curr.gx+1;
                            canvas.matrix[x+1][y-1].fx = canvas.matrix[x+1][y-1].gx + canvas.matrix[x+1][y-1].hx;
                            canvas.matrix[x+1][y-1].parx = x;
                            canvas.matrix[x+1][y-1].pary = y;
//                            canvas.matrix[x][y-1].insertTime = dbg;
//                            dbg++;
//                            vis[x][y-1] = true;
                             if( !pq.contains( canvas.matrix[x+1][y-1] ) )
                                 pq.add(canvas.matrix[x+1][y-1]);
                        }
                    }
                }//u
                
               
            }
            
            if( foundEnd ){
                int i = canvas.matrix[canvas.endCellx][canvas.endCelly].parx;
                int j = canvas.matrix[canvas.endCellx][canvas.endCelly].pary;
                
                dbg = 0;
                while( dbg < canvas.cellCountx*canvas.cellCounty ){
                    dbg += 1;
                    System.out.println(i+" "+j );
                    path2.add( canvas.matrix[i][j] );
                    if( i == canvas.matrix[i][j].parx && j == canvas.matrix[i][j].pary )
                        break;
                    i = canvas.matrix[i][j].parx;
                    j = canvas.matrix[i][j].pary;
                    
                    if( i == canvas.startCellx && j == canvas.startCelly ){
                        break;
                    }
                }
            }
            /*
            if( path.size() > 0 )
                path.remove();
            timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( path.isEmpty() ) {
                     ((Timer) e.getSource()).stop();
                } else {
                    path.element().setType(5);
                    path.remove();
                }
                canvas.updateGrid();
            }   
           });
           timer.start();
           canvas.updateGrid();
            
           timer1 = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( path2.isEmpty() ) {
                     ((Timer) e.getSource()).stop();
                } else {
                    path2.element().setType(4);
                    path2.remove();
                }
                canvas.updateGrid();
            }   
           });
           timer1.start();
           canvas.updateGrid();
           
           for( int i = 0 ; i < canvas.cellCountx ; i++ ){
               for( int j = 0 ; j < canvas.cellCounty ; j++ ){
                   System.out.print("(" + canvas.matrix[i][j].parx + "," + canvas.matrix[i][j].pary + ") " );
               }
               System.out.println();
           }
            */
           
            
        }
        public void runDFS(){
            boolean vis[][] = new boolean[canvas.cellCountx][canvas.cellCounty];
            for( int i = 0 ; i < canvas.cellCountx ; i++ ){
                for( int j = 0 ; j < canvas.cellCounty ; j++ ){
                    vis[i][j] = false;
                }
            }
            path.clear();
            path2.clear();
            Stack<Cell> stk = new Stack();
            
            stk.push( canvas.matrix[canvas.startCellx][canvas.startCelly] );
//            vis[canvas.startCellx][canvas.startCelly] = true;
           while( !stk.empty() ){
               
                int x = stk.peek().x;
                int y = stk.peek().y;
                stk.pop();
                if( vis[x][y] ){
                    continue;
                }
                path.add( canvas.matrix[x][y] );
                vis[x][y] = true;
                
                if( x == canvas.endCellx && y == canvas.endCelly ){
                    break;
                }
                
                
                
                if( y-1 >= 0 && (canvas.matrix[x][y-1].getType() == 3 || canvas.matrix[x][y-1].getType() == 1) ){
                    if( !vis[x][y-1] ){
                        stk.push( canvas.matrix[x][y-1] );
                    }
                }//u
                if( y+1 < canvas.cellCounty && (canvas.matrix[x][y+1].getType() == 3 || canvas.matrix[x][y+1].getType() == 1) ){
                    if( !vis[x][y+1] ){
                        stk.push( canvas.matrix[x][y+1] );
                    }
                }//d
                
                
                
                if( x+1 < canvas.cellCountx && (canvas.matrix[x+1][y].getType() == 3 || canvas.matrix[x+1][y].getType() == 1) ){
                    if( !vis[x+1][y] ){
                        stk.push( canvas.matrix[x+1][y] );
                    }
                }//r
                if( x-1 >= 0 && (canvas.matrix[x-1][y].getType() == 3 || canvas.matrix[x-1][y].getType() == 1) ){
                    if( !vis[x-1][y] ){
                        stk.push( canvas.matrix[x-1][y] );
                    }
                }//l
                
            }
            showPathDFS();
              
        }
        public void runBFS(){
            Queue<Cell> q = new LinkedList();
            q.add( canvas.matrix[canvas.startCellx][canvas.startCelly] );
            q.add( null );
            path.clear();
            path2.clear();
            boolean vis[][] = new boolean[canvas.cellCountx][canvas.cellCounty];
            for( int i = 0 ; i < canvas.cellCountx ; i++ ){
                for( int j = 0 ; j < canvas.cellCounty ; j++ ){
                    vis[i][j] = false;
                }
            }
            vis[canvas.startCellx][canvas.startCelly] = true;
            while( true ){
                Cell curr = q.element();
                q.remove();
                if( curr == null ){
                    if( q.isEmpty() ){
                        frame1.this.path.add(null);
                        break;
                    }else{
                        q.add( null );
                        frame1.this.path.add( null );
                    }
                    continue;
                }
                int x = curr.x;
                int y = curr.y;
                
                if( curr.x == canvas.endCellx && curr.y == canvas.endCelly ){
                    break;
                }
                frame1.this.path.add( canvas.matrix[x][y] );

                
                if( x+1 < canvas.cellCountx && (canvas.matrix[x+1][y].getType() == 3 || canvas.matrix[x+1][y].getType() == 1) ){
                    if( !vis[x+1][y] ){
                        vis[x+1][y] = true;
                        q.add( canvas.matrix[x+1][y] );
                    }
                }
                if( y+1 < canvas.cellCounty && (canvas.matrix[x][y+1].getType() == 3 || canvas.matrix[x][y+1].getType() == 1) ){
                    if( !vis[x][y+1] ){
                        vis[x][y+1] = true;
                        q.add( canvas.matrix[x][y+1] );
                    }
                }
                if( x-1 >= 0 && (canvas.matrix[x-1][y].getType() == 3 || canvas.matrix[x-1][y].getType() == 1) ){
                    if( !vis[x-1][y] ){
                        vis[x-1][y] = true;
                        q.add( canvas.matrix[x-1][y] );
                    }
                }
                if( y-1 >= 0 && (canvas.matrix[x][y-1].getType() == 3 || canvas.matrix[x][y-1].getType() == 1) ){
                    if( !vis[x][y-1] ){
                        vis[x][y-1] = true;
                        q.add( canvas.matrix[x][y-1] );
                    }
                }
            }
            showPath();
        }
    }
   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolPanel = new javax.swing.JPanel();
        speedSlider = new javax.swing.JSlider();
        sizeSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        densitySlider = new javax.swing.JSlider();
        wallRB = new javax.swing.JRadioButton();
        eraseRB = new javax.swing.JRadioButton();
        startRB = new javax.swing.JRadioButton();
        endRB = new javax.swing.JRadioButton();
        clearGridbtn = new javax.swing.JButton();
        startBFSbtn = new javax.swing.JButton();
        resetGridbtn = new javax.swing.JButton();
        speedLabel = new javax.swing.JLabel();
        startDFSbtn = new javax.swing.JButton();
        mazeBtn = new javax.swing.JButton();
        mazeBtn1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        toolPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        toolPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        toolPanel.add(speedSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, 160, -1));
        toolPanel.add(sizeSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 60, 160, -1));

        jLabel1.setText("Density");
        toolPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 90, 71, 26));

        jLabel2.setText("size");
        toolPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, 71, 26));

        jLabel3.setText("Speed");
        toolPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, 71, 26));
        toolPanel.add(densitySlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 100, 160, -1));

        wallRB.setText("WALL");
        toolPanel.add(wallRB, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 70, -1, -1));

        eraseRB.setText("ERASE");
        toolPanel.add(eraseRB, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 100, -1, -1));

        startRB.setText("START");
        toolPanel.add(startRB, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, -1));

        endRB.setText("END");
        toolPanel.add(endRB, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 40, -1, -1));

        clearGridbtn.setText("CLEAR GRID");
        clearGridbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearGridbtnActionPerformed(evt);
            }
        });
        toolPanel.add(clearGridbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(463, 10, 110, -1));

        startBFSbtn.setText("START BFS");
        startBFSbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBFSbtnActionPerformed(evt);
            }
        });
        toolPanel.add(startBFSbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 160, 30));

        resetGridbtn.setText("RESET GRID");
        resetGridbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetGridbtnActionPerformed(evt);
            }
        });
        toolPanel.add(resetGridbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 110, 30));
        toolPanel.add(speedLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, 60, 30));

        startDFSbtn.setText("START DFS");
        startDFSbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDFSbtnActionPerformed(evt);
            }
        });
        toolPanel.add(startDFSbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 160, 30));

        mazeBtn.setText("GENERATE MAZE");
        mazeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mazeBtnActionPerformed(evt);
            }
        });
        toolPanel.add(mazeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 160, 30));

        mazeBtn1.setText("GENERATE RANDOM  MAZE");
        mazeBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mazeBtn1ActionPerformed(evt);
            }
        });
        toolPanel.add(mazeBtn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 180, 30));

        jButton1.setText("RECURSIVE MAZE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        toolPanel.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 130, 40));

        jButton2.setText("RUN A*");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        toolPanel.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 120, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(toolPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(toolPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(469, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clearGridbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearGridbtnActionPerformed
        canvas.clearGrid();
    }//GEN-LAST:event_clearGridbtnActionPerformed

    private void startBFSbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBFSbtnActionPerformed
        timerStarted = true;
        canvas.resetGrid();
        algo.solve(0);
        
    }//GEN-LAST:event_startBFSbtnActionPerformed

    private void resetGridbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetGridbtnActionPerformed
        canvas.resetGrid();
    }//GEN-LAST:event_resetGridbtnActionPerformed

    private void startDFSbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDFSbtnActionPerformed
        timerStarted = true;
        canvas.resetGrid();
        algo.solve(1); 
        
    }//GEN-LAST:event_startDFSbtnActionPerformed

    private void mazeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mazeBtnActionPerformed
       canvas.generateMaze();
      
    }//GEN-LAST:event_mazeBtnActionPerformed

    private void mazeBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mazeBtn1ActionPerformed
        canvas.generateRandomMaze();
    }//GEN-LAST:event_mazeBtn1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       canvas.generateRecursiveMaze();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       algo.solve(3);
    }//GEN-LAST:event_jButton2ActionPerformed
 
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearGridbtn;
    private javax.swing.JSlider densitySlider;
    private javax.swing.JRadioButton endRB;
    private javax.swing.JRadioButton eraseRB;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton mazeBtn;
    private javax.swing.JButton mazeBtn1;
    private javax.swing.JButton resetGridbtn;
    private javax.swing.JSlider sizeSlider;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JButton startBFSbtn;
    private javax.swing.JButton startDFSbtn;
    private javax.swing.JRadioButton startRB;
    private javax.swing.JPanel toolPanel;
    private javax.swing.JRadioButton wallRB;
    // End of variables declaration//GEN-END:variables
}

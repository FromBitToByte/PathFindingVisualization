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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class frame extends javax.swing.JFrame {
    Grid canvas;
    private int WidthFrame;
    private int HeightFrame;
    private int inf, delayTime;
    private int cellCountx, cellCounty, cellSizex, cellSizey;
    private Cell startCell, endCell;
    Maze maze;
//    Timer timer;
    Algorithm algo;
    public frame() {
        inf = 1000000007;
        initComponents();
        WidthFrame = 1040;
        HeightFrame = 710;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int leftMargin = ((int)screenSize.getWidth()-WidthFrame)/2;
        setLocation( leftMargin, 0 );
        initGrid();
        initSliders();
        initRadioButtons();
        initMaze();
        initAlgo();
    }
    public void initGrid(){
        canvas = new Grid();
        canvas.setBounds( 0, 0, canvas.WidthGrid, canvas.HeightGrid  );
        this.GridPanel.add( canvas );
    }
    private void initSliders(){
        delayTime = 100;
        sizeSlider.setOpaque(false);
        speedSlider.setOpaque(false);
        densitySlider.setOpaque(false);
        
        sizeSlider.setMinimum( 0 );
        sizeSlider.setMaximum( 4 );
        sizeSlider.setValue(2);
        sizeSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                       if( sizeSlider.getValue() == 0 ){
                            cellCountx = 10;
                            cellCounty = 5;
                       }else{
                            cellCountx = sizeSlider.getValue()*20;
                            cellCounty = sizeSlider.getValue()*10;
                       }
                       canvas.initMatrix();
                       canvas.resetGrid();
                }
                
        });
        speedSlider.setMinimum( 0 );
        speedSlider.setMaximum( 50 );
        speedSlider.setValue(25);
        speedSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    delayTime = 200 - 2*speedSlider.getValue();
                }
        });
        densitySlider.setMinimum( 0 );
        densitySlider.setMaximum( 100 );
        
    }
    private void initRadioButtons(){
        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(eraseRB);
        bg1.add(startRB);
        bg1.add(endRB);
        bg1.add(wallRB);
        startRB.setOpaque(false);
        endRB.setOpaque(false);
        wallRB.setOpaque(false);
        eraseRB.setOpaque(false);
    }
    private void initMaze(){
        mazeComboBox.setSelectedItem("Recursive Division");
        maze = new Maze();
    }
    private void initAlgo(){
        searchTypeComboBox.setSelectedItem("BFS");
        algo = new Algorithm();
    }
    public synchronized  void   addDelay(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
        }
    }
   /*---------------------------------------------------------------------------------------------------------------*/
   //class Cell
   private class Cell{
       private int x, y, type, parx, pary, fx, gx, hx;
       Cell(){//constructor
           type = 3;
           x = y = parx = pary = -1;
           gx = hx = inf;
           fx = gx+hx;
       }
       Cell( int tt, int xx, int yy ){//constructor
           x = xx; y = yy; parx = pary = -1;
           type = tt;
           gx = hx = inf;
           fx = gx+hx;
       }
       Cell( Cell c ){
           x = c.x;
           y = c.y;
           type = c.type;
           parx = c.parx;
           pary = c.pary;
           fx = c.fx;
           gx = c.gx;
           hx = c.hx;
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
       public void reset( int tt){
           parx = pary = -1;
           type = tt;
           gx = hx = inf;
           fx = gx+hx;
       }
       public void setValues(){
           if( type == 0 ){
               gx = 0;
           }
           hx = Math.abs( endCell.x - x )+Math.abs(endCell.y-y);
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
            cellSizex = WidthGrid/(cellCountx);
            cellSizey = HeightGrid/(cellCounty);
            matrix = new Cell[cellCountx][cellCounty];
            startCell = new Cell(0, -1, -1 );
            endCell = new Cell(1, -1, -1 );
            for( int i = 0; i < cellCountx ; i++ ){
                for( int j = 0 ; j < cellCounty ; j++ ){
                    matrix[i][j] = new Cell( 3, i, j );
                }
            }
        }
        public void clearGrid(){
            for( int i = 0; i < cellCountx ; i++ ){
                for( int j = 0 ; j < cellCounty ; j++ ){
                    if( matrix[i][j].getType() > 2 )
                        matrix[i][j].reset(3);
                    else
                        matrix[i][j].reset( matrix[i][j].getType() );
                }
            }
            resetGrid();
        }
        public void resetGrid(){
//            Thread t = new Thread( new Runnable(){
//                @Override
//                public void run() {
//                    repaint();
//                }
//            });
//            t.start();
//            try {
//                t.join();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
//            }
            repaint();
        }
        Grid(){
            this.setOpaque(false);
            addMouseListener(this);
            addMouseMotionListener(this);
            WidthGrid = 960;
            HeightGrid = 480;
            cellCountx = 20;
            cellCounty = 10;
            initMatrix();
        }
        
        /*------- PAINT COMPONENT(inside Class Grid) -------------------------------------------------------*/
 
        @Override
        public void paintComponent( Graphics g ){ // called to change the colour of cells according to their type
            super.paintComponent(g);// calls the default function of parent class
                
            Thread t = new Thread( new Runnable(){
                @Override
                public void run() {
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
                                g.setColor( new Color(0,102,102) );
                                g.fillRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey);
                            }else if( matrix[x][y].getType() == 5 ){
                                g.setColor( new Color(255,0,127) );
                                g.fillRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey);
                            }else if( matrix[x][y].getType() == 6 ){
                                g.setColor( new Color(0,0,153) );
                                g.fillRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey);
                                int oneFourth = cellSizex/4;
                                g.setColor( new Color(102,102,255) ); 
                                g.fillOval(x*cellSizex+oneFourth , y*cellSizey+oneFourth, oneFourth*2, oneFourth*2);
                            }
                            else
                                g.fillRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey);
                           
                            g.setColor(Color.black);
                            g.drawRect(x*cellSizex , y*cellSizey, cellSizex, cellSizey );
                        }
                    }
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
            }
            
                    
        }/*------- PAINT COMPONENT END (inside Class Grid) -------------------------------------------------------*/
        
        
        /*------------------- Mouse Click Events --------------------------------------------------------*/
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        @Override
        public void mousePressed(MouseEvent e) {
            canvas.clearGrid();
            int x = e.getX()/cellSizex;
            int y = e.getY()/cellSizey;
            if( x == startCell.x && y == startCell.y ){
                matrix[startCell.x][startCell.y].setType(3);
                startCell.x = startCell.y = -1;
            }
            if( x == endCell.x && y == endCell.y ){
                matrix[endCell.x][endCell.y].setType(3);
                endCell.x = endCell.y = -1;
            }
            if( startRB.isSelected() ){
                if( startCell.x != -1 && startCell.y != -1 ){
                     matrix[startCell.x][startCell.y].setType(3);
                }
                startCell.x = x;
                startCell.y = y;
                matrix[x][y].setType(0);
            }else if( endRB.isSelected() ){
                if( endCell.x != -1 && endCell.y != -1 ){
                     matrix[endCell.x][endCell.y].setType(3);
                }
                endCell.x = x;
                endCell.y = y;
                matrix[x][y].setType(1);
            }else{
                if( wallRB.isSelected() ){
                    matrix[x][y].setType(2);
                }else if( eraseRB.isSelected() ){
                    matrix[x][y].setType(3);
                }
            }
            resetGrid();
           
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
            if( ( wallRB.isSelected() || eraseRB.isSelected()) && x >= 0 && y >= 0 && x < cellCountx && y < cellCounty ){
            
                if( x == startCell.x && y == startCell.y ){
                    matrix[startCell.x][startCell.y].setType(3);
                    startCell.x = startCell.y = -1;
                }
                if( x == endCell.x && y == endCell.y ){
                    matrix[endCell.x][endCell.y].setType(3);
                    endCell.x = endCell.y = -1;
                } 
                if( wallRB.isSelected() ){
                    matrix[x][y].setType(2);
                }else if( eraseRB.isSelected() ){
                    matrix[x][y].setType(3);
                }
                resetGrid();
            }
            }catch( Exception err ){
                
//                System.ot.println( err.getMessage() );
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {
        }
       /*------------------- Mouse Click Events --------------------------------------------------------*/
         
    }
    /*--- CLASS GRID ENDS HERE ------------------------------------------------------------------------------------*/
    
    /*--- CLASS MAZE STARTS HERE ------------------------------------------------------------------------------------*/
    class Maze{
        Random ranM;
        Queue<Cell> q, q1;
        String val;
        int tt;
        public void generateMaze(){
            ranM = new Random();
            canvas.initMatrix();
            canvas.resetGrid();
            val = (String)mazeComboBox.getSelectedItem();
            q = new LinkedList();
            q1 = new LinkedList();
            tt = 2;
            if( val.equals("Random Maze")){
                randomMaze();
            }else if( val.equals("BFS Random Maze") ){
                BFSRandomMaze();
                tt = 3;
            }else{
            
                 for( int i = 0 ; i < cellCountx ; i++ )
                    q.add( canvas.matrix[i][0] );
                for( int i = 1 ; i < cellCounty ; i++ )
                    q.add( canvas.matrix[cellCountx-1][i]);
                for( int i = cellCountx-2 ; i >= 0 ; i-- )
                    q.add( canvas.matrix[i][cellCounty-1]);
                for( int i = cellCounty-2 ; i > 0 ; i-- )
                    q.add( canvas.matrix[0][i] );
                if( val.equals("KRUSKAL'S"))
                    kruskals();
                else
                    recursiveMaze(1, 1, cellCountx-3, cellCounty-3);
            }
            int dt, s;
            s = sizeSlider.getValue();
            switch( s ){
                case 0 : dt = 15; break;
                case 1 : dt = 10; break;
                case 2 : dt = 5;  break;
                case 3 : dt = 2;  break;
                default: dt = 0;
            };
            
            if( s >= 2 && val.equals("Random Maze") )
                dt = 0;
            Timer timer = new Timer(dt, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( q.isEmpty() ) {
                    ((Timer) e.getSource()).stop();
                } else {
                    q.element().setType(tt);
                    q.remove();
                }
                canvas.resetGrid();
            }   
           });
           timer.start();
           canvas.resetGrid();
        }
        public void recursiveMaze(int x, int y, int h, int w){
           if( h < 2 || w < 2 || x <= 0 || y <= 0 || x >= cellCountx-1 || y >= cellCounty-1 ){
               return;
           }
           int ori = 0;// 1->vertival , 0->horizontal
           if( val == "Recursive Division" ){
               if( h == w ){
                   ori = ranM.nextInt(2);
               }else{
                   ori = (w > h ? 1 : 0);
               }
           }else if( val == "Horizontal skew" ){
               ori = ranM.nextInt(2)|ranM.nextInt(2);
           }else if( val == "Verticall skew" ){
               ori = ranM.nextInt(2)&ranM.nextInt(2);
           }
           int gx, gy, wx, wy;
           if( ori == 0 ){
                wx = x + 2*ranM.nextInt((h+1)/2) + ((x%2 == 0) ? 0 : 1);//must be at even position
                gy = y + 2*ranM.nextInt((w+1)/2) + ((y%2 == 0) ? 1 : 0);// must be at odd position
                
                for( int i = 0 ; i <= w && wx != cellCountx-2  ; i++ ){
                    if( i+y != gy   ){
                        q.add(canvas.matrix[wx][i+y]);
                        q1.add(canvas.matrix[wx][i+y]);
                    }
                }
                recursiveMaze( x, y, wx-x-1, w  );
                recursiveMaze( wx+1, y, (x+h)-wx-1, w  );   
            }else{
                wy = y + 2*ranM.nextInt((w+1)/2) + ((y%2 == 0) ? 0 : 1);//must be at even position
                gx = x + 2*ranM.nextInt((h+1)/2) + ((x%2 == 0) ? 1 : 0);// must be at odd position
                
                for( int i = 0 ; i <= h && wy != cellCounty-2 ; i++ ){
                    if( i+x != gx  ){
                       q.add(canvas.matrix[i+x][wy]);
                       q1.add(canvas.matrix[i+x][wy]);
                    }
                }
                recursiveMaze( x, y, h, wy-y-1  );
                recursiveMaze( x, wy+1, h, (y+w)-wy-1  );
            }
        }
        public void randomMaze(){
            int count = (int)(((double)densitySlider.getValue()*cellCountx*cellCounty)/100);
            boolean vis[][];
            vis = new boolean[cellCountx][cellCounty];
            for( int i = 0 ; i < cellCountx ; i++ ){
                for( int j = 0 ; j < cellCounty ; j++ ){
                    vis[i][j] = false;
                }
            }
            int i = 0, x, y;
            while( i < count ){
                x = ranM.nextInt(cellCountx);
                y = ranM.nextInt(cellCounty);
                if( !vis[x][y] ){
                    vis[x][y] = true;
                    q.add( canvas.matrix[x][y] );
                    i++;
                }
            }
        }
        public void BFSRandomMaze(){
            int[] dx= new int[]{1,-1,0,0,1,1,-1,-1}; 
            int[] dy= new int[]{0,0,1,-1,1,-1,1,-1};
            boolean vis[][] = new boolean[cellCountx][cellCounty];
            for( int i = 0, j ; i < cellCountx ; i++ ){
                for( j = 0 ; j < cellCounty ; j++ ){
                    vis[i][j] = false;
                    canvas.matrix[i][j].setType(2);
                }
            }
            Random ran = new Random();
            int x = ran.nextInt(cellCountx);
            int y = ran.nextInt(cellCounty);
            Queue<Cell> q1 = new LinkedList();
            q1 = new LinkedList();
            q1.add( canvas.matrix[x][y] );
            vis[x][y] = true;
            while( !q1.isEmpty() ){
                x = q1.element().x;
                y = q1.element().y;
                q.add(canvas.matrix[x][y]);
                q1.remove();
                LinkedList<Cell> arr = new LinkedList();
                for( int i = 0 ; i < 4 ; i++ ){
                    int nx = x+dx[i];
                    int ny = y+dy[i];
                    if( nx >= 0 && nx < cellCountx && ny >= 0 && ny < cellCounty && !vis[nx][ny] ){
                        arr.add( canvas.matrix[nx][ny] );
                        vis[nx][ny] = true;
                    }
                }

                if( arr.size() != 0 ){
                     int n  = ran.nextInt(arr.size());
                     int ok, val = 0;
                     if( n == 0 )
                         n = 1;
                     for( int p = 0 ; p < n ; p++ ){
                         ok = ran.nextInt( arr.size() );
                         if( (val&(1<<ok)) == 0 && ok < arr.size()){
                             q1.add( arr.get(ok) );
                             val |= (1<<ok);
                         }
                     }
                }
           }
        }
        class edge{
            int x1, x2, y1, y2;
            edge( Cell obj1, Cell obj2 ){
                x1 = obj1.x;
                y1 = obj1.y;
                x2 = obj2.x;
                y2 = obj2.y;
            }
        }
        public void kruskals(){
            int[][] a = new int[cellCountx][cellCounty];
            for( int i = 0 ; i < cellCountx ; i++ ){
                a[i][0] = 1;
                a[i][cellCounty-1] = 1;
            }
            for( int i = 0 ; i < cellCounty ; i++ ){
                a[0][i] = 1;
                a[cellCountx-1][i] = 1;
            }
            int indx = 2;
            ArrayList<edge> list = new ArrayList<edge>();
            for( int i = 1 ; i < cellCountx-1 ; i++ ){
                for( int j = 1 ; j < cellCounty-1 ; j++ ){
                    a[i][j] = indx;
                    indx++;
                }
            }
            for( int i = 0 ; i < cellCountx ; i++ ){
                for( int j = 0 ; j < cellCounty ; j++ ){
                    if( i+1 < cellCountx && ( i%4 == 0 || i%4 == 2)  ){
                        list.add( new edge( canvas.matrix[i][j], canvas.matrix[i+1][j]) );
                    }
                    if( j+1 < cellCounty && (j%4 == 0 || j%4 == 2 )){
                        list.add( new edge( canvas.matrix[i][j], canvas.matrix[i][j+1]) );
                    }
                }
            }
            Collections.shuffle(list);
            Random ran = new Random();
            edge e;
            int t1, t2;
            while( list.size() > 0 ){
                indx = ran.nextInt( list.size() );
                e = list.get(indx);
                list.remove(indx);
                t1 = a[e.x1][e.y1];
                t2 = a[e.x2][e.y2];
                if( t1 == t2 )
                    continue;
                q.add( canvas.matrix[e.x1][e.y1] );
                q.add( canvas.matrix[e.x2][e.y2] );
                for( int i = 0 ; i < cellCountx ; i++ ){
                    for( int j = 0 ; j < cellCounty ; j++ ){
                        if( a[i][j] == t2 )
                            a[i][j] = t1;
                    }
                }
            }
//            for( int i = 0 ; i < cellCountx ; i++ ){
//                for( int j = 0 ; j < cellCounty ; j++ ){
//                    System.out.print( a[i][j] + " ");
//                }
//                System.out.println();
//            }
        }
    }
    /*--- CLASS MAZE ENDS HERE ------------------------------------------------------------------------------------*/
    
    /*--- CLASS Algorithm ENDS HERE ------------------------------------------------------------------------------------*/
    class Algorithm{
        /*
        BFS
        DFS
        A STAR(A*)
        DIJKSTRA
        SWARM ALGORITHM
        BIDIRECTIONAL SWARM
        */
        String val;
        Queue<Cell> path;
        Queue<Cell> path2;
        Queue<Cell> q;
        int ok1, ok2, a, b, test;
        int[] dx;
        int[] dy;
        Algorithm(){
            dx= new int[]{0,0,1,-1,1,1,-1,-1}; 
            dy= new int[]{1,-1,0,0,1,-1,1,-1};
            path = new LinkedList();
            path2 = new LinkedList();
        }
        public void startSearch(){
            
            canvas.clearGrid();
            canvas.resetGrid();
            path.clear();
            path2.clear();
            q = new LinkedList();
           
            val = (String)searchTypeComboBox.getSelectedItem();
            if( val.equals("BFS") ){
                BFS();
            }else if(val.equals("A STAR(A*)")){
                Astar();
            }else if(val.equals("DFS")){
                DFS();
            }
        }
        /*--- BFS Function STARTS HERE ------------------------------------------------------------------------------------*/
        public void BFS(){
            
            boolean vis[][] = new boolean[cellCountx][cellCounty];
            for( int i = 0 ; i < cellCountx ; i++ ){
                for( int j = 0 ; j < cellCounty ; j++ ){
                    vis[i][j] = false;
                }
            }
            int ii = 0, n, nx, ny;
            Cell curr;
            n = cellCountx*cellCounty;
            q.add( startCell );
            vis[startCell.x][startCell.y] = true;
            q.add( null );
            startCell.parx = -1;
            startCell.pary = -1;
            endCell.parx = -1;
            endCell.pary = -1;
            while( ii < n ){//bfs algo
                curr = q.element();
                q.remove();
                if( curr == null ){
                    if( q.isEmpty() ){
                        break;
                    }else{
                        q.add( null );
                        path.add( null );
                    }
                    continue;
                }
                if( curr.x == endCell.x && curr.y == endCell.y ){
                    break;
                }
                path.add( curr );
                
                for( int k = 0 ; k < 4 ; k++ ){
                   nx = curr.x+dx[k];
                   ny = curr.y+dy[k];
                   if( nx >= 0 && ny >= 0 && nx < cellCountx && ny < cellCounty){
                       if( (canvas.matrix[nx][ny].getType()!= 2) && !vis[nx][ny] ){
                           q.add(canvas.matrix[nx][ny] );
                           canvas.matrix[nx][ny].parx = curr.x;
                           canvas.matrix[nx][ny].pary = curr.y;
                           vis[nx][ny] = true;
                       }
                   }
                
                }
                ii++;
            }
            
            if( !path.isEmpty() ){
                path.remove();
                if( path.element() == null )
                    path.remove();
                
            }
            int s = sizeSlider.getValue();
           
           SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){//show Explored Cells
                @Override
                protected Void doInBackground() throws Exception {
                    while( true ){
                       if( path.isEmpty() ){
                           
                           while( !path2.isEmpty() ){
                                path2.element().setType(5);
                                path2.remove();
                                
                           }
                           publish();
                           
                           break;
                       }else{ 
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
                             publish();
                       }
                       Thread.sleep(100);
                       
                    }
                    return null;
                }
                @Override
                protected void process(List<Void> chunks){
                    canvas.resetGrid();
                }
                @Override
                protected void done(){
                }
           
           };
           worker.execute();
           ok1 = canvas.matrix[endCell.x][endCell.y].parx;
           ok2 = canvas.matrix[endCell.x][endCell.y].pary;
           test = 1;
           SwingWorker<Void, Void> worker1 = new SwingWorker<Void, Void>(){//backtrack
                @Override
                protected Void doInBackground() throws Exception {
                    while(  true ){
                        if( !worker.isDone() ) continue;
                        if( (ok1 == startCell.x && ok2 == startCell.y) || (ok1 == -1 && ok2 == -1 ))
                            break;
                        test++;
                        canvas.matrix[ok1][ok2].setType(6);
                        a = canvas.matrix[ok1][ok2].parx;
                        b = canvas.matrix[ok1][ok2].pary;
                        ok1 = a;
                        ok2 = b;
                        Thread.sleep(100);
                        publish();
                    }
                    return null;
                }
                @Override
                protected void process(List<Void> chunks){
                    canvas.resetGrid();
                }
                @Override
                protected void done(){
                    
                }
           
           };
           worker1.execute();
           
           SwingWorker<Void, Void> worker2 = new SwingWorker<Void, Void>(){
                @Override
                protected Void doInBackground() throws Exception {
                    while(  !worker1.isDone() ){
                    }
                    System.out.println("BFS" + test );
                    return null;
                }
                
           };
           worker2.execute();
           
        }
        /*--- BFS Function ENDS HERE ------------------------------------------------------------------------------------*/
        
        /*--- Astar Function STARTS HERE ------------------------------------------------------------------------------------*/
        public class CellComparator implements Comparator<Cell>{
            @Override
            public int compare(Cell o1, Cell o2) {
                 if( o1.fx == o2.fx ){
                    return ( o1.gx >=  o2.gx ? -1 : 1 );
                }
                if( o1.fx < o2.fx )// callerObject is smaller
                    return -1;
                return 1;
            }

        }
        public void Astar(){
            boolean foundEnd = false;
            boolean vis[][] = new boolean[cellCountx][cellCounty];
            for( int i = 0 ; i < cellCountx ; i++ ){
                for( int j = 0 ; j < cellCounty ; j++ ){
                    vis[i][j] = false;
                    canvas.matrix[i][j].setValues();
                }
            }
            PriorityQueue<Cell> pq = new PriorityQueue<>( new CellComparator() );
            pq.add( canvas.matrix[startCell.x][startCell.y] );
            vis[startCell.x][startCell.y] = true;
            Cell curr;
            int nx, ny;
            while( !pq.isEmpty() ){ 
                curr = pq.peek();
                int x = curr.x;
                int y = curr.y;
                pq.remove();
                vis[x][y] = true;
                if( curr.x == endCell.x && curr.y == endCell.y ){
                    foundEnd = true;
                    break;
                }
                path.add( canvas.matrix[x][y] );
                for( int k = 0 ; k < 4 ; k++ ){
                    nx = x+dx[k];
                    ny = y+dy[k];
                    if( nx >= 0 && ny >= 0 && nx < cellCountx && ny < cellCounty && !vis[nx][ny] && canvas.matrix[nx][ny].getType() != 2){
                        if( (curr.gx+1+canvas.matrix[nx][ny].hx) < canvas.matrix[nx][ny].fx ){
                            if( pq.contains( canvas.matrix[nx][ny] ) ){
                                pq.remove( canvas.matrix[nx][ny] );
                            }
                            canvas.matrix[nx][ny].gx = curr.gx+1;
                            canvas.matrix[nx][ny].fx = canvas.matrix[nx][ny].gx + canvas.matrix[nx][ny].hx;
                            canvas.matrix[nx][ny].parx = x;
                            canvas.matrix[nx][ny].pary = y;
                            pq.add(canvas.matrix[nx][ny]);
                        }
                    }
                }
            }//while
            if( path.size() > 0 ){
                path.remove();
            }
           SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){//show Explored Cells
                @Override
                protected Void doInBackground() throws Exception {
                    while( !path.isEmpty() ){
                       path.element().setType(5);
                       path.remove();
                       publish();
                       Thread.sleep(10);
                    }
                    return null;
                }
                @Override
                protected void process(List<Void> chunks){
                    canvas.resetGrid();
                }
                @Override
                protected void done(){
                }
           
           };
           worker.execute();
            // now lets backtrack
            if( foundEnd ){
                test = 1;
                ok1 = canvas.matrix[endCell.x][endCell.y].parx;
                ok2 = canvas.matrix[endCell.x][endCell.y].pary;
                SwingWorker<Void, Void> worker1 = new SwingWorker<Void, Void>(){//BACKTRACK
                    @Override
                    protected Void doInBackground() throws Exception {
                        while(  true ){
                            if( !worker.isDone() ) continue;
                            if( (ok1 == startCell.x && ok2 == startCell.y) || (ok1 < 0 || ok2 < 0 || ok1 >= cellCountx || ok2 >= cellCounty )){
                                break;
                            }
                            test+=1;
                            canvas.matrix[ok1][ok2].setType(6);
                            a = canvas.matrix[ok1][ok2].parx;
                            b = canvas.matrix[ok1][ok2].pary;
                            ok1 = a;
                            ok2 = b;
                            Thread.sleep(100);
                            publish();
                        }
                        return null;
                    }
                    @Override
                    protected void process(List<Void> chunks){
                        canvas.resetGrid();
                    }
                    @Override
                    protected void done(){
                    }

               };
               worker1.execute();
               SwingWorker<Void, Void> worker2 = new SwingWorker<Void, Void>(){
                     @Override
                     protected Void doInBackground() throws Exception {
                         while(  !worker1.isDone() ){
                         }
                        System.out.println("ASTAR" + test );
                        return null;
                     }

                };
                worker2.execute();

            }
            
        }
        /*--- Astar Function ENDS HERE ------------------------------------------------------------------------------------*/
        /*--- DFS Function STARTS HERE ------------------------------------------------------------------------------------*/
        public void DFS(){
            boolean foundEnd = false;
            boolean vis[][] = new boolean[cellCountx][cellCounty];
            for( int i = 0 ; i < cellCountx ; i++ ){
                for( int j = 0 ; j < cellCounty ; j++ ){
                    vis[i][j] = false;
                }
            }
            Stack<Cell> stk = new Stack();
            stk.push( canvas.matrix[startCell.x][startCell.y] );
            int nx, ny;
            while( !stk.empty() ){
                int x = stk.peek().x;
                int y = stk.peek().y;
                stk.pop();
                if( vis[x][y] ){
                    continue;
                }
                if( x == endCell.x && y == endCell.y ){
                    foundEnd = true;
                    break;
                }
                path.add( canvas.matrix[x][y] );
                vis[x][y] = true;
                
                for( int k = 0 ; k < 4 ; k++ ){
                    nx = x+dx[k];
                    ny = y+dy[k];
                    if( nx >= 0 && ny >= 0 && nx < cellCountx && ny < cellCounty && !vis[nx][ny] && canvas.matrix[nx][ny].getType() != 2){
                        stk.push( canvas.matrix[nx][ny] );
                        canvas.matrix[nx][ny].parx = x;
                        canvas.matrix[nx][ny].pary = y;
                    }
                }
            }
            if( path.size() > 0 )
                path.remove();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){//show Explored Cells
                @Override
                protected Void doInBackground() throws Exception {
                    while( !path.isEmpty() ){
                       path.element().setType(5);
                       path.remove();
                       publish();
                       Thread.sleep(10);
                    }
                    return null;
                }
                @Override
                protected void process(List<Void> chunks){
                    canvas.resetGrid();
                }
                @Override
                protected void done(){
                }
           
           };
           worker.execute();
            // now lets backtrack
            if( foundEnd ){
                ok1 = canvas.matrix[endCell.x][endCell.y].parx;
                ok2 = canvas.matrix[endCell.x][endCell.y].pary;
                SwingWorker<Void, Void> worker1 = new SwingWorker<Void, Void>(){//BACKTRACK
                    @Override
                    protected Void doInBackground() throws Exception {
                        while(  true ){
                            if( !worker.isDone() ) continue;
                            if( (ok1 == startCell.x && ok2 == startCell.y) || (ok1 < 0 || ok2 < 0 || ok1 >= cellCountx || ok2 >= cellCounty )){
                                break;
                            }
                            canvas.matrix[ok1][ok2].setType(6);
                            a = canvas.matrix[ok1][ok2].parx;
                            b = canvas.matrix[ok1][ok2].pary;
                            ok1 = a;
                            ok2 = b;
                            Thread.sleep(10);
                            publish();
                        }
                        return null;
                    }
                    @Override
                    protected void process(List<Void> chunks){
                        canvas.resetGrid();
                    }
                    @Override
                    protected void done(){
                    }

               };
               worker1.execute();
            }
        }
        /*--- DFS Function ENDS HERE ------------------------------------------------------------------------------------*/
        
        
        
    }
    /*--- CLASS Algorithm ENDS HERE ------------------------------------------------------------------------------------*/
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GridPanel = new javax.swing.JPanel();
        ToolBoxPanel = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        searchTypeComboBox = new javax.swing.JComboBox<>();
        mazeComboBox = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        sizeLabel = new javax.swing.JLabel();
        densityLabel = new javax.swing.JLabel();
        speedLabel = new javax.swing.JLabel();
        speedSlider = new javax.swing.JSlider();
        sizeSlider = new javax.swing.JSlider();
        densitySlider = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();
        startRB = new javax.swing.JRadioButton();
        endRB = new javax.swing.JRadioButton();
        wallRB = new javax.swing.JRadioButton();
        eraseRB = new javax.swing.JRadioButton();
        ToolBoxLabel = new javax.swing.JLabel();
        BackPanel = new javax.swing.JPanel();
        BackGroundLabel2 = new javax.swing.JLabel();
        jInternalFrame1 = new javax.swing.JInternalFrame();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1200, 1000));
        setMinimumSize(new java.awt.Dimension(1042, 710));
        setResizable(false);
        setSize(new java.awt.Dimension(1042, 730));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout GridPanelLayout = new javax.swing.GroupLayout(GridPanel);
        GridPanel.setLayout(GridPanelLayout);
        GridPanelLayout.setHorizontalGroup(
            GridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 960, Short.MAX_VALUE)
        );
        GridPanelLayout.setVerticalGroup(
            GridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        getContentPane().add(GridPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(46, 190, 960, 480));

        ToolBoxPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        ToolBoxPanel.setMaximumSize(new java.awt.Dimension(1027, 98));
        ToolBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pathfindingproject/startButton.jpg"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        ToolBoxPanel.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 110, 30));

        jButton3.setText("GENERATE MAZE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        ToolBoxPanel.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 40, 140, 30));

        searchTypeComboBox.setBackground(new java.awt.Color(204, 204, 204));
        searchTypeComboBox.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        searchTypeComboBox.setForeground(new java.awt.Color(102, 102, 0));
        searchTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BFS", "DFS", "A STAR(A*)", "DIJKSTRA", "SWARM ALGORITHM", "BIDIRECTIONAL SWARM" }));
        searchTypeComboBox.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        ToolBoxPanel.add(searchTypeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 160, 30));

        mazeComboBox.setBackground(new java.awt.Color(204, 204, 204));
        mazeComboBox.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        mazeComboBox.setForeground(new java.awt.Color(102, 102, 0));
        mazeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recursive Division", "Horizontal skew", "Verticall skew", "BFS Random Maze", "Random Maze", "KRUSKAL'S" }));
        mazeComboBox.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        ToolBoxPanel.add(mazeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 180, 30));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(204, 204, 255))); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sizeLabel.setBackground(new java.awt.Color(204, 204, 255));
        sizeLabel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sizeLabel.setForeground(new java.awt.Color(204, 204, 255));
        sizeLabel.setText("SIZE");
        jPanel2.add(sizeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 50, 30));

        densityLabel.setBackground(new java.awt.Color(204, 204, 255));
        densityLabel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        densityLabel.setForeground(new java.awt.Color(204, 204, 255));
        densityLabel.setText("DENSITY");
        jPanel2.add(densityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 60, 30));

        speedLabel.setBackground(new java.awt.Color(204, 204, 255));
        speedLabel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        speedLabel.setForeground(new java.awt.Color(204, 204, 255));
        speedLabel.setText("SPEED");
        jPanel2.add(speedLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 50, 30));

        speedSlider.setSnapToTicks(true);
        speedSlider.setOpaque(false);
        jPanel2.add(speedSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 160, -1));

        sizeSlider.setSnapToTicks(true);
        sizeSlider.setOpaque(false);
        jPanel2.add(sizeSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 160, -1));

        densitySlider.setSnapToTicks(true);
        densitySlider.setOpaque(false);
        jPanel2.add(densitySlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 160, -1));

        ToolBoxPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, 290, 140));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cell Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(204, 204, 255))); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        startRB.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        startRB.setForeground(new java.awt.Color(204, 204, 255));
        startRB.setText("START");
        startRB.setOpaque(false);
        startRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startRBActionPerformed(evt);
            }
        });
        jPanel1.add(startRB, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 30));

        endRB.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        endRB.setForeground(new java.awt.Color(204, 204, 255));
        endRB.setText("END");
        endRB.setOpaque(false);
        jPanel1.add(endRB, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 60, 30));

        wallRB.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        wallRB.setForeground(new java.awt.Color(204, 204, 255));
        wallRB.setText("WALL");
        wallRB.setOpaque(false);
        jPanel1.add(wallRB, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 70, 30));

        eraseRB.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        eraseRB.setForeground(new java.awt.Color(204, 204, 255));
        eraseRB.setText("ERASE");
        eraseRB.setOpaque(false);
        jPanel1.add(eraseRB, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, -1, 30));

        ToolBoxPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 180, 140));

        ToolBoxLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pathfindingproject/ToolBoxBack.jpg"))); // NOI18N
        ToolBoxPanel.add(ToolBoxLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 1024, 155));

        getContentPane().add(ToolBoxPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1030, 160));

        BackPanel.setBackground(new java.awt.Color(255, 255, 255));
        BackPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        BackPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BackGroundLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pathfindingproject/wallpaper2.jpg"))); // NOI18N
        BackGroundLabel2.setMaximumSize(new java.awt.Dimension(1036, 706));
        BackGroundLabel2.setMinimumSize(new java.awt.Dimension(1036, 706));
        BackPanel.add(BackGroundLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 4, -1, 690));

        jInternalFrame1.setVisible(true);
        BackPanel.add(jInternalFrame1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 100, -1, -1));

        getContentPane().add(BackPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1050, 700));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startRBActionPerformed
    }//GEN-LAST:event_startRBActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        maze.generateMaze();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        algo.startSearch();

    }//GEN-LAST:event_jButton2ActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BackGroundLabel2;
    private javax.swing.JPanel BackPanel;
    private javax.swing.JPanel GridPanel;
    private javax.swing.JLabel ToolBoxLabel;
    private javax.swing.JPanel ToolBoxPanel;
    private javax.swing.JLabel densityLabel;
    private javax.swing.JSlider densitySlider;
    private javax.swing.JRadioButton endRB;
    private javax.swing.JRadioButton eraseRB;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox<String> mazeComboBox;
    private javax.swing.JComboBox<String> searchTypeComboBox;
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JSlider sizeSlider;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JRadioButton startRB;
    private javax.swing.JRadioButton wallRB;
    // End of variables declaration//GEN-END:variables
}

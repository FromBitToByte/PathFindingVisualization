package pathfindingproject;
import javax.swing.JOptionPane;

public class PathFindingProject {
    public static void main(String[] args) {
        back background = new back();
        background.setVisible(true);
        prac obr=new prac();
        obr.setVisible(true);
        try{
            for(int i = 0 ; i <= 100 ; i++ ){
                Thread.sleep(50);
                obr.jProgressBar1.setValue(i);
                obr.jProgressBar2.setValue(i);
                obr.jLabel8.setText(""+i);
                if(i==95){
                    obr.jLabel6.setText("LOADED");
                    obr.jLabel7.setText("WELCOME");
                }
                if (i==100){
                    obr.setVisible(false);
//                    background.setVisible(false);
                    new frame().setVisible(true);
                }
            }
        }
        catch( Exception e ){
              JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}

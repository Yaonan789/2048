import javax.swing.*;

public class GamePanel extends JPanel{
    private JFrame frame=null;
    private GamePanel panel=null;
    public GamePanel(JFrame frame){
        this.setLayout(null);
        this.setOpaque(false);
        this.frame=frame;
        this.panel=this;
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GamePanel extends JPanel implements ActionListener {
    private JFrame frame=null;
    private GamePanel panel=null;
    public GamePanel(JFrame frame){
        this.setLayout(null);
        this.setOpaque(false);
        this.frame=frame;
        this.panel=this;

        createMenu();
    }
    private Font createFont(){
        return new Font("思源宋体",Font.BOLD,28);
    }
    private void createMenu() {
        Font tFont=createFont();
        //创建JmenuBar
        JMenuBar jmb=new JMenuBar();

        JMenu jMenu1=new JMenu("游戏");
        jMenu1.setFont(tFont);

        //创建子项
        JMenuItem jmi1=new JMenuItem("新游戏");
        jmi1.setFont(tFont);
        JMenuItem jmi2=new JMenuItem("退出");
        jmi2.setFont(tFont);

        jMenu1.add(jmi1);
        jMenu1.add(jmi2);

        JMenu jMenu2=new JMenu("帮助");
        jMenu2.setFont(tFont);

        JMenuItem jmi3=new JMenuItem("操作帮助");
        jmi3.setFont(tFont);
        JMenuItem jmi4=new JMenuItem("胜利条件");
        jmi4.setFont(tFont);

        jMenu2.add(jmi3);
        jMenu2.add(jmi4);

        jmb.add(jMenu1);
        jmb.add(jMenu2);

        frame.setJMenuBar(jmb);

        //添加事件监听
        jmi1.addActionListener(this);
        jmi2.addActionListener(this);
        jmi3.addActionListener(this);
        jmi4.addActionListener(this);

        //设置指令
        jmi1.setActionCommand("restrat");
        jmi2.setActionCommand("exit");
        jmi3.setActionCommand("help");
        jmi4.setActionCommand("win");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command=e.getActionCommand();
        if("restart".equals(command)){
            System.out.println("新游戏");
            restart();
        }else if("exit".equals(command)){
            System.out.println("退出");
            Object[] options={"确定","取消"};

        }else if("help".equals(command)){
            System.out.println("帮助");
        }else if("win".equals(command)){
            System.out.println("胜利条件");
        }
    }

    private void restart() {

    }
}

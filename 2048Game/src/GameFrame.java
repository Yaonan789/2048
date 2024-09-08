import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame(){
        setTitle("2048"); // title
        setSize(670,728); // size

        // Create a JPanel to set as the content pane
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(66, 136, 83)); // Set the background color
        setContentPane(contentPane); // Set the JPanel as the content pane

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit
        setLocationRelativeTo(null); // location
        setResizable(false); // 不可变
    }
}

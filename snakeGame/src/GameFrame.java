import javax.swing.JFrame;

public class GameFrame extends JFrame{

    GameFrame() {
        GamePanel panel = new GamePanel();
        this.add(panel);
        /* this.add(panel(new GamePanel())); */
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false); //true
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }
    
}

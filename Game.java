import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("sio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new GamePanel());
        frame.setSize(1000, 1000);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
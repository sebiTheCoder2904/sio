import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements KeyListener, MouseMotionListener {
    private int x = 400, y = 300;
    private int speed = 4;
    private boolean up, down, left, right;
    private Point mouse = new Point(0, 0);
    private BufferedImage playerImage;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this);
        try {
            playerImage = ImageIO.read(new java.io.File("player.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Timer timer = new Timer(16, e -> 
                {
                    update();
                    repaint();
                });
        timer.start();
    }

    private void update() {
        if (up) y -= speed;
        if (down) y += speed;
        if (left) x -= speed;
        if (right) x += speed;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // Spieler initialisieren und deklarieren

        
        double angle = Math.atan2(mouse.y - y, mouse.x - x);  //winkel zur maus berechnen
        
        AffineTransform old = g2.getTransform();
        g2.translate(x, y); 
        g2.rotate(angle);
        g2.translate(-playerImage.getWidth() / 2, -playerImage.getHeight() / 2);
        g2.drawImage(playerImage, 0, 0, null);
        g2.setTransform(old);
    }

    // Controls
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) up = true;
        if (code == KeyEvent.VK_S) down = true;
        if (code == KeyEvent.VK_A) left = true;
        if (code == KeyEvent.VK_D) right = true;
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) up = false;
        if (code == KeyEvent.VK_S) down = false;
        if (code == KeyEvent.VK_A) left = false;
        if (code == KeyEvent.VK_D) right = false;
    }

    
    public void keyTyped(KeyEvent e) {} // exit method

    public void mouseMoved(MouseEvent e) 
    { 
        mouse = e.getPoint(); 
    }

    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }
    
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel implements KeyListener, MouseMotionListener {
    private int x = 400, y = 300;
    private int speed = 4;
    private boolean up, down, left, right;
    private Point mouse = new Point(0, 0);

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this);

        Timer timer = new Timer(16, e -> {
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
        Graphics2D player = (Graphics2D) g;

        // Calculate angle to mouse
        double angle = Math.atan2(mouse.y - y, mouse.x - x);

        // Draw rotated triangle as placeholder
        AffineTransform old = player.getTransform();
        player.translate(x, y);
        player.rotate(angle);
        player.setColor(Color.BLUE);
        int[] px = { -10, -10, 20 };
        int[] py = { -10, 10, 0 };
        player.fillPolygon(px, py, 3);
        player.setTransform(old);
    }

    // Controls
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void mouseMoved(MouseEvent e) { mouse = e.getPoint(); }
    public void mouseDragged(MouseEvent e) { mouseMoved(e); }
}


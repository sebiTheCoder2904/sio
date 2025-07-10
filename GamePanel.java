import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
    private int x = 400, y = 300;
    private int xG = 500, yG = 400;
    private int speed = 8;
    private boolean up, down, left, right;
    private Point mouse = new Point(0, 0);
    private BufferedImage playerImage;
    private BufferedImage gegnerImage;
    private BufferedImage groundImage;

    private ArrayList<Bullet> bullets = new ArrayList<>();
    private boolean shooting = false;
    private int shootCooldown = 0;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        try {
            playerImage = ImageIO.read(new java.io.File("Spieler-removebg-preview.png"));
            gegnerImage = ImageIO.read(new java.io.File("player.png"));
            groundImage = ImageIO.read(new java.io.File("ground.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        int dx = x - xG;
        int dy = y - yG;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 1) {
            double speedG = 2.0;
            xG += (int)(speedG * dx / distance);
            yG += (int)(speedG * dy / distance);
        }

        if (shooting && shootCooldown <= 0) {
            double angle = Math.atan2(mouse.y - y, mouse.x - x);
            bullets.add(new Bullet(x, y, angle));
            shootCooldown = 10;
        }
        if (shootCooldown > 0) shootCooldown--;

        // Update and check collision
        ArrayList<Bullet> toRemove = new ArrayList<>();
        for (Bullet b : bullets) {
            b.update();

            double dist = Math.sqrt(Math.pow(b.x - xG, 2) + Math.pow(b.y - yG, 2));
            if (dist < 30) { // hit radius
                // respawn gegner
                xG = (int)(Math.random() * 1000);
                yG = (int)(Math.random() * 1000);
                toRemove.add(b);
            }
        }
        bullets.removeAll(toRemove);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D player = (Graphics2D) g;
        Graphics2D gegner = (Graphics2D) g;
        Graphics2D ground = (Graphics2D) g;

        ground.translate(500, 500);
        ground.translate(-groundImage.getWidth() / 2, -groundImage.getHeight() / 2);
        ground.drawImage(groundImage, 0, 0, null);

        double angle = Math.atan2(mouse.y - y, mouse.x - x);
        double angleG = Math.atan2(yG - y, xG - x);

        AffineTransform old = player.getTransform();
        player.translate(x, y);
        player.rotate(angle);
        player.translate(-playerImage.getWidth() / 8, -playerImage.getHeight() / 8);
        player.drawImage(playerImage, 0, 0, 573 / 4, 435 / 4, null);
        player.setTransform(old);

        AffineTransform oldGegner = gegner.getTransform();
        gegner.translate(xG, yG);
        gegner.rotate(angleG + 9.2);
        gegner.translate(-gegnerImage.getWidth() / 4, -gegnerImage.getHeight() / 4);
        gegner.drawImage(gegnerImage, 0, 0, 250 / 2, 165 / 2, null);
        gegner.setTransform(oldGegner);

        for (Bullet b : bullets) {
            b.draw((Graphics2D) g);
        }
    }

    private class Bullet {
        double x, y;
        double dx, dy;

        public Bullet(double x, double y, double angle) {
            this.x = x;
            this.y = y;
            double speed = 10;
            dx = Math.cos(angle) * speed;
            dy = Math.sin(angle) * speed;
        }

        public void update() {
            x += dx;
            y += dy;
        }

        public void draw(Graphics2D g) {
            g.setColor(Color.YELLOW);
            g.fillOval((int) x - 4, (int) y - 4, 8, 8);
        }
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

    public void keyTyped(KeyEvent e) {}

    public void mouseMoved(MouseEvent e) {
        mouse = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) shooting = true;
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) shooting = false;
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private static final int WIDTH = 750;
    private static final int HEIGHT = 750;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 150;

    private final int[] x = new int[GAME_UNITS];
    private final int[] y = new int[GAME_UNITS];
    private int bodyParts = 4;
    private int applesEaten = 0;
    private int appleX;
    private int appleY;
    private char direction = 'D';
    private boolean running = false;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new GameKeyListener());
        startGame();
    }

    public void startGame() {
        spawnApple();
        running = true;
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    public void spawnApple() {
        appleX = new Random().nextInt((WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = new Random().nextInt((HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (direction == 'U') {
            y[0] -= UNIT_SIZE;
        }
        if (direction == 'D') {
            y[0] += UNIT_SIZE;
        }
        if (direction == 'L') {
            x[0] -= UNIT_SIZE;
        }
        if (direction == 'R') {
            x[0] += UNIT_SIZE;
        }
        if ((direction == 'U') && (direction == 'R' || direction == 'L')) {
            y[0] -= UNIT_SIZE;
        }
        if ((direction == 'D') && (direction == 'R' || direction == 'L')) {
            y[0] += UNIT_SIZE;
        }
        if ((direction == 'U') && (direction == 'L' || direction == 'R')) {
            y[0] -= UNIT_SIZE;
        }
        if ((direction == 'D') && (direction == 'L' || direction == 'R')) {
            y[0] += UNIT_SIZE;
        }
    }

    public void checkAppleCollision() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            spawnApple();
        }
    }

    public void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        if (x[0] >= WIDTH || x[0] < 0 || y[0] >= HEIGHT || y[0] < 0) {
            running = false;
        }

        if (!running) {
            // Game over logic
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkAppleCollision();
            checkCollision();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw the apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.green.darker());
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Display the score
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
            g.drawString("Score: " + applesEaten, 30, 25);
        } else {
            // Game over screen
            g.setColor(Color.red);
            g.setFont(new Font("SansSerif", Font.BOLD, 70));
            g.drawString("Game Over", 185, HEIGHT / 2 - 30);

            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 40));
            g.drawString("Score: " + applesEaten, 300, HEIGHT / 2 + 10);
        }
    }

    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction != 'D')
                        direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U')
                        direction = 'D';
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R')
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L')
                        direction = 'R';
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new SnakeGame());
            frame.pack();
            frame.setVisible(true);
        });
    }
}

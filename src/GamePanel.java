import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (SCREEN_HEIGHT*SCREEN_WIDTH) / UNIT_SIZE;
    private final int x[] = new int[GAME_UNITS]; // X-coordinates of body parts
    private final int y[] = new int[GAME_UNITS];// Y-coordinates of body parts

    private int bodyParts = 6;
    private int applesEaten = 0;
    private int appleX; //x-coordinate of apple
    private int appleY; //y-coordinate of apple
    char direction = 'R'; //R = Right, L = Left, D = Down, U = Up
    private static int DELAY = 100; //For game timer (higher number makes game slower, lower number makes game faster)

    private static boolean running = false;
    private static Timer timer;
    private static Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new GameKeyAdapter());
        startGame();
    }

    private void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    private void draw(Graphics g) {

        if (running) {
            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }



            //Draw apple at random coordinates
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //Draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.drawRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //Draw score string AFTER snake is drawn to prevent the snake from covering the score when it travels to that area
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score" + applesEaten))/2, 25);
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        //Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over!"))/2, SCREEN_HEIGHT/2);

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics scoreFontMetrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - scoreFontMetrics.stringWidth("Score: " + applesEaten))/2, SCREEN_HEIGHT/2 + 100);

        JButton retry = new JButton("RETRY");
        retry.setBackground(Color.BLACK);
        retry.setForeground(Color.GREEN);
        retry.setFocusable(false);
        retry.setBorder(null);

        retry.setFont(new Font("Ink Free", Font.BOLD, 40));
        retry.setBounds(200, 450, 200, 100);

        retry.addMouseListener(new MouseListener() {
               @Override
               public void mouseClicked(MouseEvent e) {

               }

               @Override
               public void mousePressed(MouseEvent e) {
                   retry.setBackground(Color.BLACK);
                   retry.setForeground(Color.WHITE);
               }

               @Override
               public void mouseReleased(MouseEvent e) {
                   retry.setBackground(Color.BLACK);
                   retry.setForeground(Color.GREEN);
               }

               @Override
               public void mouseEntered(MouseEvent e) {

               }

               @Override
               public void mouseExited(MouseEvent e) {

               }
           }
        );
        add(retry);
    }

    private void newApple() {
        appleX = (random.nextInt(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = (random.nextInt(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        //Sets the coordinates for each body part (cell) to the one in front of it, to simulate the snake's movement
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            applesEaten += 1;
            bodyParts++;
            newApple();
        }
    }
    private void checkCollisions() {
        //Checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ( (x[0] == x[i]) && (y[0] == y[i]) )
                running = false;
        }

        //Check if head touches left border
        if (x[0] < 0) running = false;
        //Check if head touches right border
        if (x[0] > SCREEN_WIDTH) running = false;
        //Check if head touches top border
        if (y[0] < 0) running = false;
        //Check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) running = false;

        if (!running)
            timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R')
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L')
                        direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D')
                        direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U')
                        direction = 'D';
                    break;
            }
        }
    }
}

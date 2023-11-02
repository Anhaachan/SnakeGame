
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SnakeGamePanel extends JPanel implements KeyListener, Runnable {
    private SnakeGame snakeGame;
    private int score;
    
    
    public SnakeGamePanel() {
        snakeGame = new SnakeGame();
        setPreferredSize(new Dimension(SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE,
        SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

  

        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        drawScore(g);
    }
    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20); // Display the score at (10, 20) coordinates
    }

    private void draw(Graphics g) {
        if (snakeGame.isGameOver()) {
            gameOver(g);
        } else {
            drawSnake(g);
            drawFood(g);
        }
    }

    private void drawSnake(Graphics g) {
        ArrayList<Point> snake = snakeGame.getSnake();
    for (int i = 0; i < snake.size(); i++) {
        Point p = snake.get(i);
        if (i == 0) {
            g.setColor(Color.YELLOW); 
        } else {
            g.setColor(Color.GREEN);
        }
        g.fillRect(p.x * SnakeGame.CELL_SIZE, p.y * SnakeGame.CELL_SIZE,
                SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE);
    }
    }

    private void drawFood(Graphics g) {
        Point food = snakeGame.getFood();
        g.setColor(Color.RED);
        g.fillRect(food.x * SnakeGame.CELL_SIZE, food.y * SnakeGame.CELL_SIZE,
                SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE);
    }

    private void gameOver(Graphics g) {
    	String message = "Game Over! Press R to"  + "\n"+ " restart.";
        String quitmessage = "Or Press Q to quit.";
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(message, SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE / 6,
                SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE / 4);
        g.drawString(quitmessage, SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE / 3,
        		SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE / 2);
    }
  
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                snakeGame.setDirection(SnakeGame.Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                snakeGame.setDirection(SnakeGame.Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                snakeGame.setDirection(SnakeGame.Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                snakeGame.setDirection(SnakeGame.Direction.RIGHT);
                break;
            case KeyEvent.VK_R:
                snakeGame = new SnakeGame();
                break;
            case KeyEvent.VK_Q: 
            	int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit the game?", "Quit Game", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0); 
                }
                break;
            	
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void run() {
        while (true) {
            snakeGame.move();
            repaint();
            try {
                Thread.sleep(SnakeGame.DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGamePanel gamePanel = new SnakeGamePanel();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SnakeGamePanel extends JPanel implements KeyListener, Runnable {
    private SnakeGame snakeGame;
    private int score;
    private boolean isGameOver;
    private PlayerDAO playerDAO = new PlayerDAO();
    private ImageIcon backgroundImage;

    public SnakeGamePanel() {
        initializeGame();
        setPreferredSize(new Dimension(SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE,
                SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        Thread gameThread = new Thread(this);
        gameThread.start();
        String imagePath = "./assets/background.jpg";
        System.out.println("Image Path: " + ClassLoader.getSystemResource(imagePath));
        backgroundImage = new ImageIcon(ClassLoader.getSystemResource(imagePath));
           }

    private void initializeGame() {
        snakeGame = new SnakeGame();
        isGameOver = false;
        score = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        
        draw(g);
        drawScore(g);
        drawFoodImage(g);
    }

    private void drawFoodImage(Graphics g) {
        Point food = snakeGame.getFood();
        Image foodImage = snakeGame.getFoodImageIcon().getImage();
        g.drawImage(foodImage, food.x * SnakeGame.CELL_SIZE, food.y * SnakeGame.CELL_SIZE,
                SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE, this);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);
    }

    private void draw(Graphics g) {
        if (isGameOver) {
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
                BufferedImage snakeHeadImage = snakeGame.getSnakeHeadImage(snakeGame.getDirection());
                if (snakeHeadImage != null) {
                    g.drawImage(snakeHeadImage, p.x * SnakeGame.CELL_SIZE, p.y * SnakeGame.CELL_SIZE,
                            SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE, this);
                }
            } else {
                if (i % 2 == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.YELLOW);
                }
                g.fillRect(p.x * SnakeGame.CELL_SIZE, p.y * SnakeGame.CELL_SIZE,
                        SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE);
            }
        }
    }
       private void drawFood(Graphics g) {
        Point food = snakeGame.getFood();
        BufferedImage foodImage = snakeGame.getFoodImage();

        // Draw the food image with transparency
        g.drawImage(foodImage, food.x * SnakeGame.CELL_SIZE, food.y * SnakeGame.CELL_SIZE,
                SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE, this);
    }
    

    private void gameOver(Graphics g) {
        String playerName = snakeGame.getPlayerName();
        String scoreText = "Score: " + score;
        String gameOverMessage = "GAME OVER!";
        String restartMessage = "Press R to restart or Q to quit.";

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        // Calculate text widths and heights
        int gameOverWidth = g.getFontMetrics().stringWidth(gameOverMessage);
        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
        int restartWidth = g.getFontMetrics().stringWidth(restartMessage);

        int maxWidth = Math.max(Math.max(gameOverWidth, scoreWidth), restartWidth);
        int panelWidth = SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE;
        int panelHeight = SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE;
        int x = (panelWidth - maxWidth) / 2;
        int y = (panelHeight - 100) / 2; // Vertical center position

        // Draw game over message
        g.drawString(gameOverMessage, x, y);
        y += 30;

        // Draw wrapped player name
        FontMetrics fontMetrics = g.getFontMetrics();
        String wrappedPlayerName = wrapText(playerName, fontMetrics, maxWidth);
        String[] playerNameLines = wrappedPlayerName.split("\n");
        for (String line : playerNameLines) {
            int playerNameWidth = fontMetrics.stringWidth(line);
            int playerNameX = x + (maxWidth - playerNameWidth) / 2;
            g.drawString(line, playerNameX, y);
            y += 30; // Increase vertical position for the next line
        }

        // Draw score and restart message
        g.drawString("Score: " + score, x, y);
        y += 30;
        g.drawString(restartMessage, x, y);

        List<Player> highScores = playerDAO.getAllPlayers();
        highScores.sort(Comparator.comparingInt(Player::getScore).reversed());

        int numHighScoresToDisplay = Math.min(3, highScores.size());
        g.setFont(new Font("Arial", Font.BOLD, 18));
        int startY = y + 50;

        for (int i = 0; i < numHighScoresToDisplay; i++) {
            Player highScorePlayer = highScores.get(i);
            g.drawString(
                    "High Score #" + (i + 1) + ": " + highScorePlayer.getName() + " - " + highScorePlayer.getScore(), x,
                    startY);
            startY += 30;
        }
    }

    private String wrapText(String text, FontMetrics fontMetrics, int maxWidth) {
        StringBuilder wrappedText = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();
        String[] words = text.split("\\s+");

        for (String word : words) {
            int wordWidth = fontMetrics.stringWidth(currentLine + word);
            if (wordWidth <= maxWidth) {
                currentLine.append(word).append(" ");
            } else {
                wrappedText.append(currentLine).append("\n");
                currentLine = new StringBuilder(word).append(" ");
            }
        }

        wrappedText.append(currentLine);
        return wrappedText.toString();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (isGameOver) {
            if (keyCode == KeyEvent.VK_R) {
                initializeGame();
                isGameOver = false;
            } else if (keyCode == KeyEvent.VK_Q) {
                int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit the game?",
                        "Quit Game", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        } else {
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
            }
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
            if (!isGameOver) {
                snakeGame.move();
                if (snakeGame.isGameOver()) {
                    isGameOver = true;
                    Player player = new Player(snakeGame.getPlayerName(), score);
                    playerDAO.addPlayer(player);
                }
                score = snakeGame.getFoodsEaten();
                repaint();
            }

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
        frame.setLocationRelativeTo(null);
    }
}

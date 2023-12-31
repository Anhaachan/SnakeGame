import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
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
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    private boolean isPaused;
    public SnakeGamePanel() {
        setPreferredSize(null);
        initializeGame();
        setPreferredSize(new Dimension(SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE,
                SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        Thread gameThread = new Thread(this);
        gameThread.start();
        String imagePath = "./assets/background.jpg";
        // System.out.println("Image Path: " + ClassLoader.getSystemResource(imagePath));
        backgroundImage = new ImageIcon(ClassLoader.getSystemResource(imagePath));
           }

    private void initializeGame() {
        snakeGame = new SnakeGame();
        isGameOver = false;
        score = 0;

        snakeGame.getSnake().add(new Point(5, 5)); // Head
        snakeGame.getSnake().add(new Point(5, 6)); //   
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        // zuraasnuudig zurah
        // drawGrid(g); 
        draw(g);
        drawScore(g);
    }

    /*
     * 
     private void drawGrid(Graphics g) {
         // Set grid color to white
         g.setColor(Color.WHITE);
         
         // Draw vertical grid lines
         for (int x = 0; x <= SnakeGame.GRID_SIZE; x++) {
             g.drawLine(x * SnakeGame.CELL_SIZE, 0, x * SnakeGame.CELL_SIZE, SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE);
            }
            
            // Draw horizontal grid lines
            for (int y = 0; y <= SnakeGame.GRID_SIZE; y++) {
                g.drawLine(0, y * SnakeGame.CELL_SIZE, SnakeGame.GRID_SIZE * SnakeGame.CELL_SIZE, y * SnakeGame.CELL_SIZE);
            }
        }
    */



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
            Point prev = i > 0 ? snake.get(i - 1) : null;
            Point next = i < snake.size() - 1 ? snake.get(i + 1) : null;
    
            BufferedImage snakeSegmentImage;
    
            if (i == 0) {
                // Draw snake head based on direction
                snakeSegmentImage = snakeGame.getSnakeHeadImage(snakeGame.getDirection());
                // Calculate the scaled size (slightly bigger than the original size)
                int scaledSize = (int) (SnakeGame.CELL_SIZE * 1.2);
                // Calculate the offset to center the image within the cell
                int xOffset = (SnakeGame.CELL_SIZE - scaledSize) / 2;
                int yOffset = (SnakeGame.CELL_SIZE - scaledSize) / 2;
                // Draw the scaled snake head image with offset
                g.drawImage(snakeSegmentImage, p.x * SnakeGame.CELL_SIZE + xOffset, p.y * SnakeGame.CELL_SIZE + yOffset, scaledSize, scaledSize, this);
            } else if (i == snake.size() - 1) {
                // Draw snake tail
                snakeSegmentImage = getSnakeTailImage(p, prev);
                g.drawImage(snakeSegmentImage, p.x * SnakeGame.CELL_SIZE, p.y * SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE, this);
            } else {
                // Draw snake body
                snakeSegmentImage = getSnakeBodyImage(prev, p, next);
                g.drawImage(snakeSegmentImage, p.x * SnakeGame.CELL_SIZE, p.y * SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE, this);
            }
        }
    }
     
    private BufferedImage getSnakeBodyImage(Point prev, Point current, Point next) {
        // Determine the orientation of the body segment and return the appropriate image
        if (prev.x == next.x && prev.x == current.x) {
            return snakeGame.getVerticalBodyImage();
        } else if (prev.y == next.y && prev.y == current.y) {
            return snakeGame.getHorizontalBodyImage();
            
        } else if ( (prev.x > current.x && current.y > next.y)   || (prev.y < current.y && current.x < next.x)) {
            return snakeGame.getSnakeBodyTurnImage(Direction.RIGHT, Direction.DOWN);

        } else if ((prev.x < current.x && current.y > next.y)  ||   (prev.y < current.y && current.x > next.x)) {
            return snakeGame.getSnakeBodyTurnImage(Direction.RIGHT, Direction.UP);

        } else if ((prev.x > current.x && current.y < next.y)||  (prev.y > current.y && current.x < next.x)) {
            return snakeGame.getSnakeBodyTurnImage(Direction.LEFT, Direction.DOWN);
            
        } else if ( (prev.x < current.x && current.y < next.y) || (prev.y > current.y && current.x > next.x)  ) {
            return snakeGame.getSnakeBodyTurnImage(Direction.LEFT, Direction.UP);
        }
        return null;
    }
    
    private BufferedImage getSnakeTailImage(Point current, Point prev) {
        // Determine the orientation of the tail segment and return the appropriate image
        if (prev.x < current.x) {
            return snakeGame.getSnakeTailImage(Direction.LEFT);
        } else if (prev.x > current.x) {
            return snakeGame.getSnakeTailImage(Direction.RIGHT);
        } else if (prev.y < current.y) {
            return snakeGame.getSnakeTailImage(Direction.UP);
        } else if (prev.y > current.y) {
            return snakeGame.getSnakeTailImage(Direction.DOWN);
        }
        return null;
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
            // Game over logic
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
        } else if (keyCode == KeyEvent.VK_P) {
            isPaused = !isPaused; // Toggle the pause state when 'P' key is pressed
        } else {
            // Handle direction changes
            SnakeGame.Direction currentDirection = snakeGame.getDirection();
            SnakeGame.Direction newDirection = null;
    
            // Set new direction based on pressed key
            if (keyCode == KeyEvent.VK_UP && currentDirection != SnakeGame.Direction.DOWN) {
                newDirection = SnakeGame.Direction.UP;
            } else if (keyCode == KeyEvent.VK_DOWN && currentDirection != SnakeGame.Direction.UP) {
                newDirection = SnakeGame.Direction.DOWN;
            } else if (keyCode == KeyEvent.VK_LEFT && currentDirection != SnakeGame.Direction.RIGHT) {
                newDirection = SnakeGame.Direction.LEFT;
            } else if (keyCode == KeyEvent.VK_RIGHT && currentDirection != SnakeGame.Direction.LEFT) {
                newDirection = SnakeGame.Direction.RIGHT;
            }
    
            if (newDirection != null) {
                snakeGame.setDirection(newDirection);
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
            if (!isPaused && !isGameOver) {
                snakeGame.move();
                if (snakeGame.isGameOver()) {
                    isGameOver = true;
                    Player player = new Player(snakeGame.getPlayerName(), score);
                    /*
                     * DB - ruu store hiih 
                     * tur ingej orhiy
                     */
                    // playerDAO.addPlayer(player);
                    System.out.println(player.toString());
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

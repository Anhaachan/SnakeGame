import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SnakeGame {
    static final int GRID_SIZE = 20;
    static final int CELL_SIZE = 20;
    static int DELAY = 150;
    private int foodsEaten;
    private Player player;
    private ArrayList<Point> snake;
    private Point food;
    private Direction direction;
    private boolean isGameOver;
    private String playerName;

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public SnakeGame() {
        foodsEaten = 0;
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        food = new Point(15, 15);
        direction = Direction.RIGHT;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));



        JTextField playerNameField = new JTextField();

        playerNameField.addKeyListener(new KeyListener() {
 
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isLetter(c) && (Character.isLowerCase(c) || Character.isUpperCase(c))) || playerNameField.getText().length() > 20) {
                    e.consume(); // Ignore the event
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Not needed for this case
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not needed for this case
            }
        });
        panel.add(new JLabel("Enter your name:"));
        panel.add(playerNameField);
        
        int result = JOptionPane.showOptionDialog(null, panel, "Snake Game",
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"OK"}, null);
        
        if (result == JOptionPane.OK_OPTION && !playerNameField.getText().isEmpty() ) {
            playerName = playerNameField.getText();
        }
         else {
            playerName = "Player"; 
        }

        isGameOver = false;
    }
    

    public String getPlayerName() {
        return playerName;
    }
public void setPlayerName(String playerName) {
    this.playerName = playerName;
}
    public int getFoodsEaten() {
        return foodsEaten;
    }

    public void move() {
        if (!isGameOver) {
            Point head = snake.get(0);
            Point newHead = new Point(head.x, head.y);

            switch (direction) {
                case UP:
                    newHead.y--;
                    break;
                case DOWN:
                    newHead.y++;
                    break;
                case LEFT:
                    newHead.x--;
                    break;
                case RIGHT:
                    newHead.x++;
                    break;
            }

            if (newHead.equals(food)) {
                snake.add(0, newHead);
                spawnFood();

                foodsEaten++;

                if (foodsEaten % 10 == 0) {
                    DELAY -= 10;
                }
            } else {
                snake.add(0, newHead);
                snake.remove(snake.size() - 1);
            }

            checkCollision();
        }
    }

    public void setDirection(Direction newDirection) {
        if (newDirection != opposite(direction)) {
            direction = newDirection;
        }
    }

    public Point getFood() {
        return food;
    }

    public ArrayList<Point> getSnake() {
        return snake;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    private void spawnFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));
        food.setLocation(x, y);
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE) {
            isGameOver = true;
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                isGameOver = true;
                break;
            }
        }
    }

    private Direction opposite(Direction dir) {
        switch (dir) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            default:
                return null;
        }
    }
}

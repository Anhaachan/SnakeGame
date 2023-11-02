import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame {
    static final int GRID_SIZE = 20;
    static final int CELL_SIZE = 20;
    static int DELAY = 150;
    private int foodsEaten;

    private ArrayList<Point> snake;
    private Point food;
    private Direction direction;
    private boolean isGameOver;
    private int score;

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public SnakeGame() {
        foodsEaten = 0;
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        food = new Point(15, 15);
        direction = Direction.RIGHT;
        isGameOver = false;
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

                score = foodsEaten;
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
        int x,y;
        do {
        	x = random.nextInt(GRID_SIZE);
        	y = random.nextInt(GRID_SIZE);
        }while(snake.contains(new Point(x,y)));
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

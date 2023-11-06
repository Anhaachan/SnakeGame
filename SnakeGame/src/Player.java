import java.util.Date;

public class Player {
    private String name;
    private int score;
    private Date timeStamp;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
        this.timeStamp = new Date();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    

    public Date getTimeStamp() {
        return timeStamp;
    }
    
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", timeStamp=" + timeStamp +
                '}';
    }
}

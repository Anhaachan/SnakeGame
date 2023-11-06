import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    private Connection connection;

    public PlayerDAO() {
        connection = ConnectionUtil.getConnection();
    }

    public void addPlayer(Player player) {
        try {
            String query = "INSERT INTO games (name, score, timestamp) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, player.getName());
                ps.setInt(2, player.getScore());
                ps.setTimestamp(3, new java.sql.Timestamp(player.getTimeStamp().getTime()));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        try {
            String query = "SELECT * FROM games";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int score = resultSet.getInt("score");
                    java.sql.Timestamp timestamp = resultSet.getTimestamp("timestamp");
                    Player player = new Player(name, score);
                    player.setTimeStamp(timestamp);
                    players.add(player);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }
    
}

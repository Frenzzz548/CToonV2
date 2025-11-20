package dao;
import model.Rating;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO {
    private Connection connection;

    public RatingDAO() {
        try {
            connection = util.DBUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Rating> getRatingsByComicId(int comicId) {
        List<Rating> ratings = new ArrayList<>();
        try {
            String query = "SELECT * FROM ratings WHERE comic_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, comicId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Rating rating = new Rating();
                rating.setId(rs.getInt("id"));
                rating.setUserId(rs.getInt("user_id"));
                rating.setComicId(rs.getInt("comic_id"));
                rating.setStars(rs.getInt("stars"));
                ratings.add(rating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    public void addRating(Rating rating) {
        try {
            String query = "INSERT INTO ratings (user_id, comic_id, stars) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, rating.getUserId());
            stmt.setInt(2, rating.getComicId());
            stmt.setInt(3, rating.getStars());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

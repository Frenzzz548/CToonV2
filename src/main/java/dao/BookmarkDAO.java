package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Bookmark;

public class BookmarkDAO {
    private Connection connection;

    public BookmarkDAO() {
        this.connection = null;
    }

    private synchronized Connection getConnection() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = util.DBUtil.getConnection();
        }
        return this.connection;
    }

    public boolean isBookmarked(int userId, int comicId) {
        try {
            String query = "SELECT COUNT(*) FROM bookmarks WHERE user_id = ? AND comic_id = ?";
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, comicId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking bookmark: " + e.getMessage());
        }
        return false;
    }

    public void addBookmark(Bookmark bookmark) {
        try {
            String query = "INSERT INTO bookmarks (user_id, comic_id) VALUES (?, ?)";
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, bookmark.getUserId());
            stmt.setInt(2, bookmark.getComicId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding bookmark: " + e.getMessage());
        }
    }

    public void removeBookmark(int userId, int comicId) {
        try {
            String query = "DELETE FROM bookmarks WHERE user_id = ? AND comic_id = ?";
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, comicId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing bookmark: " + e.getMessage());
        }
    }

    public List<model.Bookmark> getBookmarksForUser(int userId) {
        List<model.Bookmark> list = new ArrayList<>();
        try {
            String query = "SELECT id, user_id, comic_id FROM bookmarks WHERE user_id = ? ORDER BY id DESC";
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.Bookmark b = new model.Bookmark();
                b.setId(rs.getInt("id"));
                b.setUserId(rs.getInt("user_id"));
                b.setComicId(rs.getInt("comic_id"));
                list.add(b);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookmarks for user: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}

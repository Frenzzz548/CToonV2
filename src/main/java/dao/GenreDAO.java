package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Genre;

public class GenreDAO {
    private Connection connection;

    public GenreDAO() {
        this.connection = null;
    }

    private synchronized Connection getConnection() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = util.DBUtil.getConnection();
        }
        return this.connection;
    }

    public List<Genre> getGenresByComicId(int comicId) {
        List<Genre> genres = new ArrayList<>();
        try {
            String query = "SELECT g.id, g.name, g.slug FROM genres g " +
                    "INNER JOIN comic_genres cg ON g.id = cg.genre_id " +
                    "WHERE cg.comic_id = ?";
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, comicId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Genre genre = new Genre();
                genre.setId(rs.getLong("id"));
                genre.setName(rs.getString("name"));
                genre.setSlug(rs.getString("slug"));
                genres.add(genre);
            }
            if (!genres.isEmpty()) {
                System.out.println("Fetched " + genres.size() + " genres for comic ID " + comicId);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching genres for comic: " + e.getMessage());
            e.printStackTrace();
        }
        return genres;
    }

    public Genre getGenreById(long genreId) {
        try {
            String query = "SELECT id, name, slug FROM genres WHERE id = ?";
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setLong(1, genreId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Genre genre = new Genre();
                genre.setId(rs.getLong("id"));
                genre.setName(rs.getString("name"));
                genre.setSlug(rs.getString("slug"));
                return genre;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching genre by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        try {
            String query = "SELECT id, name, slug FROM genres ORDER BY name";
            PreparedStatement stmt = getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Genre genre = new Genre();
                genre.setId(rs.getLong("id"));
                genre.setName(rs.getString("name"));
                genre.setSlug(rs.getString("slug"));
                genres.add(genre);
            }
            System.out.println("Fetched " + genres.size() + " genres");
        } catch (SQLException e) {
            System.err.println("Error fetching all genres: " + e.getMessage());
            e.printStackTrace();
        }
        return genres;
    }
}

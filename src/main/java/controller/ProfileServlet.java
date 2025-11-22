package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BookmarkDAO;
import dao.ComicDAO;
import model.Bookmark;
import model.Comic;
import model.User;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
            return;
        }

        // Load bookmarks for user
        BookmarkDAO bookmarkDAO = new BookmarkDAO();
        List<Bookmark> bookmarks = bookmarkDAO.getBookmarksForUser(user.getId());

        // Build a lightweight view list with comicId and title
        List<Map<String, Object>> bookmarkViews = new ArrayList<>();
        ComicDAO comicDAO = new ComicDAO();
        for (Bookmark b : bookmarks) {
            Comic c = comicDAO.getComicById(b.getComicId());
            Map<String, Object> m = new HashMap<>();
            m.put("comicId", b.getComicId());
            m.put("title", c != null ? c.getTitle() : "(removed)");
            bookmarkViews.add(m);
        }

        req.setAttribute("bookmarks", bookmarkViews);

        // TODO: load reading history if available

        req.getRequestDispatcher("/profile.jsp").forward(req, resp);
    }
}

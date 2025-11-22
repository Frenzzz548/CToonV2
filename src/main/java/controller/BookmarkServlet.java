package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BookmarkDAO;
import model.Bookmark;
import model.User;

@WebServlet("/bookmark")
public class BookmarkServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=Please+login+to+bookmark");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=Please+login+to+bookmark");
            return;
        }

        String comicIdStr = req.getParameter("comicId");
        if (comicIdStr == null || comicIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        try {
            int comicId = Integer.parseInt(comicIdStr);
            BookmarkDAO dao = new BookmarkDAO();
            boolean exists = dao.isBookmarked(user.getId(), comicId);
            if (exists) {
                dao.removeBookmark(user.getId(), comicId);
            } else {
                Bookmark b = new Bookmark();
                b.setUserId(user.getId());
                b.setComicId(comicId);
                dao.addBookmark(b);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid comicId for bookmark: " + e.getMessage());
        }

        // Try to redirect back to referring page if available
        String referer = req.getHeader("referer");
        if (referer != null && !referer.isEmpty()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/comic-detail?id=" + comicIdStr);
        }
    }
}

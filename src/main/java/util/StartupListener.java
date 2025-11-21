package util;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String webRoot = sce.getServletContext().getRealPath("/");
            if (webRoot == null) {
                System.out.println("[StartupListener] webRoot is null (running from archive)");
            } else {
                File index = new File(webRoot, "index.jsp");
                System.out.println("[StartupListener] webRoot: " + webRoot);
                System.out.println("[StartupListener] index.jsp exists: " + index.exists() + " (path="
                        + index.getAbsolutePath() + ")");
            }
        } catch (Exception e) {
            System.err.println("[StartupListener] Error during startup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[StartupListener] context destroyed");
    }
}

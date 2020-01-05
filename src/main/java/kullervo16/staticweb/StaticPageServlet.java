package kullervo16.staticweb;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet
public class StaticPageServlet extends HttpServlet {


    private final String path;

    public StaticPageServlet(String path) {
        this.path = path;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (FileInputStream fis = new FileInputStream(path+req.getRequestURI())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            if(req.getRequestURI().endsWith(".css")) {
                resp.setContentType("text/css");
            } else if(req.getRequestURI().endsWith(".html")) {
                resp.setContentType("text/html");
            } else if(req.getRequestURI().endsWith(".js")) {
                resp.setContentType("application/javascript");
            }
            resp.setHeader("Cache-Control", "public, max-age=31536000");
            IOUtils.copy(fis, resp.getOutputStream());

        }catch (IOException ioe) {
            ioe.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

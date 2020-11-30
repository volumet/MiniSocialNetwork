/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import vinhnq.article.ArticleDAO;
import vinhnq.notification.NotificationDAO;
import vinhnq.notification.NotificationDTO;

/**
 *
 * @author Admin
 */
public class NotificationServlet extends HttpServlet {
    private final org.apache.log4j.Logger logger = LogManager.getLogger(NotificationServlet.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String ownerId = request.getParameter("txtOwnerId");
        String url = "noti.jsp";
        try {
            
            //load the list of update articles
            ArticleDAO dao1 = new ArticleDAO();
            dao1.loadListOfUpdatedArticles(ownerId);
            List<String> listOfUpdatedArticles = dao1.getListOfUpdatedArticles();
            
            
            /*if the list of updated articles not null, for each post
            get its noti*/
            NotificationDAO dao2 = new NotificationDAO();
            List<NotificationDTO> listOfNotification = new ArrayList<>();
            if (listOfUpdatedArticles != null) {
                for (String postId : listOfUpdatedArticles) {
                    NotificationDTO dto = dao2.getNoti(postId);
                    listOfNotification.add(dto);
                }
            }
            
            //set list of noti to request scope
            request.setAttribute("NOTI_LIST", listOfNotification);
            
            
            //show number of notifications by ownerId and set to request scope
            int notification = dao1.showNumberOfUpdatedArticles(ownerId);
            request.setAttribute("NOTI", notification);
        } catch (NamingException e) {
            log("NotificationServlet NamingException " + e.getMessage());
            logger.error("NotificationServlet NamingException " + e.getMessage());
        } catch (SQLException e) {
            log("NotificationServlet SQLException " + e.getMessage());
            logger.error("NotificationServlet SQLException " + e.getMessage());
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

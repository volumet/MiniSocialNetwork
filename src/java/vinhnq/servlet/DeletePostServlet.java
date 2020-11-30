/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import vinhnq.account.AccountDTO;
import vinhnq.article.ArticleDAO;
import vinhnq.emotion.EmotionDAO;
import vinhnq.notification.NotificationDAO;

/**
 *
 * @author Admin
 */
public class DeletePostServlet extends HttpServlet {

    private final org.apache.log4j.Logger logger = LogManager.getLogger(DeletePostServlet.class);

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
        String postId = request.getParameter("txtPostId");
        String url = "main.jsp";
        try {
            ArticleDAO dao = new ArticleDAO();

            //delete noti (before delete post to avoid PK constraint
            NotificationDAO dao2 = new NotificationDAO();
            dao2.deleteNoti(postId, "Active");

            //--------------------------------------------------------
            
            //delete all emotion of this post before delete post to avoid PK constraint
            EmotionDAO dao3 = new EmotionDAO();
            dao3.deleteAllEmotionOfPost(postId);
            //----------------------------------------------------------

            //Get maxdeletePostId + 1 and delete post, because after delete post, the postId can be duplicate to the deleted one
            int deletePostIdByInt = dao.getMaxDeletePostId();
            deletePostIdByInt++;
            String deletePostId = String.valueOf(deletePostIdByInt);
            dao.deletePost(postId, deletePostId);

            //show number of notifications by ownerId and set to request scope
            HttpSession session = request.getSession(false);
            AccountDTO dto2 = (AccountDTO) session.getAttribute("LOGIN_ACCOUNT");
            String ownerId = dto2.getEmail();
            int notification = dao.showNumberOfUpdatedArticles(ownerId);
            request.setAttribute("NOTI", notification);
            
        } catch (NamingException e) {
            log("DeletePostServlet NamingException " + e.getMessage());
            logger.error("DeletePostServlet NamingException " + e.getMessage());
        } catch (SQLException e) {
            log("DeletePostServlet SQLException " + e.getMessage());
            logger.error("DeletePostServlet SQLException " + e.getMessage());
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

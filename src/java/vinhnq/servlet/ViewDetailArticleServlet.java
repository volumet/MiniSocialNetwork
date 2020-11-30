/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
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
import vinhnq.article.ArticleDTO;
import vinhnq.comment.CommentDAO;
import vinhnq.comment.CommentDTO;

/**
 *
 * @author Admin
 */
public class ViewDetailArticleServlet extends HttpServlet {
    private final org.apache.log4j.Logger logger = LogManager.getLogger(ViewDetailArticleServlet.class);

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
        
        //url is viewpost
        String url = "viewpost.jsp";
        
        try {
            //search the post want to view by its postId and set to request scope
            ArticleDAO dao1 = new ArticleDAO();
            ArticleDTO dto = dao1.searchArticleById(postId);
            request.setAttribute("DETAIL_POST", dto);
            
            //load the comment of the post by postId and set to request scope
            CommentDAO dao2 = new CommentDAO();
            dao2.loadComment(postId);
            List<CommentDTO> listComment = dao2.getListComment();
            request.setAttribute("LIST_COMMENT", listComment);
            
            //get the current login account
            HttpSession session = request.getSession(false);
            AccountDTO dto2 = (AccountDTO) session.getAttribute("LOGIN_ACCOUNT");
            String ownerId = dto2.getEmail();
            
            //update noti of this post to Normal if it is the owner
            if (dto.getOwnerId().equals(ownerId)) {
                dao1.updatedNotiPost(postId, "Normal");
            }
            
            /*show number of notifications by ownerId (from session email) 
            and set to request scope*/            
            int notification = dao1.showNumberOfUpdatedArticles(ownerId);
            request.setAttribute("NOTI", notification);
            
        } catch (NamingException e) {
            log("ViewDetailArticleServlet NamingException " + e.getMessage());
            logger.error("ViewDetailArticleServlet NamingException " + e.getMessage());
        } catch (SQLException e) {
            log("ViewDetailArticleServlet SQLException " + e.getMessage());
            logger.error("ViewDetailArticleServlet SQLException " + e.getMessage());
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

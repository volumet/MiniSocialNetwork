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
public class DeleteCommentServlet extends HttpServlet {
    private final org.apache.log4j.Logger logger = LogManager.getLogger(DeleteCommentServlet.class);
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
        String commentId = request.getParameter("txtCommentId");
        
        //set url to viewpost.jsp
        String url = "viewpost.jsp";
        
        
        try {
            CommentDAO dao1 = new CommentDAO();
            
            /*Get maxdeleteCommentId, because after delete comment, the commentId can be duplicate to the deleted one
            set the maxdeletecmtId by the selected value*/
            int maxDeleteCommentIdByInt = dao1.getMaxDeleteCommentId();
            maxDeleteCommentIdByInt++;
            String maxDeleteCommentId = String.valueOf(maxDeleteCommentIdByInt);
            //-----------------------------------------------------------
            
            //delete comment by update comment status
            dao1.deleteComment(postId, commentId, maxDeleteCommentId);
            
            
            //deleted first then load comment to avoid inaccuracy, set to request scope
            dao1.loadComment(postId);
            List<CommentDTO> listComment = dao1.getListComment();
            request.setAttribute("LIST_COMMENT", listComment);
            
            
            //search this article and set to request scope
            ArticleDAO dao2 = new ArticleDAO();
            ArticleDTO dto = dao2.searchArticleById(postId);
            request.setAttribute("DETAIL_POST", dto);
            
            //show number of notifications by ownerId (session email) and set to request scope
            HttpSession session = request.getSession(false);
            AccountDTO dto2 = (AccountDTO) session.getAttribute("LOGIN_ACCOUNT");
            String ownerId = dto2.getEmail();
            int notification = dao2.showNumberOfUpdatedArticles(ownerId);
            request.setAttribute("NOTI", notification);
            
        } catch (NamingException e) {
            log("DeleteCommentServlet NamingException " + e.getMessage());
            logger.error("DeleteCommentServlet NamingException " + e.getMessage());
        } catch (SQLException e) {
            log("DeleteCommentServlet SQLException " + e.getMessage());
            logger.error("DeleteCommentServlet SQLException " + e.getMessage());
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

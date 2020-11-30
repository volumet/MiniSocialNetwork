/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import vinhnq.notification.NotificationDAO;

/**
 *
 * @author Admin
 */
public class CommentingServlet extends HttpServlet {

    private final org.apache.log4j.Logger logger = LogManager.getLogger(CommentingServlet.class);

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
        String comment = request.getParameter("txtComment");
        String commentor = request.getParameter("txtCommentor");
        String postId = request.getParameter("txtPostId");

        //set url to viewpost.jsp
        String url = "viewpost.jsp";

        try {
            //Get current date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date currentDateTime = new java.util.Date();
            String stringDate = dateFormat.format(currentDateTime.getTime());
            java.util.Date parsedDate = dateFormat.parse(stringDate);
            Timestamp date = new java.sql.Timestamp(parsedDate.getTime());
            //--------------------

            //search this article again by postId and set to request scope
            ArticleDAO dao1 = new ArticleDAO();
            ArticleDTO dto = dao1.searchArticleById(postId);
            request.setAttribute("DETAIL_POST", dto);

            //Get user account from session
            HttpSession session = request.getSession(false);
            AccountDTO dto2 = (AccountDTO) session.getAttribute("LOGIN_ACCOUNT");

            CommentDAO dao2 = new CommentDAO();
            //if comment is not empty
            if (!comment.equals("")) {

                //Set new commentId (by get max cmt id and + 1)
                int commentIdByInt = dao2.getMaxCommentIdOfPost(postId);
                commentIdByInt++; //inc commentId by 1
                String commentId = String.valueOf(commentIdByInt);
                //-----------------------------------------

                //post comment
                dao2.postComment(postId, commentId, commentor, comment, date);

                //if the commentor is not the post owner
                if (!dto2.getEmail().equals(dao1.searchOwnerByPostId(postId))) {
                    //change noti status to commented and change post noti status to updated
                    NotificationDAO dao3 = new NotificationDAO();
                    dao3.changeNotiStatus(postId, commentor, comment, "COMMENTED", date, "Active", postId, "Active"); //Notification fields
                    dao1.updatedNotiPost(postId, "Updated"); //Article updated field
                }
            } //else if comment is empty
            else {
                //set error to COMMENT_EMPTY request scope attribute
                request.setAttribute("COMMENT_EMPTY", "Can't leave comment field empty!");
            }
            //-----------------------------------------------

            //load the list of comments and set to request scope
            dao2.loadComment(postId);
            List<CommentDTO> listComment = dao2.getListComment();
            request.setAttribute("LIST_COMMENT", listComment);

            //show number of notifications by ownerId and set to request scope          
            String ownerId = dto2.getEmail();
            int notification = dao1.showNumberOfUpdatedArticles(ownerId);
            request.setAttribute("NOTI", notification);
        } catch (NamingException e) {
            log("CommentingServlet NamingException " + e.getMessage());
            logger.error("CommentingServlet NamingException " + e.getMessage());
        } catch (SQLException e) {
            log("CommentingServlet SQLException " + e.getMessage());
            logger.error("CommentingServlet SQLException " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            log("CommentingServlet UnsupportedEncodingException " + e.getMessage());
            logger.error("CommentingServlet UnsupportedEncodingException " + e.getMessage());
        } catch (ParseException e) {
            log("CommentingServlet ParseException " + e.getMessage());
            logger.error("CommentingServlet ParseException " + e.getMessage());
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

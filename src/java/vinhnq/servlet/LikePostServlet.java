/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import vinhnq.emotion.EmotionDAO;
import vinhnq.notification.NotificationDAO;

/**
 *
 * @author Admin
 */
public class LikePostServlet extends HttpServlet {

    private final org.apache.log4j.Logger logger = LogManager.getLogger(LikePostServlet.class);

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
        String emotioner = request.getParameter("txtEmotioner");
        
        //url set to viewpost.jsp
        String url = "viewpost.jsp";
        
        try {

            EmotionDAO dao1 = new EmotionDAO();
            
            //check like status
            boolean checkLiked = dao1.checkLiked(emotioner, postId);
            
            
            //if not liked
            if (checkLiked == false) {
                //set emotion to like
                dao1.likePost(emotioner, postId);
                
                //increase the like of this article by 1
                ArticleDAO dao2 = new ArticleDAO();
                int like = dao2.searchLikeById(postId);
                dao2.updateLikeById(postId, like + 1);

                //If disliked, liking will remove dislike (decrease article dislike by 1 and delete emotion)
                boolean checkDisliked = dao1.checkDisliked(emotioner, postId);
                if (checkDisliked == true) {
                    dao1.delikePost(emotioner, postId, false);
                    int dislike = dao2.searchDislikeById(postId);
                    dao2.updateDislikeById(postId, dislike - 1);
                }
                //---------------------------------------
                
                //search this article again and set to request scope
                ArticleDTO dto = dao2.searchArticleById(postId);
                request.setAttribute("DETAIL_POST", dto);

                //Get current date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date currentDateTime = new java.util.Date();
                String stringDate = dateFormat.format(currentDateTime.getTime());
                java.util.Date parsedDate = dateFormat.parse(stringDate);
                Timestamp date = new java.sql.Timestamp(parsedDate.getTime());
                //--------------------
                
                
                //Get user account
                HttpSession session = request.getSession(false);
                AccountDTO dto2 = (AccountDTO) session.getAttribute("LOGIN_ACCOUNT");
                
                //Push noti if the liker is not the post owner
                if (!dto2.getEmail().equals(dao2.searchOwnerByPostId(postId))) {
                    NotificationDAO dao3 = new NotificationDAO();

                    dao2.updatedNotiPost(postId, "Updated"); //update article status to updated            
                    dao3.changeNotiStatus(postId, emotioner, null, "LIKED", date, "Active", postId, "Active"); //change noti status to liked
                }
                //------------------------------------------
            } 
            //if liked already
            else {
                //delete like demotion
                dao1.delikePost(emotioner, postId, true);
                
                //decrease the number of like by 1
                ArticleDAO dao2 = new ArticleDAO();
                int like = dao2.searchLikeById(postId);
                dao2.updateLikeById(postId, like - 1);
                
                //set the article to request scope
                ArticleDTO dto = dao2.searchArticleById(postId);
                request.setAttribute("DETAIL_POST", dto);
            }
            
            //load the comment list and set to request scope
            CommentDAO dao3 = new CommentDAO();
            dao3.loadComment(postId);
            List<CommentDTO> listComment = dao3.getListComment();
            request.setAttribute("LIST_COMMENT", listComment);
            
            
            //show number of article notifications by ownerId (session email) and set to request scope
            HttpSession session = request.getSession(false);
            AccountDTO dto2 = (AccountDTO) session.getAttribute("LOGIN_ACCOUNT");
            ArticleDAO dao2 = new ArticleDAO();
            String ownerId = dto2.getEmail();
            int notification = dao2.showNumberOfUpdatedArticles(ownerId);
            request.setAttribute("NOTI", notification);

        } catch (NamingException e) {
            log("LikePostServlet NamingException " + e.getMessage());
            logger.error("LikePostServlet NamingException " + e.getMessage());
        } catch (SQLException e) {
            log("LikePostServlet SQLException " + e.getMessage());
            logger.error("LikePostServlet SQLException " + e.getMessage());
        } catch (ParseException e) {
            log("LikePostServlet ParseException " + e.getMessage());
            logger.error("LikePostServlet ParseException " + e.getMessage());
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

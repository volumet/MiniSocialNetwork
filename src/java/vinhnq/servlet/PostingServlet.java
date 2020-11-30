/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.log4j.LogManager;
import vinhnq.article.ArticleDAO;
import vinhnq.article.ArticleDTO;
import vinhnq.notification.NotificationDAO;

/**
 *
 * @author Admin
 */
//Set @MultipartConfig
@MultipartConfig(maxFileSize = 16177215)
public class PostingServlet extends HttpServlet {

    private final org.apache.log4j.Logger logger = LogManager.getLogger(PostingServlet.class);

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
        String title = request.getParameter("txtTitle");
        String textArea = request.getParameter("txtareaPost");
        String ownerId = request.getParameter("txtOwnerId");

        //originally url is viewpost.jsp
        String url = "viewpost.jsp";

        //Get photo
        Part filePart = request.getPart("photo"); //Get photo by Part
        InputStream photo = filePart.getInputStream(); //Convert part to input stream
        if (filePart.getSize() <= 0) { //Check file part size if it's <=0 (no picture was uploaded)
            photo = null; //set input stream null
        }
        //----------------------------


        //check if post title / content is empty
        if ("".equals(title) || "".equals(textArea)) {
            try {
                //set url to main.jsp
                url = "main.jsp";

                //set error to request scope
                request.setAttribute("BLANK_POSTING", "Please fill all the blank field!");

                //show number of notifications by ownerId andset to request scope
                ArticleDAO dao = new ArticleDAO();
                int notification = dao.showNumberOfUpdatedArticles(ownerId);
                request.setAttribute("NOTI", notification);

                //forward to main.jsp
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
                out.close();
            } catch (NamingException e) {
                log("PostingServlet NamingException " + e.getMessage());
                logger.error("PostingServlet NamingException " + e.getMessage());
            } catch (SQLException e) {
                log("PostingServlet SQLException " + e.getMessage());
                logger.error("PostingServlet SQLException " + e.getMessage());
            }
        } //the post title and content is valid
        else {
            try {
                //Get current date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date currentDateTime = new Date();
                String stringDate = dateFormat.format(currentDateTime.getTime());
                Date parsedDate = dateFormat.parse(stringDate);
                Timestamp date = new java.sql.Timestamp(parsedDate.getTime());

                //get maxPostId to set to post and upload post to Article
                ArticleDAO dao = new ArticleDAO();
                int maxPostId = dao.getMaxPostId() + 1;
                dao.uploadPost(title, ownerId, photo, textArea, date, maxPostId);

                //search the post uploaded and set to request upload (to go to view post)
                ArticleDTO dto = dao.searchArticleById("" + maxPostId);
                request.setAttribute("DETAIL_POST", dto);

                //push post noti status POSTED
                NotificationDAO dao2 = new NotificationDAO();
                dao2.pushNoti("" + maxPostId, ownerId, null, "POSTED", date, "Active");

                //show number of notifications by ownerId and set to request scope
                int notification = dao.showNumberOfUpdatedArticles(ownerId);
                request.setAttribute("NOTI", notification);
            } catch (NamingException e) {
                log("PostingServlet NamingException " + e.getMessage());
                logger.error("PostingServlet NamingException " + e.getMessage());
            } catch (SQLException e) {
                log("PostingServlet SQLException " + e.getMessage());
                logger.error("PostingServlet SQLException " + e.getMessage());
            } catch (ParseException e) {
                log("PostingServlet ParseException " + e.getMessage());
                logger.error("PostingServlet ParseException " + e.getMessage());
            } finally {
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
                out.close();
            }
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

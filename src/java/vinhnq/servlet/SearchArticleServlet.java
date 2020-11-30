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

/**
 *
 * @author Admin
 */
public class SearchArticleServlet extends HttpServlet {

    private final org.apache.log4j.Logger logger = LogManager.getLogger(SearchArticleServlet.class);

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
        String searchContent = request.getParameter("txtSearchValue");
        int searchPage = Integer.valueOf(request.getParameter("txtPage"));
        String url = "main.jsp";
        
        //check if searchContent is empty (then return to main.jsp)
        if (searchContent.equals("") || searchContent.equals(" ")) {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
        } 
        //if searchContent is not empty
        else {
            try {
                //search and get listOfArticles
                ArticleDAO dao = new ArticleDAO();
                int postPerPage = 20;
                dao.searchArticle(searchContent.toUpperCase(), searchPage, postPerPage);
                List<ArticleDTO> listOfArticles = dao.getListOfArticles();

                //Caculate the paging size
                int pagingSize = dao.searchResult(searchContent); //count the number of articles found

                if (pagingSize % postPerPage == 0) {
                    pagingSize = pagingSize / postPerPage;
                } else {
                    pagingSize = pagingSize / postPerPage + 1;
                }
                
                //Set pagingSize to request scope
                request.setAttribute("PAGING_SIZE", pagingSize);
                //-----------------------------------------
                
                //Set search result to request scope
                request.setAttribute("SEARCH_RESULT", listOfArticles);
                

                /*show number of notifications by ownerId (getEmail in session scope) 
                and set to request scope */
                HttpSession session = request.getSession(false);
                AccountDTO dto2 = (AccountDTO) session.getAttribute("LOGIN_ACCOUNT");
                String ownerId = dto2.getEmail();
                int notification = dao.showNumberOfUpdatedArticles(ownerId);
                request.setAttribute("NOTI", notification);

            } catch (NamingException e) {
                log("SearchArticleServlet NamingException " + e.getMessage());
                logger.error("SearchArticleServlet NamingException " + e.getMessage());
            } catch (SQLException e) {
                log("SearchArticleServlet SQLException " + e.getMessage());
                logger.error("SearchArticleServlet SQLException " + e.getMessage());
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import vinhnq.account.AccountDAO;
import vinhnq.account.AccountDTO;
import vinhnq.article.ArticleDAO;
import vinhnq.util.Encryption;
import vinhnq.ping.PingDAO;

/**
 *
 * @author Admin
 */
public class LoginServlet extends HttpServlet {

    private final org.apache.log4j.Logger logger = LogManager.getLogger(LoginServlet.class);

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
        String username = request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");
        String url = "error.html";
        try {
            //encrypt inputted password
            String passwordEncrypted = Encryption.toHexString(Encryption.getSHA(password));
            
            //login
            AccountDAO dao = new AccountDAO();
            AccountDTO dto = dao.loginAccount(username, passwordEncrypted);
            
            //if account not null
            if (dto != null) {
                PingDAO dao3 = new PingDAO();
                //account in active status
                if (dao3.getStatus(dto.getStatusId()).equals("Active")) {
                    //Create session
                    url = "main.jsp";
                    HttpSession session = request.getSession(true);
                    session.setAttribute("LOGIN_ACCOUNT", dto);

                    //set number of noti by username to request scope
                    ArticleDAO dao2 = new ArticleDAO();
                    int notification = dao2.showNumberOfUpdatedArticles(username);
                    request.setAttribute("NOTI", notification);
                }
                //account in new status
                else if (dao3.getStatus(dto.getStatusId()).equals("New")) {
                    //go to required verify page
                    url = "required_verify.html";
                }
            }
        } catch (NoSuchAlgorithmException e) {
            log("LoginServlet NoSuchAlgorithmException " + e.getMessage());
            logger.error("LoginServlet NoSuchAlgorithmException " + e.getMessage());
        } catch (NamingException e) {
            log("LoginServlet NamingException " + e.getMessage());
            logger.error("LoginServlet NamingException " + e.getMessage());
        } catch (SQLException e) {
            log("LoginServlet SQLException " + e.getMessage());
            logger.error("LoginServlet SQLException " + e.getMessage());
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

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
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import vinhnq.account.AccountDAO;
import vinhnq.account.AccountDTO;
import vinhnq.util.Encryption;
import vinhnq.error.AccountError;

/**
 *
 * @author Admin
 */
public class AccountRegisterServlet extends HttpServlet {

    private final org.apache.log4j.Logger logger = LogManager.getLogger(AccountRegisterServlet.class);

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
        String email = request.getParameter("txtEmail");
        String name = request.getParameter("txtName");
        String password = request.getParameter("txtPassword");
        String confirm = request.getParameter("txtConfirm");
        AccountError error = new AccountError();
        boolean foundErr = false;
        String url = "register.jsp";

        try {
            //check all errors
            if (email.length() < 5 || email.length() > 50) {
                error.setEmailLengthErr("Email length must be from 5 to 50");
                foundErr = true;
            }

            String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
            if (!Pattern.matches(regex, email)) {
                error.setEmailFormatErr("Wrong email format");
                foundErr = true;
            }

            if (name.length() < 5 || name.length() > 20) {
                error.setNameLengthErr("Name length must be from 5 to 20");
                foundErr = true;
            }
            if (password.length() < 5 || password.length() > 20) {
                error.setPassLengthErr("Password length must be from 5 to 20");
                foundErr = true;
            }
            if (!confirm.equals(password)) {
                error.setConfirmErr("Password confirm is not correct");
                foundErr = true;
            }

            //if found err
            if (foundErr) {
                //set error to request scope
                request.setAttribute("ERROR", error);
            } //if not found err
            else {
                //encrypt password
                AccountDAO dao = new AccountDAO();
                String passwordEncrypted = Encryption.toHexString(Encryption.getSHA(password));

                //create account
                AccountDTO dto = new AccountDTO(email, name, passwordEncrypted, "1");
                boolean result = dao.createAccount(dto);

                //if created successfully, go to verify.html
                if (result) {
                    url = "verify.html";
                }
            }

        } catch (SQLException e) {
            //if sqlexception occurs (mean email is duplicated) and set err to request scope
            error.setEmailExistedErr("This email is existed");
            request.setAttribute("ERROR", error);

            //log
            log("AccountRegisterServlet SQLException " + e.getMessage());
            logger.error("AccountRegisterServlet SQLException " + e.getMessage());
        } catch (NamingException e) {
            log("AccountRegisterServlet NamingException " + e.getMessage());
            logger.error("AccountRegisterServlet NamingException " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log("AccountRegisterServlet NoSuchAlgorithmException " + e.getMessage());
            logger.error("AccountRegisterServlet NoSuchAlgorithmException " + e.getMessage());
        } catch (MessagingException e) {
            log("AccountRegisterServlet MessagingException " + e.getMessage());
            logger.error("AccountRegisterServlet MessagingException " + e.getMessage());
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

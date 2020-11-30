/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.dispatcher;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */

//add @MultipartConfig
@MultipartConfig(maxFileSize = 16177215)
public class DispatcherServlet extends HttpServlet {

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
        String button = request.getParameter("btnAction");
        String url = "login.html";
        HttpSession session = request.getSession(false);

        try {
            if (button == null) {

            } else if (button.equals("Register")) {
                url = "AccountRegisterServlet";
            } else if (button.equals("Login")) {
                url = "LoginServlet";
            } else if (button.equals("Verify")) {
                url = "AccountVerifyServlet";
            } else if (session != null) {
                if (button.equals("Search")) {
                    url = "SearchArticleServlet";
                }
                else if (button.equals("View")) {
                    url = "ViewDetailArticleServlet";
                }
                else if (button.equals("Like")) {
                    url = "LikePostServlet";
                }
                else if (button.equals("Dislike")) {
                    url = "DislikePostServlet";
                }
                else if (button.equals("Post")) {
                    url = "PostingServlet";
                }
                else if (button.equals("Comment")) {
                    url = "CommentingServlet";
                }
                else if (button.equals("DeletePost")) {
                    url = "DeletePostServlet";
                }
                else if (button.equals("DeleteComment")) {
                    url = "DeleteCommentServlet";
                }
                else if (button.equals("Notification")) {
                    url = "NotificationServlet";
                }
                else if (button.equals("Return")) {
                    url = "ServletBetweenJsp";
                }
                else if (button.equals("Logout")) {
                    url = "LogoutServlet";
                }
            }
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

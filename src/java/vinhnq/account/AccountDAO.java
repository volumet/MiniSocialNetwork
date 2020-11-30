/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.account;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import vinhnq.helpers.DBHelpers;
import vinhnq.util.SendingEmail;

/**
 *
 * @author Admin
 */
public class AccountDAO implements Serializable {

    public boolean createAccount(AccountDTO dto) throws NamingException, SQLException, MessagingException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "INSERT INTO Account(email, name, password, statusId) VALUES(?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, dto.getEmail());
            stm.setString(2, dto.getName());
            stm.setString(3, dto.getPassword());
            stm.setString(4, "1");

            int result = stm.executeUpdate();
            if (result > 0) {
                //send email
                SendingEmail send = new SendingEmail(dto.getEmail(), dto.getPassword());
                send.sendEmail();
                return true;
            }

        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public AccountDTO loginAccount(String username, String password) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT name, statusId FROM Account WHERE email=? AND password=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);

            rs = stm.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String statusId = rs.getString("statusId");
                AccountDTO dto = new AccountDTO(username, name, statusId);
                return dto;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public String activateAccount(String username, String password) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        String url = "verify_failed.html";

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT email FROM Account WHERE email=? AND password=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);

            rs = stm.executeQuery();
            if (rs.next()) {
                sql = "UPDATE Account SET statusId='2' WHERE email=? AND statusId='1'";
                stm = con.prepareStatement(sql);
                stm.setString(1, username);
                int result = stm.executeUpdate();
                if (result == 1) {
                    url = "login.html";
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return url;
    }
}

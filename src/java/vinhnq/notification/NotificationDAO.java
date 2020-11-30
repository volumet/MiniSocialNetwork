/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.notification;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.naming.NamingException;
import vinhnq.helpers.DBHelpers;

/**
 *
 * @author Admin
 */
public class NotificationDAO implements Serializable {
    public void pushNoti(String postId, String interactor, String detail, String action, Timestamp date, String status) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "INSERT INTO Notification(postId, interactor, detail, action, date, status) VALUES(?,?,?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, interactor);
            stm.setString(3, detail);            
            stm.setString(4, action);
            stm.setTimestamp(5, date);
            stm.setString(6, status);
            stm.executeUpdate();

        } finally {

            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
    
    public void changeNotiStatus(String newPostId, String interactor, String detail, String action, 
            Timestamp date, String newStatus, String oldPostId, String oldStatus) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "UPDATE Notification SET postId=?, interactor=?, detail=?, action=?, date=?, status=? WHERE postId=? AND status=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, newPostId);
            stm.setString(2, interactor);
            stm.setString(3, detail);            
            stm.setString(4, action);
            stm.setTimestamp(5, date);
            stm.setString(6, newStatus);
            stm.setString(7, oldPostId);
            stm.setString(8, oldStatus);
            stm.executeUpdate();

        } finally {

            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
    
    public void deactiveNoti(String newPostId, String newStatus, String oldPostId, String oldStatus) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "UPDATE Notification SET postId=?, status=? WHERE postId=? AND status=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, newPostId);
            stm.setString(2, newStatus);
            stm.setString(3, oldPostId);
            stm.setString(4, oldStatus);
            stm.executeUpdate();

        } finally {

            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
    
    public void deleteNoti(String postId, String status) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection(); 
            String sql = "DELETE FROM Notification WHERE postId=? AND status=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, status);
            stm.executeUpdate();

        } finally {

            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
    
    public NotificationDTO getNoti(String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        ResultSet rsNew;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT interactor, detail, action, date FROM Notification WHERE postId=? AND status=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, "Active");
            rs = stm.executeQuery();
            if (rs.next()) {
                String interactor = rs.getString("interactor");
                String detail = rs.getString("detail");
                String action = rs.getString("action");
                
                Timestamp timeStamp = rs.getTimestamp("date");
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(timeStamp);
                
                //select name from account table
                sql = "SELECT name FROM Account WHERE email=?";
                stm = con.prepareStatement(sql);
                stm.setString(1, interactor);

                rsNew = stm.executeQuery();
                if (rsNew.next()) {
                    String interactorName = rsNew.getString("name");
                    NotificationDTO dto = new NotificationDTO(postId, interactor, detail, action, date, "Active", interactorName);
                    return dto;
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
        return null;
    }
}

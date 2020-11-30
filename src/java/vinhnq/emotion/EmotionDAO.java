/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.emotion;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import vinhnq.helpers.DBHelpers;

/**
 *
 * @author Admin
 */
public class EmotionDAO implements Serializable {
    public void likePost(String emotioner, String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "INSERT INTO Emotion(postId, emotioner, type, status) VALUES(?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, emotioner);
            stm.setBoolean(3, true);
            stm.setString(4, "Active");

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
    
    public void dislikePost(String emotioner, String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "INSERT INTO Emotion(postId, emotioner, type, status) VALUES(?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, emotioner);
            stm.setBoolean(3, false);
            stm.setString(4, "Active");

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
    
    public boolean checkLiked(String emotioner, String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT type FROM Emotion WHERE postId=? AND emotioner=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, emotioner);

            rs = stm.executeQuery();
            
            while (rs.next()) {
                boolean type = rs.getBoolean("type");
                if (type == true)
                    return true;
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
        return false;
    }
    
    public boolean checkDisliked(String emotioner, String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT type FROM Emotion WHERE postId=? AND emotioner=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, emotioner);

            rs = stm.executeQuery();
            
            while (rs.next()) {
                boolean type = rs.getBoolean("type");
                if (type == false)
                    return true;
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
        return false;
    }
    
    public void delikePost(String emotioner, String postId, boolean type) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "DELETE FROM Emotion WHERE postId=? AND emotioner=? AND type=? AND status='Active'";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, emotioner);
            stm.setBoolean(3, type);

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
    
    public void deleteAllEmotionOfPost(String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "DELETE FROM Emotion WHERE postId=? AND status='Active'";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);

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
}

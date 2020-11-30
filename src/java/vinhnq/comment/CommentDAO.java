/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.comment;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import vinhnq.helpers.DBHelpers;

/**
 *
 * @author Admin
 */
public class CommentDAO implements Serializable {

    private List<CommentDTO> listComment;

    /**
     * @return the listComment
     */
    public List<CommentDTO> getListComment() {
        return listComment;
    }

    public void loadComment(String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        ResultSet rsNew = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT commentor, description, date, commentId "
                    + "FROM Comment "
                    + "WHERE postId=? AND status=? "
                    + "ORDER BY date DESC";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, "Active");
            rs = stm.executeQuery();
            while (rs.next()) {
                String commentor = rs.getString("commentor");
                String commentId = rs.getString("commentId");
                String description = rs.getString("description");
                
                Timestamp timeStamp = rs.getTimestamp("date");
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(timeStamp);

                sql = "SELECT name FROM Account WHERE email=?";
                stm = con.prepareStatement(sql);
                stm.setString(1, commentor);

                rsNew = stm.executeQuery();
                if (rsNew.next()) {
                    String commentorName = rsNew.getString("name");
                    CommentDTO dto = new CommentDTO(postId, commentId, commentor, description, date, commentorName);
                    if (listComment == null) {
                        listComment = new ArrayList<>();
                    }
                    listComment.add(dto);
                }

            }
        } finally {
            if (rsNew != null) {
                rsNew.close();
            }
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
    }

    public void postComment(String postId, String commentId, String commentor, String description, Timestamp date) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "INSERT INTO Comment(postId, commentor, description, date, status, commentId) VALUES(?,?,?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, commentor);
            stm.setString(3, description);
            stm.setTimestamp(4, date);
            stm.setString(5, "Active");
            stm.setString(6, commentId);
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

    public int getMaxCommentIdOfPost(String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT MAX(CAST(commentId AS Int)) AS maxCommentId FROM Comment WHERE postId=? AND status=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, "Active");
            rs = stm.executeQuery();
            if (rs.next()) {
                int maxCommentId = rs.getInt("maxCommentId");
                return maxCommentId;
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
        return 0;
    }

    public void deleteComment(String postId, String commentId, String maxDeleteCommentId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "UPDATE Comment SET status=?, commentId=? WHERE postId=? AND commentId=? AND status=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, "Delete");
            stm.setString(2, maxDeleteCommentId);
            stm.setString(3, postId);
            stm.setString(4, commentId);
            stm.setString(5, "Active");
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

    public int getMaxDeleteCommentId() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT MAX(CAST(commentId AS Int)) AS maxValue FROM Comment WHERE status=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, "Delete");

            rs = stm.executeQuery();
            if (rs.next()) {
                int maxPostId = rs.getInt("maxValue");
                return maxPostId;
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
        return 0;
    }
}

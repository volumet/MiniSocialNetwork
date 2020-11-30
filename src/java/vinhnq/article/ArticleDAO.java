/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.article;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import vinhnq.util.ReadImage;

/**
 *
 * @author Admin
 */
public class ArticleDAO implements Serializable {

    private List<ArticleDTO> listOfArticles;
    private List<String> listOfUpdatedArticles;

    /**
     * @return the listOfArticles
     */
    public List<ArticleDTO> getListOfArticles() {
        return listOfArticles;
    }

    public int searchResult(String searchContent) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT COUNT(ownerId) AS searchNum "
                    + "FROM Article "
                    + "WHERE description LIKE ? AND status=? ";
            stm = con.prepareStatement(sql);
            stm.setString(1, "%" + searchContent + "%");
            stm.setString(2, "Active");

            rs = stm.executeQuery();
            if (rs.next()) {
                int result = rs.getInt("searchNum");
                return result;
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

    public void searchArticle(String searchContent, int searchPage, int postPerPage) throws NamingException, SQLException, IOException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        ResultSet rsNew = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT ownerId, postId, title, description, image, date, likes, dislikes, updated "
                    + "FROM Article "
                    + "WHERE UPPER(description) LIKE ? AND status=? "
                    + "ORDER BY date DESC "
                    + "OFFSET ? ROWS "
                    + "FETCH NEXT ? ROWS ONLY";
            stm = con.prepareStatement(sql);
            stm.setString(1, "%" + searchContent + "%");
            stm.setString(2, "Active");
            stm.setInt(3, postPerPage * (searchPage - 1)); //offset
            stm.setInt(4, postPerPage); //fetch next

            rs = stm.executeQuery();
            while (rs.next()) {
                String ownerId = rs.getString("ownerId");
                String postId = rs.getString("postId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                //Image
                InputStream input = rs.getBinaryStream("image");
                String image = null;
                if (input != null) {
                    image = ReadImage.encode64(input);
                    input.close();
                }

                //----------------
                
                //get date string from timestamp
                Timestamp timeStamp = rs.getTimestamp("date");
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(timeStamp);
                //------------------------------------
                
                
                int likes = rs.getInt("likes");
                int dislikes = rs.getInt("dislikes");
                String updated = rs.getString("updated");

                //select name from account table
                sql = "SELECT name FROM Account WHERE email=?";
                stm = con.prepareStatement(sql);
                stm.setString(1, ownerId);

                rsNew = stm.executeQuery();
                if (rsNew.next()) {
                    String ownerName = rsNew.getString("name");
                    ArticleDTO dto = new ArticleDTO(ownerId, postId, title, description, image, date, likes, dislikes, updated, ownerName);
                    if (listOfArticles == null) {
                        listOfArticles = new ArrayList<>();
                    }
                    listOfArticles.add(dto);
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

    public ArticleDTO searchArticleById(String postId) throws NamingException, SQLException, UnsupportedEncodingException, IOException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        ResultSet rsNew = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT ownerId, title, description, image, date, likes, dislikes, updated FROM Article WHERE postId=? AND status=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, "Active");

            rs = stm.executeQuery();
            if (rs.next()) {
                String ownerId = rs.getString("ownerId");
                String title = rs.getString("title");
                String description = rs.getString("description");

                //Get image by base64 encode UTF-8 (getbyte to string type)
                InputStream input = rs.getBinaryStream("image");
                String image = null;
                if (input != null) {
                    image = ReadImage.encode64(input);
                    input.close();
                }

                //----------------
                Timestamp timeStamp = rs.getTimestamp("date");
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(timeStamp);
                
                
                int likes = rs.getInt("likes");
                int dislikes = rs.getInt("dislikes");
                String updated = rs.getString("updated");

                sql = "SELECT name FROM Account WHERE email=?";
                stm = con.prepareStatement(sql);
                stm.setString(1, ownerId);

                rsNew = stm.executeQuery();
                if (rsNew.next()) {
                    String ownerName = rsNew.getString("name");
                    ArticleDTO dto = new ArticleDTO(ownerId, postId, title, description, image, date, likes, dislikes, updated, ownerName);
                    return dto;
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
        return null;
    }

    public int searchLikeById(String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT likes FROM Article WHERE postId=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);

            rs = stm.executeQuery();
            if (rs.next()) {
                int likes = rs.getInt("likes");
                return likes;
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

    public int searchDislikeById(String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT dislikes FROM Article WHERE postId=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);

            rs = stm.executeQuery();
            if (rs.next()) {
                int dislikes = rs.getInt("dislikes");
                return dislikes;
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

    public void updateLikeById(String postId, int like) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "UPDATE Article SET likes=? WHERE postId=?";
            stm = con.prepareStatement(sql);

            stm.setInt(1, like);
            stm.setString(2, postId);
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

    public void updateDislikeById(String postId, int dislike) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "UPDATE Article SET dislikes=? WHERE postId=?";
            stm = con.prepareStatement(sql);

            stm.setInt(1, dislike);
            stm.setString(2, postId);
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

    public void uploadPost(String title, String ownerId, InputStream photo, String description, Timestamp date, int postId) throws NamingException, SQLException, IOException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "INSERT INTO Article(ownerId, postId, title, description, image, date, likes, dislikes, status, updated) VALUES(?,?,?,?,?,?,?,?,?,?)";
            stm = con.prepareStatement(sql);

            stm.setString(1, ownerId);
            stm.setInt(2, postId);
            stm.setString(3, title);
            stm.setString(4, description);
            stm.setBinaryStream(5, photo);
            stm.setTimestamp(6, date);
            stm.setInt(7, 0); //likes
            stm.setInt(8, 0); //dislikes
            stm.setString(9, "Active");
            stm.setString(10, "Normal");
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

    public int getMaxPostId() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT MAX(CAST(postId AS Int)) AS maxValue FROM Article WHERE status=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, "Active");

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

    public void deletePost(String postId, String deletePostId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "UPDATE Article SET status=?, postId=? WHERE postId=? AND status=?";
            stm = con.prepareStatement(sql);

            stm.setString(1, "Delete");
            stm.setString(2, deletePostId);
            stm.setString(3, postId);
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

    public int getMaxDeletePostId() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT MAX(CAST(postId AS Int)) AS maxValue FROM Article WHERE status=?";
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

    public void updatedNotiPost(String postId, String updated) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "UPDATE Article SET updated=? WHERE postId=? AND status=?";
            stm = con.prepareStatement(sql);

            stm.setString(1, updated);
            stm.setString(2, postId);
            stm.setString(3, "Active");
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

    public int showNumberOfUpdatedArticles(String ownerId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT COUNT(updated) AS updatedPosts FROM Article WHERE ownerId=? AND updated=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, ownerId);
            stm.setString(2, "Updated");

            rs = stm.executeQuery();
            if (rs.next()) {
                int updatedPosts = rs.getInt("updatedPosts");
                return updatedPosts;
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

    /**
     * @return the listOfUpdatedArticles
     */
    public List<String> getListOfUpdatedArticles() {
        return listOfUpdatedArticles;
    }

    public void loadListOfUpdatedArticles(String ownerId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT postId "
                    + "FROM Article "
                    + "WHERE ownerId=? AND updated=? AND status=? ";
            stm = con.prepareStatement(sql);
            stm.setString(1, ownerId);
            stm.setString(2, "Updated");
            stm.setString(3, "Active");

            rs = stm.executeQuery();
            while (rs.next()) {
                String postId = rs.getString("postId");
                if (listOfUpdatedArticles == null) {
                    listOfUpdatedArticles = new ArrayList<>();
                }
                listOfUpdatedArticles.add(postId);
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
    }

    public String searchOwnerByPostId(String postId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            String sql = "SELECT ownerId FROM Article WHERE postId=? AND status=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, postId);
            stm.setString(2, "Active");

            rs = stm.executeQuery();
            if (rs.next()) {
                String ownerId = rs.getString("ownerId");
                return ownerId;
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

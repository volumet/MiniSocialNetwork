/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.comment;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author Admin
 */
public class CommentDTO implements Serializable {
    private String postId;
    private String commentId;
    private String commentor;
    private String description;
    private String date;
    private String commentorName;

    public CommentDTO() {
    }

    public CommentDTO(String postId, String commentId, String commentor, String description, String date, String commentorName) {
        this.postId = postId;
        this.commentId = commentId;
        this.commentor = commentor;
        this.description = description;
        this.date = date;
        this.commentorName = commentorName;
    }
    
    
    /**
     * @return the postId
     */
    public String getPostId() {
        return postId;
    }

    /**
     * @param postId the postId to set
     */
    public void setPostId(String postId) {
        this.postId = postId;
    }

    /**
     * @return the commentor
     */
    public String getCommentor() {
        return commentor;
    }

    /**
     * @param commentor the commentor to set
     */
    public void setCommentor(String commentor) {
        this.commentor = commentor;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the commentId
     */
    public String getCommentId() {
        return commentId;
    }

    /**
     * @param commentId the commentId to set
     */
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    /**
     * @return the commentorName
     */
    public String getCommentorName() {
        return commentorName;
    }

    /**
     * @param commentorName the commentorName to set
     */
    public void setCommentorName(String commentorName) {
        this.commentorName = commentorName;
    }
}

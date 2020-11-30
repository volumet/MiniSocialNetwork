/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.emotion;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class EmotionDTO implements Serializable {
    private String postId;
    private String emotioner;
    private boolean type;
    private String status;

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
     * @return the emotioner
     */
    public String getEmotioner() {
        return emotioner;
    }

    /**
     * @param emotioner the emotioner to set
     */
    public void setEmotioner(String emotioner) {
        this.emotioner = emotioner;
    }

    /**
     * @return the type
     */
    public boolean isType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(boolean type) {
        this.type = type;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    
}

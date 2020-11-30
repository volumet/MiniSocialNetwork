/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.notification;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class NotificationDTO implements Serializable {
    private String postId;
    private String interactor;
    private String detail;
    private String action;
    private String date;
    private String status;
    private String interactorName;

    public NotificationDTO() {
    }

    public NotificationDTO(String postId, String interactor, String detail, String action, String date, String status, String interatorName) {
        this.postId = postId;
        this.interactor = interactor;
        this.detail = detail;
        this.action = action;
        this.date = date;
        this.status = status;
        this.interactorName = interatorName;
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
     * @return the interactor
     */
    public String getInteractor() {
        return interactor;
    }

    /**
     * @param interactor the interactor to set
     */
    public void setInteractor(String interactor) {
        this.interactor = interactor;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
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

    /**
     * @return the interactorName
     */
    public String getInteractorName() {
        return interactorName;
    }

    /**
     * @param interactorName the interactorName to set
     */
    public void setInteractorName(String interactorName) {
        this.interactorName = interactorName;
    }
}

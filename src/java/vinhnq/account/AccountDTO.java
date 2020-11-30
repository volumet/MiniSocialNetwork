/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.account;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class AccountDTO implements Serializable {
    private String email;
    private String name;
    private String password;
    private String statusId;

    public AccountDTO() {
    }

    public AccountDTO(String email, String name, String password, String statusId) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.statusId = statusId;
    }
    
    public AccountDTO(String name) {
        this.name = name;
    }
    
    public AccountDTO(String email, String name, String status) {
        this.email = email;
        this.name = name;
        this.statusId = status;
    }
    
    
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the statusId
     */
    public String getStatusId() {
        return statusId;
    }

    /**
     * @param statusId the statusId to set
     */
    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
}

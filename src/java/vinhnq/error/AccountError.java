/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.error;

/**
 *
 * @author Admin
 */
public class AccountError {

    private String emailExistedErr;
    private String emailLengthErr;
    private String nameLengthErr;
    private String passLengthErr;
    private String confirmErr;
    private String emailFormatErr;

    /**
     * @return the emailExistedErr
     */
    public String getEmailExistedErr() {
        return emailExistedErr;
    }

    /**
     * @param emailExistedErr the emailExistedErr to set
     */
    public void setEmailExistedErr(String emailExistedErr) {
        this.emailExistedErr = emailExistedErr;
    }

    /**
     * @return the emailLengthErr
     */
    public String getEmailLengthErr() {
        return emailLengthErr;
    }

    /**
     * @param emailLengthErr the emailLengthErr to set
     */
    public void setEmailLengthErr(String emailLengthErr) {
        this.emailLengthErr = emailLengthErr;
    }

    /**
     * @return the nameLengthErr
     */
    public String getNameLengthErr() {
        return nameLengthErr;
    }

    /**
     * @param nameLengthErr the nameLengthErr to set
     */
    public void setNameLengthErr(String nameLengthErr) {
        this.nameLengthErr = nameLengthErr;
    }

    /**
     * @return the passLengthErr
     */
    public String getPassLengthErr() {
        return passLengthErr;
    }

    /**
     * @param passLengthErr the passLengthErr to set
     */
    public void setPassLengthErr(String passLengthErr) {
        this.passLengthErr = passLengthErr;
    }

    /**
     * @return the confirmErr
     */
    public String getConfirmErr() {
        return confirmErr;
    }

    /**
     * @param confirmErr the confirmErr to set
     */
    public void setConfirmErr(String confirmErr) {
        this.confirmErr = confirmErr;
    }

    /**
     * @return the emailFormatErr
     */
    public String getEmailFormatErr() {
        return emailFormatErr;
    }

    /**
     * @param emailFormatErr the emailFormatErr to set
     */
    public void setEmailFormatErr(String emailFormatErr) {
        this.emailFormatErr = emailFormatErr;
    }
}

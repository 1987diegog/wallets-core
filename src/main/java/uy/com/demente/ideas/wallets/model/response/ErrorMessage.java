package uy.com.demente.ideas.wallets.model.response;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 1987diegog
 */
public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
    private Date timestamp;

    public ErrorMessage() {
    }

    public ErrorMessage(String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

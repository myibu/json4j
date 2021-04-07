package org.github.myibu.json;
/**
 * @author myibu
 * @since 1.0
 */
public class JSONException extends RuntimeException {
    public JSONException() {
    }

    public JSONException(String message) {
        super(message);
    }


    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONException(Throwable cause) {
        super(cause);
    }
}

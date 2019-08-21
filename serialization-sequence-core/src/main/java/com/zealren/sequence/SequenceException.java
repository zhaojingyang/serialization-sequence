package com.zealren.sequence;

public class SequenceException extends Exception {
    protected String errorCode;

    public static SequenceException SERVER_ERROR = new SequenceException("500", "系统问题");


    public SequenceException(String message) {
        super(message);
        this.errorCode = SERVER_ERROR.getErrorCode();
    }

    public SequenceException(String errorCode, String message) {
        this(errorCode, message, false);
    }

    public SequenceException(String errorCode, String message, boolean propertiesKey) {
        super(message);
        setErrorCode(errorCode);
    }

    public SequenceException(Throwable cause) {
        super(cause);
    }

    public SequenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SequenceException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.setErrorCode(errorCode);
    }

    public SequenceException(String errorCode, String message,
                             Throwable cause, boolean propertiesKey) {
        super(message, cause);
        this.setErrorCode(errorCode);
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}

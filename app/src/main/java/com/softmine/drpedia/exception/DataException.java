package com.softmine.drpedia.exception;

public class DataException extends Exception{

    String message;
    public DataException()
    {

    }

    public DataException(String message)
    {
        super(message);
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }
}

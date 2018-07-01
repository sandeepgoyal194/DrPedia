package frameworks.network.model;

import java.io.IOException;

public class ResponseException extends IOException {
    public ResponseException(String message) {
        super(message);
    }

}
package taskmanager.server;

public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    NOT_FOUND(404),
    NOT_ACCEPTABLE(406);

    private final int code;

    HttpStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
package com.github.frunoyman.shell;

public abstract class Shell {
    protected final String ADAPTER_PATTERN = "(.*data=\")(.*)(\".*)";
    protected final int ERROR_CODE = 123;
    protected final int SUCCESS_CODE = 373;
    protected final int EMPTY_BROADCAST_CODE = 0;

    public abstract String execute(String... command) throws Exception;

    public abstract String getSerial();

}

package com.github.frunoyman.shell;

public abstract class Shell {
    protected final String ADAPTER_PATTERN = "(.*data=\")(.*)(\".*)";
    protected final int ERROR_CODE = 123;

    public abstract String execute(String... command) throws Exception;
}

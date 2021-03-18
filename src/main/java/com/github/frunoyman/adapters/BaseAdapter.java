package com.github.frunoyman.adapters;

import com.github.frunoyman.shell.Shell;

public class BaseAdapter {
    protected Shell shell;
    protected final String BROADCAST = "am broadcast -a ";
    protected final String ES = " --es ";

    public BaseAdapter(Shell shell) {
        this.shell = shell;
    }
}

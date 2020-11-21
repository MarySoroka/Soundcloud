package com.soundcloud.builder;

/**
 * Builder exception
 */
public class BuildException extends Exception {

    public BuildException() {
    }

    public BuildException(String s) {
        super(s);
    }

    public BuildException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BuildException(Throwable throwable) {
        super(throwable);
    }

    public BuildException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}

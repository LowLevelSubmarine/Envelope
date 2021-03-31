package com.lowlevelsubmarine.envelope.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Executor {

    private String javaHome;
    private String javaBin;
    private File jar;
    private List<String> jvmArgs = new ArrayList<>();
    private List<String> args = new ArrayList<>();

    public Executor(File jar) {
        this.javaHome = System.getProperty("java.home");
        this.javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        this.jar = jar;
    }

    public void setJVMArgs(List<String> jvmArgs) {
        this.jvmArgs = jvmArgs;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public void exec() throws IOException {
        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.addAll(jvmArgs);
        command.add("-jar");
        command.add(this.jar.getAbsolutePath());
        command.addAll(args);
        new ProcessBuilder(command).start();
    }

}

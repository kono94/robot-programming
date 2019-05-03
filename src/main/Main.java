package main;

import lib.Controller;


public class Main {
    public static void main(String[] args) {
        boolean isRunningOnDevice =  System.getProperty("os.arch").toLowerCase().matches("arm");
        Controller c = new Controller(isRunningOnDevice);
        c.init();
        c.followLine();
    }
}

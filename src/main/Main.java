package main;


import lejos.hardware.Sound;
import lejos.utility.Delay;
import lib.Controller;

import java.io.File;


public class Main {
    public static void main(String[] args) {
        boolean isRunningOnDevice =  System.getProperty("os.arch").toLowerCase().matches("arm");
        Controller c = new Controller(isRunningOnDevice);
        c.init();
        c.followLine();
    }
}

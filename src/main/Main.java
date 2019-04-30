package main;


import lib.Controller;


public class Main {
    public static void main(String[] args) {
        boolean isEV3 =  System.getProperty("os.arch").toLowerCase().matches("arm");
        Controller c = new Controller(!isEV3);
        c.init();
        c.followLine();
    }
}

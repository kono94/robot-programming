package de.scr;

import lejos.hardware.Battery;
import de.scr.lib.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        boolean isRunningOnDevice = System.getProperty("os.arch").toLowerCase().matches("arm");
        Controller c = new Controller(isRunningOnDevice);
        c.init();
//        c.followLine();
//        c.holdDistance();
        c.evadeObstacle();
        c.registerShutdownOnClick();
        logger.info("battery: " + Battery.getVoltage());
    }
}

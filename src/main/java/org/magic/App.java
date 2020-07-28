package org.magic;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App {
    public static void main(String[] args) throws AWTException {
        setLoggingLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            ex.printStackTrace();

            System.exit(1);
        }

        Robot robot = new Robot();
        ExecutorService service = Executors.newCachedThreadPool();
        GlobalListener listener = new GlobalListener(robot, service);

        GlobalScreen.addNativeKeyListener(listener);
    }

    public static void setLoggingLevel(Level level) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(level);

        Arrays.stream(Logger.getLogger("").getHandlers())
                .forEach(handler -> handler.setLevel(level));
    }
}

package org.magic;

import org.jnativehook.GlobalScreen;
import org.magic.controller.GlobalListener;
import org.magic.service.ActionService;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static void run() throws AWTException {
        // props
        // beans
        // injection

        setLoggingLevel(Level.OFF);

        Robot robot = new Robot();
        ExecutorService executorService = Executors.newCachedThreadPool();
        ActionService actionService = new ActionService(robot, executorService);
        GlobalListener listener = new GlobalListener(actionService);

        GlobalScreen.addNativeKeyListener(listener);
    }

    public static void setLoggingLevel(Level level) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(level);

        Arrays.stream(Logger.getLogger("").getHandlers())
                .forEach(handler -> handler.setLevel(level));
    }
}

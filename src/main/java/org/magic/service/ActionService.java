package org.magic.service;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.magic.App;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

// service
public class ActionService {

    private final Robot robot;
    private final ExecutorService executor;

    public HashMap<Integer, Runnable> pressActions;
    public HashMap<Integer, Runnable> releaseActions;

    public ActionService(Robot robot, ExecutorService executor) {
        this.robot = robot;
        this.executor = executor;

        pressActions = new HashMap<Integer, Runnable>(){{
            put(KeyEvent.VK_ESCAPE, () -> {
                try {
                    GlobalScreen.unregisterNativeHook();
                    executor.shutdownNow();
                    System.out.println("Exit...");
                } catch (NativeHookException ex) {
                    ex.printStackTrace();
                }
            });

            put(KeyEvent.VK_SPACE, () ->
                App.setLoggingLevel(Level.ALL)
            );

            put(KeyEvent.VK_N, () ->
                App.setLoggingLevel(Level.OFF)
            );

            put(KeyEvent.VK_E, () -> {
                try {
                    Thread.sleep(App.properties.get("delay"));
                    robot.keyPress(KeyEvent.VK_S);
                    robot.keyRelease(KeyEvent.VK_S);
                } catch(InterruptedException ignored) {}
            });
        }};

        releaseActions = new HashMap<Integer, Runnable>(){{
        }};
    }

    public void keyPress(KeyEvent keyEvent) {
        if (pressActions.containsKey(keyEvent.getKeyCode()))
            executor.submit(pressActions.get(keyEvent.getKeyCode()));
    }

    public void keyRelease(KeyEvent keyEvent) {
        if (releaseActions.containsKey(keyEvent.getKeyCode()))
            executor.submit(releaseActions.get(keyEvent.getKeyCode()));
    }
}

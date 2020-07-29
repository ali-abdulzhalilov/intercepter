package org.magic.service;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.magic.App;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

// service
public class ActionService {

    private final Robot robot;
    private final ExecutorService executor;

    public ActionService(Robot robot, ExecutorService executor) {
        this.robot = robot;
        this.executor = executor;
    }

    public void keyPress(int keyCode) {
        if (keyCode == NativeKeyEvent.VC_ESCAPE) {
            executor.submit(() -> {
                try {
                    GlobalScreen.unregisterNativeHook();
                    executor.shutdownNow();
                    System.out.println("Exit...");
                } catch (NativeHookException ex) {
                    ex.printStackTrace();
                }
            });
        }

        if (keyCode == NativeKeyEvent.VC_SPACE) {
            App.setLoggingLevel(Level.ALL);
        }

        if (keyCode == NativeKeyEvent.VC_N) {
            App.setLoggingLevel(Level.OFF);
        }

        if (keyCode == NativeKeyEvent.VC_E) {
            executor.submit(() -> {
                try {
                    Thread.sleep(1000);
                    robot.keyPress(KeyEvent.VK_S);
                    robot.keyRelease(KeyEvent.VK_S);
                } catch(InterruptedException ignored) {}
            });
        }
    }

    public void keyRelease(int keyCode) {

    }
}

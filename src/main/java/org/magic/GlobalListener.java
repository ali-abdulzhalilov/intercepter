package org.magic;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

public class GlobalListener implements NativeKeyListener {

    private final Robot robot;
    private final ExecutorService service;

    public GlobalListener(Robot robot, ExecutorService service) {
        this.robot = robot;
        this.service = service;
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            service.submit(() -> {
                try {
                    GlobalScreen.unregisterNativeHook();
                    service.shutdownNow();
                    System.out.println("Exit...");
                } catch (NativeHookException ex) {
                    ex.printStackTrace();
                }
            });
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
            App.setLoggingLevel(Level.ALL);
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_N) {
            App.setLoggingLevel(Level.OFF);
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_E) {
            service.submit(() -> {
                try {
                    Thread.sleep(1000);
                    robot.keyPress(KeyEvent.VK_S);
                    robot.keyRelease(KeyEvent.VK_S);
                } catch(InterruptedException ignored) {}
            });
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.println("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }
}

package org.magic;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App {
    public static void main(String[] args) {
        setLoggingLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            ex.printStackTrace();

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new GlobalListener());
    }

    public static void setLoggingLevel(Level level) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(level);

        Arrays.stream(Logger.getLogger("").getHandlers())
                .forEach(handler -> handler.setLevel(level));
    }
}

class GlobalListener implements NativeKeyListener {
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ex) {
                ex.printStackTrace();
            }
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
            App.setLoggingLevel(Level.ALL);
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_N) {
            App.setLoggingLevel(Level.OFF);
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.println("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }
}

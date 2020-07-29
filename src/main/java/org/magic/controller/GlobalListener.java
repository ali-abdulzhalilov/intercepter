package org.magic.controller;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.magic.service.ActionService;

// controller
public class GlobalListener implements NativeKeyListener {

    private final ActionService service;

    public GlobalListener(ActionService service) {
        this.service = service;

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            ex.printStackTrace();

            System.exit(1);
        }
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        service.keyPress(e.getKeyCode());
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        service.keyRelease(e.getKeyCode());
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.println("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }
}

package org.magic.controller;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.SwingKeyAdapter;
import org.magic.service.ActionService;

// controller
public class GlobalListener extends SwingKeyAdapter {

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

    public void nativeKeyPressed(NativeKeyEvent event) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(event.getKeyCode()));

        service.keyPress(getJavaKeyEvent(event));
    }

    public void nativeKeyReleased(NativeKeyEvent event) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(event.getKeyCode()));

        service.keyRelease(getJavaKeyEvent(event));
    }
}

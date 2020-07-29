package org.magic.service;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.magic.App;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

// service
public class ActionService {

    private final Robot robot;
    private final ExecutorService executor;

    public ActionService(Robot robot, ExecutorService executor) {
        this.robot = robot;
        this.executor = executor;
    }

    public void keyPress(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            executor.submit(this::haltProgram);
        }

        String triggerKey = App.properties.get("trigger_key");
        String keyText = KeyEvent.getKeyText(keyEvent.getKeyCode());
        if (keyText.equals(triggerKey))
            executor.submit(() -> {
                String responseKey = App.properties.get("response_key");
                try {
                    int delay = App.properties.get("delay");
                    Thread.sleep(delay);

                    int keyCode = textToKeyCode(responseKey);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } catch (IllegalAccessException ex) {
                    System.out.println(String.format("Нажимаемая клавиша [%s] не найдена", responseKey));
                    haltProgram();
                } catch (Exception ignored) {}
            });
    }

    public void keyRelease(KeyEvent keyEvent) {
        // do nothing
    }

    private void haltProgram() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        executor.shutdownNow();
        System.out.println("Exit...");
    }

    private int textToKeyCode(String text) throws IllegalAccessException {
        Field[] declaredFields = KeyEvent.class.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(declaredFields).filter(field -> {
            int mod = field.getModifiers();
            return Modifier.isStatic(mod) && Modifier.isFinal(mod) && field.getType().isPrimitive();
        }).collect(Collectors.toList());

        for (Field field : fieldList) {
            int keyCode = field.getInt(Field.class);
            String keyText = KeyEvent.getKeyText(keyCode);

            if (keyText.equals(text)) {
                return keyCode;
            }
        }

        return -1;
    }
}

package org.magic;

import org.jnativehook.GlobalScreen;
import org.magic.controller.GlobalListener;
import org.magic.service.ActionService;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static Properties properties;

    public static void run(String[] args) throws AWTException {
        prepareProperties(args);

        setLoggingLevel(App.properties.get("log_level"));

        Robot robot = new Robot();
        ExecutorService executorService = Executors.newCachedThreadPool();
        ActionService actionService = new ActionService(robot, executorService);
        GlobalListener listener = new GlobalListener(actionService);

        GlobalScreen.addNativeKeyListener(listener);
    }

    private static void prepareProperties(String[] args) {
        properties = new Properties();
        // default props
        Map<String, Object> defaultProperties = new HashMap<String, Object>(){{
            put("trigger_key", "E");
            put("response_key", "S");
            put("delay", 1000);
            put("log_level", Level.OFF);
        }};
        defaultProperties.forEach((key, value) -> properties.set(key, value));

        // props
        List<Map.Entry<String, Class<?>>> propsOrder = Arrays.asList(
                new AbstractMap.SimpleImmutableEntry<>("delay", Integer.class),
                new AbstractMap.SimpleImmutableEntry<>("trigger_key", String.class),
                new AbstractMap.SimpleImmutableEntry<>("response_key", String.class),
                new AbstractMap.SimpleImmutableEntry<>("log_level", Level.class)
        );

        for (int i = 0; i < args.length; i++) {
            Map.Entry<String, Class<?>> propData = propsOrder.get(i);
            properties.set(propData.getKey(), args[i], propData.getValue());
        }
    }

    public static void setLoggingLevel(Level level) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(level);

        Arrays.stream(Logger.getLogger("").getHandlers())
                .forEach(handler -> handler.setLevel(level));
    }

    public static class Properties {
        private Map<String, Object> stuff = new HashMap<>();

        public void set(String key, Object value) {
            stuff.put(key, value);
        }

        public void set(String key, String value, Class<?> asType) {
            if (asType.isAssignableFrom(String.class)) {
                set(key, value);
            } else if (asType.isAssignableFrom(Integer.class)) {
                set(key, Integer.valueOf(value));
            } else if (asType.isAssignableFrom(Boolean.class)) {
                set(key, Boolean.valueOf(value));
            } else if (asType.isAssignableFrom(Double.class)) {
                set(key, Double.valueOf(value));
            } else if (asType.equals(Level.class)) {
                set(key, Level.parse(value));
            } else {
                throw new IllegalArgumentException("Bad type.");
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key) {
            Object thing = stuff.get(key);
            Class<?> klass = thing.getClass();
            return (T) klass.cast(thing);
        }
    }
}

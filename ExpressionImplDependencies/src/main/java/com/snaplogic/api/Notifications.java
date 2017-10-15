package com.snaplogic.api;

import com.snaplogic.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author tstack
 */
public class Notifications {
    private static final Logger LOG = LoggerFactory.getLogger(Notifications.class);
    private static final Map<String, Integer> LINTS = new HashMap<>();

    /**
     * Register the lint definitions found in the given class.
     *
     * @param cl
     */
    public static void register(Class cl) {
        while (cl != null && cl != Object.class) {
            registerInternal(cl);
            cl = cl.getSuperclass();
        }
    }

    private static void registerInternal(Class cl) {
        for (Field field : cl.getDeclaredFields()) {
            try {
                com.snaplogic.api.Notification note = field.getAnnotation(Notification.class);
                if (note == null) {
                    continue;
                }

                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    throw new IllegalArgumentException(
                            "Field annotated with @Notification is not static");
                }

                field.setAccessible(true);
                Object definitionObj = field.get(null);
                if (!(definitionObj instanceof NotificationDefinition)) {
                    throw new IllegalArgumentException("Field annotated with @Notification is not" +
                            " a subclass of NotificationDefinition");
                }

                NotificationDefinition definition = (NotificationDefinition) field.get(null);

                if (definition.name != null) {
                    continue;
                }

                String noteName = String.format("%s.%s", cl.getCanonicalName(), field.getName());

                definition.name = noteName;
                if (note != null) {
                    definition.message = note.message();
                    definition.reason = note.reason();
                    definition.resolution = note.resolution();
                }

                if (field.getType() == Lint.class) {
                    Lint lm = (Lint) field.get(null);

                    synchronized (LINTS) {
                        Integer ord = LINTS.get(noteName);
                        if (ord == null) {
                            ord = LINTS.size();
                            LINTS.put(noteName, ord);
                        }
                        lm.ord = ord;
                    }
                    LOG.info("Found lint {}; ordinal={}", noteName, lm.ord);
                }
            } catch (IllegalAccessException e) {
                throw new ExecutionException(e, "Unable to read notification definition field: %s")
                        .formatWith(field)
                        .withResolutionAsDefect();
            }
        }
    }

    /**
     * Get the Definition of a notification.
     *
     * @param name The name of the notification.  This value should be the fully-qualified name of
     *             the NotificationDefinition static field.
     * @param notificationType The type of notification.
     * @param <T> The type of notification
     * @return The definition of the notification, if it could be found.
     */
    public static <T extends NotificationDefinition> Optional<T> getDefinition(String name,
                                                                               Class<T> notificationType) {
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1) {
            return Optional.empty();
        }
        String className = name.substring(0, lastDot);
        try {
            Class cl = Class.forName(className);

            Field field = cl.getDeclaredField(name.substring(lastDot + 1));
            field.setAccessible(true);
            if (field.getType() == notificationType) {
                return Optional.of((T) field.get(null));
            }
        } catch (ClassNotFoundException|NoSuchFieldException|IllegalAccessException e) {
            LOG.warn("Unable to find lint: {}", className, e);
        }
        return Optional.empty();
    }
}


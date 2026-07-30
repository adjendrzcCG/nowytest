package com.example;

import java.util.List;

/**
 * A simple HelloWorld application demonstrating modern Java 21 idioms:
 * <ul>
 *   <li>Records (Java 16) for immutable data carriers</li>
 *   <li>Text blocks (Java 15) for readable multi-line strings</li>
 *   <li>Local-variable type inference with {@code var} (Java 10)</li>
 *   <li>Enhanced switch expressions (Java 14)</li>
 *   <li>Factory methods like {@link List#of} (Java 9+)</li>
 * </ul>
 */
public class HelloWorld {

    /**
     * An immutable data carrier for a greeting.
     * Records automatically generate a canonical constructor, accessors,
     * {@code equals}, {@code hashCode}, and {@code toString}.
     */
    public record Greeting(String name, String message) {

        /** Compact constructor: validates inputs before the record is created. */
        public Greeting {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Name must not be blank");
            }
            if (message == null || message.isBlank()) {
                throw new IllegalArgumentException("Message must not be blank");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Core logic
    // -------------------------------------------------------------------------

    /** Returns a greeting string for the given name. */
    public static String greet(String name) {
        return "Hello, " + name + "!";
    }

    /** Builds a {@link Greeting} record for the given name. */
    public static Greeting buildGreeting(String name) {
        return new Greeting(name, greet(name));
    }

    /**
     * Classifies a name by length using a switch expression (Java 14+).
     *
     * @param name the name to classify
     * @return a short descriptor
     */
    public static String classifyName(String name) {
        return switch (name.length()) {
            case 1, 2, 3 -> "short";
            case 4, 5, 6 -> "medium";
            default       -> "long";
        };
    }

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        // var: local-variable type inference (Java 10+)
        var names = List.of("World", "Java 21", "Alice");

        for (var name : names) {
            var greeting = buildGreeting(name);
            var kind     = classifyName(name);

            // Text block (Java 15+): preserves indentation, no escape soup
            var output = """
                    Name    : %s  (%s name)
                    Message : %s
                    """.formatted(greeting.name(), kind, greeting.message());

            System.out.print(output);
        }
    }
}

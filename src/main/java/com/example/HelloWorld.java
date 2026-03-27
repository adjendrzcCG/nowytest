package com.example;

/**
 * Simple greeting application demonstrating modern Java 21 features:
 * <ul>
 *   <li>Record types (Java 16) for immutable data carriers</li>
 *   <li>{@code String.formatted()} (Java 15) instead of string concatenation</li>
 *   <li>Local-variable type inference with {@code var} (Java 10)</li>
 * </ul>
 */
public class HelloWorld {

    /**
     * Immutable value type that pairs a name with the greeting message it produces.
     * Records automatically generate a canonical constructor, accessors, {@code equals},
     * {@code hashCode}, and {@code toString}.
     */
    record Greeting(String name) {
        /** Returns the formatted greeting message for this name. */
        String message() {
            return "Hello, %s!".formatted(name);
        }
    }

    /**
     * Builds a greeting message for the given name.
     *
     * @param name the name to greet
     * @return a greeting string, e.g. {@code "Hello, World!"}
     */
    public static String greet(String name) {
        var greeting = new Greeting(name);
        return greeting.message();
    }

    public static void main(String[] args) {
        var message = greet("World");
        System.out.println(message);
    }
}

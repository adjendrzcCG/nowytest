package com.example;

import java.util.List;

public class HelloWorld {

    /**
     * Simple record acting as a data carrier for a greeting result (Java 16+).
     */
    public record Greeting(String recipient, String message) {}

    /**
     * Returns a greeting message for the given name.
     * Uses String.formatted() (Java 15+) instead of string concatenation.
     */
    public static String greet(String name) {
        // Use strip() (Java 11+) to trim whitespace, isBlank() to guard against empty input
        var trimmed = name.strip();
        if (trimmed.isBlank()) {
            trimmed = "World";
        }
        return "Hello, %s!".formatted(trimmed);
    }

    /**
     * Builds a Greeting record for the given name using pattern matching
     * and an enhanced switch expression (Java 14+).
     */
    public static Greeting buildGreeting(Object nameInput) {
        // instanceof pattern matching (Java 16+)
        if (nameInput instanceof String name) {
            var message = greet(name);
            return new Greeting(name.strip().isBlank() ? "World" : name.strip(), message);
        }
        return new Greeting("World", greet("World"));
    }

    /**
     * Returns a formatted multi-line summary using a text block (Java 15+).
     */
    public static String summary(List<String> names) {
        // Stream + lambda instead of traditional for-loop (Java 8+)
        var greetings = names.stream()
                .map(HelloWorld::greet)
                .toList(); // toList() shorthand (Java 16+)

        // Text block (Java 15+)
        var header = """
                === Greetings ===
                """;
        return header + String.join("\n", greetings);
    }

    /**
     * Describes a language tier using an enhanced switch expression (Java 14+).
     */
    public static String describeLanguage(String language) {
        return switch (language) {
            case "Java"   -> "Strongly typed, JVM-based language";
            case "Python" -> "Dynamically typed, interpreted language";
            case "Kotlin" -> "Modern JVM language with null safety";
            default       -> "Unknown language: " + language;
        };
    }

    public static void main(String[] args) {
        // var for local variable type inference (Java 10+)
        var greeting = greet("World");
        System.out.println(greeting);

        var names = List.of("Alice", "Bob", "Charlie");
        System.out.println(summary(names));

        var g = buildGreeting("Java");
        System.out.println("Recipient: " + g.recipient() + ", Message: " + g.message());

        System.out.println(describeLanguage("Java"));
    }
}

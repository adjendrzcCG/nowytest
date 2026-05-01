package com.example;

/**
 * Simple greeting application modernized for Java 21.
 *
 * <p>Demonstrates several language features introduced after Java 8:
 * <ul>
 *   <li>{@link Greeting} – a <em>record</em> (Java 16) for immutable data carriers</li>
 *   <li>{@link #greet(String)} – {@code String.formatted()} (Java 15) instead of concatenation</li>
 *   <li>{@link #main} – {@code var} (Java 10) and a switch <em>expression</em> (Java 14)</li>
 * </ul>
 */
public class HelloWorld {

    /**
     * Immutable value object that pairs a recipient name with the greeting text.
     * Records (Java 16) automatically generate constructor, accessors, equals, hashCode and toString.
     */
    public record Greeting(String name, String message) {}

    /**
     * Returns a personalised greeting for the given name.
     *
     * @param name the recipient's name; must not be {@code null}
     * @return a greeting string in the form {@code "Hello, <name>!"}
     */
    public static String greet(String name) {
        // String.formatted() (Java 15) is the instance-method equivalent of String.format()
        return "Hello, %s!".formatted(name);
    }

    /**
     * Builds a {@link Greeting} record from a name, keeping data and behaviour separate.
     *
     * @param name the recipient's name
     * @return a {@link Greeting} record holding the name and the computed message
     */
    public static Greeting buildGreeting(String name) {
        return new Greeting(name, greet(name));
    }

    public static void main(String[] args) {
        // var (Java 10) – type inferred as String[]
        var recipients = new String[]{"World", "Java 21", "Records"};

        for (var recipient : recipients) {
            var greeting = buildGreeting(recipient);

            // Switch expression (Java 14) returning a value
            var emoji = switch (recipient) {
                case "World"    -> "🌍";
                case "Java 21"  -> "☕";
                default         -> "👋";
            };

            System.out.println(emoji + " " + greeting.message());
        }
    }
}

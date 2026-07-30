package com.example;

/**
 * Simple Hello World application modernised for Java 21.
 *
 * <p>Modernisation highlights:
 * <ul>
 *   <li>{@link Greeting} — Java 16+ {@code record} replaces a plain data-holding class.</li>
 *   <li>Text block — Java 15+ multi-line string literal used in {@link #buildReport}.</li>
 *   <li>{@code var} — Java 10+ local-variable type inference used in {@code main}.</li>
 *   <li>Switch expression — Java 14+ expression form used in {@link #formalGreet}.</li>
 *   <li>Formatted string — {@code String.formatted()} (Java 15+) replaces concatenation in
 *       {@link #greet}.</li>
 * </ul>
 */
public class HelloWorld {

    // ── Java 16+ record ────────────────────────────────────────────────────────
    /** Immutable value object that carries a greeting message. */
    public record Greeting(String message) {
        /** Compact constructor — validates that the message is never blank. */
        public Greeting {
            if (message == null || message.isBlank()) {
                throw new IllegalArgumentException("Greeting message must not be blank");
            }
        }
    }

    // ── Java 15+ String.formatted() ───────────────────────────────────────────
    /**
     * Returns a {@link Greeting} record containing a personalised hello message.
     *
     * @param name the name to greet
     * @return a {@code Greeting} record
     */
    public static Greeting greet(String name) {
        return new Greeting("Hello, %s!".formatted(name));
    }

    // ── Java 14+ switch expression ────────────────────────────────────────────
    /**
     * Returns a locale-aware formal greeting using a switch expression.
     *
     * @param locale a simple locale tag: {@code "en"}, {@code "es"}, {@code "fr"}, or {@code "de"}
     * @param name   the name to greet
     * @return a language-specific greeting string
     */
    public static String formalGreet(String locale, String name) {
        var prefix = switch (locale) {
            case "es" -> "Hola";
            case "fr" -> "Bonjour";
            case "de" -> "Hallo";
            default  -> "Hello";
        };
        return "%s, %s!".formatted(prefix, name);
    }

    // ── Java 15+ text block ───────────────────────────────────────────────────
    /**
     * Builds a small multi-line report about the application using a text block.
     *
     * @param appName    application name
     * @param javaVersion Java version string
     * @return a formatted report string
     */
    public static String buildReport(String appName, String javaVersion) {
        return """
                Application : %s
                Java version: %s
                Status      : Running
                """.formatted(appName, javaVersion);
    }

    // ── main — uses var (Java 10+) ────────────────────────────────────────────
    public static void main(String[] args) {
        var greeting = greet("World");           // var: type inferred as Greeting
        System.out.println(greeting.message());  // record accessor

        var report = buildReport(
                "simple-java21-app",
                System.getProperty("java.version")
        );
        System.out.println(report);

        System.out.println(formalGreet("fr", "Java 21"));
    }
}

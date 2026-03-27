package com.example;

public class HelloWorld {

    /**
     * Immutable value type demonstrating Java 16+ records.
     * A record automatically generates a canonical constructor, accessors,
     * {@code equals}, {@code hashCode}, and {@code toString}.
     */
    public record Greeting(String recipient, String message) {

        /** Compact canonical constructor – validates input. */
        public Greeting {
            if (recipient == null || recipient.isBlank()) {
                throw new IllegalArgumentException("recipient must not be blank");
            }
            if (message == null || message.isBlank()) {
                throw new IllegalArgumentException("message must not be blank");
            }
        }
    }

    /**
     * Builds a greeting message for the given name.
     * Uses {@code String.formatted()} (Java 15+) instead of
     * {@code String.format()} for a more readable, instance-method style.
     *
     * @param name the name to greet
     * @return the greeting string
     */
    public static String greet(String name) {
        return "Hello, %s!".formatted(name);
    }

    public static void main(String[] args) {
        // 'var' (Java 10+) lets the compiler infer the local-variable type.
        var greeting = new Greeting("World", greet("World"));

        // Text block (Java 15+) – clean multi-line string formatting.
        var report = """
                === Greeting Report ===
                Recipient : %s
                Message   : %s
                ======================
                """.formatted(greeting.recipient(), greeting.message());

        System.out.print(report);
    }
}

package com.example;

/**
 * Simple greeting application modernised for Java 21.
 *
 * <p>Demonstrates several Java 21 language features:
 * <ul>
 *   <li>Records (JEP 395, finalized in Java 16) – immutable data carriers with auto-generated accessors</li>
 *   <li>Text blocks (JEP 378, finalized in Java 15) – readable multi-line string literals</li>
 *   <li>Pattern matching for {@code instanceof} (JEP 394, finalized in Java 16)</li>
 *   <li>{@code var} local-variable type inference (JEP 286, finalized in Java 10)</li>
 *   <li>Switch expressions with arrow labels (JEP 361, finalized in Java 14)</li>
 * </ul>
 */
public class HelloWorld {

    // --- Records (JEP 395, finalized in Java 16) – immutable data carriers ---
    record Greeting(String salutation, String name) {
        // Compact constructor – validates input
        Greeting {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name must not be blank");
            }
            // Canonical fields are assigned automatically after the compact constructor body
        }

        /** Returns the full greeting string. */
        String message() {
            return salutation + ", " + name + "!";
        }
    }

    // --- Core helper used by tests (keeps the original public contract) ---
    public static String greet(String name) {
        return new Greeting("Hello", name).message();
    }

    // --- Switch expression (JEP 361, finalized in Java 14) ---
    // Arrow-label switch on a primitive int: no fall-through, exhaustive, concise.
    static String salutationFor(int hourOfDay) {
        return switch (hourOfDay) {
            case 5, 6, 7, 8, 9, 10, 11    -> "Good morning";
            case 12, 13, 14, 15, 16        -> "Good afternoon";
            case 17, 18, 19, 20            -> "Good evening";
            default                        -> "Hello";
        };
    }

    public static void main(String[] args) {
        // var – local-variable type inference
        var name = args.length > 0 ? args[0] : "World";

        // Text block – clean multi-line string (Java 15+)
        var banner = """
                ╔══════════════════════════════╗
                ║   Java 21 Hello-World Demo   ║
                ╚══════════════════════════════╝
                """;

        System.out.print(banner);

        // Pattern-matching instanceof
        Object rawName = name;
        if (rawName instanceof String s && !s.isBlank()) {
            System.out.println(greet(s));
        }

        // Use the switch expression with the current hour
        int hour = java.time.LocalTime.now().getHour();
        var salutation = salutationFor(hour);
        System.out.println(new Greeting(salutation, name).message());
    }
}

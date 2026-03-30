package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link HelloWorld} using JUnit Jupiter (JUnit 5).
 *
 * JUnit 5 highlights used here:
 * <ul>
 *   <li>{@link DisplayName} — human-readable test names in reports</li>
 *   <li>{@link ParameterizedTest} + {@link CsvSource} — data-driven tests</li>
 *   <li>{@code assertAll} — grouped assertions that all run even on failure</li>
 *   <li>{@code assertThrows} — verifies that exceptions are thrown</li>
 * </ul>
 *
 * Note: In JUnit 5 test classes and methods no longer need to be {@code public}.
 */
@DisplayName("HelloWorld")
class HelloWorldTest {

    // -------------------------------------------------------------------------
    // greet()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("greet(\"World\") returns \"Hello, World!\"")
    void testGreet() {
        assertEquals("Hello, World!", HelloWorld.greet("World"));
    }

    @Test
    @DisplayName("greet(\"Java\") returns \"Hello, Java!\"")
    void testGreetWithCustomName() {
        assertEquals("Hello, Java!", HelloWorld.greet("Java"));
    }

    /**
     * Parameterized test: each CSV row supplies (name, expectedMessage).
     * The text-block delimiter avoids quoting commas inside the expected values.
     */
    @ParameterizedTest(name = "greet(\"{0}\") → \"{1}\"")
    @CsvSource(delimiter = '|', textBlock = """
            World   | Hello, World!
            Alice   | Hello, Alice!
            Java 21 | Hello, Java 21!
            """)
    @DisplayName("greet() produces the correct greeting for multiple names")
    void testGreetParameterized(String name, String expected) {
        assertEquals(expected.strip(), HelloWorld.greet(name.strip()));
    }

    // -------------------------------------------------------------------------
    // buildGreeting() / Greeting record
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("buildGreeting() returns a Greeting record with correct fields")
    void testBuildGreeting() {
        var greeting = HelloWorld.buildGreeting("Alice");

        // assertAll: runs every lambda even if earlier ones fail
        assertAll("Greeting fields",
                () -> assertEquals("Alice",          greeting.name()),
                () -> assertEquals("Hello, Alice!",  greeting.message())
        );
    }

    @Test
    @DisplayName("Greeting record toString() includes name and message")
    void testGreetingToString() {
        var greeting = HelloWorld.buildGreeting("Bob");

        var str = greeting.toString();
        // Records generate toString() automatically — verify it contains the fields
        assertAll("Greeting toString",
                () -> assertTrue(str.contains("Bob"),          "should contain name"),
                () -> assertTrue(str.contains("Hello, Bob!"),  "should contain message")
        );
    }

    @Test
    @DisplayName("Greeting constructor throws IllegalArgumentException for a blank name")
    void testGreetingBlankNameThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new HelloWorld.Greeting("", "some message"));
    }

    @Test
    @DisplayName("Greeting constructor throws IllegalArgumentException for a null name")
    void testGreetingNullNameThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new HelloWorld.Greeting(null, "some message"));
    }

    // -------------------------------------------------------------------------
    // classifyName()
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "classifyName(\"{0}\") → \"{1}\"")
    @CsvSource({
            "Al,        short",
            "Bob,       short",
            "Alice,     medium",
            "Alexander, long"
    })
    @DisplayName("classifyName() returns the correct length category")
    void testClassifyName(String name, String expectedKind) {
        assertEquals(expectedKind.strip(), HelloWorld.classifyName(name.strip()));
    }
}

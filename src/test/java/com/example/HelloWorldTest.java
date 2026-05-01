package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link HelloWorld} – migrated from JUnit 4 to JUnit 5 (Jupiter).
 *
 * <p>Key JUnit 5 changes from the original JUnit 4 tests:
 * <ul>
 *   <li>Imports changed from {@code org.junit.*} to {@code org.junit.jupiter.api.*}</li>
 *   <li>Assertions are now from {@code org.junit.jupiter.api.Assertions}</li>
 *   <li>Test methods no longer need to be {@code public}</li>
 *   <li>{@code @ParameterizedTest} replaces repetitive single-case tests</li>
 * </ul>
 */
@DisplayName("HelloWorld")
class HelloWorldTest {

    // ------------------------------------------------------------------ greet()

    @Test
    @DisplayName("greet(\"World\") returns the standard greeting")
    void testGreet() {
        assertEquals("Hello, World!", HelloWorld.greet("World"));
    }

    @Test
    @DisplayName("greet(\"Java\") returns a personalised greeting")
    void testGreetWithCustomName() {
        assertEquals("Hello, Java!", HelloWorld.greet("Java"));
    }

    @ParameterizedTest(name = "greet(\"{0}\") returns \"Hello, {0}!\"")
    @ValueSource(strings = {"World", "Java 21", "Alice", "Bob"})
    @DisplayName("greet() produces the correct message for various names")
    void testGreetParameterized(String name) {
        assertEquals("Hello, %s!".formatted(name), HelloWorld.greet(name));
    }

    // --------------------------------------------------------------- buildGreeting()

    @Test
    @DisplayName("buildGreeting() returns a non-null Greeting record")
    void testBuildGreetingNotNull() {
        assertNotNull(HelloWorld.buildGreeting("Test"));
    }

    @Test
    @DisplayName("Greeting record carries the correct name and message")
    void testGreetingRecord() {
        var greeting = HelloWorld.buildGreeting("Java 21");

        // Record accessors are generated automatically
        assertEquals("Java 21", greeting.name());
        assertEquals("Hello, Java 21!", greeting.message());
    }

    @Test
    @DisplayName("Greeting record has value-based equality")
    void testGreetingRecordEquality() {
        var g1 = new HelloWorld.Greeting("Alice", "Hello, Alice!");
        var g2 = new HelloWorld.Greeting("Alice", "Hello, Alice!");

        // Records implement equals() based on component values
        assertEquals(g1, g2);
    }
}

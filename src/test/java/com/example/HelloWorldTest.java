package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelloWorldTest {

    @Test
    void testGreet() {
        assertEquals("Hello, World!", HelloWorld.greet("World"));
    }

    @Test
    void testGreetWithCustomName() {
        assertEquals("Hello, Java!", HelloWorld.greet("Java"));
    }

    @Test
    void testGreetStripsWhitespace() {
        // Verifies Java 11+ strip() behaviour via greet()
        assertEquals("Hello, Alice!", HelloWorld.greet("  Alice  "));
    }

    @Test
    void testGreetFallsBackToWorldForBlankInput() {
        // Verifies Java 11+ isBlank() guard
        assertEquals("Hello, World!", HelloWorld.greet("   "));
    }

    @ParameterizedTest
    @CsvSource(delimiterString = "|", value = {
        "Alice   | Hello, Alice!",
        "Bob     | Hello, Bob!",
        "Charlie | Hello, Charlie!"
    })
    void testGreetParameterized(String name, String expected) {
        // CSV values are trimmed by @CsvSource; names have no surrounding whitespace
        assertEquals(expected, HelloWorld.greet(name));
    }

    @Test
    void testBuildGreetingWithString() {
        var greeting = HelloWorld.buildGreeting("Java");
        assertNotNull(greeting);
        assertEquals("Java", greeting.recipient());
        assertEquals("Hello, Java!", greeting.message());
    }

    @Test
    void testBuildGreetingWithNonString() {
        var greeting = HelloWorld.buildGreeting(42);
        assertNotNull(greeting);
        assertEquals("World", greeting.recipient());
        assertEquals("Hello, World!", greeting.message());
    }

    @Test
    void testSummary() {
        var names = List.of("Alice", "Bob");
        var result = HelloWorld.summary(names);
        assertTrue(result.contains("Hello, Alice!"));
        assertTrue(result.contains("Hello, Bob!"));
        assertTrue(result.contains("=== Greetings ==="));
    }

    @Test
    void testDescribeLanguageJava() {
        assertEquals("Strongly typed, JVM-based language", HelloWorld.describeLanguage("Java"));
    }

    @Test
    void testDescribeLanguageUnknown() {
        assertTrue(HelloWorld.describeLanguage("Rust").startsWith("Unknown language:"));
    }
}

package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link HelloWorld}, migrated to JUnit 5 (Jupiter) and
 * extended to cover new Java-21 features introduced during modernisation.
 */
class HelloWorldTest {

    // ── greet() ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("greet(\"World\") returns a Greeting record with the expected message")
    void testGreet() {
        var greeting = HelloWorld.greet("World");
        assertEquals("Hello, World!", greeting.message());
    }

    @Test
    @DisplayName("greet(\"Java\") returns a Greeting record with the expected message")
    void testGreetWithCustomName() {
        var greeting = HelloWorld.greet("Java");
        assertEquals("Hello, Java!", greeting.message());
    }

    // ── Greeting record ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Greeting record stores and exposes its message via the accessor")
    void testGreetingRecord() {
        var g = new HelloWorld.Greeting("Hi there!");
        assertEquals("Hi there!", g.message());
    }

    @Test
    @DisplayName("Greeting compact constructor rejects blank messages")
    void testGreetingRecordValidation() {
        assertThrows(IllegalArgumentException.class, () -> new HelloWorld.Greeting("  "));
        assertThrows(IllegalArgumentException.class, () -> new HelloWorld.Greeting(null));
    }

    // ── formalGreet() — switch expression ────────────────────────────────────

    @ParameterizedTest(name = "locale={0} → prefix={1}")
    @DisplayName("formalGreet uses the correct prefix per locale")
    @CsvSource({
        "en, Hello,   Alice",
        "es, Hola,    Alice",
        "fr, Bonjour, Alice",
        "de, Hallo,   Alice",
        "ja, Hello,   Alice"   // unknown locale falls back to "Hello"
    })
    void testFormalGreet(String locale, String expectedPrefix, String name) {
        var result = HelloWorld.formalGreet(locale, name.trim());
        assertTrue(result.startsWith(expectedPrefix.trim()),
                "Expected greeting to start with '" + expectedPrefix.trim() + "' but was: " + result);
    }

    // ── buildReport() — text block ────────────────────────────────────────────

    @Test
    @DisplayName("buildReport includes the app name and Java version")
    void testBuildReport() {
        var report = HelloWorld.buildReport("my-app", "21");
        assertAll("report contents",
                () -> assertTrue(report.contains("my-app"),  "report should contain the app name"),
                () -> assertTrue(report.contains("21"),       "report should contain the Java version"),
                () -> assertTrue(report.contains("Running"), "report should show Running status")
        );
    }
}

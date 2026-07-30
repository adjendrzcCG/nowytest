package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HelloWorld")
class HelloWorldTest {

    // -----------------------------------------------------------------------
    // greet() – original contract
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("greet returns 'Hello, World!'")
    void testGreet() {
        assertEquals("Hello, World!", HelloWorld.greet("World"));
    }

    @Test
    @DisplayName("greet returns 'Hello, Java!'")
    void testGreetWithCustomName() {
        assertEquals("Hello, Java!", HelloWorld.greet("Java"));
    }

    // -----------------------------------------------------------------------
    // Greeting record
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Greeting record produces correct message")
    void greetingRecordMessage() {
        var g = new HelloWorld.Greeting("Hi", "Alice");
        assertEquals("Hi, Alice!", g.message());
    }

    @Test
    @DisplayName("Greeting record accessors work correctly")
    void greetingRecordAccessors() {
        var g = new HelloWorld.Greeting("Hey", "Bob");
        assertEquals("Hey", g.salutation());
        assertEquals("Bob",  g.name());
    }

    @Test
    @DisplayName("Greeting record rejects blank name")
    void greetingRecordRejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new HelloWorld.Greeting("Hello", ""));
    }

    @Test
    @DisplayName("Greeting record rejects null name")
    void greetingRecordRejectsNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new HelloWorld.Greeting("Hello", null));
    }

    // -----------------------------------------------------------------------
    // salutationFor() – switch expression
    // -----------------------------------------------------------------------

    @ParameterizedTest(name = "hour {0} → \"{1}\"")
    @DisplayName("salutationFor maps hour ranges to correct salutations")
    @CsvSource({
        " 5, Good morning",
        "11, Good morning",
        "12, Good afternoon",
        "16, Good afternoon",
        "17, Good evening",
        "20, Good evening",
        " 0, Hello",
        "21, Hello",
        " 4, Hello",
    })
    void salutationForHour(int hour, String expected) {
        assertEquals(expected, HelloWorld.salutationFor(hour));
    }
}

package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HelloWorldTest {

    @Test
    void testGreet() {
        assertEquals("Hello, World!", HelloWorld.greet("World"));
    }

    @Test
    void testGreetWithCustomName() {
        assertEquals("Hello, Java!", HelloWorld.greet("Java"));
    }

    // ── Record tests ──────────────────────────────────────────────────────────

    @Test
    void testGreetingRecord_accessors() {
        var g = new HelloWorld.Greeting("Alice", "Hello, Alice!");
        assertEquals("Alice",         g.recipient());
        assertEquals("Hello, Alice!", g.message());
    }

    @Test
    void testGreetingRecord_blankRecipientThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new HelloWorld.Greeting("", "Hello!"));
    }

    @Test
    void testGreetingRecord_blankMessageThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new HelloWorld.Greeting("Bob", "  "));
    }
}

package com.example;

import org.junit.jupiter.api.Test;

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
    void testGreetReturnsNonNull() {
        assertNotNull(HelloWorld.greet("Test"));
    }

    @Test
    void testGreetContainsName() {
        var name = "Alice";
        var result = HelloWorld.greet(name);
        assertTrue(result.contains(name), "Greeting should contain the provided name");
    }
}

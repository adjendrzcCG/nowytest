package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldTest {

    @Test
    public void testGreet() {
        assertEquals("Hello, World!", HelloWorld.greet("World"));
    }

    @Test
    public void testGreetWithCustomName() {
        assertEquals("Hello, Java!", HelloWorld.greet("Java"));
    }
}

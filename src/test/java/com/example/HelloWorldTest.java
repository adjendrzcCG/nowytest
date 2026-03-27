package com.example;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

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

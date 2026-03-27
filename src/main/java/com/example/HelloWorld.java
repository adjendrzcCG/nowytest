package com.example;

public class HelloWorld {

    public static String greet(String name) {
        return "Hello, " + name + "!";
    }

    public static void main(String[] args) {
        String message = """
                Welcome to the Java 17 App!
                %s
                """.formatted(greet("World"));
        System.out.println(message);
    }
}

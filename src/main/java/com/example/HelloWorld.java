package com.example;

public class HelloWorld {

    public static String greet(String name) {
        return "Hello, %s!".formatted(name);
    }

    public static void main(String[] args) {
        System.out.println(greet("World"));
    }
}

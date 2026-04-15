using HelloWorld;

namespace HelloWorld.Tests;

public class GreeterTests
{
    [Fact]
    public void Greet_ReturnsHelloWorld_WhenWorldProvided()
    {
        Assert.Equal("Hello, World!", Greeter.Greet("World"));
    }

    [Fact]
    public void Greet_ReturnsHelloJava_WhenJavaProvided()
    {
        Assert.Equal("Hello, Java!", Greeter.Greet("Java"));
    }
}

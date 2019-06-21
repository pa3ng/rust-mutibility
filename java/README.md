# Java's `final`

[Final.java](Final.java) will compile. Can you predict what it'll print out? How do the following method signatures help you make your decisions:

```java
// java.util.List<E> instance methods
boolean add​(E e)
E set​(int index, E element)

// java.util.Arrays static method
@SafeVarargs public static <T> List<T> asList​(T... a)

// java.util.Collections static method
public static <T> List<T> unmodifiableList​(List<? extends T> list)
```

Would you agree with the following conclusions?

1. `final` is useless. Sure, it conveys that you can't reassign the variable, but this is a particularly low bar.
2. Method signatures tell you absolutely nothing about what the method will do to your object. Yes, there's _maybe_ JavaDoc. Sure, there's source (if you've access to it), but mutability is not enforced by a contract.
3. Behavioral Nuances are pushed to runtime and can yield surprising and difficult to debug bugs.
4. Resorting to `UnsupportedOperationException`, an unchecked exception, has implications. What happens when you want to mutate a collection you've no idea was wrapped by `Collections.unmodifiableList`?

Why _wouldn't_ you want the compiler catching these bugs for you?


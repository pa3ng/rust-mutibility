# Java's `final` vs. Rust's `mut`

## [`Final.java`](java/Final.java)

We create three lists:

```java
    final var list = new ArrayList<Integer>();
    final var asList = Arrays.asList(0, 1, 2);
    final var unmodifiableList = Collections.unmodifiableList(Arrays.asList(0, 1, 2));
```

Questions:

* Will `final` prevent us from calling `list.add(0)`?
* Can we, similarly `add` to either `asList` or `unmodifiableList`?
* What if we attempt to replace the 0th element from either `asList` or `unmodifiableList` using `.set(0, 6)`?

Let's see:

```
$ java Final 
list as originally assigned: []
list after list.add(0): [0]

asList as originally assigned: [0, 1, 2]
asList after asList.set(0, 6): [6, 1, 2]
caught exception: java.lang.UnsupportedOperationException
asList after asList.add(3): [6, 1, 2]

unmodifiableList as originally assigned: [0, 1, 2]
caught exception: java.lang.UnsupportedOperationException
unmodifiableList after asList.set(0, 6): [0, 1, 2]
caught exception: java.lang.UnsupportedOperationException
unmodifiableList after asList.add(3): [0, 1, 2]
```

What happened?

* First, it should be pretty clear `final` is almost meaningless. Per the language, `final` simply means you can not reassign the variable, but Java doesn't care what you do with anything that might be in the variable's object
* `Arrays.asList(...)` does not allow modications that would change the collection's size, but happily accepts modications that change elements in the collection
* `Collections.unmodifiableList(...)` prevents any change, is the only mechanism in this example to produce a truly immutable collection.

So what?

* `UnsupportedOperationException` is an _unchecked_ exception. It's not on the signature of any method, and code that wishes to use methods which mutate a collection has no way to enforce that the caller provides a collection supporting mutable operations
* Behavior is not a matter of a method's enforceable contract (it can't be expressed in the method's declaration), instead what we have is contract-by-documentation (see JavaDoc for [Arrays.asList(T ...)](https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/util/Arrays.html#asList(T...)) and [Collections.unmodifiableList(List)](https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/util/Collections.html#unmodifiableList(java.util.List)))
* The example _compiles_: as will become a theme here, Java pushes most bugs to runtime.

## [`immut.rs`](rust/immut.rs)

Our first attempt with Rust is simple enough to show here:

```rust
fn main() {
    let vec = vec!(0, 1, 2);
    vec[0] = 6; // reassign the 0th element to 6 from 0
    vec.push(3); // add 3 to the end after two
    println!("{:?}", vec);
}
```

Let's compile it:
```
$ rustc immut.rs 
error[E0596]: cannot borrow immutable local variable `vec` as mutable
 --> immut.rs:3:5
  |
2 |     let vec = vec!(0, 1, 2);
  |         --- help: make this binding mutable: `mut vec`
3 |     vec[0] = 6;
  |     ^^^ cannot borrow mutably

error[E0596]: cannot borrow immutable local variable `vec` as mutable
 --> immut.rs:4:5
  |
2 |     let vec = vec!(0, 1, 2);
  |         --- help: make this binding mutable: `mut vec`
3 |     vec[0] = 6;
4 |     vec.push(3);
  |     ^^^ cannot borrow mutably

error: aborting due to 2 previous errors

For more information about this error, try `rustc --explain E0596`.
```

What happened?

* Rust variables are _immutable_ by default
* Mutation is allowed only if you explicitly ask for it via `mut`
* Mutation is not a runtime bug, it's _caught by the compiler_.

This should feel "game changing", but how is this happening?

First, let's look at `vec[0] = 6`. This we get from the [`IndexMut<I>` trait](https://doc.rust-lang.org/std/vec/struct.Vec.html#impl-IndexMut%3CI%3E) (we'll discuss traits, Rust's "interface", later):

```rust
fn index_mut(&mut self, index: I) -> &mut <Vec<T> as Index<I>>::Output
```

Python programmers will recognize `self`, which refers to the vector itself. It's pretty obvious from the signature, though, that to call this function, the vector must be `mut`able (we'll cover the `&` next).

Similarly, for `vec.push(3)`, which we get from [`push`](https://doc.rust-lang.org/std/vec/struct.Vec.html#method.push):

```rust
pub fn push(&mut self, value: T)
```

That `&mut self` again! Contrast with [`len`](https://doc.rust-lang.org/std/vec/struct.Vec.html#method.len):

```rust
pub fn len(&self) -> usize
```

Here, the vector need not be `mut`able to call this function.

Rust's `mut`

* Is part of a variable's and a function's declaration
* Is used by the _compiler_ to catch bugs
* Provides the basis for other guarantees we'll discuss later.

## [`immut.rs`](rust/immut.rs)

We can fix the previous example by adding a `mut` declaration to the variable:

```rust
fn main() {
    let mut vec = vec!(0, 1, 2);
    vec[0] = 6;
    vec.push(3);
    println!("{:?}", vec);
}
```

With the following results:

```
$ rustc mut.rs && ./mut
[6, 1, 2, 3]
```
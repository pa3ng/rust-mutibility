### Rust's `mut`

Compiling [immut.rs](immut.rs):

```rust
fn main() {
    let vec = vec!(0, 1, 2);
    vec[0] = 6;
    vec.push(3);
    println!("{:?}", vec);
}
```

results in the following:

```bash
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

Why? Rust strongly enforces mutability and variables and function arguments are immutable by default. Compare [contains](https://doc.rust-lang.org/std/vec/struct.Vec.html#method.contains):

```rust
pub fn contains(&self, x: &T) -> bool where
    T: PartialEq<T>, 
```

to [push](https://doc.rust-lang.org/std/vec/struct.Vec.html#method.push):

```rust
pub fn push(&mut self, value: T)
```

The former does not mutate the vector, the latter does. The former may be called on an immutable vector, the latter may not. You must declare mutation and it's enforced by the compiler.

To fix the above issue, make the vector mutable:

```rust
    let vec = vec!(0, 1, 2);
```

Contrast this behavior with Java's closest analogy: `final`.
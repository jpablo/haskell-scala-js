# Scala as a calculator
## Numbers
```tut
2 + 2
50 - 5*6
(50 - 5*6) / 4
8 / 5
```
As you can see, each result has the type indicated to the left.

> Note: `8/5` performs integer division. In order to get a Float you need to convert one of the arguments:

```tut
8.toFloat / 5
```

There's no predefined operator to calculate powers, but you can use `Math.pow`

```tut
Math.pow(5,2)
```

To save a value for later use you can use the keyword `val` and the equal sign `(=)`:

```tut
val width = 20
val height = 5 * 9
width * height
```

> Note: names created with `val` cannot be reassigned. For such cases you need to use `var`:

```tut
var x = 1
x = x + 1
```

The most common numeric types are:

`Int`
`Long`
`Float`
`Double`
`BigInt`

Note: There's no built in support for complex or fractional numbers.

## Strings
There are 3 common ways to create literal strings:

### Double quotes

```tut
"spam eggs"
"doesn't"
"\"abc\""
```

### Triple quotes

```tut
""""Isn't," she said."""
"""a
multiline
word
"""
println("""c:\some\name""")
```

Translation: `print(s)` becomes `println(s)`

### String interpolation
```tut
val x = 1 + 2 + 3
println(s"1 + 2 + 3 = $x")
```

The `s"..."` syntax allows to embed code directly within a string.

### Characters
Characters have a special type in scala `Char` and are constructed using single quotes

```tut
'a'
```

### String operations
A String is conceptually a sequence of characters. 

#### Concatenate
```tut
"The 🐱 " + "ate the 🍎" 
```

#### Indexing
They can be indexed using the `()` syntax:

```tut
val word = "Scala"
word(0)
word(4)
```
> Notes:
> * The result is a `Char`, not a `String`
> * Indices cannot be negative.

#### Slices

```tut
"Scala".slice(0,3)
"Scala".slice(3,5)
```


#### take, drop
```tut
"Scala".take(3) + "Scala".drop(3)
```
Same as in Python, `s.take(i) + s.drop(i)` always equals `s`

#### length
```tut
"Scala".length
```

> Note: There's no global `len` function as in Python but rather each collection has its own `length` method.

### See Also
TODO

## Collections
Scala has a rich library of data structures. Unlike Python's square brackets `[]`, there's no special syntax for lists. 

By default, only the immutable data structures are imported automatically since they are better aligned with the Functional nature of Scala. 

This means that they can be used right away without any import or prefix. 

```tut
Vector(1, 3, 3)
List("aa", "bb", "cc")
```

In contrast, the **mutable** data structures have to be imported before using them:

```tut
import collection.mutable.ArrayBuffer
ArrayBuffer(1, 2, 3)
```

Let's start with a versatile immutable collection.

### Vector

```tut
val squares = Vector(1, 4, 9, 16, 25)
```

Note: Vectors contains elements of the same type.

Vectors support many of the same operations as Strings:

```tut
squares(0)
squares(1)

squares.slice(1,3)

squares.take(2)
squares.drop(2)
```

To concatenate two Vectors (and other sequences) you can use the `(++)` operator

```tut
Vector(1, 2) ++ Vector(3, 4)
```

To add elements at the beginning or the end you can use the `(+:)` and `(:+)` operators

```tut
0 +: Vector(1, 2)
Vector(1, 2) :+ 3
```

> Hint: The colon goes on the Vector side, as a reminder that the operator belongs to the Vector and not to the number.

Since we're using the *immutable* Vector what you get is a new copy of the original Vector.


```tut
val v = Vector(1, 2)
v :+ 3
v
```

```tut
v.length
```

### List

Another commonly used collection is `List`. Lists are in fact **linked lists**, which means that they are very fast at inserting elements at the beginning and iterating sequentially, but accessing element *n* requires scanning the whole list up to the *n-th* element.

Pro tip: `List` is *not* like Python's `list`. Use `Vector` (immutable) or `ArrayBuffer` (mutable) if you need fast access to an arbitrary index.

```tut
val lst = List(1, 2, 3, 4, 5, 6)
lst.length
```

List is particularly good if you need to get hold of the first element (the *head*) and everything but the head (the *tail*)

```tut
val h = lst.head
val t = lst.tail
```
In most data structures getting a copy of the tail is a relatively expensive operation that requires copying n - 1 elements. In a linked list this just involves following a pointer so it is super fast.

Another fast operation on Lists is prepending an element via the `(::)` operator

```tut
val lst2 = 0 :: lst
lst2.head
```

and watch this:

```tut
lst2.tail.hashCode
lst.hashCode
```

Note: `hashCode` is similar to Python's `id` function. It is unique and different for every object unless they are the ***same*** "physical" object in memory. 

The code above highligths a suprising (at first) technique used in immutable data structures to make them performant: they copy and reuse existing data as much as possible.



### ArrayBuffer

Probably the most similar structure to Python's `list` is `collection.mutable.ArrayBuffer`.


```tut
import collection.mutable.ArrayBuffer // required import

val b = ArrayBuffer(1, 2, 3, 4, 5)
```

All of the operations that we've seen on Strings and Vectors work for ArrayBuffer

```tut
b(0)
b(1)
b.slice(1,3)
b.take(2)
b.drop(2)
```

Since they are mutable they support a few extra operations to modify / append / preppend / extend the buffer

```tut
b(0) = 2 // modify in place
b
b(0) += 1 // increment operator
b
b.append(6)
b
b.prepend(0)
b
b.appendAll(ArrayBuffer(7,8,9))
b
```

Since a collection is completely agnostic towards its contents (i.e. "generic") we can have nested buffers

```tut
val b1 = ArrayBuffer(1, 2, 3)
val b2 = ArrayBuffer(4, 5, 6)
val c = ArrayBuffer(b1, b2)

```

let's update the element (0,0)

```tut
c(0)(0) += 1
c
```

# First Steps Towards Programming

Let's generate the classic fibonacci series (mutable version)

```tut
var a = 0
var b = 1
var tmp = 0
while (b < 10) {
  println(b)
  tmp = a + b
  a = b
  b = tmp
}
```

We can observe a few differences: 
* The Python version doesn't need the temporal variable because multiple assignment: `a, b = b, a + b` works element by element: "n to n"

 In contrast, Scala has a special version that is more like "n to 1" that works like this

 ```tut
 val x, y, z = Math.random 
 ```

 The expression on the right (1) is evaluated as many times as variables on the left (n).

* Scala does not use indentation as part of the syntax. The body of the `while` loop consists of the block immediately next to it; curly brackets `{}` are required if there is more than one line (as in this case).





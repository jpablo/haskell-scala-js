# Scala as a calculator {#calculator}
All the commands so far will assume that you started the Scala REPL via inside the `hello-world` project created in the introduction.

```
$ sbt console
```
## Numbers and arithmetic

```tut
2 + 2
50 - 5*6
(50 - 5*6) / 4
8 / 5
```
As you can see, each result has the type indicated to the left.

> Note: `8/5` performs integer division. In order to get a floating point operation you need to convert one of the arguments:

```tut
8.toDouble / 5
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

## Numeric types
Python has 4 main numeric types. All have a corresponding type in Scala except `complex`.

| Python | Scala |
| ---    |---    |
| `bool` | `Boolean`
| `int` | `BigInt`
| `float` | `Double`
| `complex` | NA


From the Scala point of view these are the numeric types:


| Type | T.MinValue | T.MaxValue | Example
| --- | --- | --- | ---
|`Int` | `-2147483648` | `2147483647` | `100`
|`Long` | `-9223372036854775808` | `9223372036854775807` | `2147483648L`
|`Float` | `-3.4028235E38` | `3.4028235E38` | `1.23F`
|`Double` | `-1.7976931348623157E308` | `1.7976931348623157E308` | `1.23`
|`BigInt` | Unbounded (limited by memory) | Unbounded | `BigInt(123)`
| `BigDecimal` | Unbounded (limited by memory) | Unbounded | `BigDecimal(1.23)`

For all bounded types you can obtain the min/max values using the `MinValue` / `MaxValue` functions.

```tut
Int.MaxValue
```

## Strings
There are 3 common ways to create literal strings:

### Double quotes

```tut
"spam eggs"
"doesn't"
"\"abc\""
"La niña comía rábanos"
"The 🐱 ate the 🍎" 
```

### Triple quotes

```tut
""""Isn't," she said."""
"""a
multiline
word
"""
"""c:\some\name"""
"""{"x":1, "y":"2"}"""
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

<table>
<tbody>
<tr>
<td></td>
<td>Python</td>
<td>Scala</td>
<td>Notes</td>
</tr>

<tr>
<td>Concatenate</td>
<td><pre>"a" + "b"</pre></td>
<td><pre>"a" + "b"</pre></td>
</tr>

<tr>

<td>Indexing</td>

<td>
<pre>word = "Scala"
word[0] == "S"
word[4] == "a"</pre>
</td>

<td>
<pre>val word = "Scala"
word(0) == 'S'
word(4) == 'a'</pre>
</td>

<td>
<ul>
<li> Insted of square brackets `seq[i]` Scala uses parenthesis `seq(i)` to access the `i-th` element of a sequence.
<li> The result is a `Char`, not a `String`
<li> Indices cannot be negative.
<ul>
</td>
</tr>

<tr>
<td>Slices</td>
<td><pre>"Python"[2:6] == "thon"</pre></td>
<td><pre>"Python".slice(2,6) == "thon"</pre></td>
<td>There is no syntax for slices (you have to use the `.slice` method)<td>
</tr>

<tr>
<td>Length</td>
<td><pre>len("Python") == 6</pre></td>
<td><pre>"Python".length == 6</pre></td>
<td>There's no global `len` function as in Python but rather each collection has its own `length` method.<td>
</tr>


</tbody>
</table>


## Collections  
Scala has a rich library of data structures. A few highlights:

* There's no special syntax for building lists. (e.g. `[1,2,3]`)
* Scala collections are *homogeneous*. They can only hold values of a single type.
* Most of the collections have a *immutable* version and a *mutable* version. In general only the immutable variant is imported automatically (`Array` is an exception).
* The immutable versions are probably more performant than what you'd expect.
* When in doubt, is better to use the immutable variant and only switch to the mutable variant when you need it for reasons like convenience, performance, etc.


Let's start with one of the most popular immutable collections.


### `List`

Lists are in fact **linked lists**, which means that they are very fast at inserting elements at the beginning and iterating sequentially, but accessing element *n* requires scanning the whole list up to the *n-th* element.

Pro tip: `List` is *not* like Python's `list`. Use `Vector` (immutable) or `Array` (mutable) if you need fast access to an arbitrary index.

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

### mutable.ArrayBuffer

Probably the most similar structure to Python's `list` is `collection.mutable.ArrayBuffer`.

The "Buffer" part refers to the fact that the size can be changed, as opposed to the `Array` which has a fixed size (see below).


```tut
import collection.mutable.ArrayBuffer // required import

val b = ArrayBuffer(1, 2, 3, 4, 5)
```

All of the operations that we've seen on Strings and Vectors work for `ArrayBuffer`:

```tut
b(0)
b(1)
b.slice(1,3)
b.take(2)
b.drop(2)
```

Since they are mutable they support a few extra operations to modify / append / preppend / extend the Arraybuffer

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

As we saw, `ArrayBuffer` allow not only updating its contents in-place but also modifing the length of the array. This imposes a small overhead; if you need maximum performance it's better to use an `Array` which allows in place modification but cannot change its size.

You can build an ArrayBuffer and the transform it into an Array
```tut
b.toArray
```

or if you know the size beforehand it's better to create the Array directly

```tut
val arr = Array(1,2,3)
arr(0) += 1
arr
Array.ofDim[Int](5)
```

# First Steps Towards Programming {#first-steps}

Let's generate the classic fibonacci series

```python
a, b = 0, 1
while b < 10:
    print(b)
    a, b = b, a+b
```

becomes

```tut:book
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

* The Python version doesn't need the temporal variable because multiple assignment: `a, b = b, a + b` works element by element.

 In contrast, Scala has a special version that works like this

 ```tut
 val x, y, z = Math.random 
 ```

 The expression on the right is evaluated as many times as variables on the left.

* Scala does not use indentation as part of the syntax. The body of the `while` loop consists of the block immediately next to it; curly brackets `{}` are required if there is more than one line (as in this case).





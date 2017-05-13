# More Control Flow Tools

Most of the usual Python control flow constructs translate into Scala but there are a few differences that we'll be careful to indicate.

## `if` Expressions

First lets copy the Python version

```scala
val input = io.StdIn.readLine("Please enter an integer: ")
var x = input.toInt // 💣
if (x < 0) {
  println("Negative changed to zero")
  x = 0
} else if (x == 0)
  println("Zero")
else if (x == 1)
  println("Single")
else
  println("More")
```
> Note: The 💣 comment indicates that this line can throw an exception (why?)

While this works, we can write a slightly more idiomatic version

```scala
val input = io.StdIn.readLine("Please enter an integer: ")
val x0 = input.toInt // 💣
val negative = x0 < 0
val message =
  if (negative) "Negative changed to zero"
  else if (x0 == 0) "Zero"
  else if (x0 == 1) "Single"
  else "More"
val x = if (negative) 0 else x0
println(message)
```

In Scala (unlike in Python) an `if` is a **expression**, which means that it has a return value. In the first case it is a String and in the second case an Int.

> Python actually has a second `if` for that behaves in a similar (albeit limited) way:

```python
# Python
x = 0 if x < 0 else x0
```

The second version has a few differences:

* We got rid of the `var x` by introducing an auxiliary value `x0`.
* We separated the (trivial) "business logic" from the message logic.
* Consolidated the `println` statement to a single place.

## `for` comprehensions

`for` expressions (called "for comprehensions") are one of the most interesting aspects of Scala.

They operate in two ways. 

### As `for` loops
The first one is similar to a `for` statement in Python:

```tut
val words = List("Cat 🐈", "Dog 🐕", "Monkey 🐒")
for (w <- words)
  println(s"$w ${w.length}")  // have to use string interpolation
```

Compare with the Python version:

```python
## 🐍
words = ["Cat 🐈", "Dog 🐕", "Monkey 🐒"]
for w in words:
    print(w, len(w))
```

> Note: `println` does not automatically transforms a list of arguments into a single space-separated string (as Python's `print` does).

#### Nested `for` loops
A nested for loop in Python:

```python
## 🐍
for x in range(2):
    for y in range(x, 4):
        print (x, y)
```

Can be written in Scala:

```tut
for {
  x <- 0 until 2
  y <- x until 4
} println(s"$x $y")
```

Which is pretty much the same except that the Scala version is "flatter".

### As lists comprehensions

The second form operates similarly to Python's list comprehensions:


```python
## 🐍
words_length = [len(w) for w in words]
print(words_length)
```
vs

```tut
val wordsLength = for (w <- words) yield w.length
println(wordsLength)
```

As you can see the only difference is that now we are using the keyword **`yield`** to indicate that we want to build a new collection based on the first `words` List.

(We'll spend more time on for comprehensions later).

## Ranges

| Python | Scala | values |
| ---    |---    | --- |
| `range(5)` | `(0 until 5)` | $0, 1, 2, 3, 4$
| `range(1,6)` | `(1 to 5)` | $1, 2, 3, 4, 5$
| `range(0, 10, 3)` | `(0 to 10 by 3)` | $0, 3, 6, 9$
| `range(5,-1,-1)` | `(5 to 0 by -1)`| $5, 4, 3, 2, 1, 0$

To iterate over the indices of a sequence you could do the same trick as in Python:

```tut
val a = List("a", "b", "c")
for (i <- 0 until a.length) println(s"$i ${a(i)}")
```

but there is a niftier way using the standard method `indices`

```tut
for (i <- a.indices) println(s"$i ${a(i)}")
```

## Enumerate
Another standard function in Python is `enumerate`:

```python
## 🐍
seasons = ['Spring', 'Summer', 'Fall', 'Winter']
list(enumerate(seasons))
[(0, 'Spring'), (1, 'Summer'), (2, 'Fall'), (3, 'Winter')]
```

Which has an analogue in Scala's `zipWithIndex` method:

```tut
val seasons = List("Spring", "Summer", "Fall", "Winder")
seasons.zipWithIndex
```

## `break`, `continue`, `else` clause

This is one of the cases where Scala shows its functional side by not having any of those.

Let's examine `break` first.

Consider the following Python code:

```python
## 🐍
for n in range(2, 10):
    for x in range(2, n):
        if n % x == 0:
            print(n, 'equals', x, '*', n//x)
            break
     else:
        print(n, 'is a prime number')
```

This can be translated mechanically to the following Scala code
 
```tut
for (n <- 2 until 10) {
  var break = false
  var x = 2
  while (x < n && !break) {
    if (n % x == 0) {
      println(s"$n equals $x * ${n / x}")
      break = true
    }
    x += 1
  }
  if (!break)
    println(s"$n is a prime number")
}
```

`continue` can be treated in a similar way:

```python
## 🐍
for num in range(2, 10):
    if num % 2 == 0:
        print("Found an even number", num)
        continue
    print("Found a number", num)
```

```tut
var num = 2
while (num < 10) {
  if (num % 2 == 0)
    println(s"Found an even number $num")
  else
    println(s"Found a number $num")
  num += 1
}
```

## `pass` statements


| Python | Scala
| ---    |---
| `while True: pass` | `while (true) {}`
| `class MyEmptyClass: pass` | `class MyEmptyClass`
| `def initlog(*args): pass` | `def initlog(args: Any*) = {}`

So in most cases the empty body (`{}`) is a good substitute for `pass` and in classes not even that is required.

## Defining Functions


```python
def fib(n):
    """Calculate a Fibonacci series up to n."""
    a, b = 0, 1
    while a < n:
        print(a, end=' ')
        a, b = b, a+b
    return b

fib(2000)
```
becomes

```tut:silent
/**
  * Print a Fibonacci series up to n.
  */
def fib(n: Int): Int = { // <= function signature
  var a = 0
  var b = 1
  var tmp = 0
  while (a < n) {
    print(s"$a ")
    tmp = a + b; a = b; b = tmp
  }
  b     // <= returned value!
}
```

```tut
fib(2000)
```

Observe how there is no need to use `return b`. The last expression to be evaluated is the one returned to the caller (in this case `b`).

So far we've relied on Scala's hability to guess the type of our `val`s and `var`s so that we don't have to write them explicitly.

Unfortunately this is not possible on function definitions, where Scala requires us to always declare the type of the arguments. 

Let's analize the function definition

```scala
def fib(n: Int): Int = { ... }
//         ^      ^      body
//         |      |
//         |   return type    
//         |
//   argument type
```

This says: "*`fib` is a function that takes a single `Int` argument `n`, and returns another `Int`*". 

In general type annotations have the form:

```
x: T
```

which is meant to be read: "`x` **is a** `T`".

If a function doesn't return anything meaninful, like in

```scala
def foo(x: Int): Unit = {
  println(x + 1)
}
```
we can use the type `Unit`.

`Unit` plays the role of `void` in other languages. It is used to indicate that a function doesn't return anything interesting. 


## More on defining functions

### Default argument values

```scala
def askOk(prompt: String, retries: Int = 4, reminder: String = "Please try again!") = ???
```

Of interest here is the `???` expression. It can be used in place of any expression when we want the code to compile but we don't want to provide an implementation yet.

The code will compile but if the method is ever executed it will throw an exception.

### Keyword arguments

Pretty much the same as in Python:

```python
def parrot(voltage, state='a stiff', action='voom', type='Norwegian Blue'):
    print("-- This parrot wouldn't", action, end=' ')
    print("if you put", voltage, "volts through it.")
    print("-- Lovely plumage, the", type)
    print("-- It's", state, "!")
```
vs

```tut:silent
  def parrot(voltage: Any, state: String = "a stiff", action: String = "voom", `type`: String = "Norwegian Blue") = {
    print("-- This parrot wouldn't " +  action + " ")
    println("if you put " + voltage + " volts through it.")
    println("-- Lovely plumage, the " + `type`)
    println("-- It's " + state + "!")
  }
```

```tut
parrot(1000)                                          // 1 positional argument
parrot(voltage=1000)                                  // 1 keyword argument
parrot(voltage=1000000, action="VOOOOOM")             // 2 keyword arguments
parrot(action="VOOOOOM", voltage=1000000)             // 2 keyword arguments
parrot("a million", "bereft of life", "jump")         // 3 positional arguments
parrot("a thousand", state="pushing up the daisies")  // 1 positional, 1 keyword
```

Note the type of the `voltage: Any` argument. In the Python function the argument `voltage` is used as a number in some cases and as a string in others.

`Any` can be uses in such cases; it means that the argument can be of any possible type. Obviously since it is a very general type there's not much we can do with it, except printing it and maybe puting it inside a continer.

### Arbitrary Argument Lists

When a function needs to take an arbitrary number of arguments the following syntax can be used:

```tut
def printMultipleInts(separator: String, args: Int*) =
  println(args.mkString(separator))
  
printMultipleInts(":", 1,2,3,4)
```

Inside the function `args` will be a sequence of integers which we can manipulate with the usual methods from the standard library.

> Note: There is no analogue to the `**kwargs` form that is common in Python libraries.
> 
> An alternative in this case is to use an explicit argument which is an actual Map. 


### Unpacking Argument Lists: `:_*`

The opposite situation to variable arguments would be when a function receives a variable list of arguments and the caller has the arguments inside a list for example:

```tut
val myArgs = List(1,2,3,4)

printMultipleInts(":", myArgs:_*)
```

This method is more limited than Python's since only functions that explicitly support varargs can be used with this "spread" operator.

### Lambda Expressions

```python
lambda a, b: a + b
```
becomes

```scala
(a: Int, b: Int) => a + b
```

Now, in many cases the type annotations can be inferred by the compiler. Consider the method `filter`. The following 3 forms are equivalent

```tut
List(1,2,3,4).filter((x: Int) => x % 2 == 0)
```
```tut
List(1,2,3,4).filter(x => x % 2 == 0)
```
```tut
List(1,2,3,4).filter(_ % 2 == 0)
```

This last form replaces each argument for a for a single `_`.

For example, to sum all elements of a list:

```tut
List(1,2,3,4).reduce(_+_)
```

which is equivalent to the more verbose

```tut
List(1,2,3,4).reduce((x,y) => x + y)
```

Lambdas can be the return value of functions:

```python
## 🐍
def make_incrementor(n):
    return lambda x: x + n

```

becomes

```tut
def makeIncrementor(n: Int) =
  (x: Int) => x + n
```

now, what is the return type of the previous function? It is the same as

```scala
val f: Int => Int = x => x + 1
//  |   \_______/   \________/
// name:    |           |
//         type       value (a lambda)
```

The above expression:

* Declares a `val` with name `f`
* and type `Int => Int`
* and value `(x: Int) => x + 1` (which is a lambda)

So lambdas of one argument have type `A => B`.

Of two arguments: `(A, B) => C`.

Of three arguments: `(A, B, C) => D`

and so on.

Lambdas have much more prominence in Scala than in Python due to the more functional bias of Scala. This can be seen in the collections library and in the special terse syntax.


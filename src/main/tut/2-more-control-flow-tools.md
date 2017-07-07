# More Control Flow Tools

Most of the usual Python control flow constructs translate into Scala but there are a few differences that we'll be careful to indicate.

## `if` Expressions {#if}

In Scala (unlike Python) an `if` is an **expression**, which means that it has a return value.

Consider the following Python code

```python
from random import randint
x = randint(-1,0,1)
message = ''
if x < 0:
    message = "Found a negative number"
elif x == 0:
    message = "Found zero"
elif x > 1:
    message = "Found a positive number"
    
print(message)
```

becomes

```scala
import util.Random.nextInt
val x = nextInt(3) - 1
val message =
  if (x < 0) "Found a negative number"
  else if (x == 0)  "Found zero"
  else if (x > 1) "Found a positive number"

println(message)
```

Python's `if` doesn't return anything, so we cannot say 

```python
message = if x < 0: ...
```
thereby forcing us to create one extra mutable variable (`message`).


> Python actually has a second `if` for that behaves in a similar (albeit limited) way:

```python
# Python
x = 0 if x < 0 else x0
```

## `for` comprehensions {#for-comprehensions}

`for` expressions (called "for comprehensions") are one of the most interesting aspects of Scala.

They operate in two ways. 

### As `for` loops
The first one is similar to a `for` statement in Python:

```python
words = ["Cat", "Dog", "Monkey"]
for w in words:
    print(w, len(w))
```
becomes

```tut:book
val words = List("Cat", "Dog", "Monkey")
for (w <- words)
  println(w, w.length)
```



> Note: `println` does not automatically transforms a list of arguments into a single space-separated string (as Python's `print` does).

#### Nested `for` loops
A nested for loop in Python:

```python
for x in range(2):
    for y in range(x, 4):
        for z in range(y, 2):
            print (x, y, z)
```

becomes

```tut:book
for {
  x <- 0 until 2
  y <- x until 4
  z <- y until 2
} println(x, y, z)
```


### As lists comprehensions

The second form operates similarly to Python's list comprehensions:


```python
words_length = [len(w) for w in words]
print(words_length)
```
becomes

```tut
val wordsLength = for (w <- words) yield w.length
println(wordsLength)
```

As you can see the only difference is that now we are using the keyword **`yield`** to indicate that we want to build a new collection based on the first `words` List.

(We'll spend more time on for comprehensions later).

## Ranges {#ranges}

| Python | Scala | values |
| ---    |---    | --- |
| `range(5)` | `(0 until 5)` | $0, 1, 2, 3, 4$
| `range(1,6)` | `(1 to 5)` | $1, 2, 3, 4, 5$
| `range(0, 10, 3)` | `(0 to 10 by 3)` | $0, 3, 6, 9$
| `range(5,-1,-1)` | `(5 to 0 by -1)`| $5, 4, 3, 2, 1, 0$

To iterate over the indices of a sequence you could do the same trick as in Python:

```python
lst = ["a", "b", "c"]
for i in range(len(lst)): print(i, lst[i])
```

becomes

```tut
val lst = List("a", "b", "c")
for (i <- 0 until lst.length) println(i, lst(i))
```

but there is a niftier way using the standard method `.indices`

```tut
for (i <- lst.indices) println(i, lst(i))
```

## Enumerate {#enumerate}
Another standard function in Python is `enumerate`:

```python
seasons = ['Spring', 'Summer', 'Fall', 'Winter']
list(enumerate(seasons))
[(0, 'Spring'), (1, 'Summer'), (2, 'Fall'), (3, 'Winter')]
```

Which has an analogue in Scala's `zipWithIndex` method:

```tut
val seasons = List("Spring", "Summer", "Fall", "Winder")
seasons.zipWithIndex
```

## `break`, `continue`, `else` clause {#break}

This is one of the cases where Scala shows its functional side by not having any of those.

Let's examine `break` first.

Consider the following Python code:

```python
for n in range(2, 10):
    for x in range(2, n):
        if n % x == 0:
            print(n, 'equals', x, '*', n//x)
            break
     else:
        print(n, 'is a prime number')
```

This can be translated mechanically to the following Scala code
 
```tut:book
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
for num in range(2, 10):
    if num % 2 == 0:
        print("Found an even number", num)
        continue
    print("Found a number", num)
```

becomes

```tut:book
var num = 2
while (num < 10) {
  if (num % 2 == 0)
    println(s"Found an even number $num")
  else
    println(s"Found a number $num")
  num += 1
}
```

## `pass` statements {#pass}


| Python | Scala
| ---    |---
| `while True: pass` | `while (true) {}`
| `class MyEmptyClass: pass` | `class MyEmptyClass`
| `def initlog(*args): pass` | `def initlog(args: Any*) = {}`

So in most cases the empty body (`{}`) is a good substitute for `pass` and in classes not even that is required.

## Defining Functions {#defining-functions}


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

This is not possible on function definitions, where Scala requires us to always declare the type of the arguments. 

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

which is meant to be read: "`x` is a variable that can only hold Ts".

For example:

| | | example |
|---| ---| --- |
| `s: String` | s is a String | `"abc"`
| `vec: Array[Double]`| vec is an array of Doubles | `Array(0.1, 0.2)`
| `mat: Array[Array[Double]]` | mat is an array of array of Doubles | `Array(Array(0.1, 0.2))`
| `a: Any` | a can be anything | `1` or `'c'` or `List()`, etc.

If a function doesn't return anything meaninful we can use the type `Unit`.

```python
def foo(x):
    print x + 1
```

becomes

```scala
def foo(x: Int): Unit = {
  println(x + 1)
}
```


In other languages `Unit` is called `void`.

A function signature with `Unit` should alerts us that the function's main purpose is not calculate but to interact with the world. For example it could print to the console, or write to a database, or remove a file.

### Generic functions
Many functions need to know the type of its arguments so that it can invoke methods / call functions or them.

```tut:silent
def add1(x: Int) = x + 1
//                   ^
//                   |
//            plus operator
```

The compiler needs to know that `x` is an `Int` (`x: Int`) in order to allow the use of the plus operator.

But what if within the body of the function we don't actually make use of any method or property associated with the type? In such cases we can make the function *agnostic* to the type of the argument. 

This is common for functions that wrap arguments, for example

```tut:silent
def toList[A](x: A) = List(x)
//         ^   
//         |
//  A is an unspecified type (a "type argument")
//  Note the use of square brackets [] !!

// full form:
toList[Int](1) == List(1)

// short form:
toList(1) == List(1)
```

This function takes 2 arguments: (1) a type, called `A` inside the function and (2) a value of type `A`.

In many cases (but not all), the type argument can be inferred by something else (in this case by the value argument `x`).

One example when the type cannot be inferred is when we want to construct an empty list:

```tut:silent
List[Int]()
```

Clearly in this case the compiler doesn't have any extra information so we have to provide the type of the elements explicitly.

## More on defining functions

### Default argument values
```python
def ask_ok(prompt, retries=4, reminder='Please try again!'): ...
```
becomes

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
becomes

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

```python
def print_multiple_items(separator, *args):
    print(separator.join(args))
```
becomes

```tut
def printMultipleItems(separator: String, args: Any*) =
  println(args.mkString(separator))
  
printMultipleItems(":", 1,2,3,4)
```

Inside the function `args` will be a sequence of values which we can manipulate with the usual methods from the standard library.

> Note: There is no analogue to the `**kwargs` form that is common in Python libraries.
> 
> An alternative in this case is to use an explicit argument which is an actual Map. 


### Unpacking Argument Lists: `:_*`

The opposite situation to variable arguments would be when a function receives a variable list of arguments and the caller has the arguments inside a list for example:

```python
my_args = [1,2,3,4]
print_multiple_items(":",*my_args)
```

becomes

```tut
val myArgs = List(1,2,3,4)
printMultipleItems(":", myArgs:_*)
```

This method is more limited than Python's since only functions that explicitly support varargs can be used with this "spread" operator.

### Lambda Expressions
A lambda expresion is just a name for an expression that represents a function but doesn't necessarily have a name. 

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

* Declares a value with name `f`
* and type `Int => Int`
* and value `(x: Int) => x + 1` (which is a lambda)

So lambdas of one argument have type `A => B`.

Of two arguments: `(A, B) => C`.

Of three arguments: `(A, B, C) => D`

and so on.

Lambdas have much more prominence in Scala than in Python due to the more functional bias of Scala. This can be seen in the collections library and in the special terse syntax.


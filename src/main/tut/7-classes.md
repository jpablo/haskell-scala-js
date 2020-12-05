# Classes

## Scopes {#scopes}

Scala uses curly braces `{}` instead of indentation to indicate code blocks. Every such block creates a new scope.

```scala:book
val i = 1
{
  val i = 2
  val j = 3
  println(i, j)
}
println(i)
```
In this code `j` is only visible inside the block. `i` has a different value inside and outside the block. We say that the inner `i` *shadows* the outer `i`.

One difference here is that in Python the expression `i = 1` serves both to create a new variable and update an existing one. Thus in some cases we need to use the keyword `global` to indicate that we want to reuse an existing name.

```python
a = 0
def function():
    global a
    a = 1
```
Becomes

```scala:silent
var a = 0
def function() =
  a = 1
```

## A first look at Classes {#first-look}

### Class Definition Syntax

```python
class ClassName:
    <statement-1>
    ...
    <statement-N>
```

Becomes

```scala
class ClassName {
    <statement-1>
    ...
    <statement-N>
}
```

### Class objects
Consider the following class declaration:

```python
class MyClass:
    i = 12345

    def f(self):
        return 'hello world'
```
Becomes

```scala:silent
class MyClass {
  val i = 12345
  
  def f() = "hello world"
}
```


There are two big differences here:

In Python

* A class behaves like a glorified function. 
* It is a value, and thus it can be passed around, stored in variable and in particular hold attributes that are not attached to any specific instance of the class. Such attributes are called *static*.

In Scala

* A class is not a value. It defines a new *type* + a way to create values of such type (using the `new` operator).
* It doesn't have static attributes. 

This means that `i` and `f` **cannot** be accesed via the class:

```scala:fail
MyClass.i
MyClass.f
```

## Class instances {#class-instances}
Class instantiation uses `new` keyword:

```python
x = MyClass()
```
Becomes

```scala:silent
val x = new MyClass
```

Now we can access the attributes view the newly created object

```scala
x.f()
```

### Class arguments
Python has the magic method `__init__` that is executed when you create a new instance of the class. It's arguments become the class's arguments.

In Scala, the arguments of the constructor are defined right after the class name and the body of the class is executed at instance creation time:

```python
class Complex:
    def __init__(self, realpart, imagpart):
        self.r = realpart
        self.i = imagpart
        self.rxi = self.r * self.i

c = Complex(3.0, -4.5)
c.r, c.i,c.rxi
```
Becomes

```scala:book
class Complex(val r: Double, val i: Double) {
  val rxi = r * i
}
val c = new Complex(3.0, -4.5)
(c.r, c.i, c.rxi)
```

* We use `val r: ...` to indicate that the attributes are *public*. Otherwise they are private.
* If we want *public mutable* attributes we would use `var r: ...`

### Case classes

For this type of container classes it is convenient to give them a nice string representation and also to not having to call `new` everytime. 

The keyword `case` can be used to create a *case class*:

```scala
case class Complex(r: Double, i: Double)
val x = Complex(3.0, -4.5)
(x.r, x.i)
val y = x.copy(i = 0)
val Complex(r,i) = y
```

Case classes:

* have public immutable attributes by default
* don't require `new` to instantiate
* have a nice string representation
* have a `copy` method
* can be used in pattern matching

As mentioned before they have some resemlance with Python's `namedtuple`s.

### Class and Instance Variables

In contrast to most OO languages, classes in Scala cannot contain static (shared) attributes. Instead all the shared attributes can be defined in an singleton `object` with the same name as the class, refered to as the `companion object` of the class.

For example

```python
class Dog:
    kind = 'canine'         # class variable shared by all instances
    
    def __init__(self, name):
        self.name = name    # instance variable unique to each instance
```
Becomes

```scala:silent
class Dog(val name: String)

object Dog {
  val kind = "canine"
}
```
`class Dog(...)` creates a __type__ (besides the class structure itself). `object Dog {...}` creates a __value__. Types and values live in different namespaces and thus there is *no* collision.

While a companion object can be defined everywhere, if it's defined in the same file than the class then it can access the companion class private members (and viceversa)


## Inheritance {#inheritance}

```python
class DerivedClassName(BaseClassName):
    <statement-1>
    ...
    <statement-N>
```
Becomes

```scala
class DerivedClassName extends BaseClassName {
  <statement-1>
  ...
  <statement-N>
}
```

### Multiple Inheritance

```python
class DerivedClassName(Base1, Base2, Base3):
    <statement-1>
    ...
    <statement-N>
```
becomes

```scala
class DerivedClassName extends Base1 with Base2 with Base3 {
  <statement-1>
  ...
  <statement-N>
}
```

## Abstract members / incomplete definitions {#abstract-members}

Consider the following class

```scala:silent
abstract class Showable {
  def show(): String
}
```

Notice how we didn't provide any implementation of the method `show`. Such methods are called *abstract*. If there is at least one abstract method in a class declaration Scala requires us to indicate that by using the `abstract` modifier.

An abstract class *cannot* be instanciated directly (since it is "incomplete"). 

Nevertheless, it is very useful since it provides a *contract* about some functionality.

For example, we can define a function that operates on Showables:

```scala:silent
def print(s: Showable) = s.show()
```

In order to use `show` we can use inheritance:

```scala:silent
class MyClass(x: Int) extends Showable {
  def show(): String = s"[$x]"
}

class MyUnrelatedClass(l: List[Int]) extends Showable {
  def show(): String = s"count: ${l.length}"
}

val mc = new MyClass(10)

print(mc) == "[10]"

val uc = new MyUnrelatedClass(List(1,2,3))

print(uc) == "count: 3"
```

The beauty of this is that `print` can ignore all the "irrelevant" (to the task at hand) details about the concrete argument, as long as it complies with the contract.

### Objects that behave like functions

Both Python and Scala provide a way to create an object that can be invoked as a function:


```python
class Factory:
    def __init__(self, x):
        self.x = x

    def __call__(self, y):
        return self.x + y

fn = Factory(10)    
fn(5) == 15
```

becomes

```scala:silent
class Factory(x: Int) {
  def apply(y: Int) = x + y 
}

val fn = new Factory(10)
fn(5) == 15
```

It's worth noting that the companion object of a class can also have an `apply` method. This is commonly used for factories. In our previous example, if we wanted to avoid calling `new ...` every time we could've done this:

```scala:silent
class Factory(x: Int) { 
  // allow *instances* to be invoked
  def apply(y: Int) = x + y
}

object Factory {
  // allow the *Factory singleton object* to be invoked
  def apply(x: Int) = new Factory(x)
}

val fn = Factory(10)
fn(5) == 15
```

To reiterate, 

```scala:silent
Factory(10) == Factory.apply(10)

// and

fn(5) == fn.apply(5) == 10 + 5 == 15
```

This idiom is used a lot in Scala code, so it's very useful to be familiar with it. 


* **Any time you see something used as a function, there is an `apply` method involved.**

* **If you see something that looks like a _type_ used as a function, then it is the _companion object's apply method_ what is invoked.**



## Iterators {#iterators}

Python has the notion of *iterable* objects capable of returning its members one at a time. This is accomplished by implementing a method `__iter__` that returns an *iterator*.

An Iterator is an object that has a method `__next__` to get the next element of the container. 


The standard function `iter()` invokes `__iter__` while `next()` invokes `__next__()`

```python
l = [1,2,3]
it = iter(l)
next(it) == 1
next(it) == 2
next(it) == 3
next(it) # raises StopIteration
```
becomes

```scala:silent
val l = List(1,2,3)
val it = l.iterator
it.next == 1
it.next == 2
it.next == 3
```

```scala:fail:book
it.next
```

in Python iterators are extremly common. In fact, `for` expressions are implemented in terms of `iter()` / `next()`.

For example, say we want to be able to use instances of our class in `for` comprehensions:

```python
class Reverse:
    """Iterator for looping over a sequence backwards."""
    def __init__(self, data):
        self.data = data
        self.index = len(data)

    def __iter__(self):
        return self

    def __next__(self):
        if self.index == 0:
            raise StopIteration
        self.index = self.index - 1
        return self.data[self.index]
        
r = Reverse([1,2,3])
[x for x in r] == [3, 2, 1]
```


In Scala things are implemented slightly different. Collections and iterable things are implemented in terms of 2 trais: `Traversable` and `Iterable`.

### Trait `Traversable`.
`Traversable` declares the method foreach:

```scala
def foreach[U](f: Elem => U)
```

Any class that implements `foreach` can be used as part of a `for` expression.


```scala:silent
class Reverse[Elem](data: Seq[Elem]) {
  
  def foreach(f: Elem => Unit) = {
    var i = data.length - 1
    while (i >= 0) {
      f(data(i))
      i -= 1
    }
  }
  
}

val r = new Reverse("123")
```

```scala
for (i <- r) println(i)
```

If a class also *extends* the trait `Traversable` then it gains access to *a lot* of methods for free.

```scala:silent
class Reverse2[Elem](data: Seq[Elem]) extends Traversable[Elem] {
  
  def foreach[U](f: Elem => U) = {
    var i = data.length - 1
    while (i >= 0) {
      f(data(i))
      i -= 1
    }
  }
  
}

val t2 = new Reverse2(List(1,2,3))
```
```scala
t2 ++ t2
t2.head
t2.toArray
t2.groupBy(_ % 2 == 0)
t2.size
t2.isEmpty
t2.filter(_ != 0)
t2.count(_ > 1)
t2.reduceLeft(_ + _)
```

### Trait `Iterable`
The other trait of interest for us now is `Iterable`. It declares a method `iterator`, which yields the contents of this collection one by one.

```scala
def iterator: Iterator[Elem]
```

Let's reimplement our silly container using `Iterable`:

```scala:silent
class Reverse3[Elem](data: Seq[Elem]) extends Iterable[Elem] {
  
  def iterator = new Iterator[Elem] {
    private var i = data.length
    def hasNext: Boolean = i > 0
    def next: Elem = { i -= 1; data(i) }
  }
  
}

val t3 = new Reverse3(List(1,2,3))
```

```scala
val it3 = t3.iterator
it3.next

for (i <- t3) println(i)
```

Again, by implementing this abstract method `Reverse3` gains a lot of standard methods for free, including `foreach`!. Check how `foreach` can be implemented in terms of `iterator`:

```scala
def foreach[U](f: Elem => U): Unit = {
  val it = iterator
  while (it.hasNext) f(it.next())
}
```

## Generators and Generator Expressions {#generators}

Generators in Python provide a lightweight way to create iterators. They come it two types:

1. Via *generator expressions*
2. Via the `yield` keyword


While Scala lacks direct support for generators, the same functionality can be achieved in a slightly different way.

Case 1 is supported via the method `.view` in the stdlib collections: 

```python
sum(i*i for i in range(10))
```
becomes

```scala
(for(i <- (0 until 10).view) yield i*i).sum
```


`view` creates a new iterator that only process the elements on an as-needed basis.


Case 2 can be handled using `collection.immutable.Stream`.

Streams in the stdlib are essentially a lazy version of a Lists.  It can be used like so:

```scala
val s = 1 #:: 2 #:: 3 #:: Stream.empty
```

Similarly to lists which are constructed with the `::` operator (`1 :: 2 :: Nil`), Streams are constructed with the `#::` operator. The difference is that `#::` doesn't evaluate the second argument. 

To see this let's define a function that prints and returns its argument:

```scala
def print(i: Int) = { println(i); i }
val s2 = print(1) #:: print(2) #:: print(3) #:: Stream.empty
s2.tail
```

As you can see only the first `print()` was actually evaluated. Dereferencing the tail triggered an evaluation of the second `print()`.  

Using this kind of lazy behaviour it is possible to construct infinite lists.

For example, we could generate the fiboncci sequence like this:

```scala:silent
def fib(a: Int, b: Int): Stream[Int] = a #:: fib(b, a + b)
```

```scala:book
fib(1,1).take(10).toList
```
which is the same as 

```python
def fib(a, b):
    while True:
        yield a
        a, b = b, a + b
        
[f for (f, _) in zip(fib(1,1), range(10))] == [1, 1, 2, 3, 5, 8, 13, 21, 34, 55]
```


In a normal list this would cause a stack overflow since the recursion never terminates. In our Stream example we are terminating the recursion explicitly after 10 steps and then forcing the evaluation by transforming into a list.

Armed with streams we can go back to our Python example

```python
def reverse(data):
    for index in range(len(data)-1, -1, -1):
        yield data[index]
        
for char in reverse("golf"):
    print(char)        
    
### f
### l
### o
### g    
```

becomes

```scala:silent
def reverse[A](data: Seq[A]) = {
  def go(index: Int): Stream[A] = 
    if (index >= 0) data(index) #:: go(index - 1) else Stream.empty
    
  go(data.length - 1)
}
```

```scala:book
for (char <- reverse("golf")) 
    println(char)
```


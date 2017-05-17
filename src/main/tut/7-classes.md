# Classes

## Scopes

Scala uses curly braces `{}` instead of indentation to indicate code blocks. Every such block creates a new scope.

```tut:book
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

```tut:silent
var a = 0
def function() =
  a = 1
```

## A first look at Classes

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

### Class Objects
Consider the following class declaration:

```python
class MyClass:
    i = 12345

    def f(self):
        return 'hello world'
```
Becomes

```tut:silent
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

```tut:fail
MyClass.i
MyClass.f
```

## Class instances
Class instantiation uses `new` keyword:

```python
x = MyClass()
```
Becomes

```tut:silent
val x = new MyClass
```

Now we can access the attributes view the newly created object

```tut
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

```tut:book
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

```tut
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

```tut:silent
class Dog(val name: String)

object Dog {
  val kind = "canine"
}
```
`class Dog(...)` creates a __type__ (besides the class structure itself). `object Dog {...}` creates a __value__. Types and values live in different namespaces and thus there is *no* collision.

While a companion object can be defined everywhere, if it's defined in the same file than the class then it can access the companion class private members (and viceversa)

## Inheritance

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

```tut:silent
class Factory(x: Int) {
  def apply(y: Int) = x + y 
}

val fn = new Factory(10)
fn(5) == 15
```

It's worth noting that the companion object of a class can also have an `apply` method. This is commonly used for factories. In our previous example, if we wanted to avoid calling `new ...` every time we could've done this:

```tut:silent
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

```tut:silent
Factory(10) == Factory.apply(10)

// and

fn(5) == fn.apply(5) == 10 + 5 == 15
```

This idiom is used a lot in Scala code, so it's very useful to be familiar with it. 


* **Any time you see something used as a function, there is an `apply` method involved.**

* **If you see something that looks like a _type_ used as a function, then it is the _companion object's apply method_ what is invoked.**



## Iterators

Python has the notion of *iterable* objects capable of returning its members one at a time. This is accomplished by implementing a method `__iter__` that returns an *iterator*.

An Iterator is an object  has a method `__next__` to get the next element of the container. 


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

```tut:silent
val l = List(1,2,3)
val it = l.iterator
it.next == 1
it.next == 2
it.next == 3
```

```tut:fail:book
it.next
```

in Python iterators are extremly common. In fact, `for` expressions are implemented in terms of `iter()` / `next()`.

For example:

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


```tut:silent
class Reverse[Elem](val data: Seq[Elem]) {
  
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

```tut
for (i <- r) println(i)
```

If a class also *extends* the trait `Traversable` then it gains access to *a lot* of methods for free.

```tut:silent
class Reverse2[Elem](val data: Seq[Elem]) extends Traversable[Elem] {
  
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
```tut
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

```tut:silent
class Reverse3[Elem](val data: Seq[Elem]) extends Iterable[Elem] {
  
  def iterator = new Iterator[Elem] {
    private var i = data.length
    def hasNext: Boolean = i > 0
    def next: Elem = { i -= 1; data(i) }
  }
  
}

val t3 = new Reverse3(List(1,2,3))
```

```tut
val it3 = t3.iterator
it3.next

for (i <- t3) println(i)
```

Again, by implementing this abstract method `Reverse3` gains a lot of standard methods for free, including `foreach`!. Check how `foreach` can be implemented in terms of `iterator`:

```scala
def foreach[U](f: Elem => U): Unit = {  val it = iterator  while (it.hasNext) f(it.next())}
```

## Generators and Generator Expressions

There is no equivalent notion of generators in Scala




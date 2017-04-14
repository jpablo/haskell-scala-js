# Classes

## Scopes

Scala uses curly braces `{}` instead of indentation to indicate code blocks. Every such block creates a new scope.

```tut
val i = 1
{
  val i = 2
  val j = 3
  println(i, j)
}
println(i)
```
In this code j is only visible inside the block. `i` has a different value inside and outside the block. We say that the inner `i` *shadows* the outer `i`.

One difference here is that in Python the expression `i = 1` serves both to create a new variable and update an existing one. Thus in some cases we need to use the keyword `global` to indicate that we want to reuse an existing name.

```python
a = 0
def function():
    global a
    a = 1
```
Becomes

```tut
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

There is one big difference here. In Scala there is a strict separation between *static* attributes vs regular attributes.

This means that `i` and `f` **cannot** be accesed via the class:

```tut:fail
MyClass.i
MyClass.f
```

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
x.i
x.f()
```

### Class arguments
```python
class Complex:
    def __init__(self, realpart, imagpart):
        self.r = realpart
        self.i = imagpart

x = Complex(3.0, -4.5)
x.r, x.i
```
Becomes

```tut
class Complex(val r: Double, val i: Double)
val x = new Complex(3.0, -4.5)
(x.r, x.i)
```

We use `val r: ...` to indicate that the attributes are *public*. Otherwise they are become private.

### Case classes

For this type of container classes it is convenient to give them a nice string representation and also to not having to call `new` everytime. 

The keyword `case` can be used to create a *case class*:

```tut
case class Complex(r: Double, i: Double)
val x = Complex(3.0, -4.5)
(x.r, x.i)
val y = x.copy(i =0)
val Complex(r,i) = y
```

Case classes:

* have public immutable attributes by default
* don't require `new` to instantiate
* have a nice string representation
* have a `copy` method
* can be used in pattern matching


### Class and Instance Variables

In contrast to most OO langagues, classes in Scala cannot contain static (shared) attributes. Instead all the shared attributes can be defined in an `object` with the same name as the class, refered to as the `companion object` of the class.

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

Strictly speaking there's no relationship between the class `Dog` and the object `Dog` except for the name. So it is more  a convention than anything else.


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

## Iterators

Python has the notion of *iterable* objects capable of returning it's members one at a time. This is accomplished by implementing a method `__iter__` that returns an *iterator*.

An Iterator is an object  has a method `__next__` to get the next element of the container. 

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
```

The iterator can be obtained by calling the global function `iter` on an instance

```python
rev = Reverse('spam')
it = iter(rev)
```

Once an object implements this interface, it can be used in for loops:

```python
for c in rev:
  print c
```

In Scala a class needs to implement the method `foreach` to be usable in for loops:

```tut:silent
class Reverse(data: String) {
  def foreach[U](f: Char => U) =
    data.reverse.foreach(f)
}
```

```tut
val rev = new Reverse("spam")
for (c <- rev) println(c)
```

## Generators and Generator Expressions

There is no equivalent notion of generators in Scala




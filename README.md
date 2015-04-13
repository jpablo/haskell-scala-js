# Functions
## Haskell
```haskell
g :: Int -> Int -> Int -> Int
g x y z = x + y + z
```
### Currying
All functions are curried in haskell
```haskell
h :: Int -> Int
h = g 2 3
```
## Scala
method
```scala
object myModule {
    def g(x: Int, y: Int, z: Int) = x + y + z
}
```
function
```scala
val f = (x: Int, z: Int, z: Int) => x + y + z
```
methods are not values, just object attributes: pros: can have type parameters

functions are values, so they can't have type parameters. pros: they can be passed around and have several useful methods, as `curried`

explicit η-expansion transforms a method into a function:
```scala
g _
```
```sbt
scala> :t g _
(Int, Int, Int) => Int
```
functions can be curried
```scala
scala> g _ curried
res4: Int => (Int => (Int => Int)) = <function1>
scala> (g _ curried)(1)
Int => (Int => Int) = <function1>
scala> (g _ curried)(1)(2)
Int => Int = <function1>
scala> (g _ curried)(1)(2)(3)
Int = 6
```
### JS
```javascript
function g(x, y, z) { return x + y + z }
// or 
var g = function(x, y, z) { return x + y + z }
// no currying in the language or std library
// must use a library like lodash:
_.curry(g)(1)(2)(3)
// essentially the same as scala's `g _ curried`
// the manually coded version would be:
var curried = function(x) {
    return function(y) {
        return function(z) { return x + y + z }
    }
}
curried(1)(2)(3)
```
## High order functions
Haskell, scala and JS support passing around and returning functions

### Haskell
```haskell
compose f g = \x -> f (g x)
```
### Scala
```scala
def compose[A,B,C](f: B => C, g: A => B) = (x: A) => f(g(x))
```
### JS
JS being a dynamic language allows you do pass around whatever the #@!% you want.
```javascript
function compose(f, g) { return function(x) { return f(g(x)) }}
```
# Datatypes
## Sum types
Sum types are also called tagged unions
### haskell
```haskell
data Sum = A Int | B Bool
```
this defines a type `Sum` and two data constructors `A` and `B`
### scala
Scala uses sealed traits as sum types
```scala
sealed trait Sum
case class A(a: Int) extends Sum
case class B(b: Int) extends Sum

// pattern matching support:
x match {
  case A(a) => println("got an A!")
  case B(b) => println("got a B!")
}
```
### JS
Not sure this can be done with vanilla JS

## Product types
### haskell
```haskell
data Vector = Vector { x::Float, y :: Float }
```
### scala
```scala
case class Vector(val x: Float, val y: Float)
```

### JS
```javascript
function Vector(x, y) {
  if(!(this instanceof Vector) ) return new Vector(x,y);
  this.x = x;
  this.y = y;
}
```
## Parametrized datatypes
### haskell
```haskell
data Option a = Some a | None
```
### scala
```scala
sealed trait Option[+A]
case class Some[+A](a: A) extends Option[A]
case object None extends Option[Nothing]
```

# Pattern matching
## haskell
```haskell
data Option a = Some a | None

option :: b -> (a -> b) -> Option a -> b
option n f None  = n
option n f (Some a) = f a

-- can also be written using case statements:
option :: b -> (a -> b) -> Option a -> b
option n f x = case x of
  None -> n
  Some a  -> f a
```
## scala
```scala
def option[A, B](n: B, f: A => B, x: Option[A]) = x match {
    case None ⇒ n
    case Some(a) ⇒ f(a)
}
```

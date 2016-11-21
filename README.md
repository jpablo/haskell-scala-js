
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
## kinds
Values have types and can be passed around.
Types have kinds (not unlike the type of a type)
### haskell
```haskell
-- values
1, [1,2,3]
-- types
Int, [Int]
-- kinds
*
```
```haskell
-- this defines a type Bool, and to values: False and True
data Bool = False | True

Prelude> :t True
True :: Bool
Prelude> :k Bool
Bool :: *

-- this defines a type IntContainer (the left side) and a value (a function value), 
-- the constructor IntContainer
data IntContainer = IntContainer Int

Prelude> :t IntContainer
Container :: Int -> IntContainer
Prelude> :k IntContainer
Container :: *

-- this defines a type constructor * -> *
data Container t = Container t

Prelude> :t Container
Container :: t -> Container t
Prelude> :k Container
Container :: * -> *


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
def option[A, B](n: B, f: A ⇒ B, x: Option[A]) = x match {
    case None ⇒ n
    case Some(a) ⇒ f(a)
}
```

# Variables

```python
x = 5
# all variables are mutables
# Ok!
x = 6 
```
```scala
// mutable
var x = 1
// immutable
val y = 2
```

```javascript
// mutable variable
let x = 1
x = 2
// immutable variable
const y = 1
// error!
// y = 2
```
```haskell
-- all variables are immutable
-- top level:
x = 1
-- let block
let x = 1
in
   ... -- x == 1 here
-- where block
x + 1
    where x = 1
```

# Functions

```python
# named functions
def f(x,y,z):
    return x + y + z
    
# anonymous functions    
# (several limitations)
g = lambda x, y, z: x + y + z
```

## Scala
### Methods

```scala
class C {
    // monomorphic method
    def m(x: Int): Int = x + 1
    
    // polymorphic method
    def p[A](a: A): List[A] = List(a)
    
    // multiple arguments
    def f(x: Int, y: Int, z: Int): Int = x + y + z
    // or, special support for a pattern matched tuple functions
    // f: (Int, Int, Int) => Int
}

val c = new C
c.f(1,2,3)
```
Methods are not values unto themselves, just object attributes. But can be transformed easily and in many cases automatically into functions, which are values (see below).

They can have type parameters.

####Functions
```scala
// the unicode arrow ⇒ is an alias for the fat arrow =>
val f = (x: Int, z: Int, z: Int) ⇒ x + y + z
```

Functions are values, so they can't have type parameters because in Scala values are always monomorphic.

As any value they can be passed around and have several useful methods, as `curried`

It is possible to explictly transform a method into a fuction by a process called  η-expansion.

```scala
// the placeholder operator in argument position transforms a method into a function:
def f(x: Int) = x

scala> :t f _
(Int) => Int

// The _ operator triggers the transformation
// f _ == (x: Int) => f(x)
// where the left hand side is a method and the rhs is a function object

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

// or
val f = (x: Int)(y: Int)(z: Int) = x + y + z
f(1)(2)(3)
```

## Haskell
A named function in haskell is defined via pattern matching.

```haskell
-- g is a function from Int to functions Int -> Int

g :: Int -> Int -> Int -> Int
g x y z = x + y + z

-- anonymous functions
h = \a b c -> a + b + b
```
### Currying
All functions are curried in Haskell. An uncurried function would be simply a function that receives a tuple as an argument.


```haskell
h :: Int -> Int
h = g 2 3

-- functions on tuples resemble a regular function in most languages
f :: (Int, Int) -> Int
f (x,y) = x + y
```

Any undeclared type in a function definition becomes generic

```haskell
f x y = x + y
```
Without any type annotation, the inferred type is 

```haskell
f :: Num a => a -> a -> a
```
Which means that both `x` and `y` arguments are assumed to have a generic type `a` such that an instance of type `Num a` exists.


### JS
```javascript
function g(x, y, z) { return x + y + z }
// or 
var g = function(x, y, z) { return x + y + z }
// or
const g = (x,y) => x + y
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
// es6
const g = (x) => (y) => (z) = x + y + z
g(1)(2)(3)
```


## High order functions
Haskell, scala and JS support passing around and returning functions

### Haskell
```haskell
compose f g = \x -> f (g x)
-- functions are generic my default in haskell, types are inferred at call site
compose (+1) (+2) 1
4
```
### Scala
```scala
def compose[A,B,C](f: B ⇒ C, g: A ⇒ B) = (x: A) ⇒ f(g(x))
// generic type need to be specified in the function definition in scala.
// this method takes 5 arguments: types A, B, C and functions f, b

// the type arguments can be specified explicitly
compose[Int, Int, Int](_+1, _+2)(1)

// or inferred from the definitions of f and g
compose((x: Int) ⇒ x+1, (x: Int) ⇒ x+2)(1)
```
### JS
JS being a dynamic language allows you do pass around whatever the #@!% you want.

```javascript
function compose(f, g) { return function(x) { return f(g(x)) }}
```
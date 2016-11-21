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

-- tuples
f :: Int -> Int -> Int
f (x,y) = x + y
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
// the unicode arrow ⇒ is an alias for the fat arrow =>
val f = (x: Int, z: Int, z: Int) ⇒ x + y + z
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

// or
val f = (x: Int)(y: Int)(z: Int) = x + y + z
f(1)(2)(3)
```
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

### Python
```python
def f(x,y):
    return x + y
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
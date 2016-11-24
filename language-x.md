# Language X

# basics

Unholy child of Scala + PureScript + JavasScript + Python + Haskell + OCaml

* Strict
* Interpreted + Compiled
* Compiles to JS, JVM, Native
* Type System similar to PureScript / Haskell
* Optionally: flag to turn off the type system
* Effects?
* Built-in Liquid Haskell
* Records with dot notation
* importers from different languages (typescript, scala, etc)
* first class type classes
* first class support for ADTs
* easy to define interpreters
* Library of algebraic structures (with common names a la fantasyland? or more beginner friendly names?)
* Sum types
* String separation of data vs behaviour
* structural types ???
* Optional  { } ??
* Tab as syntax ?
* Clean syntax / model for type level programming
* macros
* type providers ??
* OCaml object system??
* awesome repl


## ideas
analogous to how you can use prefixes to string to construct different things in Scala / Python

use prefixes to arrays to indicate for example mutable arrays, immutable arrays, vectors, lists, etc

mutable array
`marray = m[1,2,3]`

etc

### versions
maintain multiple versions of the syntax optimized for
* JS users
* Python users

Provide ports of the std libraries for each version

### language levels
0. Dynamic, no effect tracking, etc
1. Static, no effect tracking
2. Static, effect tracking
    explore Eff, Monad transformers, etc
3. Liquid Types
4. Dependent types?

# Functions
Curried by default

```
def f x y z = x + y + z

f 1 2 3 = 6

f 1 2 3 == (((f 1) 2) 3)
```

But powerful language for pattern matching

```
def f | (x, y, z) = x + y + z
      | (0, _, _) = 0

def g { name, age } = age + 1
ob = { name: 'John', age: 20 }
g ob == 21 == g(ob) == (g ob)


def a |+| b = ...
def a + b = ...
def a . b = ... 

// compilation time (macros)
transform a + b = (+ a b) 
```

Flexible type annotation syntax
```
// 1. no annotation
def f x y = x + y

// 2. PureScript like
 f : Int => Int => Int 
def f x y = x + y

// 3. Scala like
def f(x:Int, y:Int): Int = x + y

g : { name: A, age: Int } => Int 
def g { name, age } = age + 1 
```

Generic arguments
```
f : A => B => A
def f x y = x

// type constraints
 
[A : Semiring] 
f : A => A => A
def f x y = x + y

```

# lambda syntax
```
// option 1
\x y = x + y

// option 2
x y => x + y

// option 3
_ + _

```

# Records
```
// option 1   ???
point = { x: 1, y: 2 }
// this seems complicated (TypeScript)
// potential confusion with type ascription

// option 2
point = { x = 1, y = 2 }
```

# Type definitions

```
type TInt A = (A, Int)
type I = Int
type Id X = X
type Function1 A B = A => B
type Point2 = { x: Int, y: Int }
type Option1 = Int || Char || Float
type \A => A
```

# Data
```
data Point2 A = { x: A, y: A }

// we need to define
// 1. A type Point2
// 2. A constructor (A,A) => Point2 A
// 3. Extractors .x, .y
// 4. Patter matcher

// defines
// 1. A constructor function
// Point2 1 2  ??
// Point2(1,2) ??
// Point2(x=1,y=2)
// 2. Extractors
// p = Point2 1 2
// p.x == .x p
// p.y == .y p

// analogous to Scala's
case class Point2[A](x: A, y: A)
```

# Behaviour
```
interface Point2 A {
    def p1 + p2 = { x = p1.x + p2.x, y = p1.y + p2.y }
}

interface Show A {
    def show : A => String
}




```
# Data Structures

## More on mutable.Buffer

| Python | Scala `mutable.Buffer`| Notes
| ---    | --- |---
|`list = []` | `val b = Buffer[Int]()`
|`list.append(x)`|`b.append(x)`
|`list.extend(iterable)`|`b.appendAll(iterable)`
|`list.insert(i,x)`|`b.insert(i, elems*)`
|`list.remove(x)`|`b.remove(i)`
|`list.pop([i])`|`b.remove(b.length - 1)`
|`list.clear()`|`b.clear()`
|`list.index(x[,start[,end]])`|`b.indexOf(x[,start])`
|`list.count(x)`|`b.count(_ == x)`
|`list.sort([key])`|`b.sorted` <br>`b.sortBy(key)`| Creates a new Buffer
|`list.reverse()`|`b.reverse`
|`list.copy()`|`b.clone`

### Stacks
Scala has a `Stack` class that is deprecated now. The recommended alternative is to use a `List` assigned to a `var`. 



```scala
var stack = List(3,2,1)
stack = 4 :: stack // "push"
stack.head // "top"
stack = stack.tail // "pop"
```

### Queues

```tut
import collection.mutable
val q = mutable.Queue[String]("Eric", "John")
q.enqueue("Terry")
q.dequeue()
q.dequeue()
q
```

### For comprehensions

```python
squares = [x**2 for x in range(10)]
```

Translates into

```tut
val squares = for(x <- 0 until 10) yield x*x
```

#### Guards
```python
[(x, y) for x in [1,2,3] for y in [3,1,4] if x != y]
```
Translates

```tut
for {
  x <- List(1,2,3)
  y <- List(3,1,4) if x != y
} yield (x,y)
```

More examples

```tut:silent
val vec = List(-4, -2, 0, 2, 4)
for(x <- vec) yield x*2
 // same as
vec.map(_ * 2)

for(x <- vec if x >= 0) yield x
// same as
vec.filter(_ >= 0) 

val freshFruit = List("  banana", "  loganberry ", "passion fruit  ")
for(fruit <- freshFruit) yield fruit.trim
 // same as
freshFruit.map(_.trim)

for(x <- 0 until 6) yield (x, x*x)
 // same as
(0 until 6).map(x => (x, x*x))
val lists = List(List(1,2,3),List(4,5,6),List(7,8,9))

for(lst <- lists; l <- lst) yield l
 // same as
lists.flatMap(lst => lst.map(identity))
 // or even
lists.flatten
```


#### Nested For Comprehensions
For comprehensions have the effect of *flattening* inner structures:

```tut
val lst = List(Vector(1,2), Vector(), Vector(3))
for(v <- lst; i <- v) yield i+1
```

If we don't want this then we can nest them:


```tut
val matrix = List(
  List(1, 2, 3, 4),
  List(5, 6, 7, 8),
  List(9, 10, 11, 12)
)

for(i <- 0 until 4) yield { for(row <- matrix) yield row(i) }
```

(which can be done with the builtin `matrix.transpose`)

## `del` statement

Although you can remove elements from collections (we've seen `buffer.remove(i)`), there's no way to remove *variables* from a scope in Scala.

## Tuples
Tuples in Scala share some similarities with Python tuples

| Python | Scala | Notes
| ---    | --- |---
| `t = 1, True, "Hello"` | `val t = (1, true, "Hello")` | Heterogenous types
| `u = t, (4, 5)` | `val u = (u, (4, 5))` | Nested tuples
| `t[0] == 1` | `t._1 == 1` | element access
| `t[0] = 2 # Error!!` | `t._1 = 2` // Error!! | immutable
| Differences|
| `for i in t: print(t)` | `for(i <- t.productIterator) println(i)` | catch: `productIterator` forgets type information

Scala tuples can have heterogenous types, but there's a caviat: the only way to safely recover the elements is using the accessor funcions `t._1, t._2, t._3, ... ` etc (up to 22).

Also, tuples of different lengths are completely unrelated to each other, so if a function for example expects a 2-tuple then the only way to use 3-tuple is by manually constructing the required 2-tuple.

There's a way to access an arbitrary element of a tuple given a index `i` using the function `t.productElement(i)`, but with a catch: The result will have the type `Any` which is not very useful or safe except for printing to the console perhaps.

In other words, the type of each element will be lost.

Btw, Python is in a similar situation here since being a dynamic language the interpreter doesn't have any kind of type information until it actually runs the program.

### Namedtuples / Case Classes
Instead of `namedtuples` you can use *case classes* which will be discussed at length later

```python
from collections import namedtuple
Point = namedtuple('Point', ['x', 'y'])
p = Point(11, y=22)
p[0] + p[1]
x, y = p
```

Becomes

```tut
case class Point(x: Int, y: Int)
val p = Point(1, 22) // or Point(x=1,y=22)
p.x + p.y
val Point(x,y) = p // destructuring
(x,y)
val p2 = p.copy(y = 0) // copy constructor
```

## Sets

There's no special syntax for sets in Scala so we'll just use the standard library constructors.

| Python | Scala | Notes
| ---    | --- |---
| `basket = {'apple', 'orange', 'apple'}`|`val basked = Set("apple", "orange" apple")`| Scala: Immutable version
|`'orange' in basket`|`basket contains "apple"`
|`a = set('abracadabra')`|`val a = Set("abracadabra":_*)`| Spread operator `(:_*)` required
| `b = set('alacazam')`|`val b = "alacazam".toSet` | Much easier
|`a - b`|`a &~ b` | Or `a diff b`
|<code>a &#124; b</code>|<code>a &#124; b</code>| Or `a union b`
|`a & b`|`a & b` | Or `a intersect b`
|`a ^ b`| <code>(a &~ b) &#124; (b &~ a)</code> | Or `(a diff b) union (b diff a)`


Unless you are a heavy user of Sets I suggest sticking with the named functions instead of the operators.

There's no special support for set comprehensions, so

```python
{x for x in 'abracadabra' if x not in 'abc'}
```

becomes

```tut
(for (x <- "abracadabra" if "abc" contains x) yield x).toSet
```

## Dictionaries

We'll use `collection.mutable.Map[A,B]`. One big difference here is that Scala `Map` is again a *homogenous* collection whereas Python dicts are heterogenous. We'll see ways to cope with this situation.

| Python | Scala | Notes
| ---    | --- |---
|`d = {}`| `val d = mutable.Map[String, Int]()` | Empty dict requires explicit type information
|`tel = {'jack': 4098, 'sape': 4139}`|`val tel = mutable.Map("jack" -> 4098, "sape" -> 439)` | type info can be inferred from args here
|`tel['guido'] = 4127` | `tel('guido') = 4127`
|` del tel['sape']` | `tel -= "sape"`
|`list(tel.keys())` | `tel.keys.toList`
|`sorted(tel.keys())`|`tel.keys.toList.sorted`
|`'guido' in tel` | `tel contains "guido"`
| `dict([('a',1),('b',2)])`| `List(("a",1),("b",2)).toMap`


As you can see the `Map` constructor takes a variable number of pairs `key -> value`. The `(->)` operator is in fact a helper operator that constructs tuples, so that `Map(('a',1),('b',2))` is the same as 
`Map('a' -> 1, 'b' -> 2)` but (arguably) harder to read.

## Looping Techniques

### Over maps

```python
knights = {'gallahad': 'the pure', 'robin': 'the brave'}
for k, v in knights.items():
    print(k, v)
```

Becomes (no `item` method required)

```tut
val knights = Map("gallahad" -> "the pure", "robin" -> "the brave")
for ((k,v) <- knights) println(k, v)
```

### With the position index

```python
for i, v in enumerate(['tic', 'tac', 'toe']):
    print(i, v)
```

```tut
for ((v,i) <- List("tic", "tac", "toe").zipWithIndex)
  println(i,v)
```

### Over two or more sequences

```python
questions = ['name', 'quest', 'favorite color']
answers = ['lancelot', 'the holy grail', 'blue']
for q, a in zip(questions, answers):
    print('What is your {0}?  It is {1}.'.format(q, a))
```

```tut
val questions = List("name", "quest", "favorite color")
val answers = List("lancelot", "the holy grail", "blue")
for ((q,a) <- questions.zip(answers)) 
  println(s"What is your $q?  It is $a.")
```

## boolean operators

| Python | Scala
| ---    | --- 
| `and`  | `&&`
| `or`  | <code>&#124;&#124;</code>
| `not` | `!`

## Comparing Sequences and Other Types

TODO
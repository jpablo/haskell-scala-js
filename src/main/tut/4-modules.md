# Bundling things

## Objects {#objects}

Scala has the keyword `object` to create a named container of definitions like methods, values, classes, etc.


```scala:silent
object constants {
  val Pi = 3.141592653589793
  val E = 2.718281828459045
  def doublePi = 2 * Pi
}
```
The definitions can be used directly

```scala
constants.doublePi
```
 or can be imported
 
```scala
import constants.doublePi
doublePi
```

Similar functionality can be achieved in Python by declaring static elements of a class:

```python
class constants:
    Pi = 3.141592653589793
    E = 2.718281828459045
    
2 * constants.Pi
```

One diference is that Python static members cannot be imported so the only way to refer to them would be with the class prefix: `constants.Pi`, etc.

`objects` are also called *companion Objects* for reasons we'll explain when we talk about classes.

## Packages (modules) {#packages}

In Python a module is simply a file with the `.py` extension. It can contain any definition like functions, classes, names, etc.

Let's follow the tutorial and assume we have a file name `fibo.py` with a function `fib` defined in it.

```python
### fibo.py

initial = (0, 1)

def fib(n):
    a, b = initial
    while b < n:
        print(b, end=' ')
        a, b = b, a+b
    print()
```

Then this can be used in the repl by importing it:

```python
>>> import fibo
>>> fibo.fib(1000)
1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987
```

The equivalent notion in Scala is a `package`. 

```scala
package example

object fibo {

  val initial = (0, 1)

  def fib(n: Int): Unit = {
    var (a,b) = initial
    var tmp = 0
    while (a < n) {
      print(s"$a ")
      tmp = a + b; a = b; b = tmp
    }
    println()
  }
}
```

This introduces the name `example.fibo.fib` in the **global** namespace of packages available inside a project.

So we can use it directly:

```scala
example.fibo.fib(100)
```
or we can import it first:

```scala
import example.fibo.fib
fib(100)
```

Notes:

* Packages are not defined implicitly by files, but rather by an explicit `package` declaration
* packages *cannot* contain functions as top level objects
* they can only contain
    * `class` declarations
    * `object` declarations (as in our example)
    * type declarations
    * nested packages


## Importing packages {#imports}

| Python | Scala
| ---    | --- 
| `from fibo import fib, initial` | `import example.fibo.{fib, initial}`
| `from fibo import *` | `import example.fibo._`
| `from fibo import fib as Fib` | `import example.fibo.{fib => Fib}`

## Executing packages {#excuting-packages}

> Note: This is not the best way to run code in production!

Executing modules can be useful when developing an app. The entry point of the program is an object that extends `App`

```scala
package example

object fibo {
  val initial = (0, 1)

  def fib(n: Int): Unit = {
    var (a,b) = initial
    var tmp = 0
    while (a < n) {
      print(s"$a ")
      tmp = a + b; a = b; b = tmp
    }
    println()
  }
}

object main extends App {
  fibo.fib(100)
}  
```


If there's only one such object in your code you can run it with `sbt` like so:

```sh
sbt run 
[info] Loading global plugins from /Users/user/.sbt/0.13/plugins
[info] Loading project definition from /Users/user/myProject/project
[info] Set current project to Python To Scala (in build file:/Users/user/myProject/)
[info] Running example.main
0 1 1 2 3 5 8 13 21 34 55 89
[success] Total time: 0 s, completed Apr 2, 2017 2:52:43 PM
```

If you prefer to hide the extra messages you can do this:

```sh
sbt --error 'set showSuccess := false' run
```

#### Multiple main objects

If on the other hand you have multiple main files you have to tell sbt which one to run by using the `runMain` command:

```sh
sbt "runMain example.main"
```

### Package search path

Since Scala is a compiled language based on the Java Virtual Machine, everything has to be compiled before it can be used.

When you have a project that is managed by `sbt` you normally don't have to care about the details; `sbt` will make sure that when you do

```sh
sbt console
```

All the code is compiled and available to be imported.

The equivalent of Python's `PYTHONPATH` is `CLASSPATH` which specifies the location of user defined classes and packages.

You can print the value used by sbt with

```sh
sbt 'show runtime:fullClasspath'
```

which would be analogous to

```python
>>> import sys; print sys.path
```

### Compiled Scala files

Pyhon compiles source files into a binary file with the extension `.pyc`. Scala compiles sourcode into a Java  class file (`.class`) that contains Java bytecode  executed by the Java Virtual Machine (JVM). They usually live under `target/scala-2.12/classes/`.

The following command removes the compiled files under `target/`:

```sh
sbt clean
```

## Standard packages {#std-packages}

The Scala standard library lives under the package `scala`. It contains the core types like `Int`, `Float`, `String`; the collection library (`List`, `Map`, etc), plus many packages dealing with concurrent programming, IO, Regular expressions, etc.

The official documentation can be found [here](http://www.scala-lang.org/api/current).

> *Identifiers in the scala package and the scala.Predef object are always in scope by default.*

> *Some of these identifiers are type aliases provided as shortcuts to commonly used classes. For example, List is an alias for scala.collection.immutable.List.*

> *Other aliases refer to classes provided by the underlying platform. For example, on the JVM, String is an alias for java.lang.String.*
> 
> -- [http://www.scala-lang.org/api/current/](http://www.scala-lang.org/api/current/)

## The `dir()` function / reflection. {#reflection}

There's no direct equivalent for this in Scala. 

It is possible to inspect objects at runtime using what is known as *reflection*.  

For example, here's how you would get the list of elements of our `fibo` object:

```scala
scala> fibo.getClass.getDeclaredMethods
res8: Array[java.lang.reflect.Method] = Array(public scala.Tuple2 example.fibo$.initial(), public void example.fibo$.fib(int))
```

Unfortunately the api for manipulating objects like this at runtime is much lower level and cumbersome to use than the Python equivalent. On top of that, operating at this level involves bypassing the type checker in many cases, so it is inherently less safe than regular Scala programming.

For that reason it is advisable to leave this as the last resort.


## Modules {#modules}

Scala doesn't have a distinction between Packages vs Modules as in Python. 

Instead of modules, a package can have "subpackages" that are identified by having a name starting with the parent name dot and the the subpackage, like `sound.formats`.

Let's examine the hypothetical hierarchy presented in the tutorial:

<pre>
sound/                          Top-level package
      __init__.py               Initialize the sound package
      formats/                  Subpackage for file format conversions
              __init__.py
              wavread.py
              wavwrite.py
              ...
      effects/                  Subpackage for sound effects
              __init__.py
              echo.py
              ...
</pre>

In Scala this would be accomplished by this structure:

<pre>
sound/                          Top-level package
      formats/                  Subpackage for file format conversions
              wavread.scala
              wavwrite.scala
              ...
      effects/                  Subpackage for sound effects
              echo.scala
              ...
</pre>

But the structure itself is not enough, the package declaration is actually more important:

```scala
// file: sound/formats/wavread.scala
package sound.formats
object wavread {
  ...
}
```

```scala
// file: sound/formats/wavwrite.scala
package sound.formats
object wavwrite {
  ...
}
```
```scala
// file: sound/effects/echo.scala
package sound.effects
object echo {
  ...
}
```

If the top level objects defined above become to big they can easily be replaced by a subpackage and split into multiple files.

> Note: There is no need to create the special `__init__` file when adding a new folder with packages.

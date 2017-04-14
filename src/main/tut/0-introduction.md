# Installing Scala

## Quick Start

First you need to install the Java platform (Java SE)

* [http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)


Next you'll need `sbt` (the Scala Build Tool):

* [http://www.scala-sbt.org/download.html](http://www.scala-sbt.org/download.html)

## Your first Scala program

With `sbt` installed, let's use a starter template:

```sh
$ sbt new scala/scala-seed.g8
```

When prompted for the project name type `Hello World` and hit enter:

```sh
Minimum Scala build.

name [My Something Project]: Hello World

Template applied in ./hello-world
```

This is what was created:

<pre>
hello-world/
├── build.sbt
├── project
│   ├── Dependencies.scala
│   └── build.properties
└── src
    ├── main
    │   └── scala
    │       └── example
    │           └── Hello.scala
    └── test
        └── scala
            └── example
                └── HelloSpec.scala
</pre>

`cd` to the newly created folder

```sh
$ cd hello-world
```

This is the *root* of the project. 

Open up the file `src/main/scala/example/Hello.scala` with any editor (we'll get back to that later) and replace the contents with this:

```scala
package example

object Hello extends App {
  println("hello world!")
}
```

### Running the program

we'll use `sbt` to compile and run the program:

```sh
sbt run
```

> Note: The very first time you run `sbt` it will download and cache many depencies, including the Scala compiler! It might take some time but the second time it will be faster.

> The cached files live under `$HOME/.ivy2/`.

Eventually you'll see something similar to this:

```
[info] Loading global plugins from /Users/jpablo/.sbt/0.13/plugins
[info] Loading project definition from /Users/jpablo/tmp/hello-world/project
[info] Set current project to Hello (in build file:/Users/jpablo/tmp/hello-world/)
[info] Compiling 1 Scala source to /Users/jpablo/tmp/hello-world/target/scala-2.12/classes...
[info] Running example.Hello
hello world!
[success] Total time: 3 s, completed Apr 13, 2017 11:52:49 PM
```

### Running the tests

We need to adjust the tests to account for the code we just changed. Open up the file `src/test/scala/example/HelloSpec.scala` and replace the contents to this:

```scala
package example

import org.scalatest._

class HelloSpec extends FlatSpec with Matchers {
  "Addition of small numbers" should "work correctly" in {
    (1 + 1) shouldEqual 2
  }
}
```


Now let's run the tests:

```sh
$ sbt test
[info] Loading global plugins from /Users/jpablo/.sbt/0.13/plugins
[info] Loading project definition from /Users/jpablo/tmp/hello-world/project
[info] Set current project to Hello (in build file:/Users/jpablo/tmp/hello-world/)
[info] HelloSpec:
[info] Addition of small numbers
[info] - should work correctly
[info] Run completed in 353 milliseconds.
[info] Total number of tests run: 1
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 1, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 1 s, completed Apr 14, 2017 12:07:49 AM
```

Hooray!! You have you're first Scala program compiled, executed and tested.

### The REPL

Add the following code at the bottom of our `Hello.scala` file:

```scala
object Secret {
  val number = 1
}
```

`sbt` provides with a REPL via the command `sbt console`. 

```sh
$ sbt console
[info] Loading global plugins from /Users/jpablo/.sbt/0.13/plugins
[info] Loading project definition from /Users/jpablo/tmp/hello-world/project
[info] Set current project to Hello (in build file:/Users/jpablo/tmp/hello-world/)
[info] Starting scala interpreter...
[info]
Welcome to Scala 2.12.1 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_40).
Type in expressions for evaluation. Or try :help.

scala> example.Secret.number
res0: Int = 1

scala> 1 + 2
res1: Int = 3
```

Observe how we can use our own code by using the correct package name (`example` in this case).

> Hints:
> 
> * `sbt compile` will only compile the code and nothing else
> * Adding `~` to many `sbt` commands will enable *file watch mode*. For example `sbt "~test"` will recompile / rerun the tests every time a change in the project's source code is detected. (`sbt "~compile"` will recompile on every change)

## Editing Scala Code

In order to enjoy many of the benefits of using a statically typed language like Scala (such as precise autocompletion, code generation, etc) you'll need to configure correctly your editor. A good option is to use IntelliJ IDEA which is an IDE that has support for Scala via a plugin. If you've ever used PyCharm you'll feel at home.

* [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)

(Download and install the **Comunity** edition which is free).

Once IJ is running you'll need to install the Scala plugin:

`Preferences => Plugins => Browse repositories`

Search for "Scala". Install the plugin and restart the IDE.

After restarting IJ, on the welcome dialog click on "Open" and choose our Scala `hello-world` folder. You'll be asked to confirm a few options in the dialog "Import Project from SBT". Accept the defaults and click "Ok".

Another dialog will ask for modules to include. Again accept the defaults and click "Ok".

After that you can explore the project on the panel to the left and start making changes to your code.


## About Java

There are a few concepts that are useful to understand while using Scala

### Java Language
You probably already know about the Java Programming Language. It is an Object Oriented statically typed language released by Sun Microsystems back in 1995.

Unlike languages like C or C++, Java doesn't compile to native code, but rather to *bytecode* that can be executed on any *Java Virtual Machine* (`JVM`).

### JVM
A *Java Virtual Machine* or *JVM* for short is a an idealized version of a computing machine. The idea is that once a program has been compiled to bytecodes it can then run unmodified into any platform for which an JVM exists.

There's nothing that prevents any language to be compiled to Java bytecode, and thus variants of many common languages exist that can run on the JVM. 

### Java 2 Platform
Also known as *Java Standard Edition* or *Java SE* is a set of libraries and APIs that form the *standard library* of the Java language. 

### Scala and Java
Scala was originally designed as a language that targets the JVM. It was also designed to make it very easy to use existing Java libraries. In particular, all the Java SE libraries can be imported and used within Scala programs. 

The Java std library is accessible in Scala programs under the `java.*` namespace. Scala's own std library is available under the `scala.*` namespace. We'll talk more about packages and namespaces later.

## `sbt`

`sbt` is a commonly used build tool for Scala. It can take care amongs other things

* Start a project from scratch via templates
* Managing dependencies
* Building a project
* Running tests
* etc





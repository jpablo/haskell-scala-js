# Full Scala installation {#full-installation}

Finally, in order to run a full fledged Scala project with tests, etc. follow these instructions:

## Quick Start {#quick-start}

1. As usual, first you need to install the Java platform (Java SE):
 [https://www.oracle.com/technetwork/java/javase/downloads/index.html](https://www.oracle.com/technetwork/java/javase/downloads/index.html)

2. Next, install `sbt` (the Scala Build Tool):
 [http://www.scala-sbt.org/download.html](http://www.scala-sbt.org/download.html)

## Your first Scala program {#first-scala-program}

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

Let's examine quickly the file `Hello.scala`. 

```sh
$ cat src/main/scala/example/Hello.scala
package example

object Hello extends Greeting with App {
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
```

We'll explain the details later but this code is defining a string `greeting` inside `Greeting` and then it is combining `Greeting` with the predefined `App` into an object called `Hello`.

### Running the program

we'll use `sbt` to compile and run the program:

```sh
sbt run
```

> Note: The very first time you run `sbt` it will download and cache many depencies, including the Scala compiler! It might take some time but the second time it will be faster.

> The cached files live under `$HOME/.ivy2/`.

Eventually you'll see something similar to this:

```sh
...
[info] downloading https://repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.2/scala-library-2.12.2.jar ...
[info] 	[SUCCESSFUL ] org.scala-lang#scala-library;2.12.2!scala-library.jar (1686ms)
...
[info] Done updating.
[info] Compiling 1 Scala source to /Users/jpablo/tmp/hello-world/target/scala-2.12/classes...
[info] 'compiler-interface' not yet compiled for Scala 2.12.2. Compiling...
[info]   Compilation completed in 19.152 s
[info] Running example.Hello
hello
[success] Total time: 31 s, completed Jul 6, 2017 10:58:08 PM
```

### Running the tests
Now let's run the tests:

```sh
$ sbt test
...
[info] HelloSpec:
[info] The Hello object
[info] - should say hello
[info] Run completed in 546 milliseconds.
[info] Total number of tests run: 1
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 1, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 10 s, completed Jul 6, 2017 11:00:08 PM
```

Hooray!! You have you're first Scala program compiled, executed and tested.

### The REPL

`sbt` provides with a REPL via the command `sbt console`. 

```sh
$ sbt console
...
[info] Loading project definition from /Users/jpablo/tmp/hello-world/project
[info] Set current project to Hello (in build file:/Users/jpablo/tmp/hello-world/)
[info] Starting scala interpreter...
[info]
Welcome to Scala 2.12.2 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_131).
Type in expressions for evaluation. Or try :help.

scala> example.Hello.greeting
res0: String = hello

scala> 1 + 2
res1: Int = 3
```

Observe how we can use our own code by using the correct package name (`example` in this case).

> Hints:
> 
> * `sbt compile` will only compile the code and nothing else
> * Adding `~` to many `sbt` commands will enable *file watch mode*. For example `sbt "~test"` will recompile / rerun the tests every time a change in the project's source code is detected. (`sbt "~compile"` will recompile on every change)

## Editing Scala Code {#editing-scala-code}

In order to enjoy many of the benefits of using a statically typed language like Scala (such as precise autocompletion, code generation, etc) you'll need to configure correctly your editor. A good option is to use IntelliJ IDEA which is an IDE that has support for Scala via a plugin. If you've ever used PyCharm you'll feel at home.

* [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)

(Download and install the **Comunity** edition which is free).

Once IntelliJ is running you'll need to install the Scala plugin:

`Preferences => Plugins => Browse repositories`

Search for "Scala". Install the plugin and restart the IDE.

After restarting IntelliJ, on the welcome dialog click on "Open" and choose our Scala `hello-world` folder. You'll be asked to confirm a few options in the dialog "Import Project from SBT". Accept the defaults and click "Ok".

Another dialog will ask for modules to include. Again accept the defaults and click "Ok".

After that you can explore the project on the panel to the left and start making changes to your code.


## About Java {#about-java}

There are a few java-related terms that are useful to know while using Scala.

### The Java Language
You probably already know a bit about the Java Programming Language. It is an Object Oriented statically typed language released by Sun Microsystems back in 1995.

Unlike languages like C or C++, Java doesn't compile directly to native code, but rather to *bytecode* that can be executed on any *Java Virtual Machine* (`JVM`).

### JVM
A *Java Virtual Machine* or *JVM* for short is a an idealized version of a computer. The idea is that once a program has been compiled to bytecodes it can then run unmodified into any platform for which an JVM exists.

There's nothing that prevents any language to be compiled to Java bytecode, and thus variants of many common languages exist that can run on the JVM. 

### Java 2 Platform
Also known as *Java Standard Edition* or *Java SE* is a set of libraries and APIs that form the *standard library* of the Java language. 

### Scala and Java

Scala was originally designed as a language that targets the JVM. It was also designed to make it very easy to use existing Java libraries. In particular, all the Java SE libraries can be imported and used within Scala programs. 

The Java standard library is accessible in Scala programs under the `java.*` namespace. Scala's own std library is available under the `scala.*` namespace. We'll talk more about packages and namespaces later.

## `sbt` {#sbt}

`sbt` is a commonly used build tool for Scala. It can take care amongst other things:

* Start a project from scratch via templates
* Managing dependencies
* Building a project
* Running tests
* etc


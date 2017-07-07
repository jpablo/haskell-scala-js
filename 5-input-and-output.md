# Input and Output

So far we've used `println()` and `print()`.

## Fancier Output Formatting {#output-formatting}
> Note: There's no analog of Python functions `str.rjust()`, `str.ljust()`, or `str.center()`.


The first tool for string formatting is using string interpolation

```scala
scala> s"${math.Pi}"
res0: String = 3.141592653589793
```

There is another type of interpolator used to format strings that is similar to Python's `string.format`. For example to print only 2 decimal values of Pi:

```scala
scala> f"${math.Pi}%.2f"
res1: String = 3.14
```

As you can see it is very similar to the regular `s""` interpolator, but you have to add a `%` and format string after the embedded expression.

```scala
scala> for (x <- 1 to 10) println(f"$x%2d ${x*x}%3d ${x*x*x}%4d")
 1   1    1
 2   4    8
 3   9   27
 4  16   64
 5  25  125
 6  36  216
 7  49  343
 8  64  512
 9  81  729
10 100 1000
```

As another alternative, you can use the `String.format` method:

```python
for x in range(1, 11):
    print('{0:2d} {1:3d} {2:4d}'.format(x, x*x, x*x*x))
```
Becomes

```scala
scala> for (x <- 1 to 10) 
     |   println("%2d %3d %4d".format(x, x*x, x*x*x))
 1   1    1
 2   4    8
 3   9   27
 4  16   64
 5  25  125
 6  36  216
 7  49  343
 8  64  512
 9  81  729
10 100 1000
```


In both cases the format is implemented in terms of `java.util.Formatter`.

* [Formatting Guide](https://docs.oracle.com/javase/tutorial/essential/io/formatting.html)
* [Formatting API docs](https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html)


## Reading and Wrinting Files {#reading-writing}

### Reading 

To read all the lines from a file into a list of strings:

```scala
val lines = io.Source.fromFile("my-file").getLines().toList
```

### Writing

Scala defers to the `Java` classes for writing to files. Here's how you can open a file for writing and write a line using `.println()`. Dont forget to close the file!


```scala
val append = true
val writer = new java.io.FileWriter("README.md", append)
writer.write("Lorem ipsum dolor sit amet, consectetur adipiscing elit\n")
writer.close()
```

For something more elaborate a good option is the library `better-files` that wraps the `java.io` package in a more Scala friendly face

> Hint: Use [https://github.com/pathikrit/better-files](https://github.com/pathikrit/better-files)!



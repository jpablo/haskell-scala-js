# Errors and Exceptions

Python has 2 kinds of errors: *syntax errors* and *exceptions*.

## Handling Exceptions {#handling-exceptions}

Let's make our code that writes to files bulletproof

```python
import sys

try:
    f = open("/8745934759/lorem.txt")
    f.write("Lorem ipsum dolor sit amet, consectetur adipiscing elit\n")
except FileNotFoundError as err:
    print("File not found: {0}".format(err))
except:
    print("Unexpected error:", sys.exc_info()[0])
    raise
finally:
    f.close()    
```
Becomes

```scala
import java.io.{FileNotFoundException, FileWriter}

var writer: FileWriter = null
try {
  writer = new FileWriter("/8745934759/README.md")
  writer.write("Lorem ipsum dolor sit amet, consectetur adipiscing elit\n")
} catch {
  case ex: FileNotFoundException =>
    println(s"File not found: $ex")
  case ex: Exception =>
    println(s"Unexpected error: $ex")
    throw ex
} finally {
  if (writer != null)
    writer.close()
}
```

Besides the syntatic differences there is one issue worth mentioning: in Python all variables defined inside the `try` block are available in the `else` block. 

That is not the case in Scala, where each `{ ... }` block creates a new scope. For that reason we're forced to declare an un-initialized `FileWriter` variable in a place where is accessible in both the `try` and the `finally` blocks.

## Raising exceptions {#raising-exceptions}
```python
raise Exception('error!')
```
Becomes

```scala
throw new Exception("error!")
```

## Predefined Clean-up Actions {#cleanup-actions}

There's no direct equivalent in Scala to Python's `with` statement, but it can be more or less emulated with the following construct, called "the loaner pattern" in the Scala book.

```scala
def withFileWriter(file: String)(op: FileWriter => Unit) = {
  val writer = new FileWriter(file)
  try {
    op(writer)
  } finally {
    writer.close()
  }
}
```

And then use it like this:

```scala
withFileWriter("/8745934759/README.md") { writer =>
  try {
    writer.write("Lorem ipsum dolor sit amet, consectetur adipiscing elit\n")
  } catch {
    case ex: FileNotFoundException =>
      println(s"Error not found: $ex")
    case ex: Exception =>
      println(s"Unexpected error: $ex")
      throw ex
  }
}
```

That way there's no possibility of somebody forgetting to close the file (and you can focus on catching the exceptions that your logic actually cares).

Observe that we're using 2 argument groups, so that we can use a block and a lambda after the file name. The idea is make the code feel more like a control structure similar to the `with` statement in Python.

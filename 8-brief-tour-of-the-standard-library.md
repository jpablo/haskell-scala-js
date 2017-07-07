# Brief Tour of the Standard Library

## Operating System Interface

[`scala.sys.process`](http://www.scala-lang.org/files/archive/api/current/scala/sys/process/index.html) provides functions similar to `os`

```scala
import scala.sys.process._
"ls".!!
```

## Command line arguments

Inside an `App` you can access the command line arguments:

```scala
object main extends App {
  println(args)
}
```


## String Pattern Matching

```scala
  val dateR = "(.*)T(.*)Z".r

  "2017-04-04T08:02:06.996Z" match {
    case dateR(date, time) =>
      println(s"date=$date, time=$time")
  }
```

## Mathematics

```scala
```

## Internet Access

## Dates and Times


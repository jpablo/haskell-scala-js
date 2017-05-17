import java.io.{File, FileNotFoundException, FileWriter, PrintWriter}

object MoreControlFlowTools {
  {
import util.Random.nextInt
val x = nextInt(3) - 1
val message =
  if (x < 0) "Found a negative number"
  else if (x == 0)  "Found zero"
  else if (x > 1) "Found a positive number"


println(message)
  }

  val lst = List(Vector(1,2), Vector(), Vector(3))
for {
  v <- lst
  i <- v
} yield i+1



  val input = io.StdIn.readLine("Please enter an integer: ")
  var y     = input.toInt // 💣
  if (y < 0) {
    println("Negative changed to zero")
    y = 0
  } else if (y == 0)
    println("Zero")
  else if (y == 1)
    println("Single")
  else
    println("More")

  val x0 = input.toInt // 💣
  val negative = x0 < 0
  val message  =
    if (negative) "Negative changed to zero"
    else if (x0 == 0) "Zero"
    else if (x0 == 1) "Single"
    else "More"

  val x = if (negative) 0 else x0

  println(message)


  //  val words = ["cat", "window", "defenestrate"]

  for (n <- 2 until 10) {
    for (x <- 2 until n) {
      if (n % x == 0) {
        println(s"$n equals $x * ${n / x}")
        ???
      }
    }
  }


  for (n <- 2 until 10) {
    var break = false
    var x = 2
    while (x < n && !break) {
      if (n % x == 0) {
        println(s"$n equals $x * ${n / x}")
        break = true
      }
      x += 1
    }
    if (!break)
      println(s"$n is a prime number")
  }


  {
    var num = 2
    while (num < 10) {
      if (num % 2 == 0) {
        println(s"Found an even number $num")
      } else {
        println(s"Found a number $num")
      }
      num += 1
    }
  }

  /**
    * Print a Fibonacci series up to n.
    */
  def fib(n: Int) = {
    var a = 0
    var b = 1
    var tmp = 0
    while (a < n) {
      print(s"$a ")
      tmp = a + b;
      a = b;
      b = tmp
    }
    println()
  }

  def fib2(n: Int): List[Int] = {
    def go(a: Int, b: Int, result: List[Int]): List[Int] =
      if (a < n) go(b, a + b, a :: result) else result

    go(0, 1, List())
  }

  def askOk(prompt: String, retries: Int = 4, reminder: String = "Please try again!") = ???

  def parrot(voltage: Any, state: String = "a stiff", action: String = "voom", `type`: String = "Norwegian Blue") = {
    print("-- This parrot wouldn't " + action)
    print("if you put " + voltage + " volts through it.")
    print("-- Lovely plumage, the " + `type`)
    print("-- It's " + state + "!")
  }


  parrot(1000) // 1 positional argument
  parrot(voltage = 1000) // 1 keyword argument
  parrot(voltage = 1000000, action = "VOOOOOM") // 2 keyword arguments
  parrot(action = "VOOOOOM", voltage = 1000000) // 2 keyword arguments
  parrot("a million", "bereft of life", "jump") // 3 positional arguments
  parrot("a thousand", state = "pushing up the daisies") // 1 positional, 1 keyword


  //[(x, y) for x in [1,2,3] for y in [3,1,4] if x != y]


  for {
    x <- List(1, 2, 3)
    y <- List(3, 1, 4) if x != y
  } yield (x, y)


  val matrix = List(
    List(1, 2, 3, 4),
    List(5, 6, 7, 8),
    List(9, 10, 11, 12)
  )

  for (i <- 0 until 4) yield {
    for (row <- matrix) yield row(i)
  }


  if (!"abc".contains('a')) println("si")


  //  questions = ["name", "quest", "favorite color"]
  //  answers = ["lancelot", "the holy grail", "blue"]


  var break = false
  while (break) {
    try {
      io.StdIn.readLine("Please enter an integer: ").toInt
      break = true
    } catch {
      case ex: NumberFormatException =>
        println("Oops!  That was no valid number.  Try again...")
    }
  }


  var writer: FileWriter = null
  try {
    writer = new FileWriter("/8745934759/README.md")
    writer.write("Lorem ipsum dolor sit amet, consectetur adipiscing elit\n")
  } catch {
    case ex: FileNotFoundException =>
      println(s"Error not found: $ex")
    case ex: Exception =>
      println(s"Unexpected error: $ex")
      throw ex
  } finally {
    if (writer != null)
      writer.close()
  }

def withFileWriter(file: String)(op: FileWriter => Unit) = {
  val writer = new FileWriter(file)
  try {
    op(writer)
  } finally {
    writer.close()
  }
}

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
}

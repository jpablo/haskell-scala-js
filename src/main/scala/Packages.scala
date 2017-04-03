package example

object fibo {

  val initial = (0, 1)

  def fib(n: Int): Unit = {
    var (a, b) = initial
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
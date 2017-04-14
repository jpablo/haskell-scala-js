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

  args
  fibo.fib(100)


  val dateR = "(.*)T(.*)Z".r

  "2017-04-04T08:02:06.996Z" match {
    case dateR(date, time) =>
      println(s"date=$date, time=$time")
  }


}
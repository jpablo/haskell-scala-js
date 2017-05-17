object Iterators {

  class Reverse(data: String) {
    def foreach[U](f: Char => U) =
      data.reverse.foreach(f)
  }

}


class Factory(x: Int) {
  def apply(y: Int) = x + y
}
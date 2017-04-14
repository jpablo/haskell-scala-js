object Iterators {

  class Reverse(data: String) {
    def foreach[U](f: Char => U) =
      data.reverse.foreach(f)
  }

}

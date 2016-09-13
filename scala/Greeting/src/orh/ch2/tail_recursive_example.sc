package orh.ch2


object tail_recursive_example {

	@annotation.tailrec
  def factorial(n: Int, m: Int): Int = {
    if (n <= 0) m
    else {
      factorial(n - 1, m * n)
    }
  }

  factorial(5, 1)
}
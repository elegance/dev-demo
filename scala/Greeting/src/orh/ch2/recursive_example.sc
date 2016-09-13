package orh.ch2

/**
 * 递归
 */
object recursive_example {
  def factorial(n: Int): Int = {
    if (n <= 0) 1
    else {
      println(n + " * " + (n - 1))
      n * factorial(n - 1)
    }
  }                                               //> factorial: (n: Int)Int

  factorial(15)                                   //> 15 * 14
                                                  //| 14 * 13
                                                  //| 13 * 12
                                                  //| 12 * 11
                                                  //| 11 * 10
                                                  //| 10 * 9
                                                  //| 9 * 8
                                                  //| 8 * 7
                                                  //| 7 * 6
                                                  //| 6 * 5
                                                  //| 5 * 4
                                                  //| 4 * 3
                                                  //| 3 * 2
                                                  //| 2 * 1
                                                  //| 1 * 0
                                                  //| res0: Int = 2004310016
}
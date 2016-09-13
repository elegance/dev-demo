package orh.ch2

/**
 * 综合实例
 */
object sumFunc {

  // 高阶、柯里化函数
  def sum(f: (Int) => Int)(a: Int)(b: Int): Int = {

    // 尾递归
    @annotation.tailrec
    def loop(n: Int, acc: Int): Int = {
      if (n > b) {
        println(s"n=${n}, acc=${acc}")
        acc
      } else {
        println(s"n=${n}, acc=${acc}")
        loop(n + 1, acc + f(n))
      }
    }

    loop(a, 0)
  }                                               //> sum: (f: Int => Int)(a: Int)(b: Int)Int

  sum(x => x)(1)(5)                               //> n=1, acc=0
                                                  //| n=2, acc=1
                                                  //| n=3, acc=3
                                                  //| n=4, acc=6
                                                  //| n=5, acc=10
                                                  //| n=6, acc=15
                                                  //| res0: Int = 15

  sum(x => x * x)(1)(5)                           //> n=1, acc=0
                                                  //| n=2, acc=1
                                                  //| n=3, acc=5
                                                  //| n=4, acc=14
                                                  //| n=5, acc=30
                                                  //| n=6, acc=55
                                                  //| res1: Int = 55
  sum(x => x * x * x)(1)(5)                       //> n=1, acc=0
                                                  //| n=2, acc=1
                                                  //| n=3, acc=9
                                                  //| n=4, acc=36
                                                  //| n=5, acc=100
                                                  //| n=6, acc=225
                                                  //| res2: Int = 225
  val sumSquare = sum(x => x * x)_                //> sumSquare  : Int => (Int => Int) = <function1>
  
  sumSquare(1)(5)                                 //> n=1, acc=0
                                                  //| n=2, acc=1
                                                  //| n=3, acc=5
                                                  //| n=4, acc=14
                                                  //| n=5, acc=30
                                                  //| n=6, acc=55
                                                  //| res3: Int = 55
}
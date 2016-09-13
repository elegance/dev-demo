package orh.ch2

object sumSq_example {

  // 一个函数返回集合的 元素个数、累计求和、平方和
  def sumSq(in: List[Int]): (Int, Int, Int) = {
    in.foldLeft((0, 0, 0))((t, v) => (t._1 + 1, t._2 + v, t._3 + v * v))
  }                                               //> sumSq: (in: List[Int])(Int, Int, Int)
  
  val a = List(1, 2, 3)                           //> a  : List[Int] = List(1, 2, 3)
  sumSq(a)                                        //> res0: (Int, Int, Int) = (3,6,14)
  
  val b = List(1, 2, 3, 4, 5, 6)                  //> b  : List[Int] = List(1, 2, 3, 4, 5, 6)
  sumSq(b)                                        //> res1: (Int, Int, Int) = (6,21,91)
}
package orh.ch2

object sumSq_example {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(196); 

  // 一个函数返回集合的 元素个数、累计求和、平方和
  def sumSq(in: List[Int]): (Int, Int, Int) = {
    in.foldLeft((0, 0, 0))((t, v) => (t._1 + 1, t._2 + v, t._3 + v * v))
  };System.out.println("""sumSq: (in: List[Int])(Int, Int, Int)""");$skip(27); 
  
  val a = List(1, 2, 3);System.out.println("""a  : List[Int] = """ + $show(a ));$skip(11); val res$0 = 
  sumSq(a);System.out.println("""res0: (Int, Int, Int) = """ + $show(res$0));$skip(36); 
  
  val b = List(1, 2, 3, 4, 5, 6);System.out.println("""b  : List[Int] = """ + $show(b ));$skip(11); val res$1 = 
  sumSq(b);System.out.println("""res1: (Int, Int, Int) = """ + $show(res$1))}
}

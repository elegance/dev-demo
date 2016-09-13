package orh.ch2

/**
 * 综合实例
 */
object sumFunc {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(386); 

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
  };System.out.println("""sum: (f: Int => Int)(a: Int)(b: Int)Int""");$skip(22); val res$0 = 

  sum(x => x)(1)(5);System.out.println("""res0: Int = """ + $show(res$0));$skip(26); val res$1 = 

  sum(x => x * x)(1)(5);System.out.println("""res1: Int = """ + $show(res$1));$skip(28); val res$2 = 
  sum(x => x * x * x)(1)(5);System.out.println("""res2: Int = """ + $show(res$2));$skip(35); 
  val sumSquare = sum(x => x * x)_;System.out.println("""sumSquare  : Int => (Int => Int) = """ + $show(sumSquare ));$skip(22); val res$3 = 
  
  sumSquare(1)(5);System.out.println("""res3: Int = """ + $show(res$3))}
}

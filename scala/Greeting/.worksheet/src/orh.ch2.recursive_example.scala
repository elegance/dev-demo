package orh.ch2

/**
 * 递归
 */
object recursive_example {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(192); 
  def factorial(n: Int): Int = {
    if (n <= 0) 1
    else {
      println(n + " * " + (n - 1))
      n * factorial(n - 1)
    }
  };System.out.println("""factorial: (n: Int)Int""");$skip(18); val res$0 = 

  factorial(15);System.out.println("""res0: Int = """ + $show(res$0))}
}

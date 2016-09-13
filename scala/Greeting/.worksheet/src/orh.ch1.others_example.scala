package orh.ch1

object others_example {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(150); 
  // 默认值
  def decorate(str: String, left: String = "[", right: String = "]") = {
    left + str + right
  };System.out.println("""decorate: (str: String, left: String, right: String)String""");$skip(20); val res$0 = 
  decorate("hello");System.out.println("""res0: String = """ + $show(res$0));$skip(99); 

  // 变长参数
  def sum(args: Int*) = {
    var res = 0
    for (arg <- args) res += arg
    res
  };System.out.println("""sum: (args: Int*)Int""");$skip(25); val res$1 = 
  
  sum(1, 2, 3, 4, 5);System.out.println("""res1: Int = """ + $show(res$1))}
}

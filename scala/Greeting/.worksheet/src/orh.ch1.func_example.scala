package orh.ch1

object func_example {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(102); 
  def hello(name: String): String = {
    s"Hello ${name}"
  };System.out.println("""hello: (name: String)String""");$skip(16); val res$0 = 
  hello("john");System.out.println("""res0: String = """ + $show(res$0));$skip(58); 
  
  def hello2(name: String) = {
  	s"Hello ${name}"
  };System.out.println("""hello2: (name: String)String""");$skip(17); val res$1 = 
  hello2("poxi");System.out.println("""res1: String = """ + $show(res$1));$skip(37); 
  
  def add(x: Int, y: Int) = x + y;System.out.println("""add: (x: Int, y: Int)Int""");$skip(14); val res$2 = 
  add(18, 27);System.out.println("""res2: Int = """ + $show(res$2))}
}

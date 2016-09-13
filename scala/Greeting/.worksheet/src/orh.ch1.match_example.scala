package orh.ch1

object match_example {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(54); 
  val code = 2;System.out.println("""code  : Int = """ + $show(code ));$skip(92); 
  val res = code match {
    case 1 => "one"
    case 2 => "two"
    case _ => "others"
  };System.out.println("""res  : String = """ + $show(res ))}
}

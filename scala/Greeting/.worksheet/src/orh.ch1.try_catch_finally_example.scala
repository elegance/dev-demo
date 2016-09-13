package orh.ch1

object try_catch_finally_example {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(177); 
  val res = try {
    Integer.parseInt("dot")
  } catch {
    case _ => 0
  } finally {
    println("always be printed")
  };System.out.println("""res  : Int = """ + $show(res ))}
}

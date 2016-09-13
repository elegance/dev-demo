package orh.ch1

object for_exapmle {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(79); 
  val l = List("alice", "bob", "cathy");System.out.println("""l  : List[String] = """ + $show(l ));$skip(47); ;
  for {
    s <- l // generator
  } println(s);$skip(66); 
  for {
    s <- l
    if (s.length > 3) // filter
  } println(s);$skip(113); 
  var result_for = for {
    s <- l
    s1 = s.toUpperCase() //variable binding
    if (s1 != "")
  } yield (s1);System.out.println("""result_for  : List[String] = """ + $show(result_for ))}
}

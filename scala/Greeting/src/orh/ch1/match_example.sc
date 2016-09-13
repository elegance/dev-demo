package orh.ch1

object match_example {
  val code = 2                                    //> code  : Int = 2
  val res = code match {
    case 1 => "one"
    case 2 => "two"
    case _ => "others"
  }                                               //> res  : String = two
}
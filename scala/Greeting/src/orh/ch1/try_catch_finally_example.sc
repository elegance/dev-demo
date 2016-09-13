package orh.ch1

object try_catch_finally_example {
  val res = try {
    Integer.parseInt("dot")
  } catch {
    case _ => 0
  } finally {
    println("always be printed")
  }                                               //> always be printed
                                                  //| res  : Int = 0
}
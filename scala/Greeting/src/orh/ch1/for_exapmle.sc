package orh.ch1

object for_exapmle {
  val l = List("alice", "bob", "cathy");          //> l  : List[String] = List(alice, bob, cathy)
  for {
    s <- l // generator
  } println(s)                                    //> alice
                                                  //| bob
                                                  //| cathy
  for {
    s <- l
    if (s.length > 3) // filter
  } println(s)                                    //> alice
                                                  //| cathy
  var result_for = for {
    s <- l
    s1 = s.toUpperCase() //variable binding
    if (s1 != "")
  } yield (s1)                                    //> result_for  : List[String] = List(ALICE, BOB, CATHY)
}
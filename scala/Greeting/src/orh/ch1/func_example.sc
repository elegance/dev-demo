package orh.ch1

object func_example {
  def hello(name: String): String = {
    s"Hello ${name}"
  }                                               //> hello: (name: String)String
  hello("john")                                   //> res0: String = Hello john
  
  def hello2(name: String) = {
  	s"Hello ${name}"
  }                                               //> hello2: (name: String)String
  hello2("poxi")                                  //> res1: String = Hello poxi
  
  def add(x: Int, y: Int) = x + y                 //> add: (x: Int, y: Int)Int
  add(18, 27)                                     //> res2: Int = 45
}
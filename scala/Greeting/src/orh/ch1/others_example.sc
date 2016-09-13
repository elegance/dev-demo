package orh.ch1

object others_example {
  // 默认值
  def decorate(str: String, left: String = "[", right: String = "]") = {
    left + str + right
  }                                               //> decorate: (str: String, left: String, right: String)String
  decorate("hello")                               //> res0: String = [hello]

  // 变长参数
  def sum(args: Int*) = {
    var res = 0
    for (arg <- args) res += arg
    res
  }                                               //> sum: (args: Int*)Int
  
  sum(1, 2, 3, 4, 5)                              //> res1: Int = 15
}
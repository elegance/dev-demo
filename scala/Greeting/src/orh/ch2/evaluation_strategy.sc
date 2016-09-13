package orh.ch2

/**
 * 求值策略
 * <ol>
 * <li>1. Call by Value, 对函数实参进行求值，且仅求值一次</li>
 * <li>2. Call by Name，函数实参没在在函数体内被用到时都会被求值</li>
 * </ol>
 */
object evaluation_strategy {

  def test1(x: Int, y: Int) = x * x               //> test1: (x: Int, y: Int)Int
  def test2(x: => Int, y: => Int) = x * x         //> test2: (x: => Int, y: => Int)Int

  test1(3 + 4, 8)                                 //> res0: Int = 49
  // 三步
  // test1(7, 8)
  // 7 * 7
  // 49

  test2(3 + 4, 8)                                 //> res1: Int = 49
  // 四步
  // (3 + 4) * (3 + 4)
  // (7) * (3 + 4)
  // 7 * 7
  // 49

  test1(7, 3 + 5)                                 //> res2: Int = 49
  // 三步
  // test1(7, 8)
  // 7 * 7
  // 49

  test2(7, 3 + 5)                                 //> res3: Int = 49
  // 两部
  // 7 * 7
  // 49

  // 下面使用无限递归的 函数 加深下对 这两种策略区别的理解
  def bar(x: Int, y: => Int): Int = 1             //> bar: (x: Int, y: => Int)Int
  def loop(): Int = loop                          //> loop: ()Int
  
  // loop对应的形参是y， y的求值策略是Call by Name,在函数体内用到时才会执行，所以不会出现无限递归问题
  bar(1, loop)                                    //> res4: Int = 1
  
  // loop对应的形参是x，x的求值策略是Call by Value,在函数体执行前会执行loop，所以会出现无限递归问题
  //bar(loop, 1)
}
package orh.ch2

/**
 * 求值策略
 * <ol>
 * <li>1. Call by Value, 对函数实参进行求值，且仅求值一次</li>
 * <li>2. Call by Name，函数实参没在在函数体内被用到时都会被求值</li>
 * </ol>
 */
object evaluation_strategy {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(213); 

  def test1(x: Int, y: Int) = x * x;System.out.println("""test1: (x: Int, y: Int)Int""");$skip(42); 
  def test2(x: => Int, y: => Int) = x * x;System.out.println("""test2: (x: => Int, y: => Int)Int""");$skip(20); val res$0 = 

  test1(3 + 4, 8);System.out.println("""res0: Int = """ + $show(res$0));$skip(64); val res$1 = 
  // 三步
  // test1(7, 8)
  // 7 * 7
  // 49

  test2(3 + 4, 8);System.out.println("""res1: Int = """ + $show(res$1));$skip(89); val res$2 = 
  // 四步
  // (3 + 4) * (3 + 4)
  // (7) * (3 + 4)
  // 7 * 7
  // 49

  test1(7, 3 + 5);System.out.println("""res2: Int = """ + $show(res$2));$skip(64); val res$3 = 
  // 三步
  // test1(7, 8)
  // 7 * 7
  // 49

  test2(7, 3 + 5);System.out.println("""res3: Int = """ + $show(res$3));$skip(101); 
  // 两部
  // 7 * 7
  // 49

  // 下面使用无限递归的 函数 加深下对 这两种策略区别的理解
  def bar(x: Int, y: => Int): Int = 1;System.out.println("""bar: (x: Int, y: => Int)Int""");$skip(25); 
  def loop(): Int = loop;System.out.println("""loop: ()Int""");$skip(82); val res$4 = 
  
  // loop对应的形参是y， y的求值策略是Call by Name,在函数体内用到时才会执行，所以不会出现无限递归问题
  bar(1, loop);System.out.println("""res4: Int = """ + $show(res$4))}
  
  // loop对应的形参是x，x的求值策略是Call by Value,在函数体执行前会执行loop，所以会出现无限递归问题
  //bar(loop, 1)
}

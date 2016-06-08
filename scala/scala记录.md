## 特点
函数式编程

## 关键字


## 环境
scala 下载：http://www.scala-lang.org/download/
sbt(Simple Build Tool)：http://www.scala-sbt.org/
开发工具：http://scala-ide.org/download/sdk.html

## 基础命令
scalac xxx.scala
scala xxx

## 基础语法

### 变量
三种修饰符：
val 定义常量(immutable variable)
var 定义变量(mutable variable)
lazy val 惰性求值常量

### 数据类型
数值：
Byte 1、Short 2、Int 4、Long 8、Float、Double 

Boolean:
```
val m=false
```

Char:
```
val c='x'
```

Unit: 类似java的void,有副作用的函数
```
scala> val p=()
```


Nothing: 程序异常终止
```
scala> def foo() = throw new Exception("err..");
```

String: 新增字符串插值特性
```
scala> val name="John"
scala> s"hello ${name}"
```

### 代码块 Block
在一行使用";"分号分隔
```
{exp1; exp2}
```
不在一行，大括号包裹 不需要分号
```
{
    exp1
    exp2
}
```

### 函数定义
```
def funcName(paramName:paramType):ReturnType = {
    //function body   
}
```
example:
```
def hello(name: String): String = {
    s"Hello ${name}"
}
hello("John")

def hello2(name: String) = {
    s"Hello ${name}"
}
hello("返回类型自动判断")

def add(x:Int, y:Int) = x + y
add(1, 20)
```

### if 表达式
if(condition) valA else B
if(true) 1 else 2

### for 表达式
```
for {
    x <- XS
    y = x + 1
    if (y > 0)
} yield y
```
exapmle:
```
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
```

### try
```
try{}
catch{}
finally{}
```
exapmle:
```
try{
    Integer.parseInt("dog")
} catch {
    case _ => 0 //下划线 代表同通配符，即匹配所有异常 再返回0
} finally {
    println("always be printed")
}
```

### match 类似于java中的switch
```
exp match {
    case p1 => val1
    case p2 => val2
    ...
    case _ => dftVal   
}
```
exapmle:
```
val code = 2
val res = code match {
    case 1 => "one"
    case 2 => "two"
    case _ => "others"
}
```

---------------- 基础分隔线
--

## 求值策略
1. Call by Value, 对函数实参进行求值，且仅求值一次
2. Call by Name，函数实参没在在函数体内被用到时都会被求值

Scala 通常使用 `Call by Value`
如果函数形参类型以 `=>` 开头，那么会使用`Call by Name`
```
def foo(x: Int) = x //Call by Value
def foo(x: => Int) = x //Call by Name
```
exapmle:
```
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
```

## 函数与匿名函数
函数与js中的函数类似，作为参数传递、作为方法的返回值、可以赋值给变量、把函数存储在数据结构里

### 函数类型
Scala语言中，函数类型的格式为`A => B`,表示一个接受类型A的参数，并返回类型B的函数
例：`Int => String` 是把整形映射为字符串的函数类型

### 高阶函数
高阶函数：接收的参数是函数、或者返回值可能是函数，或者两者皆有之。
exapmle:
参数为函数：
```
def operate(f: (Int, Int) => Int) = {
    f(4, 4)
}
```
返回值为函数：
```
def gretting() = (name: String) => {"hello" + " " + name}
```

### 匿名函数
匿名函数(Anonymous Function), 就是函数常量，也称函数文字量(Function Literal)
在Scala里，匿名函数的定义格式为：
    ```(形参列表) => {}```
example:
```
val a = (x: Int, y: Int) => x + y
```

### 柯里化
柯里化函数(Curried Function)把具有多个参数的函数转换为一条函数链,每个节点上都是单一参数。
例子：以下两个add函数是等价的
```
def add(x: Int, y: Int) = x + y

def add(x: Int)(y: Int) = x + y //Scala里柯里化的语法
```
example:
```
def curriedAdd(a: Int)(b: Int) = a + b

curriedAdd(3)(4)

val m = curriedAdd(3)____
m(4)
```

### 递归函数
递归函数(Recursive Function)在函数式编程中是实现循环的一种技术
example:
计算n!(n的阶乘)
```
def factorial(n: Int): Int = {
    if (n <=0 ) 1
    else n * factorial(n-1)
}
```

### 尾递归(Tail Recursive Function)
尾递归函数中所有递归形式的调用都出现在函数的末尾。
编译器检测到一个函数是用尾递归函数时，它就覆盖当前的活动记录而不是在栈中创建一个新的。
堆栈实现函数的调用，层数深 会导致堆栈溢出，优化方案：尾递归,在函数末尾,覆盖掉当前栈的状态 不会创建一个新的栈
example:
```
@annotation.tailrec //注解标识告诉编译器这是个尾递归函数
def  factorial(n: Int, m: Int): Int =
    if (n <=0 ) m
    else factorial(n-1, m*5)

factorial(5, 1)
```

## 集合 collection
List[T]:
val a = List(1, 2, 3, 4)

### 连接
val b = 0 :: a
val c = a ::: b

### 访问
head: a.head
tail: a.tail
非空：a.isEmpty、Nil.isEmpty

### 高级使用
1. filter:过滤
example: 
过滤
```
a.filter(x => x % 2 == 1)
“99 Red Balloons”.toList.filter(x => Character.isDigit(x))  //取得里面的数字
```

2. takeWhile 取值，当不满足条件时退出while取值
```
“99 Red Balloons”.toList.takeWhile(x => x != 'B')  // 当元素等于B时会退出
```

3. map 将每个元素映射到另外的值
```
List("x", "y", "z").map(x => x.toUpperCase)
List("x", "y", "z").map(_.toUpperCase) //同上的简写方式
```
思考下将 List(List(1, 2, 3), List(4, 5, 6)) 中的偶数取出来？
```
val q = List(List(1, 2, 3), List(4, 5, 6))
q.map(x => x.filter(x => x %2 ==0))
q.map(_.filter(_ %2 == 0)) //同上简写
> 输出结果
> List[List[Int]] = List(List(2), List(4, 6))
```
上面虽然过滤的只剩下偶数了，但是它还是一个两层的集合，怎么样把这个map打平让它变为List(2, 4, 6)?

4. flatMap
```
q.flatMap(_.filter(_ % 2 == 0))
> List[Int] = List(2, 4, 6)
```

5. reduceLeft
reduceLeft(op: (T, T) => T)
```
List(1, 2, 3).reduceLeft((x, y) => x + y)
List(1, 2, 3).reduce(_ + _) //简写
```

6. foldLeft
foldLeft(z: U)(op: (U, T) => U)
```
List(1, 2, 3).foldLeft(0)(_ + _)
List(1, 2, 3).foldLeft(1)(_ * _)
```

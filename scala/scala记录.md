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

### try与match表达式

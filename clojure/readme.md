### 出发
最近由于在探究`并发`相关的一些东西，找寻到了`七周七并发模型`这本书，继而又再次接触到了`函数式`编程思想的`clojure`语言，所以就跟着文章中的例子进而学习了起来。以便对并发、函数式编程有更好的理解。

#### clojure
`clojure` 是运行在 `jvm`的`Lisp`方言。`jvm`、`Lisp` ? 什么鬼，`jvm` java虚拟机，其`跨平台`特性的诱惑，所以很多语言就建立在它之上了，而不是直接建立在某个具体的操作系统上。
最主要的实现是`java`，其他的还有很多，比如开发大数据处理工具`spark`的`scala`，处理字符串、xml等推断出变量类型的`Groovy`，最近可能要火的`Kotlin`，还有仿其他语言实现的语言如`JRuby`、`JPython`、`Rhino(犀牛，js引擎)`; 有人可能知道`Lisp`本身是一种语言，而这里的`LISP`是指思维方式、编程方式。

#### 环境
1. 基于`jvm`，所以`jdk`要先装好啦，不然玩不下去，最好根据要求是`1.6+`版本
2. 下载 `clojure`，[下载页](https://www.clojure.org/community/downloads)我下载的是`clojure`的maven包，[clojure-1.8.0.zip](https://repo1.maven.org/maven2/org/clojure/clojure/1.8.0/clojure-1.8.0.zip)
3. 我暂时用到的命令
```clojure
; 进入 REPL
java -jar clojure-1.8.0.jar
; 执行 xxx.clj 脚本
java -jar clojure-1.8.0.jar /dir/test.clj  ; 哈哈，我实际用的是window操作系统，搞笑的是，我假装在使用别的操作系统，而没有使用`D:/dir/test.clj`
```

#### 基础语法
```clj
(+ 1 1); => 2
(+ 1 (- 3 2)); => 2
(max 3 5); => 5
(def meaning-of-life 42); 定义变量
(if (< meaning-of-life 0) "negative" "non-negative"); 判断 => non-negative
(def droids ["Huey" "Dewey" "Louie"]) ; 定义 数组
(droids 0) ; => Huey
(droids 1) ; => Dewey
(def me {:name "Paul" :age 45 :sex :male}) ; 定义map
(:age me) ; => 45
(defn percentage [x p] (* x (/ p 100.0))) ; 定义函数
(percentage 200 10)

;----------------------------------------------------------------------------惰性 相关分隔线， 下面开始介绍一些 “惰性” 相关的item
; 惰性，仅在需要时被执行
; 你不用为下面的命令担心内存不足的问题，即是它序列的长度有 一千万
(take 10 (range 0 10000000)); 立即得到结果，并不会真的产生一千万个数字，意味着不会耗尽内存
(take 10 (map (partial * 2) (range 0 10000000))); 嵌套同样适用该规则

; partial 函数：符号，操作数； 返回一个柯里化的函数 (符合约定，只传一个参数)
(partial * 2); => #object[clojure.core$partial$fn_xxx]
; 下面我们将返回的函数，传入一个函数再执行下
((partial * 2) 3); => 6，可以看出 partial 只接收两个参数：符号，数字，至于另外一个和符号、数字做运算的数字，则需要通过返回的函数来传入

; 无穷序列
(take 10 (iterate inc 0)) ; iterate 函数：函数，初参； 递归无穷的执行 
; 就比如 下面会打印一片的0
(iterate (fn [x] (* 0 x)) 0)

; 所谓的懒惰，不仅意味着不用到不会执行，还意味着如果不再用到将被舍弃，比如下面的例子需要运行一段时间，但不会耗尽内存：
(take-last 5 (range 0 10000000))
; 慢慢的发现clojure函数的特点，函数做尽量少的事情，参数顺序方面，先关注自己本身解决问题的的核心，比如take-last，解决的问题是："取后面几个"，首先关心的是 “几个” ，其次在是“谁”

```


#### 其他-一些思考
并行冲突的关键是在于"变化"，函数式编程是“不变”的一种处理方式。

#### 其他-附录
关于`LISP`的一些参考，出自[链接](http://www.vaikan.com/home-at-last-or-the-last-programming-language-i-will-ever-learn-lisp-clojure/)，另外这篇文章的表达的内容、方式都有很吸引我的地方，比如`创业开公司带来的自己掌舵愉悦的感觉`、`编程带来的影响：限定与迫使带来的好处、影响思维方式、言行、感知等`、`两条谏言`、`身为CTO，再学一门新语言，即使会“失身份”`等等。

关于方言、LISP 你还可以阅读下这里[为什么Lisp语言如此先进？-阮兄的](http://www.ruanyifeng.com/blog/2010/10/why_lisp_is_superior.html)

[Clojure-cheat-sheet](http://cljs.info/cheatsheet/)
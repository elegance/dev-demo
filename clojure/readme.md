### 出发
最近由于在探究`并发`相关的一些东西，找寻到了`七周七并发模型`这本书，继而又再次接触到了`函数式`编程思想的`clojure`语言，所以就跟着文章中的例子进而学习了起来。以便对并发、函数式编程有更好的理解。

```clojure
; 静心学习，不要带着压力
; 就当是探索

; 看是一种感觉
; 照着小例子在REPL，是一种相对深入的理解
; 细细的按着例子，写出你的 .clj 实现书中的例子，是更深一层的理解

; so，视情况，有舍有得，得与投入时间成正比。
```


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

;----------------------------------------------------------------------- 数据流 -------------------------
; ---------------------------------  future 模型
(def sum (future (+ 1 2 3 4 5))) ; 发现 前缀表达比中缀表达 省略了  n 个 + 号
(deref sum) ; => 15 , 解引用, 获取引用代表的值
@sum ; => 15，同上，解引用

; future 小例子
(let [a (future (+ 1 2))
      b (fubure (+ 3 4))]
    (+ @a @b))

; let 将 (future (+ 1 2)) 赋值给 a
; let 将 (future (+ 3 4)) 赋值个 b
; 对 (+ 1 2) 和 (+ 3 4) 的求值可以分别在不同的线程中进行。
; 外层的 (+ @a @b) 加法将一直阻塞，直到内层加法的完成。

; ---------------------------------  promise 模型
(def meaning-of-life (promise))
(future (println "The meaning of life is:" @meaning-of-life)) ; @解引用，但是meaning-of-life 是promise，处于pending，还未交付
(deliver meaning-of-life 42)

; 首先，构造了一个叫meaning-of-life的promise对象
; 然后，用future函数创建一个线程来打印其值 , 利用future函数创建线程是Clojure的惯例
; 最后，用deliver为promise对象赋值，之前创建的线程就不在阻塞了。

; future模型、promise模型 应用：3.web_server.clj
; 3.web_server.clj 在我这里并没有运行起来，因为需要project.clj 与 leiningen 来构建，我就没有深入去演示了
; 可以参考：http://www.liaoxuefeng.com/article/0014171500694729a42a2c8b7f245e0bd54612c88d78a03000

; ---------------------------------- 原子变量
(def my-atom (atom 42))
deref my-atom ; => 42
@my-atom ; => 42
(swap! my-atom inc) ; => 43
(swap! my-atom + 2) ; => 45

(def session (atom {}))
(swap! session assoc :username "paul")
(swap! session assoc :session-id 1234)

; 标识 与 状态
; 你的汽车有多少油？ 现在这一刻可能有一半，一段时间后可能空了，再过几分钟又满了
; 有多少(量) 是一个标志，其状态是一直在改变的，也就是说它是一些列不同的值 --- 20170605 12:30, 值 0.53，20170605 12:40 值 0.12..
; 命令式语言中，一个变量混合了标识与状态，一个标识只能有一个值，这让我们很容易忽略一个事实，“状态(值)是随着时间变化的一些列值”
; 持久数据结构将标识与状态分离开来，如果获取了一个标识的当前状态，无论将来找个标识怎么修改，获取的那个状态将不改变。

; 重试
; 原子变量无锁，是利用的 CAS(compareAndSet) ，swap! 当不满足 compare时则会自动重试，放弃之前重函数中的取值，重新取、尝试设值

; 检验器
; 假设我们需要一个非负值的原子变量。在创建原子变量时可以提供一个 校验器 (validator)
(def non-negative (atom 0 :validator #(>= % 0)))
(reset! non-negative 42) ; => 42
(reset! non-negative -1) ; => IllegalStateException Invalid reference state

; 监视器
; 可以为原子变量添加一个监视器
(def a (atom 0))
(add-watch a :print #(println "Changed from " %3 " to " %4)) ; 使用了读取器宏 #(...)来定义匿名函数，该函数用于打印原子变量的旧值和原子变量的新值
(swap! a + 2)
; 添加监视器时需要提供一个键值和一个监视函数

; ---------------------------------- 代理
; 与原子变量类似，代理包含了对一个值的引用，可以通过deref与@获取该值
(def my-agent (agent 0))
@my-agent ; => 0
(send my-agent inc)
@my-agent ; => 1
(send my-agent + 2)
@my-agent ; => 3

; 错误处理
(def non-negative (agent 1 :validator (fn [new-val] (>= new-val 0)))) ; 代理同原子变量一样都支持校验器和监视器，这里的校验器确保其值不为负数
(send non-negative dec) ; 自减1次
@non-negative ; => 0
(send non-negative dec) ; 再自减1次
@non-negative ; => 0 ，校验器生效值， 还是0，但是如果**继续**使用这个发生过错误的代理，会怎样呢？
(send non-negative inc) ; => IllegalStateException Invalid refrence state...
@non-negative ; => 0
; 一旦代理发生错误，就会进入生效状态，之后对代理数据的任何操作都会失败。
; 使用agent-error 可以查看一个代理是否为失效状态，使用 restart-agent 可以充值失效状态的代理
(agent-error non-negative)
(restart-agent non-negative 0)


; ---------------------------------- 引用 (ref)
; 应用(ref)比原子变量(atom)和代理(agent)更复杂，通过引用可以实现软件事务内存(Software Transactional Memory, STM)
; 通过原子变量和代理每次仅能修改一个变量，而通过STM可以对多个变量进行并发的一致的修改，就像数据库事务可以对多条记录进行并发一致的修改一样
(def my-ref (ref 0)) ; 与原子变量、代理类似，引用使用 ref 包装了对一个值的引用
@my-ref ; => 0
(ref-set my-ref 42) ; => IllegalStateException No transaction running
(alter my-ref inc) ; => IllegalStateExeception No transaction running
; 引用的值可以通过 ref-set 来设置，Clojure提供了alter函数来修改引用的值，它类似于之前提到的 swap! 和 send，但是它只能再事务中调用
; dosync 创建一个事务
(dosync (ref-set my-ref 42)) ; dosync 包装的所有元素构成了一个事务
(dosync (alter my-ref inc))

; 多个引用，模拟转账事务
(defn transfer [from to amount]
  (dosync from - amount)
  (dosync to + amount))

; 验证
(def checking (ref 1000))
(def savings (ref 2000))
(transfer savings checking 100) ; => 1100
@checking ; => 1100
@savings ; => 1900
```


#### 其他-一些思考
并行冲突的关键是在于"变化"，函数式编程是“不变”的一种处理方式。

#### 其他-附录
关于`LISP`的一些参考，出自[链接](http://www.vaikan.com/home-at-last-or-the-last-programming-language-i-will-ever-learn-lisp-clojure/)，另外这篇文章的表达的内容、方式都有很吸引我的地方，比如`创业开公司带来的自己掌舵愉悦的感觉`、`编程带来的影响：限定与迫使带来的好处、影响思维方式、言行、感知等`、`两条谏言`、`身为CTO，再学一门新语言，即使会“失身份”`等等。

关于方言、LISP 你还可以阅读下这里[为什么Lisp语言如此先进？-阮兄的](http://www.ruanyifeng.com/blog/2010/10/why_lisp_is_superior.html)

[Clojure-cheat-sheet](http://cljs.info/cheatsheet/)
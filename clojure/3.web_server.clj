; ------------------------------------------------------------------- 目标
; 加深理解 future 模型 和 promise 模型 的 函数式 web 服务

; ---------------------- 需求
; 1. 创建一个web服务，用来接收实时的文本数据，并进行翻译。
; 2. 文本数据由片段(snippet)构成，片段带有序号，片段举例
    ; 0 Twas brilling, and the slithy roves
    ; 1 Did gyre and gimble in the wabe:
    ; 2 All mimsy were the borogoves,
    ; 3 And the mome raths outgrable.
    ; 如果将片段0提交给服务，将构造发往snippet/0的PUT请求，其内容是 “Twas brilling, and the slithy roves”，片段1将发往/snippet/1以此类推。


; ---------------------- 简要分析
; 这是一个看似简单，但实现起来并不那么简单。
; 首先代码是运行在一个并发的web服务器上的，这就要求代码是线程安全的。
; 其次，由于网络的特性，代码需要处理一些特殊的情况，例如片段丢失、重试、重复提交和乱序提交。


; --------------------------------------------------------------------- coding
(def snippets (repeatedly promise)) ; snippets 是一个有promise对象构成的无穷懒惰序列

; 当某个snippet可用时，调用 accept-snippet来为对应的promise对象赋值（交付）
(defn accept-snippet [n text]
    (deliver (nth snippets n) text)) ; nth 取集合中索引为n的元素

; 要串行地处理偏关，只需要创建一个线程，按序号对每个promise进行解析即可。例如，下面的代码可以串行输出每个片段值：
(future
    (doseq [snippet (map deref snippets)]
        println(snippet)))

; doseq 会串行处理一个序列。本例中

; --- 下面将上面的这些组装成一个web服务。下面的代码利用了Compojure库
(defroutes app-routes
    (PUT "/snippet/:n" [n :as {:keys [body]}]
        (accept-snippet (end/read-string n) (slurp body))
        (response "OK")))

(defn -main [& args]
    (run-jetty (site app-routes) {:port 3000}))

; 上面代码 PUT 定义了路由，其将调用accept-snippet函数
; 我们使用了内置的web服务Jetty
; 这里引用到了 Compojure，需要project.clj与leiningen 来支持，这里就没有再深入下去了
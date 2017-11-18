(defn recursive-sum [numbers]
    (println (rest numbers))
    (if (empty? numbers) 
    0
    (+ (first numbers) (recursive-sum (rest numbers)))
    )
)

(println(recursive-sum [100 200 300 400]))

; 利用  (reduce fn [acc, cur] initialVal array)
; fn 定义匿名函数

(defn reduce-sum [numbers]
    (reduce (fn [acc x] (+ acc x)) 0 numbers))

(println (reduce-sum [1 2 3 4]))

; 优化3
; + 是现成函数
(defn reduce-sum2 [numbers]
    (reduce + numbers)
)
(println (reduce-sum2 [1 2 3 4]))

; 并行
(ns sum.core ; 定义命名空间 sum.core
    (:require [clojure.core.reducers :as r])) ; 将 clojure.core.reducers 缩写为 r

(defn sum [numbers]
    (r/reduce + numbers))

(defn parallel-sum [numbers]
    (r/fold + numbers)) ; "并行" 真正修改之处: 适用 clojure.core.reducers/fold 函数 替换了 reduce 函数

; 对比测试
; 定义测试数据
(def numbers (into [] (range 0 10000000)))

(println "(代码运行在jvm中，多运行几次代码以便预热 JIT编译器，得到比较客观的运行时间")
(println)

(println "串行reduce计算：")
(time (sum numbers))
(time (sum numbers))
(time (sum numbers))

(println)
(println "并行reducers/fold计算：")
(time (parallel-sum numbers))
(time (parallel-sum numbers))
(time (parallel-sum numbers))
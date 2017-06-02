; Wikipedia 词频统计的函数式版本

; 先来串行版，需要以下三个函数:
; 函数 1： 接受Wikipedia XML dump，返回dump中的页面序列
; 函数 2：接受一个页面，返回页面上的词序列
; 函数 3：接受一个词序列，返回含有词频的map

; // 书籍中，也就是本代码示例中，不会介绍前两个函数；因为这里的主题是并发，而不是 XML或字符串处理，所以在此重点讨论如何统计词频，再进而对这部分进行并行处理

;--------------------------------------------------- 理论 分割线 --------------------------------------
; 认识一下函数式map
; get 函数
(def counts {"apple" 2 "orange" 1}) ; 定义map
(get counts "apple") ; 取 map  的 key 的 值
(get counts "banana" 0) ; 取 map 的 key 的 值，没有则返回 默认值 ，不设默认将返回 nil
; assoc => associate 联合；联想
(assoc counts "banana" 1) ; map 联合了一项 key,value，（以新的值返回，不会影响counts 变量本身哦）
(assoc counts "apple" 3)

;--------------------------------------------------- 干正事 1. 分割线 --------------------------------------
; 词频统计函数
(defn word-frequencies [words]
    (reduce 
        (fn [counts word] (assoc counts word (inc (get counts word 0)))) 
        {} words)); {} 空的对象作为 initialVal ，而 words 是 reduce 另外的一个参数

(word-frequencies ["one" "potato" "two" "potato" "three" "potato" "four"]) ; 序列间的 都好 “," 都不需要了，多么的"简洁"呀，因为空格、双引号都是分隔

; 上面的结果是map，不能直接的println来打印，可以放到repl里执行看到效果 。。。。 -_- 后面应该能发现怎样在文件里打印map

; 另外，Clojure 已经走在了我们前面，已经提供了现成的 frequencies函数，该函数能针对任何集合，输出集合中每个元素出现的次数
(frequencies ["one" "potato" "two" "potato" "three" "potato" "four"]) ; 序列间的 都好 “," 都不需要了，多么的"简洁"呀，因为空格、双引号都是分隔

; 我们已经学会了如何统计词频，剩下的就是将其与 XML处理结合起来


;------------------------------------------------------- 为了 理解后面的代码，先来一点 序列相关的函数 ------------------
(map inc [0 1 2 3 4 5]) ; => (1 2 3 4 5 6)
;   map 函数入参：函数，集合； 集合中的每一项元素，都会执行一遍传入的函数
;   inc 函数入参：x ；inc将入参数，increment 即叠加一次
(map (fn [x] (* 2 x)) [0 1 2 3 4 5]) ;=> (0 2 4 6 8 10)
; fn 定义了一个匿名函数，函数内部就是对入参 * 2

; 最后假设一个函数会返回一个序列，比如用正则表达式将字符串分割成词的序列：
(defn get-words [text] (re-seq #"\w+" text))
(get-words "one two three four")
; re-seq 函数入参：#"\w+" , 字符串

; 如果我们要对一个字符串序列进行 get-words 进行映射，会得到一个二维序列
(map get-words ["one two three" "four five six" "seven eight nine"]) ; => (("one" "two" "three") ("four" "five" "six") ("seven" "eight" "nine"))

; 如果我们需要所有的结果，并且是一个一维序列，则可以使用 mapcat
(mapcat get-words ["one two three" "four five six" "seven eight nine"]) ; => ("one" "two" "three" "four" "five" "six" "seven" "eight" "nine")

; ok 一切准备就绪，现在可以组装我们的词频统计函数了
(defn count-words-sequential [pages] 
    (frequencies (mapcat get-words pages))
)
; get-words 将句子，处理为 单词数组(序列)；
; mapcat 将pages，也就是句子序列，遍历传递给 get-words，每一次遍历返回的是序列，所以就是一堆的序列，即序列的序列，二维数组，mapcat会将二维数组摊平
; frequencies 得到集合中每个元素的出现次数
; 测试调用
(count-words-sequential ["one two three" "four five six" "seven eight nine"])
; => {"six" 1, "three" 1, "two" 1, "seven" 1, "five" 1, "eight" 1, "one" 1, "nine" 1, "four" 1}
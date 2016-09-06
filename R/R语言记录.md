## 软件安装 R、RStudio
R下载地址：https://cloud.r-project.org/
RStudio下载地址：https://www.rstudio.com/products/rstudio/download/

## 1. 数据结构
. 字符(charactor)
. 数值(numeric: real numbers)
. 整数(integer)
. 复数(complex): 1+2i
. 逻辑(logical: True/False)

### 基础记录(1)
    ```
    > x <- 1 #定义x变量并赋值为1, 赋值使用左向单箭头"<-"，虽然也可以用"="但是不建议，在某些特殊情况下会出错
    > x
    > class(x)
    > x <- 2L
    > class(x)
    > y <- "hello"
    > class(y)
    > t <- TRUE
    > x <- 1+2i
    ```

### 1.1 属性(attribute)
    1. 名称(name)
    2. 维度(dimensions: martrix, array)
    3. 类型(class)
    4. 长度(length)

### 1.2 向量(vector)
    1. 只能包括**同一类型**的对象
    2. 创建向量
        1. vector()
        2. c()
        3. as.logical()/as.numeric()/as.charactor()
        ```
        # Vector
        x <- vector("character", length = 10)
        x1 <- 1:4
        x2 <- c(1,2,3,4)
        x3 <- c(TRUE, 10, "a") #默认隐式转换
        x4 <- c("a", "b", "c")
        as.numric(x4)
        class(x1)
        names(x1) <- c("a", "b", "c", "d")
        x1
        ```

### 1.3 矩阵(matrix)
    . 向量+维度属性(整数向量:nrow, ncol)
    . 创建矩阵
        matrix(): 填充按先列后行
        vector() + dim
        cbind(),rbind()
        attributes()

        ```
        # martrix & Array
        x <- matrix(nrow = 3, ncol = 2) # 定义矩阵
        x
        x <- matrix(1:6, nrow = 3, ncol = 2) # 设定值，按列天成

        dim(x) #输出矩阵的维度

        attributes(x) #查看矩阵属性

        y <- 1:6 #定义向量
        dim(y) <- c(2, 3) #向量添加维度属性

        y2 <- matrix(2:7, nrow = 2, ncol = 3)

        rbind(y, y2) #按row拼接矩阵
        cbind(y, y2) #按列拼接矩阵
        ```
### 1.4 数组(array)
    . 与矩阵类似，但是维度可以大于2
    . 创建数组
        array
        ```
        x <- array(1:24, dim = c(4, 6))
        x2 <- array(1:24, dim = c(2, 3, 4))
        x2
        ```
### 1.5 列表(list)
    . 可以包含多个不同的元素
    . 创建列表
        list
        ```
        l <- list("a", 1, TRUE, 3 + 4i, 10L)
        l2 <- list(a=1, b=2, c=3)
        l3 <- list(c(1,2,3), c(4,5,6,7))

        x <- matrix(c(1:6), nrow = 2, ncol = 3)
        dimnames(x) <- list(c("a", "b"), c("c", "d", "e")) #给矩阵每行每列命名
        ```
### 1.6 因子(factor)
    . **分类**数据/有序 vs. 无序
    . 整数向量+标签(label)(优于整数向量)
        Male/Female vs. 1/2
        常用于lm(),glm()
        ```
        x <- factor(c("female", "female", "male", "male", "female"))
        x1 <- factor(c("female", "female", "male", "male", "female"), levels = c("male", "female"))
        x
        table(x)
        unclass(x)
        class(unclass(x))
        ```
### 1.7 缺失值(missing value)
    . NA/NaN: NaN属于NA, NA不属于NaN
    . NA 有类型属性: integer NA, character NA等
    . is.na()/is.nan()
    ```
    x <- c(NA, 1, NA, 2, TRUE)
    is.nan(x)
    is.na(x)

    x <- c(NaN, 1, NaN, 2, TRUE)
    is.nan(x)
    is.na(x)
    ```
### 1.8 数据框(data Frame)
    . 存储表格数据(tabular data), 与矩阵不同的是可以存储不同的数据类型
    ```
    df <- data.frame(id=c(1, 2, 3, 4), name=c("a", "b", "c", "d"), gender=c(TRUE, TRUE, FALSE, FALSE))
    nrow(df)
    ncol(df)

    df2 <- data.frame(id=c(1,2,3,4), score=c(90,80,50,70))
    data.matrix(df2)
    ```
### 1.9 日期和时间(data，time)
    . 日期：Date
        . 距离1970-01-01的天数,date(),Sys.Date()
        . weekDays(),months(),quarters()
    . 时间：POSIXct/POSIXlt
        . 距离1970-01-01的秒数, Sys.time()
        . POSIXct: 整数，常用于存入数据框
        . POSIXlt: 列表，包括星期、年、月、日等信息

### 小结
```
        +2维度      -> 矩阵     data.martrix() <-----
向量    >2维度      -> 数组                         |
        +不同该类型 -> 列表     +元素长度相同   -> 数据框
        +整数+标签  -> 因子
```
## 2. 数据操作
### 2.1 构建子集
    1. []: 提取一个或多个类型相同的元素
    2. [[]]: 从列表或数据框中提取元素
    3. $: 按名称从列表或数据框中提取元素

    向量子集 exapmple:
    ```
    x <- 1:10 #定义向量
    x[1] #取向量的第1个值，区别于其他语言 下标从1开始
    x[6:10] #取6-10间的元素
    x[x>5] #取向量中大于5的元素
    x>5 #对x中每个元素判断是否大于5
    x[x>5 & x<7] #取大于5并且小于7的元素
    x[x<3 | x>7]  #取小于3或者大于7的元素

    y <- 1:4
    names(y) <- c("a", "b", "c", "d") #使用names给向量加上名字
    y[2]
    y["b"] #用名称取值
    ```
    矩阵子集 example:
    ```
    m = matrix(1:6, nrow = 2, ncol = 3)
    m[1,2]
    m[2,1]
    m[2,]
    m[,2]
    m[1, c(2, 3)]
    class(m[1, c(2, 3)])
    m[1, 2, drop = FALSE]
    ```
    dataframe exapmle:
    ```
    df <- data.frame(v1=1:5, v2=6:10, v3=11:15)
    df$v1
    df$v3[c(2,4)] <- NA #定位v3列的第2、4行，并赋值为NA
    df
    df[,2]
    df[df$v1<4 & df$v2>3,]

    df[df$v1>2,]
    df[which(df$v1>2),]

    subset(df, df$v1 > 2)
    ```
    list example:
    ```
    l <- list(id=1:4, height=170, gender=FALSE)
    l[1]
    l["id"]
    l[[1]]
    l[["id"]]
    l$id

    l <- list(a= list(1,2,3,4), b= c("Monday", "Tuesday"))
    l[[1]][[2]]
    l[[1]][2]

    l[[c(1,3)]]
    l[[c(2,2)]]

    l <- list(asdfgh = 1:10)
    l$a
    l[["a", exact = FALSE]]
    ```

### 2.2 重要函数的使用


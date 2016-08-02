## 基础环境 安装 java
下载地址：http://www.apache.org/dyn/closer.cgi/hadoop/common

## 配置文件
配置文件在`conf/`目录下
1. `hadoop-env.sh` 的 `JAVA_HOME`配置
2. `core-site.xml` 添加配置
```
<property>
    <name>hadoop.tmp.dir</name>
    <value>/hadoop</value>
</property>
<property>
    <name>dfs.name.dir</name>
    <value>/hadoop/name</value>
</property>
<property>
    <name>fs.default.name</name>
    <value>hdfs://master:9000</value>
</property>
```
3.`hdfs-site.xml` 添加配置
```
<property>
        <name>dfs.data.dir</name>
        <value>/hadoop/data</value>
</property>
```
4. `mapred-site.xml` 添加配置
```
<property>
    <name>mapred.job.tracker</name>
    <value>master:9001</value>
</property>
```
5. 将`hadoop`目录添加至`PATH`

6. 对`namenode`执行格式化操作
```
# hadoop namenode -format
# start-all.sh
# jps
# hadoop fs -ls /
```

## HDFS 基础概念
块存储，默认块大小为`64M`
### 特点：
1. 数据冗余，硬件容错
2. 流式数据访问,一次写入，多次读
3. 适合存储大文件

### 节点的两种类型：
1. NameNode: 管理节点，存放文件元数据
    文件与数据块的映射表
    数据块与数据节点的映射表

2. DataNode: 工作节点，存放真正的数据块

### HDFS中数据管理与容错
每个数据块有3个副本, 数据块与`NameNode`间有心跳检测，以便知道哪些数据块是健康存活的,`Secondary NameNode`会定期同步`NameNode`的原数据作为`NameNode`的备份

### HDFS文件读取流程
`客户端`(1. 发起文件请求) -> `NameNode`(2. 返回元数据)

### HDFS文件写流程
`客户端`(1. 文件拆分成64M块) -> `NameNode`(2. 返回可用的DataNodes) -> `客户端`(3. 写入Blocks) -> 4. 流水线复制块 -> 5. 更新元数据

### HDFS 使用
```
# hadoop fs -ls /
# hadoop fs -ls /user
# hadoop fs -mkdir input
# hadoop fs -put /etc/profile input/  #把本地文件放入HDFS
# hadoop fs -cat input/profile #输出HDFS文件内容
# hadoop fs -get input/profile #把HDFS文件拿出来 
# hadoop dfsadmin -report #文件系统所有信息
```

## MapReduce原理
分而治之，一个大任务拆分成多个小的子任务(map),并行执行后，合并结果(reduce)

### 基本概念
Job & Task: 一个作业(Job) 拆分为多个Task, Task又分为MapTask、ReduceTask

两类节点：
JobTracker: 1. 作业调度 2. 分配任务、监控任务进度 3. 监控TaskTracker状态
TaskTracker： 1. 执行任务 2. 向JobTracker汇报任务状态

### 容错机制
1. 重复执行(默认4次 )
2. 推测执行(某个task特别慢，其它任务已经完成,建立新的重复的task去执行)


## Hive 基于Hadoop的数据仓库
数据仓库有别于数据库，数据仓库是一个面向主题的、集成的、不可更新的、随时间不变化的数据集合，用于企业或组织的决策分析处理

Hive中的表对应的是HDFS中的目录，数据则对应HDFS中的文件

### Hive元数据
1. Hive将元数据存储值在数据库中(metasore)，支持mysql、derby等数据库，默认是derby数据库
2. Hive元数据包括表的名称、表的列和分区及其属性、表的属性(是否为外部表等)、表数据所在目录等

### Hive安装
下载地址：https://mirrors.tuna.tsinghua.edu.cn/apache/hive/

元数据存储在`mysql`中的配置
1. 在`conf\`目录下新增`hive-site.xml`,内容如下：
```
<configuration>
        <property>
                <name>javax.jdo.option.ConnectionURL</name>
                <value>jdbc:mysql://192.168.2.11/hive</value>
        </property>
        <property>
                <name>javax.jdo.option.ConnectionDriverName</name>
                <value>com.mysql.jdbc.Driver</value>
        </property>
        <property>
                <name>javax.jdo.option.ConnectionUserName</name>
                <value>root</value>
        </property>
        <property>
                <name>javax.jdo.option.ConnectionPassword</name>
                <value>root</value>
        </property>
</configuration>
```
2. 上传`jdbc连接jar`至`lib/`目录下

3. 将`hive`目录加入`PATH`, 执行 `hive`

### Hive 的管理
1. Cli 命令行
```
# hive
# hive --service cli
> show tables;
> show function;
> desc tableName;
> dfs -ls /;
> ! linux命令
> select * from tableName;
# hive -S #静默命令行，不产生MapReduce的调试信息
# hive -e 'select * from tableName';
```
2. Web 界面管理
`hive --service hwi &`, 端口号`9999`, 提示没有xxx.war,需要下载源码，将源码下的`hwi`下的`web`进行打包。
`jar cvfM0 hive-hwi-1.2.1.war -C web/ .`,并且编辑配置文件`hive-site.xml`,增加如下配置：
```
<property>
  <name>hive.hwi.listen.host</name>
  <value>0.0.0.0</value>
  <description>This is the host address the Hive Web Interface will listen on</description>
</property>
 
<property>
  <name>hive.hwi.listen.port</name>
  <value>9999</value>
  <description>This is the port the Hive Web Interface will listen on</description>
</property>
 
<property>
  <name>hive.hwi.war.file</name>
  <value>${HIVE_HOME}/lib/hive-hwi-<version>.war</value>
  <description>This is the WAR file with the jsp content for Hive Web Interface</description>
</property>
```
3. 远程服务启动方式(Thrift Server)
`hive --service hiveserver`

### 数据类型
基本数据类型：https://cwiki.apache.org/confluence/display/Hive/LanguageManual+Types
int string ...

复杂数据类型
1. Array: 数组，相同类型数据的集合
```
create table student
(
sid int,
sname string,
grade array<float>
);
//插入数据就是以下格式
//{1, Tom, [80, 90, 50]}
```
2. Map：集合类型，包含key->value键值对，可以通过key来访问元素
```
create table student1
(
sid int,
sname string,
grade map<string, float>
);
//插入数据就是以下格式
//{1, Tom, <'大学语文', 95>}
```
结合数组与map：
```
create table student2
(
sid int,
sname string,
grades array<map<string, float>>
);
//插入数据就是以下格式
//{1, Tom, [<'大学语文', 95>, <'大学英语', 98>]}
```
3. Struct: 结构类型，可以包含不同的数据类型.这些元素可以通过“点语法”的方式来得到所需要的元素
```
create table student3
(
sid int,
info struct<name:string, age:int, sex:string>
);
//插入数据就是以下格式
//{1, {'Tom', 10, '男'}}
```
时间数类型
1. Date: 日期 年月日,不包含时间
2. Timestamp: 

### Hive数据存储
1. 基于HDFS
2. 没有专门的数据存储格式
3. 存储结构主要包括：数据库、文件、表、视图
4. 可以直接加载文本文件
5. 创建表时，指定Hive数据的列分隔符与行分隔符

#### 表
. Table内部表
    1. 与数据库中的Table在概念上是类似的
    2. 每一个Table在Hive中都有一个相对应的目录存储数据
    3. 所有的Table数据(不包含External Table)都保存在这个目录中
    4. 删除表时，元数据与数据都会被删除
    ```
    create table t1(tid int, tname string, age int);

    create table t2(tid int, tname string, age int)
    location '/mytable/hive/t2'; --指定位置

    create table t3(tid int, tname string, age int)
    row format delimited fields terminated by ','; -- 指定行的格式，delimited指定使用逗号作为分隔符

    create table t4
    as 
    select * from sample_data; --使用查询语句建立一张表

    create table t5Scott,21
    row format deliKing,20mited fields terminated by ','
    as
    select * from sample_data; --使用查询语句、并指定行中字段的分隔符为逗号来创建表
    --使用命令查看比对下 t4、t5文件所对应的数据
    # hdfs dfs -cat /user/hive/warehouse/t4/000000_0
    # hdfs dfs -cat /user/hive/warehouse/t5/000000_0

    alter table t1 add columns(english int); --给表添加新的列
    ```
. Partition 分区表
    分区条件：比如将性别作为条件，或者年龄、用户ID等
    ```
    create table partition_table
    (sid int, sname string)
    partitioned by (gender string)
    row format delimited fields terminated by ',';
    ```
. External Table 外部表
    . 指向已经在HDFS中存在的数据，可以创建Partition
    . 它和内部表在元数据的组织上是相同的，而实际数据的存储则有较大的差异
    . 外部表**只有一个过程**，加载数据和创建表同时完成，并不会移动到数据仓库目录中，只是与外部数据建立一个连接
    当删除一个外部表时，仅删除该链接。
    --
    举例如下：
    ```
    --1. 建立三个文件：
    student01.txt：   
        Tom,23
        Mary,20
    student02.txt:
        Mike,25
    student03.txt:
        Scott,21
        King,20

    --2. 将文件放入HDFS中
    # hadoop fs -put ~/hive-data/student01.txt input/
    # hadoop fs -put ~/hive-data/student02.txt input/
    # hadoop fs -put ~/hive-data/student03.txt input/

    --3. 建表语句
    hive> create external table extenal_student
        > (sid int, sname string, age int)
        > row format delimited fields terminated by ','
        > location '/input';
    ```
. Bucket Table 桶表
    桶表是对数据进行哈希取值，然后放入到不同文件中存储。
    ```
    create table bucket_table
    (sid int, sname string, age int)
    clustered by(sname) into 5 buckets;
    ```
#### 视图
    虚表(View) 可以跨越多张表
    ```
    create view empinfo
    as
    select e.empno, e.name, d.name
    from emp e, dept d
    where e.deptno=d.deptno;
    ```

## Hive 进阶

### 使用Load语句执行数据导入
- 语法：
    ```
    LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE]
    INTO TABLE tableName [PARTITION (partCol1=val1, partCol2=val2 ...)]
    ```
- exapmle:
    ```
    -- 1. 准备数据文件
    student01.txt:
        1,Tom,23
        2,Mary,20
    student01.txt:
        3,Mike,25
    student01.txt:
        4,Scott,21
        5,King,20
    -- 2. 将student01.txt导入t3
    hive> load data local '/root/data/student01.txt' into table t3;
    -- 3. 将/root/data下的所有文件导入t3中，并且覆盖原来的数据
    hive> load data local inpath '/root/data/' overwrite into table t3;
    ```
### 使用Sqoop进行数据的导入
    Apache Sqoop 工具用于数据导入、导出,下载页：http://www.apache.org/dyn/closer.lua/sqoop/1.4.6
    安装：解压后配置环境变量`HADOOP_COMMON_HOME`、`HADOOP_MAPRED_HOME`
    ```
    export HADOOP_COMMON_HOME=/usr/local/src/hadoop-1.2.1
    export HADOOP_MAPRED_HOME=/usr/local/src/hadoop-1.2.1
    ```
    
    example 
        1.使用Sqloop将oracle数据导入到HDFS中
        ```
        ./sqoop import --connect jdbc:oracle:thin:@192.168.2.11:1521:orcl --username scott 
        --password tiger --table emp --columns 'empno,ename,job,sal,deptno' -m 1 --target-dir '/sqloop/emp'
        ```
        `-m`:指定`mapreduce`的进程数

        2.使用Sqloop将oracle数据导入到Hive中
        ```
        ./sqoop import --hive-import --connect jdbc:oracle:thin:@192.168.2.11:1521:orcl --username scott 
        --password tiger --table emp --columns 'empno,ename,job,sal,deptno' -m 1 
        ```

        3.使用Sqloop将oracle数据导入到Hive中，并且指定表名
        ```
        ./sqoop import --hive-import --connect jdbc:oracle:thin:@192.168.2.11:1521:orcl --username scott 
        --password tiger --table emp --columns 'empno,ename,job,sal,deptno' -m 1 --hive-table emp1
        ```

        4.使用Sqloop将oracle数据导入到Hive中，并且指定where条件
        ```
        ./sqoop import --hive-import --connect jdbc:oracle:thin:@192.168.2.11:1521:orcl --username scott 
        --password tiger --table emp --columns 'empno,ename,job,sal,deptno' -m 1 --hive-table emp2 --where 'DEPTNO=10'
        ```

        5.使用Sqloop将oracle数据导入到Hive中，并且使用查询语句
        ```
        ./sqoop import --hive-import --connect jdbc:oracle:thin:@192.168.2.11:1521:orcl --username scott 
        --password tiger -m 1 --query 'SELECT * FROM EMP WHERE SAL<2000 AND $CONDITIONS' --target-dir '/sqloop/emp5' --hive-table emp5
        ```

        6.使用Sqloop将Hive数据导入到oracle中
        ```
        ./sqoop export --connect jdbc:oracle:thin:@192.168.2.11:1521:orcl --username scott 
        --password tiger -m 1 --table MYEMP --export-dir ****
        ```

### Hive数据查询

#### 函数 `nvl`

数学函数：`round`,`ceil`,`floor`等
字符函数：`lower`,`upper`,`length`,`concat`,`substr`,`trim`,`lpad`,`rpad`等
收集函数：返回map中集合的个数，size(map(<key, value>, <key, value>))
    `select size(map(1,'Tom', 2, 'Marry'))`
转换函数：cast 转换数据格式
    `select cast(1 as bigint); select cast('2016-06-15' as date);`
日期函数：`to_date`,`year`,`month`,`day`,`weekofyear`,`datediff`,`date_add`,`date_sub`
条件函数：
    - coalesce: 从左到右返回第一个不为null的值
    - case.. when... :条件表达式
聚合函数：`count`,`sum`,`min`,`max`,`avg`
表生成函数：`explode` 将集合生成行
    `select explode(map(1, 'Tom', 2, 'Marry', 3, 'Mike'));`

#### Hive的表连接
. 等值连接
. 不等值连接
. 外连接 左外、右外
. 自连接

#### 简单查询的Ftech Task功能
. 从Hive0.10.0版本开始支持
. 三种配置方式
    . set hive.fetch.task.conversion=more; #在当前hive提示符下 就不会开启mapreduce作业来执行
    . hive --hiveconf hive.fetch.task.conversion=more #启动hive命令行之前
    . 修改hive-site.xml文件

#### Hive子查询
. hive只支持：from和where子句中的子查询

### Hive的java客户端操作
. 启动Hive远程服务 `# hive --service hiveserver`

####客户端连接方式
1. JDBC
2. Thrift Client

### 自定义函数
1. 创建java类并继承org.apache.hadoop.hive.ql.exec.UDF
2. 需要实现evaluate函数，evaluate函数支持重载
3. 将程序打包放置到目标机器
4. 进入hive客户端，添加jar包
    `hive> add jar /root/udf-test.jar`
5. 创建临时函数
```
hive> create temporary function <函数名>
as 'Java类名';
```

6. `select <函数名> from table;`

7. `hive> drop temporary function <函数名>;`
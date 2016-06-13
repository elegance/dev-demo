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

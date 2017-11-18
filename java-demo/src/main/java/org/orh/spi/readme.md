参考: [Java的Spi机制研究](http://blog.csdn.net/Evankaka/article/details/58265019)

### SPI机制概念
 SPI的全称是Service Provider Interface。简单来说，SPI机制提供了一个表达接口和其具体实现类之间的绑定关系的方案。具体是在JAR包的"META-INF/services/"目录下建立一个文件，文件名是接口的全限定名，文件的内容可以有多行，每行都是该接口对应的具体实现类的全限定名。

  SPI可以理解是为接口寻找服务实现类。
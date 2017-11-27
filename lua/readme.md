## Lua 
Lua脚本是一个很轻量级的脚本，也是号称性能最高的脚本，用在很多需要性能的地方，比如：游戏脚本，nginx，wireshark的脚本，当你把他的源码下下来编译后，你会发现解释器居然不到200k，这是多么地变态啊（/bin/sh都要1M，MacOS平台），而且能和C语言非常好的互动。
参考自： [LUA简明教程LUA简明教程](https://coolshell.cn/articles/10739.html)

#### 练习：
* [基础语法练习](LuaBase.lua)，注释、变量、函数、控制语句、循环
* [Metatable-操作符重载](MetaTableTest.lua)
* [面向对象-OOP](OOP.lua)，封装、继承
* [模块化](MyModuleTest.lua)
* [随机数](RandomTest.lua)

#### 依赖包管理
比如`os.time()`返回的是秒，不满足一些需求，于是搜索到可以使用`socket.gettime()`，但是依赖`socket`模块，顺便查阅记录下lua的包管理相关。

使用的是`LuaRocks`，Luarocks是一个Lua包管理器，基于Lua语言开发，提供一个命令行的方式来管理Lua包依赖、安装第三方Lua包等，社区比较流行的包管理器之一，另还有一个LuaDist，Luarocks的包数量比LuaDist多。

[Quick-start](https://luarocks.org/#quick-start):
```
$ wget https://luarocks.org/releases/luarocks-2.4.3.tar.gz
$ tar zxpf luarocks-2.4.3.tar.gz
$ cd luarocks-2.4.3
$ ./configure; sudo make bootstrap
$ sudo luarocks install luasocket
$ lua
Lua 5.3.4 Copyright (C) 1994-2017 Lua.org, PUC-Rio
> require "socket"
```
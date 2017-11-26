require('ModuleHello') -- 直接输出 hello world 了

--
--1) require 函数,载入同样的lua文件,只有第一次的时候会去执行,后面的相同就不会执行
--2) 如果你想要每一次都执行的话,可以使用 dofile('hello')函数
--3) 如果你要载入后不执行,等需要的时候再执行,可以使用 loadfile()函数

local hello = loadfile('ModuleHello.lua') -- 注意此处 加上了 .lua 后缀 才有效
hello()


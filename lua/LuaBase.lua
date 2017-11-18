print('hi')

--------------------------------------------- 注释
-- 两个减号是注释
--
--
--

--[[
	这是块注释
	这是块注释
--]]


--------------------------------------------- 变量
------ 数字
--
-- Lua 的数字只有double型,64bits,不必担心Lua处理浮点数会慢,或是有精度问题
--
num = 1024
num = 3.0
num = 3.1416
num = 314.16e-2
num = 0.3146E1
num = 0xff
num = 0x56

------ 字符串
--
-- 字符串可以使用单引号,也可以使用双引号,支持C类型的转义 如'\n','\t'等等
-- 下面用4中方式定义了完全相同的字符串
a = 'alog\n123"'
a = "alo\n123\""
a = '\97lo\10\0423"'
a = [[alo
123"]]

print(a)


------ nil
--
-- C语言中的NULL在Lua中是nil,比如你访问一个没有声明过的变量,就是nil,比如下面的v值
v = UndefinedVariable
print(v)
print(type(v))

------ 变量作用域: 全局 /局部
--
-- 需要注意的是: Lua中的变量如果没有特殊说明, 全是全局变量,那怕是语句块或者函数里.变量前加local关键字的是局部变量
--
theGlobalVar = 50
local theLocalVar = 'local variable'


--------------------------------------------- 控制语句
--
--
------ while 循环
--
sum = 0
num = 1
while num <= 100 do
	sum = sum + num
	num = num + 1
end
print('sum = ', sum)


------ if-else 分支
--
-- 切换以下四行的注释进行测试
age, sex = 40, 'Male'   --男人四十一枝花 的条件
-- age, sex = 80, 'Male'   --old man without country 的条件
-- age = 15				   --too young, too native 的条件
-- age, sex = 80, 'Female' --else, 等待输入, You age is 18

if age == 40 and sex == 'Male' then
	print('男人四十一枝花')
elseif age > 60 and sex ~= 'Female' then
	print('old man without country!')
elseif age < 20 then
	io.write('too young, too native!\n')
else
	local age = io.read()
	print('You age is '..age)
end
-- 1) 不等于判断是: ~=
-- 2) io库的分别从 stdin和stdout读写的read和write函数
-- 3) 字符串拼接操作符: ..
-- 4) 条件表达式与/或/非: and or not
--


------ for 循环
--
-- 累加[1..100]间的整数
sum = 0
for i = 1, 100 do -- => js: for (let i = 1; i <= 100; i++) --默认i步进1
	sum = sum + i
end
print('for sum:'..sum)

-- 累加[1..100]间的奇数
sum = 0
for i = 1, 100, 2 do -- => js: for (let i = 1; i <= 100; i+=2)
	sum = sum + i
end
print('for sumOdd:'..sum)

-- 累加[100..1]间的偶数
sum = 0
for i = 100, 1, -2 do
	sum = sum + i
end
print('for sumEven:'..sum)

------ until 循环
sum = 2
repeat
	sum = sum ^ 2 -- 幂操作
	print(sum)
until sum > 1000


--------------------------------------------- 函数
-- Lua的函数和Javascript函数很像
--
--
------ 递归
--
function fib(n)
	if n < 2 then return 1 end
	return fib(n - 2) + fib(n -1)
end
print(fib(10))

------ 闭包
-- 示例1
--
function newCounter()
	local i = 0
	return function()
		i = i + 1 -- 函数访问外部变量, 外部函数局部变量使用后不销毁
		return i
	end
end

c1 = newCounter()
print(c1()) --> 1
print(c1()) --> 2

-- 示例2
--
function myPower(x)
	return function(y) return y ^ x end;
end

power2 = myPower(2)
power3 = myPower(3)

print(power2(4)) -- 4 的2次方
print(power3(5)) -- 5 的3次方


------ 函数返回值
--
-- 类似js中的解构赋值,可以一条语句上赋多个值
name, age, bGay = 'ouyang', 18, false, 'ouyang@163.com' -- 第4个值会被丢弃

-- 函数返回多个值
--
function getUserInfo(id)
	print(id)
	return 'ouyang', 37, 'ouyang@163.com', 'http://ouyang.com'
end
name, age, email, website, bGay = getUserInfo() -- 不传id, id打印为nil
print(name, age, email, website, bGay) -- gGay 5索引不存在, nil


------ 局部函数
--
-- 函数前面加上local 就是局部函数,Lua和Javascript中的函数一个模子
--
-- 下面两个函数是一样的
function foo(x) return x ^ 2 end
foo = function(x) return x ^ 2 end
print(foo(2))

--------------------------------------------- Table
-- 所谓Table起始就是一个Key-Value的数据结构, 向Javascript中的Object
--
-- 长这样
ouyang = {name = 'orh', age = 18, handsome = true}
print(ouyang) -- 将打印出16进制内存引用地址

-- table的CRUD操作
ouyang.website = 'ouronghui.com'
local age = ouyang.age
ouyang.handsome = false
ouyang.name = nil

--
-- 还可以像下面这样定义Table
t = {[20] = 100, ['name'] = 'orh', [3.14] = 'PI'}

print(t[20])
print(t['name'])
print(t[3.14])

--
-- 再来看看数组
arr = {10, 20, 30, 40, 50}
-- 等价于
arr = {[1] = 10, [2] = 20, [3] = 30, [4] = 40, [5] = 50}

-- 可以定义存放不同类型的数组
arr = {'string', 100, 'ouyang', function() print('ouronghui.com') end}
arr[4]() -- 调用其中的函数

-- Lua的下标是从1开始的
for i = 1, #arr do
	print(arr[i])
end
-- 1) 其中 #arr的意思是 arr的长度


-- 之前有说过,Lua中的变量,如果没有local关键字, 就是全局变量, Lua也是用Table来管理全局遍历的,Lua把全局变量放在了一个叫 "_G" 的Table中
--
--如下的方式来访问一个全局变量
globalVal = 'hi'
print(_G.globalVal)
print(_G['globalVal'])

--
-- 可以通过下面的方式来遍历一个Table
--
for k, v in pairs(_G) do
	print(k, v)
end


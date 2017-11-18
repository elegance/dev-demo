--------------------------------------------- MetaTable 和 MetaMethod
-- MetaTable和MetaMethod是Lua中的重要的语法，MetaTable主要是用来做一些类似于C++重载操作符式的功能。
--
-- 比如,有两个分数:
--
fraction_a = {numerator = 2, denominator = 3}
fraction_b = {numerator = 4, denominator = 7}

-- 我们想实现分数的相加: 2/3 + 4/7 ,如果直接执行: fraction_a + fraction_b 是会报错的
-- 所有我们可以动用MetaTable, 如下
fraction_op={}
function fraction_op.__add(f1, f2)
	ret = {}
	ret.numerator = f1.numerator * f2.denominator + f2.numerator * f1.denominator
	ret.denominator = f1.denominator * f2.denominator -- 公倍数, 上面是分子*相应倍数
	return ret
end

-- 位置前定义的两个table设置MetaTable: (setmentatable是库函数)
setmetatable(fraction_a, fraction_op)
setmetatable(fraction_b, fraction_op)


-- 于是就可以 使用 + 符号 了,相当于重载了操作符
fraction_s = fraction_a + fraction_b

print(fraction_s.numerator)
print(fraction_s.denominator)

-- 1.) 至于 __add 这是 MetaMethod, 这是Lua内建约定的, 还有如下的MetaMethod:
[[--
dd(a, b)                     对应表达式 a + b
__sub(a, b)                     对应表达式 a - b
__mul(a, b)                     对应表达式 a * b
__div(a, b)                     对应表达式 a / b
__mod(a, b)                     对应表达式 a % b
__pow(a, b)                     对应表达式 a ^ b
__unm(a)                        对应表达式 -a
__concat(a, b)                  对应表达式 a .. b
__len(a)                        对应表达式 #a
__eq(a, b)                      对应表达式 a == b
__lt(a, b)                      对应表达式 a < b
__le(a, b)                      对应表达式 a <= b
__index(a, b)                   对应表达式 a.b
__newindex(a, b, c)             对应表达式 a.b = c
__call(a, ...)                  对应表达式 a(...)
--]]


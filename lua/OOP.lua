-- 从 LuaBase的 MetaMethod 中可以看到有 __index(a, b) 对应表达式 a.b
--
-- 上面的__index这个重载,这个东西主要重载了 find key 操作.这操作有点像Javascript的prototype
--
-- 两个对象: a, b, 我们想让b作为a的prototype,只需要:
local a = {}
local b = {name = 'b'}

setmetatable(a, {__index = b})
print(a.name)

--
-- 示例2: 用 Window_Prototype的模板加上 __index 的MetaMethod来创建另一个实例
--
local Window_Prototype = {x = 0, y = 0, width = 100, height = 100}
local MyWin = {title = 'Hello'}
setmetatable(MyWin, {__index = Window_Prototype})

-- 于是,MyWin中就可以访问x, y, width, height 了
-- （注：当表要索引一个值时如table[key], Lua会首先在table本身中查找key的值, 如果没有并且这个table存在一个带有__index属性的Metatable, 则Lua会按照__index所定义的函数逻辑查找）

local Person = {}

function Person:new(p)
	local obj = p
	if obj == nil then
		obj = {name = 'orh', age = 26, handsome = true}
	end
	self.__index = self
	return setmetatable(obj, self)
end

function Person:toString()
	return self.name .. ' : ' .. self.age .. ' : ' .. (self.handsome and 'handsome' or 'ugly')
end

--
--1) self 就是 Person, Person:new(p) 相当于 Person.new(self, p)
--2) new 方法的 self.__index = self 的意图是怕self被扩展改写,所以,让其保持原样
--3) setmetatable 这个函数返回的是第一个参数值
--
--于是: 我们可以这样调用
--
local me = Person:new()
print(me:toString())

local kf = Person:new{name = 'King\'s fucking', age = 70, handsome = false}
print(kf:toString())


---------------- 继承
-- 类似Javascript都是在Prototype实例上修改
local Student = Person:new()

function Student:new(p)
	local obj = p
	if p == nil then
		obj = {year = 2017}
	end
	self.__index = self
	return setmetatable(obj, self)
end

function Student:toString()
	return 'Student : ' .. self.year .. ' : ' .. self.name
end

local student1 = Student:new{name = 'pipi', year = 2010, age = 7}
print(student1:toString())
print(student1.age)

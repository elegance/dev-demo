-- math.random 用来生成伪随机数
--
-- 1) 不带参,将生成 [0, 1]范围内的随机数
print(math.random())

-- 2) 带一个参数n,将产生 1 <=x <=n 范围内的随机数x (a 需为整数, x返回的也是整数)
print(math.random(5))
print(math.random(2))

-- 3) 带两个参数a,b, 将产生 a <= x <= b范围内的随机整数 x
print(math.random(5, 10))

-- 设置随机种子
math.randomseed(os.time())

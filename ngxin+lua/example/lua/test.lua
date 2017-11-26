-- 访问如http://host/lua 会发现全局变量一直不变，而共享内存一直递增
count = count + 1
ngx.say('global variable :', count)

local shared_data = ngx.shared.shared_data
ngx.say(', shared memory : ', shared_data:get('count'))

shared_data:incr('count', 1)
ngx.say('hello world')

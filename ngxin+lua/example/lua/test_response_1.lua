-- 写响应头
ngx.header.a = '1'

-- 多个响应头可以使用table
ngx.header.b = {'2', '3'}

-- 输出响应
ngx.say('a', 'b', '<br/>')
ngx.say('c', 'd', '<br/>')

-- 200 状态码退出
return ngx.exit(200)

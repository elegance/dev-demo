-- 初始化耗时模块
local redis = require 'resty.redis'
local cjson = require 'cjson'

-- 全局变量， 不推荐
count = 1

local shared_data = ngx.shared.shared_data
shared_data:set('count', 1)

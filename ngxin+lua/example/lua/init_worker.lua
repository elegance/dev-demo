local count = 0
local delayInSeconds = 300
local heartbeatChek = nil

heartbeatChek = function(args)
	count = count + 1
	ngx.log(ngx.ERR, 'do check ', count)

	-- ngx.time.at : 延时调用相应的回调方法；ngx.time.at(秒单位延时, 回调函数, 回调函数参数列表)
	-- 可以将延迟时间设值为0,即立即执行，任务在一个轻量级线程中执行不阻塞当前请求
	-- 可以设置：
	-- lua_max_pending_timers 1024; #最大等待任务数
	-- lua_max_running_timers 256; # 最大同时运行任务数
	local ok, err = ngx.timer.at(delayInSeconds, heartbeatChek)

	if not ok then
		ngx.log(ngx.ERR, 'failed to startup heartbeat worker...', err)
	end
end

heartbeatChek()

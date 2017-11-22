if ngx.req.get_uri_args()['jump'] == '1' then
	-- ngx.req.set_uri(uri, false): 可以内部重写uri（可以带参数），等价于rewrite ^/lua_rewrite_3;
	-- 通过配合if/else 可以实现 rewrite ^/lua_rewrite_3 break;
	-- 此处两者都是location内部url重写，不会重新发起新的location匹配；

	-- ngx.req.set_uri_args：重写请求参数，可以是字符串(a=1&b=2)也可以是table

	ngx.req.set_uri('/lua_rewrite_3', false)
	ngx.req.set_uri('/lua_rewrite_4', false)
	ngx.req.set_uri_args({a = 1, b = 2})
end

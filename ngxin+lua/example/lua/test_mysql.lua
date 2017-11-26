local mysql = require('resty.mysql')

local function close_db(db)
	if not db then
		return
	end
	db:close()
end

-- 创建实例
local db, err = mysql:new()
if not db then
	ngx.say('new mysql error :', err)
	return
end

-- 设置超时时间-毫秒
db:set_timeout(1000)

local props = {
	host = '127.0.0.1',
	port = 3306,
	database = 'mysql',
	user = 'root'
}

local function print_err(msg_prefix, err, errno, sqlstate)
	ngx.say(msg_prefix, err, ', errno :', errno, ', sqlstate : ', sqlstate)
	return close_db(db)
end

local res, err, errno, sqlstate = db:connect(props)

if not res then
	return print_err('connect to mysql error : ', err, errno, sqlstate)
end

--删除表
local drop_table_sql = 'drop table if exists test'
res, err, errno, sqlstate = db:query(drop_table_sql)
if not res then
	return print_err('drop table error : ', err, errno, sqlstate)
end

--创建表
local create_table_sql = 'create table test(id int primary key auto_increment, ch varchar(100))'
res, err, errno, sqlstate = db:query(create_table_sql)
if not res then
	return print_err('create table error: ', err, errno, sqlstate)
end

-- 插入
local insert_sql = 'insert into test(ch) values("hello")'
res, err, errno, sqlstate = db:query(insert_sql)

ngx.say('insert rows : ', res.affected_rows, ', id: ', res.insert_id, '<br/>')

--更新
local update_sql = 'update test set ch = "hello2" where id =' .. res.insert_id
res, err, errno, sqlstate = db:query(update_sql)
if not res then
	return print_err('update error: ', err, errno, sqlstate)
end

ngx.say('update rows : ', res.affected_rows, '<br/>')

-- 查询
local select_sql = 'select id, ch from test'
res, err, errno, sqlstate = db:query(select_sql)
if not res then
	return print_err('select error: ', err, errno, sqlstate)
end

for i, row in ipairs(res) do
	for name, value in pairs(row) do
		ngx.say('select row ', i, ' : ', name, ' = ', value, '<br/>')
	end
end

ngx.say('<br/>')

-- 防止sql注入
local ch_param = ngx.req.get_uri_args()['ch'] or ''

--使用 ngx.quote_sql_str 防止sql注入
local query_sql = 'select id , ch from test where ch = ' .. ngx.quote_sql_str(ch_param)
res, err, errno, sqlstate = db:query(query_sql)

if not res then
	return print_err('select error: ', err, errno, sqlstate)
end

for i, row in ipairs(res) do
	for name, value in pairs(row) do
		ngx.say('select row ', i, ' : ', name, ' = ', value, '<br/>')
	end
end

-- 删除
local delete_sql = 'delete from test'
res, err, errno, sqlstate = db:query(delete_sql)

if not res then
	return print_err('delete error: ', err, errno, sqlstate)
end

ngx.say('delete rows : ', res.affected_rows, '<br/>')

close_db(db)
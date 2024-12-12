
-- 格式化日期为数字
local function date_to_number(date_str)
    local m_date = ngx.re.match(date_str, [[(\d{4})-(\d{2})-(\d{2})]], "jo")
    return tonumber(m_date[1] + m_date[2] + m_date[3])
end

-- 录音服务域名 与 mp3 投产时间
local base_url = 'http://upload.record-test.com:9000/records/'
local mp3_start_day_number = date_to_number('2024-12-12')

local today = ngx.today()
local uri = ngx.var.uri
local access_url = ngx.var.scheme  .. "://" .. ngx.var.host .. uri
local m = ngx.re.match(uri, [[/records/(?<date>[^/]+)/(?<fs_ip>[^/]+)/(?<file_name>[^/]+)]], 'jo')

-- 不符合格式的请求直接返回
if not m then
    ngx.status = 400
    ngx.header["Content-Type"] = 'text/html; charset=utf-8'
    ngx.say('<h3>access_time: ' .. ngx.localtime() .. '</h3>')
    ngx.say('<h4>invalid uri: ' .. uri .. '</h4>')
    ngx.say('<h4>access_url: ' .. access_url .. '</h4>')
    return ngx.exit(400)
end

local proxy_url
-- 当日代理至fs 服务
if m['date'] == today then
    local fs_ip_after = ngx.re.gsub(m['fs_ip'], '_', '.')
    proxy_url = 'http://' .. fs_ip_after .. ':8033/records/' .. m['date'] .. '/' .. m['file_name']
else
    -- 访问文件是在mp3投产时间之后的，则将文件后缀改为mp3
    if date_to_number(m['date']) >= mp3_start_day_number then
        m['file_name'] = ngx.re.gsub(m['file_name'], '\\.wav$', '.mp3')
    end
    -- 代理至录音服务
    proxy_url = base_url .. m['fs_ip'] .. '/' .. m['date'] .. '/' .. m['file_name']
end

--  测试模式
if ngx.req.get_uri_args()['test'] == '1' then
    ngx.header["Content-Type"] = 'text/html; charset=utf-8'
    ngx.say('<h3>access_time: ' .. ngx.localtime() .. '</h3>')
    ngx.say('<h4>access_url: ' .. access_url .. '</h4>')
    ngx.say('<h4>proxy_url: <a href="' .. proxy_url .. '">' .. proxy_url .. '</h4>')
    return ngx.exit(200)
end
ngx.var.backend_url = proxy_url
ngx.exec('@proxy_location')
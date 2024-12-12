-- 未经解码的请求 uri
local request_uri = ngx.var.request_uri;
ngx.say('request_uri ', request_uri, '<br/>')

-- 解码
ngx.say('decode request_uri : ', ngx.unescape_uri(request_uri), '<br/>')

-- MD5
ngx.say('ngx.md5 : ', ngx.md5(123), '<br/>')

-- http time
ngx.say('ngx.http_time : ', ngx.http_time(ngx.time()), '<br/>')

local function date_to_number(date_str)
    local m_date = ngx.re.match(date_str, [[(\d{4})-(\d{2})-(\d{2})]], "jo")
    return tonumber(m_date[1] + m_date[2] + m_date[3])
end

ngx.say("ngx.today#" .. ngx.today() .. "<br>")
ngx.say("ngx.time#" .. ngx.time() .. "<br>")
ngx.say("ngx.now#" .. ngx.now() .. "<br>")
ngx.say("ngx.localtime#" .. ngx.localtime() .. "<br>")
ngx.say("ngx.utctime#" .. ngx.utctime() .. "<br>")
ngx.say("ngx.cookie_time#" .. ngx.cookie_time(1583484791) .. "<br>")
ngx.say("ngx.http_time#" .. ngx.http_time(1583484791) .. "<br>")
ngx.say("ngx.parse_http_time#" .. ngx.parse_http_time("Fri, 06 Mar 2020 08:53:11 GMT") .. "<br>")
--ngx.say("ngx.update_time#" .. ngx.update_time() .. "<br>")

local today = ngx.today()

local today_number = date_to_number(today)
local d0 = '2024-12-09'
local d1 = '2024-12-10'
local d2 = '2024-12-11'
local d3 = '2024-12-12'

ngx.say("d0#" .. tostring(date_to_number(d1) < today_number) .. "<br>")
ngx.say("d1#" .. tostring(date_to_number(d1) < today_number) .. "<br>")
ngx.say("d2#" .. tostring(date_to_number(d2) == today_number) .. "<br>")
ngx.say("d3#" .. tostring(date_to_number(d3) > today_number) .. "<br>")

local name = '1wav_file_name.wav'
ngx.say(ngx.re.gsub(name, '\\.wav$', '.mp3') .. '<br/>')

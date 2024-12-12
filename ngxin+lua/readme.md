## 下载 openresty 包：
[https://openresty.org/cn/download.html](https://openresty.org/cn/download.html), windows 下也有对应的包

## 运行
```bash
resty -e "ngx.say('Hello, OpenResty!')"

# 代码里面引用脚本路径是 /usr/example/lua/ ，所以下面先创建这些目录，然后软链到对应的目录
cd /
mkdir usr
cd usr

# 假定当前Git 代码拉取在 C:\dev\dev-demo 下
mklink /J example C:\dev\dev-demo\ngxin+lua\example

## openresty lualib 软链
# 假如openresty 解压在 C:\tmp\openresty-1.27.1.1-win64\
mklink /J lualib C:\tmp\openresty-1.27.1.1-win64\lualib

cd C:\dev\dev-demo\ngxin+lua\
start C:\tmp\openresty-1.27.1.1-win64\nginx.exe -p $PWD

tasklist /fi "imagename eq nginx.exe"

nginx -s stop
```

## 访问测试

### windows 下对应的操作命令
https://github.com/openresty/openresty/blob/master/doc/README-windows.md


## 其他参考文档：
[OpenResty最佳实践](https://moonbingbing.gitbooks.io/openresty-best-practices/content/lua/function_before_use.html)
[lua-nginx-module官方文档](https://github.com/openresty/lua-nginx-module)
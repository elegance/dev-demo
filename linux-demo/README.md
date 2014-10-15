## 用此README 作为记录一些命令笔记到文档

### 查看系统基本信息基础命令
1. `# uname -a`	#显示操作系信息(包括内核版本号、操作系统版本等)
2. `# cat /etc/issue`		#显示发型版本信息
3. `# cat /proc/cpuinfo`	#查看cpu信息
4. `# cat /proc/memoinfo`	#查看内存信息
5. `# hostname`	#查看计算机名
6. `# lsblk`	#查看计算机块设备,包括还未挂载的磁盘等,比如需要手动mount的前可以用此命令查看块设备
7. `# lsusb`	#查看系统中的usb设备。比如在插用usb接口格式的外置网卡时，可用此查看网卡的基本信息来查找驱动
8. `# env`	#显示用户环境变量

### 网络设置
1. `# ifconfig`	#查询、设定网卡与ip网域等相关参数
2. `# ifup eth0`	#启动网络接口，eth0为网络卡的代号 ,在ifconfig输出的第一列可以查看到
3. `# ifdown eth0`	#停止网络接口
4. `# route`	#查询、设定路由表
5. `# ip`	#复合式命令，可修改上述提到的功能

### 系统管理、组管理
1. 查看用户系信息 `# id root`
2. groupadd [-g gid] [-r] 组名
> -g: 指定的GID,不常用
  -r: 建立系统群组

`groupadd test_group`
3. groupmod [-g gid] [-n new_grpname] 群组名
`groupmod -n test_group1 test_group`
4. groupdel grpname
`groupdel test_group1`
5. *gpasswd: 群组管理员功能，待补充*

### 用户管理
1. useradd [-u UID] [-g初始群组] [-G 次要群组] [-mM] [-c 说明栏] [-d 家目录绝对路径] [-s shell] 使用者账号名
`useradd -m -g grp_test -c "张三的第二个帐号" zhangsan2`  #创建用户`zhagnsan2`，给定初始群组`grp_test`,-m 强制建立家目录(默认建立) ，-c是备注说明(comment)
`useradd -D`   #useradd的默认参数
2. passwd [-l] [-u] [--sdtin] [-S] 
3. usermod [-cdegGlsuLU] username
-c  ：后面接账号的说明
-d  ：后面接账号的家目录
-g  ：后面接初始群组
-G  ：后面接次要群组
-a  ：与-G 合用，可『增加次要群组的支持』而非『设定』
-l  ：后面接账号名称。即修改账号名称
-L  ：暂时将用户冻结，使他无法登入
-U  ：将 /etc/shadow 密码栏的 ! 拿掉，解冻啦


### 文件权限、文件夹权限
1. `chgrp` 改变文档所属群组
```
# chgrp [-R] grp_name dir_name/file_name
```
2. `chown` 改变文档拥有者
```
# chown [-R] user_name dir_name/file_name
# chown [-R] user_name:grp_name dir_name/file_name
```
3. `chmod` 改变文档的权限(x:1, w:2, r:4)
```
# chmod [-R] nnn dir_name/file_name
# chmod (a|u|g|o)(=|+|-)(r|w|x) dirName/file_name
# chmod u=rwx,g=rx,o=r file_name
```


setfacl [m]:
	m:修改
	R: 递归

### 文件操作 
1. `scp root@192.168.80.3:/root/lrzsz-0.12.20.tar.gz ./` 从目标机器拷贝文件到本机


### 磁盘/挂载相关
1. `# eject` 	#弹出光驱，加上 "-t"参数收回光驱托盘
2. `# mount -t ntfs-3g /dev/sdb5 /mnt/ntfs`	#挂载ntfs的磁盘，要先安装好了`ntfs-3g`哦，还有用lsblk看下计算机块设备信息
3. `# mount -o loop xyz.iso /media/cdrom/`	#挂载iso镜像

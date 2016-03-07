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

### 禁止、允许主机被ping
```
# echo 1 > /proc/sys/net/ipv4/icmp_echo_ignore_all	##禁止被ping
# echo 0 > /proc/sys/net/ipv4/icmp_echo_ignore_all	##启用被ping
```
系统启动时生效：
`# vim /etc/rc.d/rc.local` 添加下面一行
` echo 1 > /proc/sys/net/ipv4/icmp_echo_ignore_all`

### 修改ssh默认22端口
`# vim /etc/ssh/sshd_config` 修改Port的值，去掉前面的#号注释,修改完后重启下`sshd`服务
`# service sshd restart`
`# vim /etc/sysconfig/iptables` 如果有防火墙的话记得把端口添加到防火墙去。

### 系统管理、组管理
1. 查看用户系信息 `# id root`
2. 新增组 `groupadd`
	groupadd [-g gid] [-r] 组名
	> -g: 指定的GID,不常用
	  -r: 建立系统群组

	例：
		groupadd test_group
3. 修改组`groupmod`
	groupmod [-g gid] [-n new_grpname] 群组名
	例：
		groupmod -n test_group1 test_group
4. 删除组`groupdel`
	groupdel grpname
	例：
		groupdel test_group1
5. *gpasswd: 群组管理员功能，待补充*
6. 系统最近重启时间 `who -b`
7. 重启记录 `last reboot`

### 用户管理
1. useradd [-u UID] [-g初始群组] [-G 次要群组] [-mM] [-c 说明栏] [-d 家目录绝对路径] [-s shell] 使用者账号名
	例：
	创建用户`zhagnsan2`，给定初始群组`grp_test`,-m 强制建立家目录(默认建立) ，-c是备注说明(comment)
		`useradd -m -g grp_test -c "张三的第二个帐号" zhangsan2`  #创
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
2. `ln -s /path/file /path2/link_file` 创建软链接
3. `ldd /usr/local/nginx/sbin/nginx` 查看依赖库


### 挂载相关
1. `# eject` 	#弹出光驱，加上 "-t"参数收回光驱托盘
2. `# mount -t ntfs-3g /dev/sdb5 /mnt/ntfs`	#挂载ntfs的磁盘，要先安装好了`ntfs-3g`哦，还有用lsblk看下计算机块设备信息
3. `# mount -o loop xyz.iso /media/cdrom/`	#挂载iso镜像

### 磁盘相关

#### 相关命令
1. 查看系统支持的文件系统
	`cat /proc/filesystems`
	或
	`ll /lib/modules/2.6.32-431.23.3.el6.x86_64/kernel/fs`
2. 查看自己系统的文件格式
	`cat /etc/fstab`
	或
	`df -T`


##### 磁盘io监控
1. 安装`iotop`监控程序读写磁盘
	`yum install iotop`
2. `dd`命令简单测试磁盘读写操作性能
* 测试磁盘写能力
`time dd if=/dev/zero of=/test.dbf bs=8k count=300000`
因为/dev//zero是一个伪设备，它只产生空字符流，对它不会产生IO，所以，IO都会集中在of文件中，of文件只用于写，所以这个命令相当于测试磁盘的写能力。
 
* 测试磁盘读能力
`time dd if=/dev/sdb1 of=/dev/null bs=8k`
因为/dev/sdb1是一个物理分区，对它的读取会产生IO，`/dev/null`是伪设备，相当于黑洞，of到该设备不会产生IO，所以，这个命令的IO只发生在`/dev/sdb1`上，也相当于测试磁盘的读能力。
 
* 测试同时读写能力
`time dd if=/dev/sdb1 of=/test1.dbf bs=8k`
这个命令下，一个是物理分区，一个是实际的文件，对它们的读写都会产生IO（对`/dev/sdb1`是读，对`/test1.dbf`是写），假设他们都在一个磁盘中，这个命令就相当于测试磁盘的同时读写能力

#### 新增磁盘
磁盘连接好主机后，第一步要做好磁盘分区
1. 使用`fdisk`进行磁盘分区
`fdisk` 语法： `fdisk -l 装置名称` （大于2TB的硬盘用`parted`命令，这里暂没记录）
	> 
	-l  ：输出后面接的装置所有的 partition 内容。
若仅有 fdisk -l 时，则系统将会把整个系统内能够搜寻到的装置的 partition 均列出来。
	
	范例：
	```
	# df /	##重点在找出磁盘文件名而已
	Filesystem     1K-blocks    Used Available Use% Mounted on
	/dev/xvda1      20641404 2498956  17093924  13% /
	
	# fdisk /dev/xvda  ##仔细看，不要加上数字喔！ 
	Command (m for help): 	## 等待你的输入！
	Command (m for help): m	## 输入 m 后，就会看到底下这些命令介绍
	Command action
	   a   toggle a bootable flag
	   b   edit bsd disklabel
	   c   toggle the dos compatibility flag
	   d   delete a partition	## 删除一个partition 
	   l   list known partition types
	   m   print this menu
	   n   add a new partition	## 新增一个partition 
	   o   create a new empty DOS partition table
	   p   print the partition table	## 在屏幕上显示分割表
	   q   quit without saving changes	## 不储存离开fdisk程序 
	   s   create a new empty Sun disklabel
	   t   change a partition's system id
	   u   change display/entry units
	   v   verify the partition table	##	将刚刚的动作写入分割表 
	   w   write table to disk and exit
	   x   extra functionality (experts only)
	
	```
分割好磁盘，按`w`保存，若出现“The new table will be used at the next reboot. ”等信息，可以执行命令`partprobe`及时生效.(强制讥核心重新捉一次 partition table)
2. 磁盘分割好了，接下来当然是格式化啰！
* mkfs
	`# mkfs [-t 文件系统格式] [-c] 装置文件名`
	> -t  ：可以接文件系统格式，例如 ext3, ext2, vfat 等(系统有支持才会生效)
	> -c :检查磁盘

	执行命令，将磁盘格式化为`ext4`的格式：
	`# mkfs -t ext4 -c /dev/xvdb1`
3. 创建挂载目录
	`# mkdir /data`
> 挂载须知
> * 单一文件系统不应该被重复挂载在不同的挂载点(目录)中； 
> * 单一目录不应该重复挂载多个文件系统； 
> * 要作为挂载点的目录，理论上应该都是空目录才是。
> 尤其是上述的后两点！如果你要用来挂载的目录里面不是空的，挂载了文件系统后，原目录下
的东西就会暂时消失。 举个例子来说，假设你的 `/home` 原本不根目录 (`/`) 在同一个文件系统中，底下原本就有 `/home/test`目录。然后你想要加入新的硬盘，直接挂载 `/home`底下，当你挂载上新的分割槽时，则 `/home` 目录显示的是新分割槽内的资料，至于原先的 `test`这个目录就会暂时 被隐藏掉了！注意喔！并不是被覆盖掉， 而是暂时的隐藏了起来，等到新分割槽被卸除后，则 `/home`原本的内容就会再次跑出来啦！ 
4. 挂载分区
	`# mount /dev/xvdb1 /data/`
5. 开机自动挂载
	`echo "/dev/xvdb1 /data/ ext4 defaults 0 0" >> /etc/fstab`
6. 测试
	```
	umount /data/
	mount -a /data
	df -h
	```

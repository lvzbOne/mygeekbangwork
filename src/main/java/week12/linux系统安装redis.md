# linux系统安装redis实操

## 一、下载Redis 安装程序包

> 到[redis官网](https://redis.io/download/) 或 [redis中文官网](http://www.redis.cn/) 查找当前redis的稳定版本获取链接用命令下载
> 为方便下载的包都放在 `/home/common/install-package` 目录下,解压后安装的位置放在`/opt/middleware`下

## 二、升级gcc

> Redis是C语言开发，安装Redis需要先将Redis的源码进行编译，编译依赖gcc环境。
> CentOS7安装有默认GCC环境，默认4.8.5版本！编译 redis-6.x，要求 C5.3以上 编译器，否则会遇到大量的错误。
> 主要原因是从 redis-6.x 开始的多线程代码依赖C标准库中的新增类型 _Atomic 。
> 但是注意 gcc 从 4.9 版本才开始正式和完整地支持 stdatomic（gcc-4.8.5 部分支持）。centos7默认的 gcc 版本为：4.8.5 < 5.3 无法编译。
>  [**GCC_升_级_参_考_文_章**](https://blog.csdn.net/LvQiFen/article/details/124984283?spm=1001.2014.3001.5502)

## 三、Redis 安装

### （一）下载启动redis

```shell
[root@VM-16-13-centos install-package]# wget -P /home/common/install-package/ http://download.redis.io/releases/redis-6.0.6.tar.gz`
[root@VM-16-13-centos install-package]# tar -xvf redis-6.0.6.tar.gz -C /opt/middleware
[root@VM-16-13-centos install-package]# cd /opt/middleware/redis-6.0.6
[root@VM-16-13-centos redis-6.0.6]# make
# 安装到指定目录下 /opt/middleware/redis/redis-master
[root@VM-16-13-centos redis-6.0.6]# make install PREFIX=/opt/middleware/redis/redis-master
[root@VM-16-13-centos redis-6.0.6]# cd /opt/middleware/redis/redis-master/bin
[root@VM-16-13-centos bin]# ll
total 40796
-rwxr-xr-x 1 root root  5406432 May 26 15:14 redis-benchmark
-rwxr-xr-x 1 root root 10219432 May 26 15:14 redis-check-aof
-rwxr-xr-x 1 root root 10219432 May 26 15:14 redis-check-rdb
-rwxr-xr-x 1 root root  5709480 May 26 15:14 redis-cli
lrwxrwxrwx 1 root root       12 May 26 15:14 redis-sentinel -> redis-server
-rwxr-xr-x 1 root root 10219432 May 26 15:14 redis-server

[root@VM-16-13-centos bin]# mkdir conf
# 复制 redis.conf 到 conf 目录、
[root@VM-16-13-centos bin]# cp /opt/middleware/redis-6.0.6/redis.conf /opt/middleware/redis/redis-master/bin/conf/
# 启动reidis服务：并指定使用的配置文件
[root@VM-16-13-centos bin]# ./redis-server conf/redis.conf 
13690:C 26 May 2022 15:29:57.674 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
13690:C 26 May 2022 15:29:57.674 # Redis version=6.0.6, bits=64, commit=00000000, modified=0, pid=13690, just started
13690:C 26 May 2022 15:29:57.674 # Configuration loaded
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 6.0.6 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in standalone mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6379
 |    `-._   `._    /     _.-'    |     PID: 13690
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

13690:M 26 May 2022 15:29:57.676 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
13690:M 26 May 2022 15:29:57.676 # Server initialized
13690:M 26 May 2022 15:29:57.676 # WARNING overcommit_memory is set to 0! Background save may fail under low memory condition. To fix this issue add 'vm.overcommit_memory = 1' to /etc/sysctl.conf and then reboot or run the command 'sysctl vm.overcommit_memory=1' for this to take effect.
13690:M 26 May 2022 15:29:57.676 # WARNING you have Transparent Huge Pages (THP) support enabled in your kernel. This will create latency and memory usage issues with Redis. To fix this issue run the command 'echo never > /sys/kernel/mm/transparent_hugepage/enabled' as root, and add it to your /etc/rc.local in order to retain the setting after a reboot. Redis must be restarted after THP is disabled.
13690:M 26 May 2022 15:29:57.676 * Ready to accept connections

```

### （二）连接redis

```shell
[root@VM-16-13-centos bin]# ./redis-cli -h 127.0.0.1 -p 6379
127.0.0.1:6379> 
127.0.0.1:6379> 
```

### （三）设置后台进程（可不设）

> 进入到`redis.conf`文件 修改 `daemonize yes` 启动时就是后台进程，当前窗口也能执行其它操作了，否则当前窗口将被占用

vim 常用命令

- vim 文件名
- 字符匹配：`/daemonize`
- n 向上查找，N向下查找
- 开启编辑：i
- 保存退出 按esc :wq

### （四）查看进程服务

```shell
[root@VM-16-13-centos bin]# ps -ef | grep redis
root     18648     1  0 15:56 ?        00:00:00 ./redis-server 127.0.0.1:6379
root     18865 15167  0 15:57 pts/3    00:00:00 grep --color=auto redis
```

### （五）redis 关闭
```shell
[root@VM-16-13-centos bin]# ./redis-cli -h 127.0.0.1 -p 6379 127.0.0.1:6379>
127.0.0.1:6379>shutdown
```

### 或者
[ps -ef|grep详解](https://www.cnblogs.com/freinds/p/8074651.html)
```shell
[root@VM-16-13-centos datadir]# ps -ef | grep redis
root     13002 27829  0 21:15 pts/5    00:00:00 grep --color=auto redis
root     26930 15167  0 16:38 pts/3    00:00:19 ./redis-server 127.0.0.1:6380
root     27309 24387  0 16:40 pts/1    00:00:00 ./redis-cli -p 6380
root     28084 12473  0 16:44 pts/2    00:00:18 ./redis-server 127.0.0.1:6379
root     28163 24326  0 16:44 pts/0    00:00:00 ./redis-cli -p 6379
root     32670 30372  0 17:08 pts/6    00:00:16 ./redis-server 127.0.0.1:6381
[root@VM-16-13-centos bin]# kill -9 26930
```

### redis 卸载

```shell
# 删除安装目录文件即可
[root@VM-16-13-centos bin]# rm -rf  XXX
```

## 参考资料

- [Linux系统发行版ContOS7演示安装Redis](https://www.cnblogs.com/xsge/p/13841875.html)
- [Linux 系统目录结构](https://www.runoob.com/linux/linux-system-contents.html)
- [wget命令详解](https://www.jianshu.com/p/2e2ba8ecc22a)
- [Linux tar打包命令详解](http://c.biancheng.net/view/3976.html)
- [Linux 命令详解（三）./configure、make、make install 命令](https://www.cnblogs.com/tinywan/p/7230039.html)
- [CentOS 镜像](https://developer.aliyun.com/mirror/?spm=a2c6h.25603864.0.0.77b74f7cfkc8ZT)
# redis主从+哨兵高可用实操

## 前言

实操前置准备说明 [linux 下载安装Redis参考博客](https://blog.csdn.net/LvQiFen/article/details/124992803?spm=1001.2014.3001.5502)

- 在redis安装的bin目录下 新建 `conf`目录 本示例位置 `/opt/middleware/redis/redis-master/bin/conf`
- 在conf目录下再复制redis.conf2份分别命名为 `redis6380.conf`，`redis6381.conf` 并修改里面的`port`、`pidfile`、`dir`的参数值
- 在`/opt/middleware/redis/redis-master/datadir` 目录下新建三个目录 `data6379`,`data6380`,`data6381`

- 主redis
    - 使用 `redis.conf`
    - `bind 127.0.0.1`
    - `port 6379`
    - `pidfile /var/run/redis_6379.pid`
    - `dir "/opt/middleware/redis/redis-master/datadir/data6379"`

- 从slave1
    - 使用 `redis6380.conf`
    - `bind 127.0.0.1`
    - `port 6380`
    - `pidfile /var/run/redis_6380.pid`
    - `dir "/opt/middleware/redis/redis-master/datadir/data6380"`

- 从slave2 端口 6381
    - 使用 `redis6381.conf`
    - `bind 127.0.0.1`
    - `port 6381`
    - `pidfile /var/run/redis_6381.pid`
    - `dir "/opt/middleware/redis/redis-master/datadir/data6381"`

## 实操演示

> 主要演示：
> - master->slave1(slave1作为slave2的主节点)->slave2
> - 设置主从后 master 新增key value,slave1,slave2自动同步key。
> - 设置主从后，master 可写可读，从节点只读。

### （一）redis主从

> redis主从的作用和mysql的主从复制极其类似。redis主从的特点是：**从节点只读、异步复制**。设置主从的命令如：`SLAVEOF 127.0.0.1 6379`
> 模式一般有2中如下图，这里我们设置第二种的主从模式。

![在这里插入图片描述](https://img-blog.csdnimg.cn/2aea204968094916b5891f6647c4c549.png)

参考命令

- slaveof ip port
- info

开启三个窗口分别启动这三个redis服务端

```shell
[root@VM-16-13-centos bin]# ./redis-server conf/redis.conf
````

```shell
[root@VM-16-13-centos bin]# ./redis-server conf/redis6380.conf
````

```shell
[root@VM-16-13-centos bin]# ./redis-server conf/redis6381.conf
````

再开启三个窗口分别连接这3个服务

```shell
[root@VM-16-13-centos bin]# ./redis-cli -h 127.0.0.1 -p 6379
````

```shell
[root@VM-16-13-centos bin]# ./redis-cli -h 127.0.0.1 -p 6380
````

```shell
[root@VM-16-13-centos bin]# ./redis-cli -h 127.0.0.1 -p 6381
````

> 3个客户端分别写入一个名为 `port` 的key 从这里开始只演示端口`6379`的，特殊情况再给出另外2个说明。
> 端口为`6380`，`6381`的客户端 也分别设置key port 值给对应的本身端口号，至于为什么这么设置后面会说明
> 分别用info 命令查看redis服务的信息时 此时3个都是 role:master，也就是每个都是独立的master

```shell
[root@VM-16-13-centos bin]# ./redis-cli -h 127.0.0.1 -p 6379
127.0.0.1:6379> keys *
(empty array)
127.0.0.1:6379> 
127.0.0.1:6379> 
127.0.0.1:6379> 
127.0.0.1:6379> set port 6379
OK
127.0.0.1:6379> 
127.0.0.1:6379> get port
"6379"
127.0.0.1:6379> info
# 此处省略很多信息....
# Replication
role:master
connected_slaves:0
master_replid:c75107919d2de0fc1db4db93022fc6e5b978c6ed
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0

# 此处省略很多信息....
```

> 把6380的加入到6379作为从服务后，再获取key port 会发现，之前设置的port值6380 变成了 6379和 主服务的一模一样,
> 并且想尝试给添加新key为 ip时会报错提示只读复制，此时再查看info 可以看到 role:slave 以及master的host,port等信息，说明成功加入到master的从节点并且自动同步了master的key内容
>

```shell
127.0.0.1:6380> slaveof 127.0.0.1 6379
OK
127.0.0.1:6380> 
127.0.0.1:6380> get port 
"6379"
127.0.0.1:6380> 
127.0.0.1:6380> 
127.0.0.1:6380> set ip 100.100.100.100
(error) READONLY You can\'t write against a read only replica.
127.0.0.1:6380> 
127.0.0.1:6380> info
# 此处省略很多信息...
# Replication
role:slave
master_host:127.0.0.1
master_port:6379
master_link_status:up
master_last_io_seconds_ago:5
master_sync_in_progress:0
slave_repl_offset:266
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:ef1a64acd2be773dae833b75ae77b0392ee88e68
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:266
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:266
# 此处省略很多信息...
```

> 同理把 6381的服务加入到6380的从节点，此时就是前面提到的 master(6379)->slave1(6380)->slave2(6381)这种模式
> 在master(6379)的操作都将会被同步复制到从节点slave1(6380)再传递到slave2(6381)

```shell
127.0.0.1:6381> slaveof 127.0.0.1 6380
OK
127.0.0.1:6381> 
127.0.0.1:6381> get port
"6379"
127.0.0.1:6381> 
127.0.0.1:6381> 
127.0.0.1:6381> info
# 此处省略很多信息...
# Replication
role:slave
master_host:127.0.0.1
master_port:6380
master_link_status:up
master_last_io_seconds_ago:9
master_sync_in_progress:0
slave_repl_offset:1232
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:ef1a64acd2be773dae833b75ae77b0392ee88e68
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:1232
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1177
repl_backlog_histlen:56
# 此处省略很多信息...

```

## 踩坑

- 启动报错

```shell
[root@VM-16-13-centos bin]# ./redis-server conf/redis6381.conf 
30501:C 26 May 2022 16:56:47.554 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
30501:C 26 May 2022 16:56:47.554 # Redis version=6.0.6, bits=64, commit=00000000, modified=0, pid=30501, just started
30501:C 26 May 2022 16:56:47.554 # Configuration loaded
30501:M 26 May 2022 16:56:47.554 # Could not create server TCP listening socket 175.24.235.70:6381: bind: Cannot assign requested address
30501:M 26 May 2022 16:56:47.554 # Configured to not listen anywhere, exiting.
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/ccd28ba7e46a4113a6b19037a91d50de.png)

- 解决：bind ip 的问题

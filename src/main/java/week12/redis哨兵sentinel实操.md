# redis哨兵sentinel实操

> 复制一份redis安装包里的`sentinel.conf` 命名为 `sentinel26380.conf`
> 分别修改里面的内容为如下配置 注意 dir的路径是我自己建的可更具自身情况选择映射
> 

```shell
[root@VM-16-13-centos conf]# ll
total 384
-rw-r--r-- 1 root root 83498 May 27 17:42 redis6380.conf
-rw-r--r-- 1 root root 83443 May 26 16:44 redis.conf
-rw-r--r-- 1 root root 11085 May 27 17:42 sentinel26380.conf
-rw-r--r-- 1 root root 11133 May 27 17:42 sentinel.conf
-rw-r--r-- 1 root root 10743 May 27 17:18 sentinel.conf.bak
```


| 配置项 | 参数类型 | 说明 | |--|--|--| | dir | 文件目录 | 哨兵进程服务的文件存放目录，默认为 /tmp。 | | port | 端口号 | 启动哨兵的进程端口号，默认为 26379。 | |
sentinel down-after-milliseconds | <服务名称><毫秒数(整数)>     | 在指定的毫秒数内，若主节点没有应答哨兵的 PING 命令，此时哨兵认为服务器主观下线，默认时间为 30 秒。 | |
sentinel parallel-syncs | <服务名称><服务器数(整数)>     | 指定可以有多少个 Redis 服务同步新的主机，一般而言，这个数字越小同步时间越长，而越大，则对网络资源要求就越高。 | | sentinel
failover-timeout | <服务名称><毫秒数(整数)>     | 指定故障转移允许的毫秒数，若超过这个时间，就认为故障转移执行失败，默认为 3 分钟。 | | sentinel notification-script | <
服务名称><脚本路径>     | 脚本通知，配置当某一事件发生时所需要执行的脚本，可以通过脚本来通知管理员，例如当系统运行不正常时发邮件通知相关人员。 | | sentinel auth-pass <
master-name> <password>     | <服务器名称><密码>     | 若主服务器设置了密码，则哨兵必须也配置密码，否则哨兵无法对主从服务器进行监控。该密码与主服务器密码相同。 |

| 配置项 | 参数类型 | 说明 |

## sentinel.conf配置

- sentinel.conf
    - port 26379
    - dir "/opt/middleware/redis/redis-master/sentineldatadir/data26379"
    - sentinel monitor mymaster 127.0.0.1 6379 2
    - sentinel down-after-milliseconds mymaster 10000
    - sentinel failover-timeout mymaster 180000
    - sentinel parallel-syncs mymaster 1

- sentinel26380.conf
    - port 26380
    - dir "/opt/middleware/redis/redis-master/sentineldatadir/data26380"
    - sentinel monitor mymaster 127.0.0.1 6379 2
    - sentinel down-after-milliseconds mymaster 60000
    - sentinel failover-timeout mymaster 180000
    - sentinel parallel-syncs mymaster 1

## 实操演示结果总结

- 新开2个窗口启动redis服务 master，slave1。
- 再新开2个窗口连接master，slave1 并设置slave1为master的从结点
- 再新开2个窗口启动sentinel,sentinel26380 监听master 
- 此时在master服务窗口 ctrl+c 退出服务 slave1服务窗口会在10秒后变成master
- info 命令查看slave1的 # Replication 会发现已经变成 role:master，也可以set key了
- 原来的master服务再启动，会发现自动变成了 slave1的从节点

## 参考资料

-[Redis 的 Sentinel 文档](http://www.redis.cn/topics/sentinel.html)
-[Redis集群：Sentinel哨兵模式（详细图解）](http://c.biancheng.net/redis/sentinel-model.html)
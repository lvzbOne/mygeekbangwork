# 第十二周作业概览

## 1.（必做）配置 redis 的主从复制，sentinel 高可用，Cluster 集群。

### 作业完成情况说明
原本为了快速学习打算window系统里安装redis再实操主从、`sentinel`高可用的。结果用命令`slaveof`的方式主redis会直接一直阻塞，可能Windows的redis有些问题。
最终放弃只能在腾讯云上一步步搭建环境来完成实验。
- [gcc升级整理](https://github.com/lvzbOne/mygeekbangwork/blob/master/src/main/java/week12/gcc%E5%8D%87%E7%BA%A7.md)
- [linux系统安装redis](https://github.com/lvzbOne/mygeekbangwork/blob/master/src/main/java/week12/linux%E7%B3%BB%E7%BB%9F%E5%AE%89%E8%A3%85redis.md)
- [redis主从+哨兵配置实操](https://github.com/lvzbOne/mygeekbangwork/blob/master/src/main/java/week12/redis%E4%B8%BB%E4%BB%8E%E5%93%A8%E5%85%B5%E5%AE%9E%E6%93%8D.md)

## 2.（选做）练习示例代码里下列类中的作业题:`08cache/redis/src/main/java/io/kimmking/cache/RedisApplication.java`

## 3.（选做☆）练习 redission 的各种功能。

## 4.（选做☆☆）练习 hazelcast 的各种功能。

## 5.（选做☆☆☆）搭建 hazelcast 3 节点集群，写入 100 万数据到一个 map，模拟和演 示高可用。

## 第十二周总结
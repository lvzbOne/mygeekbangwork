# Redis主从+哨兵配置实操

## 前言
>redis 的主从异步复制和mysql的主从异步复制的机制极其类似，这里就记录下。主要是熟悉如何配置和快速学习，就走个捷径在window环境里做
> 避免在虚拟机里的Linux系统因环境问题造成困扰，后续有时间linux系统再部署重新记录一遍。

- Windows环境
- 1主（master）2从(slave1,slave2)

## 主从配置

> redis设置主从很方便,只要配置好`redis.conf`文件 然后在从redis执行下 `slaveof ip port`
> 即可添加到主服务器(ip指代主redis所在的服务ip地址，port为主redis服务的端口)
- windows版本的redis配置文件叫 `redis.windows.conf` 文件 
- master设置: 
  - bind 127.0.0.1 
  - port 6479 (默认值port是6379)
  - dir D:\redis\redis-master\logs  (该文件价是我新建的)
  - slaveof  (没用到)
  - pidfile (linux系统下才有效window系统下无效)  

###

## 哨兵配置

###

## 参考资料
# 第七周作业概览
## 必做作业二
> 2.（必做）按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率
>
### 完成情况说明
- 插入100W条耗时如下图 预编译批量插入的情况下 104962ms,非预编译批量插入耗时：124000ms,单条插入太久了。测试代码地址: [100W数据批量插入测试]()
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/7580d197a39747b883d74ae3253921fc.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

TODO:待完成
## 选做作业八
> 8.（选做）配置一遍异步复制，半同步复制、组复制
> 
### 完成情况说明
- 完成配置基于windows系统的mysql8主从复制的异步复制 [windows下mysql8主从异步配置](./windows下mysql8主从异步配置.md)。
- 半同步复制、组复制后面有时间再完善。

## 必做作业九
> 9.（必做）读写分离 - 动态切换数据源版本 1.0
>
### 完成情况说明
- 通过继承 `Spring` 的`AbstractRoutingDataSource` 完成了实现动态路由机制。设置了1主2从，在自定义切面里跟具切面注解进行自动切换，主服务用于写操作，读时采用轮询机制。
代码地址 [读写分离 - 动态切换数据源版本 1.0]()。   [单元测试类代码地址]()
- 具体实操文档笔记和踩坑记录后续整理补齐。[2022-04-17,2022-06-01]  
- 下面放几张成果结果图

## 必做作业十
> 10.（必做）读写分离 - 数据库框架版本 2.0
>
### 完成情况说明
- 完成通过基于作业九进行升级，使用 ShardingSphere 框架来控制读写分离2.0版本，发现基于spring动态路由的方式同时又用ShardingSphere有冲突，所以单独分开一个模块放代码。代码地址:[读写分离 - 数据库框架版本 2.0](), 尝试过几次用ShardingSphere5.0版本进行控制读写分离但是失败了，本次使用的是4.1版本的，后续再升级到5.0
- ShardingSphere 控制读写分离确实方便，只要在配置文件里配置下数据源和路由的算法规则即可实现，太方便了！  
- 运行结果示例：写master,读取是随机slave1,slave2
- ![写master](https://img-blog.csdnimg.cn/b1ead446ea0841ffa8177a60d415a6c4.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)
- ![读随机到slave1](https://img-blog.csdnimg.cn/9e01ef809c7a42b1b5cb9003124110ca.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)
- ![读随机到slave2](https://img-blog.csdnimg.cn/0c6967e974204d5e8758668fd25d4211.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

## 选做作业十一
> 11.（选做）读写分离 - 数据库中间件版本 3.0
> 
TODO:待完成


## 第七周总结：
- 本周内容收获颇多，尤其是数据库的主从复制、读写分离，开启了一个新视野
- 本周内容的基础例如数据库事务、锁、索引的结构机制等基础内容因为作业耗时的，原因深入涉及较浅，短板还是存在...
- 主从复制的配置是在Windows上，后续要改进到linux或docker容器内。
- 本周作业到处碰壁踩坑需得过几天在进行整理记录了...


## 参考资料
- [java核心技术卷II](https://blog.csdn.net/weixin_45317595/article/details/107743620)
- [阿里巴巴开发手册2020年泰山版](./question_6/阿里巴巴开发手册2020年泰山版.pdf)
# 第八周作业概览

## 必做作业2

> 2.（必做）设计对前面的订单表数据进行水平分库分表，拆分 2 个库，每个库 16 张表。并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。
>

### 作业完成情况说明

- [作业代码地址](https://github.com/lvzbOne/mygeekbangwork/tree/master/homework/shardingsphere_proxy/src)
- [测试代码地址](https://github.com/lvzbOne/mygeekbangwork/blob/master/homework/shardingsphere_proxy/src/test/java/com/example/shardingsphere_proxy/service/impl/OrderServiceImplTest.java)  
- [sql文件](https://github.com/lvzbOne/mygeekbangwork/tree/master/homework/shardingsphere_proxy/src/main/resources/sql)  
- [操作演示笔记](./windows环境下使用ShardingSphere-Proxy设置分库分表初体验.md)

## 必做作业6

> 6.（必做）基于 hmily TCC 或 ShardingSphere 的 Atomikos XA 实现一个简单的分布式事务应用 demo（二选一），提交到 Github。
>

### 作业完成情况说明
网上各种查找资料，包括官网，发现这块资料还是比较少的，最终查阅了2偏文章和官网的信息结合起来完成了
- [作业代码地址](https://github.com/lvzbOne/mygeekbangwork/tree/master/homework/shardingsphere_xa)
- [测试代码地址](https://github.com/lvzbOne/mygeekbangwork/blob/master/homework/shardingsphere_xa/src/test/java/com/example/shardingsphere_xa/service/impl/OrderServiceImplTest.java)
## 第八周总结
- 有关X-A这块的内容是第一次接触，分库分表也是第一次，毕竟这总对数据量规模极其庞大的系统和项目和面临的问题，不是我这个刚入行1年左右的小年轻能直接接触的，感觉有点高级了，勉强跟下来开阔了知识面，真实收获和吸收的有限
- 作业的题目网上能参考的资料比较少，几天下来，发现还是官网的最全和完善，只是分布的散需要自己摸索，关于柔性事务等还没时间涉及
- 膜拜萧大佬

## 参考资料
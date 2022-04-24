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
- [Apache ShardingSphere ](https://shardingsphere.apache.org/index_zh.html)
- [ShardingSphere-Proxy 分库分表 简单示例](https://blog.csdn.net/github_35735591/article/details/110726978?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522165053479316782248525421%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fblog.%2522%257D&request_id=165053479316782248525421&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~blog~first_rank_ecpm_v1~times_rank-10-110726978.nonecase&utm_term=sharding&spm=1018.2226.3001.4450)
- [使用 ShardingSphereDataSource](https://shardingsphere.apache.org/document/5.0.0-alpha/cn/user-manual/shardingsphere-jdbc/usage/sharding/yaml/)
- [分片配置ShardingSphere官网参考](https://shardingsphere.apache.org/document/5.0.0-alpha/cn/user-manual/shardingsphere-jdbc/configuration/yaml/sharding/)
- [ShardingSphere的分布式事务](https://www.cnblogs.com/dalianpai/p/14001823.html)
- [ShardingSphere RAW JDBC 分布式事务 Atomikos XA 代码示例](https://xie.infoq.cn/article/75196b1dfdc0c71b8d66391b2)
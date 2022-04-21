# 第六周作业概览

## 选做作业一、二、

> 1.（选做）尝试使用 Lambda/Stream/Guava 优化之前作业的代码。
> 2.（选做）尝试使用 Lambda/Stream/Guava 优化工作中编码的代码。
>

- [x] 由于工作中多少已经用大量到了lambda和stream，这里借此机会只动手写了些示例用来进一步熟悉 Lambda/Stream, Guava 还未使用到后面有时间再熟悉

## 必做作业六

> 6.（必做）基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交 DDL 的 SQL 文件到 Github（后面 2 周的作业依然要是用到这个表结构）。

### 作业完成说明

设计了5张表，建表原则基本参考阿里巴巴开发手册来。设计时先分析实体之间的对应关系按第三范式把基本字段设计好，然后进一步更具需要添加冗余字段

- user  [用户表](./question_6/user.sql)
- shop  [店铺表](./question_6/shop.sql)
- goods [商品表](./question_6/goods.sql)
- shop_goods_repertory  [店铺商品库存表](./question_6/shop_goods_repertory.sql)
- orders [订单表](./question_6/orders.sql)

## 参考资料

- [java核心技术卷II](https://blog.csdn.net/weixin_45317595/article/details/107743620)
- [阿里巴巴开发手册2020年泰山版](./question_6/阿里巴巴开发手册2020年泰山版.pdf)
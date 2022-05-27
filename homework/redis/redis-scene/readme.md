# Redis场景

- 排行榜用sortSet
- 全局id用incr
- 去重bitMap
- 点击量访问量初略用HyperLogLog

## 参考资料

- [Bitmap算法 整合版](https://mp.weixin.qq.com/s/xxauNrJY9HlVNvLrL5j2hg)
- [redis：基数计数HyperLogLog](https://blog.csdn.net/zhizhengguan/article/details/120703798)
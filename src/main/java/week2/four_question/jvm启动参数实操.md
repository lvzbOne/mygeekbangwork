# 实操

## 一、使用 `GCLogAnalysis.java` 自己演练一遍 串行/并行/CMS/G1 的案例

> 前言：
>
>  注意事项：`GCLogAnalysis.java`去掉包名后，先 `javac  GCLogAnalysis.java` 进行编译后，再用java 命令添加启动参数进行分析

### （一）串行GC 分析

> 1. 串行GC是 Parallel Scavenge + Serial Old

```java
 // -XX:+PrintGCDetails 打印GC详细日志，未设置堆的大小，此时堆大小用的默认最大是4G(物理内存16G)，堆的最小值是256M（物理内存的1/64）
 // 当物理内存大于1G 时一般堆的默认最大内存是物理内存的1/4，小于1G时是 1/2） 
java -XX:+PrintGCDetails GCLogAnalysis 
```

![image-20220311170814793](E:\学习\01_学习笔记\笔记图片\image-20220311170814793.png)

#### 分析首条GC日志：

执行:` java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseSerialGC -Xms512m -Xmx512m   GCLogAnalysis`

```java
2022-03-12T23:06:04.455+0800: [GC (Allocation Failure) 2022-03-12T23:06:04.455+0800: [DefNew: 139776K->17472K(157248K), 0.0216249 secs] 139776K->47520K(506816K), 0.0220683 secs] [Times: user=0.00 sys=0.01, real=0.02 secs]
```

```java
GC (Allocation Failure)  // 产生GC的原因是 Allocation Failure（内存申请分配失败）
DefNew: 			     // 产生GC的位置是年轻代,使用的单线程、标记-复制、STW 垃圾收集器 DefNew。

139776K->17472K(157248K) // yang区GC前占用容量是 139776K GC后 占用容量 17472K 说明GC后释放内存了
						 // (157248K) yang区的总容量大小是 157248K
    
139776K->47520K(506816K) // GC前堆内存的占用量 139776K，GC后堆内存的占用量 47520K，说明释放内存了
                         // (506816K) 堆内存的总容量
    
0.0216249 secs           // GC 所占用的时间，单位是秒 
    
[Times:user=0.00         // user表示GC线程所消耗的总CPU时间 0.00秒,是个四舍五入值
sys=0.01				 // sys表示操作系统调用和等待事件所消耗的时间
real=0.02                // real表示应用程序实际暂停的时间，Serial GC中，real约等于 user+sys。
 						 // 其实它和是上面0.0216249 secs保留2位小数的四舍五入时间的值。 
						 
```

```java
// 首条GC 日志分析可以得出以西内容：
1. 首次GC发生时，yang区内存的使用量和堆内存的使用量相等=139776K，说明java进程启动时此时old区此时是空的 
2. 首次GC发生后，yang区从 139776K->17472K 释放了 120M左右内存，但是堆内存的使用量从 139776K->47520K释放了 92M左右内存说明，部分yang区部分数据晋升到了old区，就是差值的 30M左右这部分。
3. 关注点：本次yangGC暂停时间：21毫秒
4. 关注点：GC后yang区内存使用率为 17472K/157248K=11% 
5. 关注点：GC之后old区容量： 506816K-157248K=349568K    
6. 关注点：GC前堆内存使用率： 139776K/506816K=27.5%, GC后堆内存使用率为:47520K/506816K=9.3%    
```

#### 分析Full GC日志：

```java
2022-03-12T23:06:04.838+0800: [GC (Allocation Failure) 2022-03-12T23:06:04.839+0800: [DefNew: 157245K->157245K(157248K), 0.0002413 secs]2022-03-12T23:06:04.839+0800: [Tenured: 334371K->288879K(349568K), 0.0411367 secs] 491617K->288879K(506816K), [Metaspace: 2704K->2704K(1056768K)], 0.0423803 secs] [Times: user=0.03 sys=0.00, real=0.04 secs]
```

```java
// Full GC 时 yangGC 和old GC 都会触发
1. GC (Allocation Failure)          // 说明这是一次Full GC gc的原因内存分配失败
2. DefNew: 157245K->157245K(157248K) // DefNew年轻代使用的单线程、标记-复制、STW 垃圾收集器
    								 //yang区GC前使用157245K GC后不变，yang区的容量是157248K基本
    								 // 差3K就是完全用满了，这就是本次发生Full GC的原因，
    
3. 0.0002413 secs					 //	yang区GC暂停时间，很快短短的0.24毫秒可认为基本没有处理yang区    
4. Tenured: 						// old区GC，Tenured 表明使用的是单线程的STW垃圾收集器，使用的
								    // 算法为 标记-清除-整理(mark-sweep-compact ) 

5. 334371K->288879K(349568K)    	// old区GC前占用 334371K 堆空间，GC后占用堆内存 288879K old									 // 区的总容量是 349568K
    
6. 491617K->288879K(506816K)   		// GC前堆内存使用量 491617K，GC后堆内存使用量 288879K，GC后堆 									// 内存总容量 506816K
    
7. Metaspace: 2704K->2704K          // 元数据区GC前占用2704K，GC后不变，比较难以释放
8. 0.0423803 secs                   // Full GC暂停时间，使用42毫秒，占用时间很长
9. 其它和首条GC分析类似 
    
10. 关注点：本次yangGC暂停时间： 0.24毫秒
11. 关注点：GC后yang区内存使用率为 157245K/157248K=99.99% 
12. 关注点：GC前old区使用率： 334371K/349568K=95.65%,GC之后old区使用率： 288879K/349568K=82.63%   
13. 关注点：GC前堆内存使用率： 491617K/506816K=97%, GC后堆内存使用率为:288879K/506816K=57%    
    
```

```java
// 串行GC 的Full GC 的特征总结
1. yang区GC暂停时间很短,可认为基本没有处理yang区
2. Metaspace区很难被释放
3. Full GC 触发的原因是old区满了导致old区开始进行GC
4. DefNew年轻代使用的单线程、标记-复制、STW 垃圾收集器
5. Tenured老年代使用的是单线程的STW垃圾收集器，使用的算法为 标记-清除-整理(mark-sweep-compact )     
```



### （二）并行GC[jdk8默认的]分析

> 实操关注点：
>
> 1. **年轻代GC，我们可以关注暂停时间，以及GC后的内存使用率是否正常，但不用特别关注GC前 的使用量，而且只要业务在运行，年轻代的对象分配就少不了，回收量也就不会少。** 
> 2. **Full GC时我们更关注老年代的使用量有没有下降，以及下降了多少。如果FullGC之后内存不怎么 下降，使用率还很高，那就说明系统有问题了。**
>
> 
>
> 预设计命令:
>
> 1. 打印详细GC日志、打印一般GC日志、 加时间戳、 输出指定日志文件、时间戳
> 2. GC 参数设置堆大小分析、yang区、old区、非堆区、堆外内存区
> 3. 串行GC，并行GC，CMS GC，G1GC, ZGC, ShenandoahGC 开启分析
> 4. 调试工具诊断分析，dump出快照，OOM（深入理解JVM课程内的实例手操一边）
> 5. 并行GC是 Parallel Scavenge  + Serial Old 收集器

```java
 // -XX:+PrintGCDetails 打印GC详细日志，未设置堆的大小，此时堆大小用的默认最大是4G(物理内存16G)，堆的最小值是256M（物理内存的1/64）
 // 当物理内存大于1G 时一般堆的默认最大内存是物理内存的1/4，小于1G时是 1/2） 
java -XX:+PrintGCDetails GCLogAnalysis 
// 打印GC详细日志,加上时间戳, 此时堆大小用的默认值256m-4G
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
 // 打印GC详细日志,加上时间戳, 设置最小堆和最大堆内存1g,内存变小GC变的更加频繁
java -XX:+PrintGCDetails  -XX:+PrintGCDateStamps -Xmx1g -Xms1g GCLogAnalysis    
 // 打印GC详细日志,加上时间戳, 设置最小堆和最大堆内存1g,输出gc日志到当前文件夹，名称叫gc.demo.log
 java -Xms1g -Xmx1g -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:gc.demo.log   GCLogAnalysis
     
 // 执行该命令时 yang初始区容量大概是堆的1/3开始自适应变化  gc频率和时间 基本很稳定【好习惯】   
 java -XX:+PrintGCDetails -XX:+PrintGCDateStamps   -Xmx1g -Xms1g   GCLogAnalysis    
 
 // 执行该命令时 yang区容量大概是堆的1/3开始自适应变化，开始会给堆分配512m内存很快就会有一次FullGC，随后,yang和堆的容量都从小往大适应比例1：3（当内存特别大时就不一定是1：3的变化趋势了 Xms512,Xmx4G的情况后期是1:2的趋势），gc时间和频率 小->大->趋于稳定
 java -XX:+PrintGCDetails -XX:+PrintGCDateStamps   -Xmx1g -Xms512m   GCLogAnalysis     
     
 // 执行该命令时 yang区容量开始就基本和堆的容量大小接近1:1（因为设置的yang区的大小是512m,初始时就会给yang区分配接近512m的大小，此时old区还没内容，就会很小，因此堆的总容量和yagn区的容量就接近1:1）,随后堆的容量从小往大适应，yang区大小从大往小适应到稳定，gc次数 低->频繁->稳定；gc时间变化趋势 大->小->稳定。    
 java -XX:+PrintGCDetails -XX:+PrintGCDateStamps   -Xmx1g -Xmn512m   GCLogAnalysis
     
```

![image-20220311170814793](E:\学习\01_学习笔记\笔记图片\image-20220311170814793.png)

#### 分析首条GC日志：

> **年轻代GC，我们可以关注暂停时间，以及GC后的内存使用率是否正常，但不用特别关注GC前 的使用量，而且只要业务在运行，年轻代的对象分配就少不了，回收量也就不会少。** 

执行`java -XX:+PrintGCDetails GCLogAnalysis `:

```java
[GC (Allocation Failure) [PSYoungGen: 65536K->10720K(76288K)] 65536K->21534K(251392K), 0.0038592 secs] [Times: user=0.05 sys=0.11, real=0.00 secs]
```

```java
GC (Allocation Failure)  // 产生GC的原因是 Allocation Failure（内存申请分配失败）
PSYoungGen: 			 // 产生GC的位置是年轻代（yang区）用的GC收集器是Parallel Scavenge
						 // 并行的 标记-复制(mark-copy) ，全线暂停(STW) 垃圾收集器

65536K->10720K(76288K)   // yang区GC前占用容量是 65536K GC后 占用容量 10720K 说明GC后释放内存了
						 // (76288K) yang区的总容量大小是76288K
    
65536K->21534K(251392K)  // GC前堆内存的占用量65536K，GC后堆内存的占用量 21534K，说明释放内存了
                         // (251392K) 堆内存的总容量
    
0.0038592 secs           // 本次GC 所使用的时间，单位是秒 
    
[Times:user=0.05         // user表示GC线程所消耗的总CPU时间 0.05秒
sys=0.11				 // sys表示操作系统调用和等待事件所消耗的时间
real=0.00                // real表示应用程序实际暂停的时间，因为并不是所有的操作过程都能全部并行，所以在
                         // Parallel GC 中， real 约等于 user + system /GC线程数 。
 						 // 其实它和是上面0.0038592 secs保留2位小数的四舍五入时间的值，所以展示成0.00
						 // 分析这个时间，可以发现，如果使用串行GC，可能得暂停160毫秒，但并行GC只暂停了   					   // 3.86毫秒，实际上性能是大幅度提升了。
                         // CPU 或者多核的话，多线程操作会叠加这些 CPU 时间，所以看到 user 或 sys 时 
                         // 间超过 real 时间完全是正常的。 
```

```java
// 首条GC 日志分析可以得出以西内容：
1. 启动时用未显示的指定参数，jvm就会使用一些默认参数，例如堆内存的大小，最大为默认为物理内存的1/4，最小为默认物理内存的1/64 我们发现首次执行时堆的容量是 251392K=250M左右，说明JVM 是会进行动态的扩容最大到接近最大堆内存的容量（根据后面的GC日志可以看出来） 自适应参数是-XX:+UseAdaptiveSizePolicy 默认开启。
2. 首次GC发生时，yang区内存的使用量和堆内存的使用量相等=65536K，说明java进程启动时此时old区此时是空的 
3. 首次GC发生后，yang区从65536K->10720K 释放了500M左右内存，但是堆内存的使用量从 65536K->21534K释放了    400M左右内存说明，部分yang区部分数据晋升到了old区，就是差值的100M左右这部分。
4. 关注点：首次Minor GC 暂停时间是:3.85毫秒，GC 后的yang区内存使用率: 10720/76288=14.1%
```

#### 分析Full GC日志：

> **Full GC时我们更关注老年代的使用量有没有下降，以及下降了多少。如果FullGC之后内存不怎么 下降，使用率还很高，那就说明系统有问题了。**

```java
[Full GC (Ergonomics) [PSYoungGen: 166385K->0K(889344K)] [ParOldGen: 290479K->309829K(508416K)] 456865K->309829K(1397760K), [Metaspace: 2704K->2704K(1056768K)], 0.0385150 secs] [Times: user=0.28 sys=0.03, real=0.04 secs]
```

```java
// Full GC 时 yangGC 和old GC 都会触发
1. Full GC (Ergonomics)    			// 说明这是一次Full GC,表明本次GC清理年轻代和老年代
2. PSYoungGen: 166385K->0K(889344K) // yang区清理年轻代的垃圾收集器是名为 “PSYoungGen” 的STW收集器
    								// 采用 标记-复制(mark-copy) 算法。GC前使用166385K GC 后清
    								//零，yang区的容量是889344k。
    
3. ParOldGen: 						// old区GC，ParOldGen用于清理老年代空间的垃圾收集器类型
									// 算法为 标记-清除-整理(mark-sweep-compact) 

4. 290479K->309829K(508416K)    	// old区GC前占用290479K堆空间，GC释放后占用堆内存309829K，old									// 区的总容量是508416K
5. Metaspace: 2704K->2704K          // 元数据区GC前占用2704K，GC后不变，比较难以释放??????原因    
6. 其它和首条GC分析类似
7. 关注点：本次Full GC 总暂停时间是: 40毫秒 
8. 关注点：old区GC前的使用率：290479K/508416K=57.1%, old区的GC后使用率是:309829K/508416K=61.9% 很    明显本次Full GC yang区域晋级过来的比释放的对象占用内存更大，这种情况就需要关注下。  
    
9. 关注点：在垃圾收集之前和之后堆内存的使用情况，以及可用堆内存的总容量。简单分析可知,GC之前堆内存使用率    为:456865K/1397760K=32.6%,GC之后堆内存为:309829K/1397760K=22.1%,虽然此次GC老年代的使用率升高了但    是堆内存的总使用率还是降低的  
       
```

```java
// 并行GC的 Full GC 的特征总结
1. yang区，old 区同时进行GC，且yang区清零
2. Metaspace区很难被释放
3. yang区的垃圾收集器是名为 "Parallel Scavenge" 的STW收集器 采用 标记-复制(mark-copy) 算法
4. old区使用"ParOldGen"的垃圾收集器类型,采用标记-清除-整理(mark-sweep-compact)算法     
```

#### 堆内存大小对GC的影响

> 从运行情况来看相对来说：堆内存设置越大，GC频率就低，单次GC时间就长；最大堆内存和最小堆内存值设置相对的话，GC频率就相对稳定，反之就有个蓄水的效应，即开始时堆内存容量小首次Full GC 很快就会出现，堆内存的容量会逐渐增大，直到接近最大堆内存的值。此次实操确实感受到 Xms 和 Xmx设置相等是一个 **好习惯**

### （三）CMS GC 分析

### （四）G1GC 分析

## （五）疑惑

```java
1. 采用串行GC时，Full GC yang区并没有减少，为什么堆减少的量竟然比old区减少的量多呢？？？
 
2. yang区清零的原因？
        young区清零，因为他本来就不大，而且基本上存活对象很少，直接全部提升到老年代，减少复杂度。
    
3. Metaspace区很难被释放原因？？？
        meta需要classloader对象释放了才会释放他加载的class这个条件很苛刻
    
4. 自适应参数是？？？？？  
    -XX:+UseAdaptiveSizePolicy是一个开关参数
    
```


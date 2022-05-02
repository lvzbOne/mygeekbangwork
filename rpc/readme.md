# rpc作业

> 前言：
## 一、结构分析
## 二、

## 踩坑
- maven 编译版本问题
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/d049360aee024dd8a7cf9ab5c8cb6fd3.png)
  解决在该子模块的`pom.xml` 添加如下内容：
  ```xml
   <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
  ```
- 循环依赖问题
  ```java
  Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
  2022-04-28 16:11:02.637 ERROR 22220 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   :

  ***************************
  APPLICATION FAILED TO START
  ***************************

  Description:

  The dependencies of some of the beans in the application context form a cycle:

  ┌──->──┐
  |  rpcfxDemoProviderApplication (field com.example.rpcfxcore.server.RpcfxInvoker com.example.rpcfxdemoprovider.RpcfxDemoProviderApplication.invoker)
  └──<-──┘


  Action:

  Relying upon circular references is discouraged and they are prohibited by default. Update your application to remove the dependency cycle between beans. As a last resort, it may be possible to break the cycle automatically by setting spring.main.allow-circular-references to true.

  ```
  解决循环依赖问题，在yaml文件添加如下内容:
  ```yaml
  spring:
    profiles:
      active: true
    main:
      #allow-bean-definition-overriding: true
      allow-circular-references: true
  ```
 
- 忘使用@RestController 报错 加上该注解就正常了
  ![忘记使用@RestController](https://img-blog.csdnimg.cn/794278f2d19648f9bc4540e8bc801002.png)
- 使用成 @Controller 报错 改成 @RestController 就正常了
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/73f17b236c1d45f09dd6023b2d03d09e.png)
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/4725c527e6464118a27e51f19d00ef5b.png)
```java
2022-05-01 22:15:12.935 ERROR 26444 --- [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Circular view path [test]: would dispatch back to the current handler URL [/test] again. Check your ViewResolver setup! (Hint: This may be the result of an unspecified view, due to default view name generation.)] with root cause
```
- com.alibaba.fastjson.JSONException: autoType is not support [解决 添加`autotype`白名单](https://blog.csdn.net/cdyjy_litao/article/details/72458538) 
![在这里插入图片描述](https://img-blog.csdnimg.cn/564f279c5bb249dda51feb93b1e90033.png)
```java
        static {
            ParserConfig.getGlobalInstance().addAccept("com.example");
        }
```

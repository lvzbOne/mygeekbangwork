# 认识 Spring Bean 的生命周期
## 前言

> 一直对spring的bean生命周期好奇，有过几次查阅资料和看spring实战第四版本的书，但是还是没很明白
> 这次决心再来一遍，找到了偏好文【 [一文读懂 Spring Bean 的生命周期](https://blog.csdn.net/riemann_/article/details/118500805) 】跟着走了下，终于释惑了。
## 一、生命周期
> 这里我们说的 Spring Bean 的生命周期主要指的是 singleton bean，对于 prototype 的 bean ，Spring 在创建好交给使用者之后则不会再管理后续的生命周期。

> 我们也来复习下 Spring 中的 bean 的作用域有哪些?@Scope注解可以设置
> - singleton : 唯一 bean 实例，Spring 中的 bean 默认都是单例的。
> - prototype : 每次请求都会创建一个新的 bean 实例。
> - request : 每一次 HTTP 请求都会产生一个新的 bean，该 bean 仅在当前 HTTP request 内有效。
> - session : 每一次 HTTP 请求都会产生一个新的 bean，该 bean 仅在当前 HTTP session 内有效。
> -  global-session： 全局 session 作用域，仅仅在基于 Portlet 的 web 应用中才有意义，Spring5 已经没有了。Portlet 是能够生成语义代码（例如：HTML）片段的小型 Java Web 插件。它们基于 portlet 容器，可以像 servlet 一样处理 HTTP 请求。但是，与 servlet 不同，每个 portlet 都有不同的会话。

> 我们知道对于普通的 Java 对象来说，它们的生命周期就是：
> - 实例化
> - 该对象不再被使用时通过垃圾回收机制进行回收
>
> 而对于 Spring Bean 的生命周期来说：
> - 实例化 Instantiation
> - 属性赋值 Populate
> - 初始化 Initialization
> - 销毁 Destruction
>
> 实例化 -> 属性赋值 -> 初始化 -> 销毁，在这四大的阶段里有很多入口可以进行自行扩展。分容器级别和Bean级别
>

> **容器级别：** 主要是后处理器方法，比如下图的 `InstantiationAwareBeanPostProcessor`(4个default方法)、`BeanPostProcessor` （2个default方法）接口方法。**这些接口的实现类是独立于 Bean 的，并且会注册到 Spring 容器中。在 Spring 容器创建任何 Bean 的时候，这些后处理器都会发生作用。**
>- **实例化可以进行干预，有自定义的`InstantiationAwareBeanPostProcessor`的实例，即可在bean实例化前和后设置属性等进行扩展操作**
>- **初始化阶段可以进行干预，有自定义的 `BeanPostProcessor`的实例，即可在bean初始化前和后进行扩展操作**
>
>

> **Bean 级生命周期方法：** 可以理解为 Bean 类直接实现接口的方法，比如 `BeanNameAware`、`BeanFactoryAware`、`ApplicationContextAware`、`InitializingBean`、`DisposableBean` 等方法，这些方法只对当前 Bean 生效。
>- 初始化阶段之前 bean如果有继承 `BeanNameAware`,`BeanFactoryAware`,`ApplicationContextAware`,`InitializingBean`,`DisposableBean`**可以获取spring的一些资源，如beanName、ApplicationContext 等。
   > 这些方法只对当前 Bean 生效。
> - `InitializingBean` 对应生命周期的初始化阶段，因为 Aware 方法都是执行在初始化方法之前，所以可以在初始化方法中放心大胆的使用 Aware 接口获取的资源，这也是我们自定义扩展 Spring 的常用方式。
> - `DisposableBean` 类似于 `InitializingBean`，对应生命周期的销毁阶段，会调用其 destroy() 方法
>

## 二、常用接口说明
### （一）`BeanNameAware`

该接口只有一个方法 `setBeanName(String name)`，用来获取 bean 的 id 或者 name。

### （二）`BeanFactoryAware`

该接口只有一个方法 `setBeanFactory(BeanFactory beanFactory)`，用来获取当前环境中的 BeanFactory。

### （三）`ApplicationContextAware`

该接口只有一个方法 `setApplicationContext(ApplicationContext applicationContext)`，用来获取当前环境中的 ApplicationContext。

### （四）`InitializingBean`

该接口只有一个方法 `afterPropertiesSet()`，在属性注入完成后调用 在用户指定的 `init-method`之前。

### （五）`DisposableBean`

该接口只有一个方法 `destroy()`，在容器销毁的时候调用，在用户指定的 `destroy-method` 之前调用。

### （六）`BeanPostProcessor`

该接口有两个方法：

- `postProcessBeforeInitialization(Object bean, String beanName)`：在初始化之前调用此方法
- `postProcessAfterInitialization(Object bean, String beanName)`：在初始化之后调用此方法
  通过方法签名我们可以知道，我们可以通过 beanName 来筛选出我们需要进行个性化定制的 bean。

### （七）`InstantiationAwareBeanPostProcessor`

该类是 `BeanPostProcessor` 的子接口，常用的有如下三个方法：

- `postProcessBeforeInstantiation(Class beanClass, String beanName)`：在bean实例化之前调用
- `postProcessProperties(PropertyValues pvs, Object bean, String beanName)`：在bean实例化之后、设置属性前调用
- `postProcessAfterInstantiation(Class beanClass, String beanName)`：在bean实例化之后调用

![简单bean生命周期流程](https://img-blog.csdnimg.cn/ca6ef94ea850427faec19b16ea4c0ed4.png)

![复杂bean生命周期流程](https://img-blog.csdnimg.cn/ca10f1aa040a445799b70865c8803fc8.png)

## 三、示例代码
- `MyBeanFactoryPostProcessor`
```java
package com.example.spring_life_cycle.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/9
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("0. 调用 BeanFactoryPostProcessor.postProcessBeanFactory() 方法");
    }
}
```
- `MyInstantiationAwareBeanPostProcessor`
```java
package com.example.spring_life_cycle.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * TODO:很奇怪，InstantiationAwareBeanPostProcessor 全是 default 方法
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/5/9
 */
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    private static final String USER_BEAN = "userBean";

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (USER_BEAN.equals(beanName)) {
            System.out.println("1. 调用 InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation() 方法");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (USER_BEAN.equals(beanName)) {
            UserBean userBean = (UserBean) bean;
            System.out.println("3. 调用 InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation() 方法");
            System.out.println(userBean);
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (USER_BEAN.equals(beanName)) {
            System.out.println("4. 调用 InstantiationAwareBeanPostProcessor.postProcessProperties() 方法");
        }
        return null;
    }
}
```
- `UserBean`
```java
package com.example.spring_life_cycle.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/9
 */
public class UserBean implements BeanNameAware, ApplicationContextAware, InitializingBean, DisposableBean {
    private int id;
    private String name;

    public UserBean(int id, String name) {
        this.id = id;
        this.name = name;
        System.out.println("2. 调用构造函数");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        System.out.println("5. 属性注入 id");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("5. 属性注入 name[" + name + "]");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("6. 调用 BeanNameAware.setBeanName() 方法s[" + s + "]");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        /// TODO:打开就会报错 Error creating bean with name 'userBean': Requested bean is currently in creation: Is there an unresolvable circular reference?
//        UserBean userBean = (UserBean) applicationContext.getBean("userBean");
//        System.out.println(userBean);
        System.out.println("7. 调用 ApplicationContextAware.setApplicationContext() 方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("9. 调用 InitializingBean.afterPropertiesSet() 方法");
    }

    // @PostConstruct
    public void myInit() {
        System.out.println("10. 调用 init-method 方法");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("12. 调用 DisposableBean.destroy() 方法");
    }


    //@PreDestroy
    public void myDestroy() {
        System.out.println("13. 调用 destroy-method 方法");
    }
    
}
```
- `MyBeanPostProcessor`
```java
package com.example.spring_life_cycle.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/9
 */
public class MyBeanPostProcessor implements BeanPostProcessor {
    private static final String USER_BEAN = "userBean";

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (USER_BEAN.equals(beanName)) {
            System.out.println("8. 调用 BeanPostProcessor.postProcessBeforeInitialization() 方法");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (USER_BEAN.equals(beanName)) {
            System.out.println("11. 调用 BeanPostProcessor.postProcessAfterInitialization() 方法");
        }
        return bean;
    }
}
```
- `LifeCycleConfig`
```java
package com.example.spring_life_cycle.config;

import com.example.spring_life_cycle.bean.MyBeanFactoryPostProcessor;
import com.example.spring_life_cycle.bean.MyBeanPostProcessor;
import com.example.spring_life_cycle.bean.MyInstantiationAwareBeanPostProcessor;
import com.example.spring_life_cycle.bean.UserBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/9
 */
@Configuration
public class LifeCycleConfig {

    @Bean
    public MyInstantiationAwareBeanPostProcessor myInstantiationAwareBeanPostProcessor() {
        return new MyInstantiationAwareBeanPostProcessor();
    }

    /**
     * TODO: initMethod 和 destroyMethod
     * @return
     */
    @Bean(initMethod = "myInit", destroyMethod = "myDestroy")
    public UserBean userBean() {
        UserBean userBean = new UserBean(1, "spring生命周期验证的一个bean");
        //userBean.setId(2);
        //userBean.setName("重新设置的bean");
        return userBean;
    }

    @Bean
    public MyBeanPostProcessor myBeanPostProcessor() {
        return new MyBeanPostProcessor();
    }

    @Bean
    public MyBeanFactoryPostProcessor myBeanFactoryPostProcessor() {
        return new MyBeanFactoryPostProcessor();
    }

}
```
- 测试类
```java
package com.example.spring_life_cycle.bean;

import com.example.spring_life_cycle.config.LifeCycleConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/9
 */
@SpringBootTest
class UserBeanTest {

    @Test
    void beanLifeCycleTest() {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
//        UserBean userBean = (UserBean) context.getBean("userBean");
//        ((AbstractApplicationContext) context).close();
    }
}
```
## 四、验证运行结果
![在这里插入图片描述](https://img-blog.csdnimg.cn/ebc2596028bd4cec9fb88fc7d6640e68.png)


## 报错解决记录
- Error creating bean with name 'userBean': Requested bean is currently in creation: Is there an unresolvable circular reference?【创建名为“userBean”的 bean 时出错：当前正在创建请求的 bean：是否存在无法解析的循环引用？】
- 解决：userBean正在创建还没初始化完，时就其它代码尝试获取使用就会报这个错，排除代码发现是实现`ApplicationContextAware`接口时在其`setApplicationContext` 中获取userBean导致，XXXAware的接口方法是在bean初始化之前执行的，注释掉获取userBean的代码即可正常运行。
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/561e7288de7e41de9226ecec1a046900.png)


## 参考资料
- [一文读懂 Spring Bean 的生命周期](https://blog.csdn.net/riemann_/article/details/118500805)
- [深究Spring中Bean的生命周期](https://www.cnblogs.com/javazhiyin/p/10905294.html)
- [spring注解为bean指定InitMethod和DestroyMethod](https://blog.csdn.net/qq_36722039/article/details/81811584)
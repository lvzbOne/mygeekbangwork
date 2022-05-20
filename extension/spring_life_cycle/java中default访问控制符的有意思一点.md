# java中 `default`访问控制符的有意思一点

我们知道，java中的四种访问控制符控制范围

- public (公共访问权限): 在该类中、子类、同一个包的其它类、其它包中的其它类都能访问
- protected (子类访问权限): 在该类中、子类（同一个包中和不同包中）、同一个包中的其它类能访问到。
- default (包访问权限): 在该类中、子类（同一个包中）、同一个包中的其它类都能访问。
- private (当前类访问权限): 在该类中能访问。

**有意思的是 java8中`interface` 里可以有`default`方法,接口里的`default`方法,可以被它实现的子类继承得到也可以重写覆盖掉，不论子类是否和该接口在同一个包中，就像是个特例一样。然而类中的`default`
方法依旧遵循上面提到的规则**
，下面我门来具体看看。

## 具体说明

> 示例代码结构图如下所示：
> - 一个`DefaultClass`类含有default方法`setDe()`、`getDe()` 和default属性 `de`
> - 一个 `DefaultInterface`接口含有default方法 `sayHello()`
> - defaults 包下有个`Package`类 继承 `DefaultClass` 实现 `DefaultInterface`
> - test 包下有个`Package`类 继承 `DefaultClass` 实现 `DefaultInterface`

![在这里插入图片描述](https://img-blog.csdnimg.cn/63e042364df645b9b90dca23e26749dc.png)

> 下图中 defaults 包下的`Package`类 继承了同包下的`DefaultClass`和同包下的`DefaultInterface`，由于是同包下，所以能顺理成章的获得父类和父接口的 default的方法和属性

![在这里插入图片描述](https://img-blog.csdnimg.cn/7c0ac2355c1b4b2cab40f67fcc11b5c0.png)

> 下图中 test 包下的Package类 继承了不同包下的`DefaultClass`和`DefaultInterface`，由于是不同包下，
> 所以不能获得父类的default属性和方法，**但是特殊的是能继承获得到父接口的 default的方法`SayHello()` 这就是特殊点 接口的default作用和类的default修饰的细微区别**


![在这里插入图片描述](https://img-blog.csdnimg.cn/a3bbe836253e4fd9b449b3d069a48896.png)


## 示例代码
- DefaultClass
```java
package com.example.spring_life_cycle.defaults;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/10
 */
public class DefaultClass {
    String de;
    protected String name;

    void setDe(String de) {
        this.de = de;
    }

    String getDe() {
        return de;
    }
}
```  
- DefaultInterface
```java
package com.example.spring_life_cycle.defaults;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/10
 */
public interface DefaultInterface {

    /**
     * 默认的普通方法
     */
    default void sayHello() {
        System.out.println("Hello!");
    }
}
```  
- defaults 包下的 Package
```java
package com.example.spring_life_cycle.defaults;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/10
 */
public class Package extends DefaultClass implements DefaultInterface {
    public static void main(String[] args) {
        DefaultClass defaultClass = new DefaultClass();
        defaultClass.setDe("aaaa");
        System.out.println(defaultClass.getDe());

        Package aPackage = new Package();
        aPackage.sayHello();
    }
}
```  
- test 包下的 Package
```java
package com.example.spring_life_cycle.test;

import com.example.spring_life_cycle.defaults.DefaultClass;
import com.example.spring_life_cycle.defaults.DefaultInterface;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/10
 */
public class Package extends DefaultClass implements DefaultInterface {
    public static void main(String[] args) {
        DefaultClass defaultClass = new DefaultClass();
        System.out.println(defaultClass.get);

        Package aPackage = new Package();
        aPackage.sayHello();
    }
}
```  
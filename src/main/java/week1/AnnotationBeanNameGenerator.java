package week1;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.util.Assert;

/**
 * 自定义bean命名为全路径
 *
 * @author 起凤
 */
public class AnnotationBeanNameGenerator implements BeanNameGenerator {
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
//        if (definition instanceof AnnotatedBeanDefinition) {
//            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
//            if (StringUtils.hasText(beanName)) {
//                // Explicit bean name found.
//                return beanName;
//            }
//        }
        String beanClassName = definition.getBeanClassName();
        Assert.state(beanClassName != null, "No bean class name set");
        return beanClassName;
    }

}
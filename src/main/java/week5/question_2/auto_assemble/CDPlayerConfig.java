package week5.question_2.auto_assemble;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：@ComponentScan 作用是扫描带有@Component、@Service、@Controller等注解的类，并为其创建bean。默认是扫描同一包下的类，当然也可以加入参数，指定包名。
 * 如 @ComponentScan(basePackages={"com.zcs"})或者 @ComponentScan(basePackageClasses={CDPlayer.class,DVDPlayer.class})(即这些类所在的包)
 * 还有 @Autowired 指满足自动依赖装配，可以作用在任何方法上面。
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */
@ComponentScan
@Configuration
public class CDPlayerConfig {
}

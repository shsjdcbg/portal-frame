package cn.dyx.protal.frame.annotation;

import java.lang.annotation.*;

/**
 * Description：系统日志注解，用于方法上，记录系统日志
 *
 * @author dyx
 * @date 2019/7/16 17:05
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 用于描述系统日志
     *
     * @return 系统日志描述
     */
    String description() default "";

}

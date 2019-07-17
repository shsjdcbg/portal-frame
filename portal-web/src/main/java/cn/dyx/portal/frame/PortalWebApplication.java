package cn.dyx.portal.frame;

import cn.dyx.protal.frame.config.RedissonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author dyx
 * @date 2019/7/16 14:09
 */
@SpringBootApplication
@ComponentScan(value = "cn.dyx.protal.frame.*",
        /**
         * 排除加载的class
        */
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {RedissonConfig.class}))
public class PortalWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortalWebApplication.class, args);
    }
}

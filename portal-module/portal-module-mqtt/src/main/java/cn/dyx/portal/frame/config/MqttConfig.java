package cn.dyx.portal.frame.config;

import cn.dyx.portal.frame.MqttExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

/**
 * Description：MQTT 配置
 *
 * @author dyx
 * @date 2019/2/15 14:41
 */
@Configuration
@PropertySource("classpath:mqtt.properties")
public class MqttConfig {
    private Logger logger = LoggerFactory.getLogger(MqttConfig.class);

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.broker.login.name}")
    private String brokerLoginName;

    @Value("${mqtt.broker.password}")
    private String brokerPassword;

    @Bean(initMethod = "connect", destroyMethod = "close")
    @Scope("prototype")
    public MqttExecutor mqttExecutor() {
        logger.info(">>>>>>>>>>> mqtt config init.");
        return new MqttExecutor(brokerUrl, brokerLoginName, brokerPassword);
    }
}

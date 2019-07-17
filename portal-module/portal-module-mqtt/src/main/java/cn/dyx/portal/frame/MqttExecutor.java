package cn.dyx.portal.frame;

import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;

/**
 * Description：MQTT执行器
 *
 * @author dyx
 * @date 2019/2/15 14:51
 */
public class MqttExecutor {
    private MqttClient mqttClient;
    private MqttConnectOptions options;
    private String brokerUrl;
    private String brokerLoginName;
    private String brokerPassword;
    private String clientId;

    public MqttExecutor(String brokerUrl, String brokerLoginName, String brokerPassword) {
        this.brokerUrl = brokerUrl;
        this.brokerLoginName = brokerLoginName;
        this.brokerPassword = brokerPassword;
    }

    /**
     * 连接
     */
    public void connect() {
        try {
            String clientId = "intermediary-job-" + UUID.randomUUID();
            if (mqttClient == null) {
                mqttClient = new MqttClient(brokerUrl, clientId);
            }
            options = new MqttConnectOptions();
            options.setUserName(brokerLoginName);
            options.setPassword(brokerPassword.toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(false);
            options.setKeepAliveInterval(30);
            mqttClient.connect(options);
        } catch (MqttException e) {
        }
    }

    /**
     * 连接状态
     *
     * @return
     */
    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    /**
     * 订阅
     *
     * @param topics       主题数组
     * @param mqttCallback Callback 类
     */
    public void subscribe(String[] topics, MqttCallback mqttCallback) {
        try {
            if (!isConnected()) {
                connect();
            }
            if (mqttCallback != null) {
                mqttClient.setCallback(mqttCallback);
            }
            int[] qos = new int[topics.length];
            for (int i = 0; i < qos.length; i++) {
                qos[i] = 2;
            }
            IMqttToken result = mqttClient.subscribeWithResponse(topics, qos);
            String topic = "";
            for (String temp : topics) {
                topic += " " + temp;
            }
            XxlJobLogger.log("已订阅主题：" + topic + " " + result.getResponse());
        } catch (MqttException e) {
            XxlJobLogger.log("已订阅主题失败" + e.getMessage());
        }
    }

    /**
     * 订阅
     *
     * @param topic        主题
     * @param mqttCallback Callback 类
     */
    public void subscribe(String topic, MqttCallback mqttCallback) {
        subscribe(new String[]{topic}, mqttCallback);
    }

    /**
     * 退订
     *
     * @param topics 主题数组
     */
    public void unsubscribe(String[] topics) {
        try {
            mqttClient.unsubscribe(topics);
            String topic = "";
            for (String temp : topics) {
                topic += " " + temp;
            }
            XxlJobLogger.log("已退订主题：" + topic);
        } catch (MqttException e) {
            XxlJobLogger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 退订
     *
     * @param topic 主题
     */
    public void unsubscribe(String topic) {
        unsubscribe(new String[]{topic});
    }

    /**
     * 发布
     *
     * @param topic   主题
     * @param message 消息
     */
    public void publish(String topic, String message) {
        publish(topic, message, 2, false);
    }

    /**
     * 发布
     *
     * @param topic    主题
     * @param message  消息
     * @param qos      服务质量
     * @param retained 是否离线保留消息
     */
    public void publish(String topic, String message, int qos, boolean retained) {
        try {
            if (!isConnected()) {
                connect();
            }
            final MqttTopic mqttTopic = mqttClient.getTopic(topic);
            mqttTopic.publish(message.getBytes(), qos, retained);
            XxlJobLogger.log("Sending Topic：" + topic + "  消息" + message);
        } catch (MqttException e) {
            XxlJobLogger.log("发送消息失败：" + e.getMessage());
        }
    }

    /**
     * 关闭
     */
    public void close() {
        try {
            if (isConnected()) {
                mqttClient.disconnect();
                mqttClient.close();
            }
        } catch (MqttException e) {
            XxlJobLogger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (isConnected()) {
                mqttClient.disconnect();
            }
        } catch (MqttException e) {
            XxlJobLogger.log(e.getMessage());
            e.printStackTrace();
        }
    }
}

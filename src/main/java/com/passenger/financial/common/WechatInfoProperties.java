package com.passenger.financial.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "wechat.info")
@Component
@Data
public class WechatInfoProperties {

    //小程序id
    private String appId;

    //签名类型
    private String signType;
    //登陆地址
    private String loginUrl;
    //小程序后台配置的随机字符串
    private String secret;

    private String tradeType;
}

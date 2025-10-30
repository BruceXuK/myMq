package com.bruce.mq.shared.order.message.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 点对点消息实体类
 * 用于表示只需要一个消费者处理的消息
 *
 * @author BruceXuK
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointToPointMessage {
    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 带参构造函数
     *
     * @param content 消息内容
     * @param type 消息类型
     */
    public PointToPointMessage(String content, String type) {
        this.content = content;
        this.type = type;
        this.sendTime = LocalDateTime.now();
    }
}

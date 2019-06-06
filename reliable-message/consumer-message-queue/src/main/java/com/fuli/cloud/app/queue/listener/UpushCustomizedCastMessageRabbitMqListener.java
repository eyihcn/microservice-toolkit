package com.fuli.cloud.app.queue.listener;

import com.alibaba.fastjson.JSONObject;
import com.fuli.cloud.app.queue.service.biz.UpushMsgQueueBiz;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.message.provider.annotation.MessageConsumerAroundHandler;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.enums.ConsumeConcurrentlyStatusEnum;
import com.fuli.cloud.message.provider.util.MessageBizResultUtils;
import com.fuli.cloud.model.upush.PushVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 添加监听友盟自定义播推送队列的Mq队列消息的处理逻辑
 *
 * @author: 易煌
 */
@Component
@Slf4j
public class UpushCustomizedCastMessageRabbitMqListener {

    @Autowired
    private UpushMsgQueueBiz upushMsgQueueBiz;


    @MessageConsumerAroundHandler
    @RabbitListener(queues = "UPUSH_CUSTOMIZEDCAST_MSSAGE_QUEUE")
    public void handlerPojoQueueDemo(FuliMqMessage message) {
        log.info("《====== 友盟自定义播推送队列的Mq队列 的监听器收到了消息，消息id为[{}] ，消息体为[{}]", message.getId(), message.getMessageBody());

        PushVo pushVo = JSONObject.parseObject(message.getMessageBody(), PushVo.class);
        Result result = upushMsgQueueBiz.sendMsgCustomizedCast(pushVo);

        log.info("===========友盟定义播推送响应结果=====" + result);

        // 强制必填 ，业务处理成功
        MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.CONSUME_SUCCESS);
    }
}

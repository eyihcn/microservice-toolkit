package com.fuli.cloud.app.queue.service.biz;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.model.upush.PushVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/** 友盟消息推送feign
 * @author 易煌
 */
@FeignClient("upush-message")
public interface UpushMsgQueueBiz {

    /**
     * 发送广播消息
     * @param pushVo
     * @return
     */
    @RequestMapping("/app_push/sendMsgBroadCast")
    Result sendMsgBroadCast(@RequestBody PushVo pushVo);

    /**
     * 发送自定义播消息
     * @param pushVo
     * @return
     */
    @RequestMapping("/app_push/sendMsgCustomizedCast")
    public Result sendMsgCustomizedCast(@RequestBody PushVo pushVo);
}

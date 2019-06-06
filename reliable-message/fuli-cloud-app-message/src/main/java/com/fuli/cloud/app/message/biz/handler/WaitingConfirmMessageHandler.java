package com.fuli.cloud.app.message.biz.handler;

import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;

/**
 * @author chenyi
 */
public interface WaitingConfirmMessageHandler {


   void handleWaitingConfirmTimeOutMessage(FuliMqMessage fuliMqMessage) ;
   
}

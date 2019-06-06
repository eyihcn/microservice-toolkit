package com.fuli.cloud.message.provider.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.dto.FuliMqMessageQueryDto;

import eyihcn.common.core.model.Response;
import io.swagger.annotations.ApiOperation;

@FeignClient(value = "provider-message-service")
public interface FuliMqMessageFeignApi {

	@PostMapping(value = "/api-message/message/saveAndWaitingConfirm")
	@ApiOperation(httpMethod = "POST", value = "预存储消息")
	public Response<?> saveMessageWaitingConfirm(@RequestBody FuliMqMessage fuliMqMessage);

	@PutMapping(value = "/api-message/message/confirmAndSend")
	@ApiOperation(httpMethod = "PUT", value = "确认并发送消息")
	public Response<?> confirmAndSendMessage(@RequestParam("id") String id);

	@PostMapping(value = "/api-message/message/saveAndSend")
	@ApiOperation(httpMethod = "POST", value = "存储并发送消息")
	public Response<?> saveAndSendMessage(@RequestBody FuliMqMessage fuliMqMessage);

	@PostMapping(value = "/api-message/message/directSend")
	@ApiOperation(httpMethod = "POST", value = "直接发送消息")
	public Response<?> directSendMessage(@RequestBody FuliMqMessage fuliMqMessage);

	@PutMapping(value = "/api-message/message/resendById")
	@ApiOperation(httpMethod = "PUT", value = "根据消息ID重发消息")
	public Response<?> resendMessageById(@RequestParam("id") String id);

	@PutMapping(value = "/api-message/message/setToAlreadyDeadById")
	@ApiOperation(httpMethod = "PUT", value = "根据消息ID将消息标记为死亡消息")
	public Response<?> setMessageToAlreadyDeadById(@RequestParam("id") String id);

	@PutMapping(value = "/api-message/reSendAllDeadMessageByQueueName")
	@ApiOperation(httpMethod = "PUT", value = "重发某个消息队列中的全部已死亡的消息")
	public Response<?> reSendAllDeadMessageByQueueName(@RequestParam("queueName") String queueName);

	@GetMapping(value = "/api-message/message/{id}")
	@ApiOperation(httpMethod = "GET", value = "根据消息ID查詢消息")
	public Response<?> getMessageByMessageId(@PathVariable("id") String id);

	@DeleteMapping(value = "/api-message/message/{id}")
	@ApiOperation(httpMethod = "DELETE", value = "根据消息ID删除消息")
	public Response<?> deleteMessageByMessageId(@PathVariable("id") String id);

	@PostMapping(value = "/api-message/message/getPage")
	@ApiOperation(httpMethod = "POST", value = "根据分页查询参数，返回一页消息数据")
	public Response<?> getMessagePage(@RequestBody FuliMqMessageQueryDto fuliMqMessageQueryDto);

}

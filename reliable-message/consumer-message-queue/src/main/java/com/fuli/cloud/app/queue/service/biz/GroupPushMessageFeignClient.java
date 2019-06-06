package com.fuli.cloud.app.queue.service.biz;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fuli.cloud.commons.Response;
import com.fuli.cloud.model.system.GroupPushMessage;

import io.swagger.annotations.ApiOperation;

@FeignClient(value = "system-server")
public interface GroupPushMessageFeignClient {

	@PostMapping(value = "/sys/pushMessageManagement/message/exsitByMessageId")
	@ApiOperation(httpMethod = "POST", value = "根据可靠消息ID，判定消息是否存在")
	public Response<?> exsitByMessageId(@RequestParam("messageId") String messageId) ;

	@PutMapping(value = "/sys/pushMessageManagement/message/updatePushStatusById")
	@ApiOperation(httpMethod = "PUT", value = "根据id更新推送状态")
	public Response<?> updatePushStatusById(@RequestParam("id")Long id, @RequestParam("pushStatus") int pushStatus, @RequestParam(name = "failMsg")  String failMsg);

	@PostMapping(value = "/sys/pushMessageManagement/message/createOrUpdate")
	@ApiOperation(httpMethod = "POST", value = "新建或者更新推送结果")
	public Response<?> createOrUpdate(@RequestBody GroupPushMessage groupPushMessage);

	@PostMapping(value = "/sys/pushMessageManagement/message/createIfNotExsitByMessageId")
	@ApiOperation(httpMethod = "POST", value = "若根据可靠消息id判定不存在，则新建")
	public Response<?> createIfNotExsitByMessageId(@RequestBody GroupPushMessage groupPushMessage);
	
}
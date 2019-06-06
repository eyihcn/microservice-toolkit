package com.fuli.cloud.app.queue.service.biz;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fuli.cloud.commons.Response;
import com.fuli.cloud.model.system.GroupPushMessage;

@FeignClient("saas-server")
public interface UserGroupSentMessageFeignClient {

	@PostMapping("/workbench/messageCenter/createIfNotExsit")
	public Response<?> createIfNotExsit(@RequestParam("userIds") Set<Long> userIds, @RequestBody GroupPushMessage groupPushMessage) ;

	
}

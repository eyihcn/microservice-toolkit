package com.fuli.cloud.app.queue.service.biz;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fuli.cloud.commons.Response;
@FeignClient(value = "user-auth")
public interface SaaSUserCenterFeignClient {

	@PostMapping(value = "inner/userIds")
	public Response<?> getUserIdPage(@RequestParam ("currentPage") int currentPage, @RequestParam ("pageSize") int pageSize);
	
}

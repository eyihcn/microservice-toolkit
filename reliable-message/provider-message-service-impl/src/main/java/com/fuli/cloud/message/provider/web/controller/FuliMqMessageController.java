package com.fuli.cloud.message.provider.web.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuli.cloud.message.provider.exceptions.FuliMqMessageBizException;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.dto.FuliMqMessageQueryDto;
import com.fuli.cloud.message.provider.model.enums.PublicEnum;
import com.fuli.cloud.message.provider.service.FuliMqMessageService;
import com.fuli.cloud.message.provider.util.QueryWrapperUtils;

import eyihcn.common.core.constants.CommonConstant;
import eyihcn.common.core.model.Response;
import eyihcn.common.core.page.PageBean;
import eyihcn.common.core.utils.QWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/16 15:07
 */
@Slf4j
@RestController
@RequestMapping("/api-message")
@Api(tags = "消息服务API")
public class FuliMqMessageController {

	@Resource
	private FuliMqMessageService rpMqMessageService;

	@Resource
	private AmqpTemplate rabbitTemplate;

	@PostMapping(value = "/message/saveAndWaitingConfirm")
	@ApiOperation(httpMethod = "POST", value = "预存储消息")
	public Response<?> saveMessageWaitingConfirm(@RequestBody FuliMqMessage fuliMqMessage) {
		log.debug("预存储消息. fuliMqMessage={}", fuliMqMessage);
		rpMqMessageService.saveMessageWaitingConfirm(fuliMqMessage);
		return Response.ok();
	}

	@PutMapping(value = "/message/confirmAndSend")
	@ApiOperation(httpMethod = "PUT", value = "确认并发送消息")
	public Response<?> confirmAndSendMessage(@ApiParam("消息Id") @RequestParam("id") String id) {
		log.debug("根据消息ID确认并发送消息. messageId={}", id);
		FuliMqMessage fuliMqMessage = rpMqMessageService.confirmAndSendMessage(id);
		try {
			rabbitTemplate.convertAndSend(fuliMqMessage.getConsumerQueue(), fuliMqMessage);
		} catch (Exception e) {
			log.warn("消息状态已成功修改为SENDING，但是投递到MQ中发生异常，消息id={}，异常信息={}", id, e);
			e.printStackTrace();
		}
		return Response.ok();
	}

	@PostMapping(value = "/message/saveAndSend")
	@ApiOperation(httpMethod = "POST", value = "存储并发送消息")
	public Response<?> saveAndSendMessage(@RequestBody FuliMqMessage fuliMqMessage) {
		log.debug("存储并发送消息. fuliMqMessage ={}", fuliMqMessage);
		rpMqMessageService.saveAndSendMessage(fuliMqMessage);
		try {
			rabbitTemplate.convertAndSend(fuliMqMessage.getConsumerQueue(), fuliMqMessage);
		} catch (Exception e) {
			log.warn("直接存储消息成功，消息状态为SENDING，但是投递到MQ中发生异常，消息id={}，异常信息={}", fuliMqMessage.getId(), e);
			e.printStackTrace();
		}
		return Response.ok();
	}

	@PostMapping(value = "/message/directSend")
	@ApiOperation(httpMethod = "POST", value = "直接发送消息, 不进入消息服务DB")
	public Response<?> directSendMessage(@RequestBody FuliMqMessage fuliMqMessage) {
		log.info("直接发送消息. mqMessageDto={}", fuliMqMessage);
		rpMqMessageService.directSendMessage(fuliMqMessage);
		return Response.ok();
	}

	@PutMapping(value = "/message/resendById")
	@ApiOperation(httpMethod = "PUT", value = "根据消息ID重发消息")
	public Response<?> resendMessageById(@ApiParam("消息Id") @RequestParam("id") String id) {
		log.debug("根据消息ID重发消息. messageId={}", id);
		rpMqMessageService.resendMessageById(id);
		return Response.ok();
	}

	@PutMapping(value = "/message/setToAlreadyDeadById")
	@ApiOperation(httpMethod = "PUT", value = "根据消息ID将消息标记为死亡消息")
	public Response<?> setMessageToAlreadyDeadById(@RequestParam("id") String id) {
		log.info("根据消息ID将消息标记为死亡消息. messageId={}", id);
		rpMqMessageService.setMessageToAlreadyDead(id);
		return Response.ok();
	}

	@PutMapping(value = "/reSendAllDeadMessageByQueueName")
	@ApiOperation(httpMethod = "PUT", value = "重发某个消息队列中的全部已死亡的消息")
	public Response<?> reSendAllDeadMessageByQueueName(@RequestParam("queueName") String queueName) {
		log.debug("重发某个消息队列中的全部已死亡的消息. queueName={}", queueName);
		reSendAllDeadMessageByQueueName(queueName, 5000);
		return Response.ok();
	}

	private void reSendAllDeadMessageByQueueName(final String queueName, int batchSize) {

		log.debug("==>reSendAllDeadMessageByQueueName");

		int pageSize = CommonConstant.DEFAULT_BATCH_SIZE;
		if (batchSize > 0 && batchSize < CommonConstant.MIN_PAGE_SIZE) {
			pageSize = CommonConstant.MIN_PAGE_SIZE;
		} else if (batchSize > CommonConstant.MIN_PAGE_SIZE && batchSize < CommonConstant.MAX_PAGE_SIZE) {
			pageSize = batchSize;
		} else if (batchSize > CommonConstant.MAX_PAGE_SIZE) {
			pageSize = CommonConstant.MAX_PAGE_SIZE;
		}
		int pageNum = 1;
		QWrapper<FuliMqMessage> queryWrapper = new QWrapper<FuliMqMessage>();
		// 查询当前时间点之前创建的死亡消息
		queryWrapper.le(FuliMqMessage.Fields.createdTime, new Date());
		queryWrapper.eq(FuliMqMessage.Fields.consumerQueue, queueName);
		queryWrapper.eq(FuliMqMessage.Fields.dead, PublicEnum.YES.getCode());

		IPage<FuliMqMessage> page = new Page<FuliMqMessage>(pageNum, pageSize);
		rpMqMessageService.page(page, queryWrapper);
		List<FuliMqMessage> records = page.getRecords();
		resendMessageList(records, true);
		long pageTotalCount = page.getPages();
		for (pageNum = 2; pageNum <= pageTotalCount; pageNum++) {
			IPage<FuliMqMessage> localPage = new Page<FuliMqMessage>(pageNum, pageSize);
			rpMqMessageService.page(page, queryWrapper);
			List<FuliMqMessage> list = localPage.getRecords();
			resendMessageList(list, true);
		}
	}

	private void resendMessageList(final List<FuliMqMessage> msgList, boolean ignoreResendTimes)
			throws FuliMqMessageBizException {
		if (CollectionUtils.isEmpty(msgList)) {
			log.info("==> resendMessageList is empty");
			return;
		}
		msgList.forEach(msg -> {
			// 重发忽略已重发次数的检查
			FuliMqMessage fuliMqMessage = rpMqMessageService.resendMessage(msg, true);
			try {
				rabbitTemplate.convertAndSend(fuliMqMessage.getConsumerQueue(), fuliMqMessage);
			} catch (Exception e) {
				log.error("==>reSendAllDeadMessageByQueueName ,DB操作成功，单个重发消息到MQ出现异常,消息ID={}，消息队列={}",
						fuliMqMessage.getId(), fuliMqMessage.getConsumerQueue());
				e.printStackTrace();
			}
		});
	}

	@DeleteMapping(value = "/message/{id}")
	@ApiOperation(httpMethod = "DELETE", value = "根据消息ID删除消息")
	public Response<?> deleteMessageByMessageId(@ApiParam("消息Id") @PathVariable("id") String id) {
		log.debug("根据消息ID删除消息. messageId={}", id);
		rpMqMessageService.removeById(id);
		return Response.ok();
	}

	@GetMapping(value = "/message/{id}")
	@ApiOperation(httpMethod = "GET", value = "根据消息ID查詢消息")
	public Response<?> getMessageByMessageId(@ApiParam("消息Id") @PathVariable("id") String id) {
		log.debug("根据消息ID查詢消息. messageId={}", id);
		return Response.ok(rpMqMessageService.getById(id));
	}

	@PostMapping(value = "/message/getPage")
	@ApiOperation(httpMethod = "POST", value = "根据分页查询参数，返回一页消息数据")
	public Response<?> getMessagePage(@RequestBody FuliMqMessageQueryDto rpMqMessageQueryDto) {
		log.debug("根据分页查询参数，返回一页消息数据， 查询参数rpMqMessageQueryDto={}", rpMqMessageQueryDto);
		IPage<FuliMqMessage> pageBean = QueryWrapperUtils.getPageBean(rpMqMessageQueryDto);
		QueryWrapper<FuliMqMessage> queryWrapper = QueryWrapperUtils.getQueryWrapper(rpMqMessageQueryDto);
		queryWrapper.orderByAsc(FuliMqMessage.Fields.createdTime);
		rpMqMessageService.page(pageBean, queryWrapper);
		return Response.ok(PageBean.newPageBean(pageBean));
	}

}

package com.fuli.cloud.message.provider.util;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.dto.FuliMqMessageQueryDto;
import com.fuli.cloud.message.provider.model.enums.MessageStatusEnum;
import com.fuli.cloud.message.provider.model.enums.PublicEnum;
import com.google.common.base.Preconditions;

import eyihcn.common.core.utils.QWrapper;

public abstract class QueryWrapperUtils {

	public static QWrapper<FuliMqMessage> getQueryWrapper(final FuliMqMessageQueryDto fuliMqMessageQueryDto) {

		Preconditions.checkNotNull(fuliMqMessageQueryDto);

		QWrapper<FuliMqMessage> queryWrapper = new QWrapper<>();
		if (null != fuliMqMessageQueryDto.getGeCteatedTime()) {
			queryWrapper.ge(FuliMqMessage.Fields.createdTime, fuliMqMessageQueryDto.getGeCteatedTime());
		}
		if (StringUtils.isNoneBlank(fuliMqMessageQueryDto.getConsumerQueue())) {
			queryWrapper.eq(FuliMqMessage.Fields.consumerQueue, fuliMqMessageQueryDto.getConsumerQueue().trim());
		}
		if (null != fuliMqMessageQueryDto.getMessageStatus()) {
			if (MessageStatusEnum.SENDING.getCode() == fuliMqMessageQueryDto.getMessageStatus().intValue()
					|| MessageStatusEnum.WAITING_CONFIRM.getCode() == fuliMqMessageQueryDto.getMessageStatus()) {
				queryWrapper.eq(FuliMqMessage.Fields.messageStatus, fuliMqMessageQueryDto.getMessageStatus());
			} else {
				// TODO 传入的messageStatus是非法值，抛出异常
			}
		}
		if (fuliMqMessageQueryDto.getDead() != null) {
			if (PublicEnum.YES.getCode() == fuliMqMessageQueryDto.getDead()
					|| PublicEnum.NO.getCode() == fuliMqMessageQueryDto.getDead()) {
				queryWrapper.eq(FuliMqMessage.Fields.dead, fuliMqMessageQueryDto.getDead());
			} else {
				// TODO 传入的dead是非法值，抛出异常
			}
		}
		return queryWrapper;
	}

	public static IPage<FuliMqMessage> getPageBean(final FuliMqMessageQueryDto fuliMqMessageQueryDto) {
		Preconditions.checkNotNull(fuliMqMessageQueryDto);
		return new Page<>(fuliMqMessageQueryDto.getPageNo(), fuliMqMessageQueryDto.getPageSize());
	}
}

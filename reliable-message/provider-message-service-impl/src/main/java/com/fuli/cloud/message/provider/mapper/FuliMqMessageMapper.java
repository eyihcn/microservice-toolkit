package com.fuli.cloud.message.provider.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/16 16:08
 */
@Mapper
public interface FuliMqMessageMapper extends BaseMapper<FuliMqMessage> {

	@Select("select id from mq_message msg where msg.consumer_queue = #{consumerQueue} and msg.dead = #{dead}")
	public List<String> getIdsByConsumerQueueAndDead(@Param("consumerQueue") String consumerQueue,
			@Param("dead") Integer dead);

	@Update("UPDATE mq_message SET message_status=#{messageStatus} ,update_time=#{updateTime} WHERE id=#{id} ")
	public boolean updateMessageStatusById(@Param("id") String id, @Param("messageStatus") int messageStatus,
			@Param("updateTime") Date updateTime);

}
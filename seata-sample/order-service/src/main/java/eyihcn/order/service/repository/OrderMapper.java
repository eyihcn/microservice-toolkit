package eyihcn.order.service.repository;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import eyihcn.order.service.entity.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}

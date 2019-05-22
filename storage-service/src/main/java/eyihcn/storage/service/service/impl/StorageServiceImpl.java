package eyihcn.storage.service.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import eyihcn.storage.service.entity.Storage;
import eyihcn.storage.service.repository.StorageMapper;
import eyihcn.storage.service.service.IStorageService;

@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements IStorageService {

	@Transactional
	@Override
	public void deduct(String commodityCode, Integer count) {
		Storage storage = findByCommodityCode(commodityCode);
		storage.setCount(storage.getCount() - count);

		save(storage);
	}

	public Storage findByCommodityCode(String commodityCode) {

		QueryWrapper<Storage> queryWrapper = new QueryWrapper<Storage>();
		queryWrapper.eq(Storage.COMMODITY_CODE, commodityCode);
		return getOne(queryWrapper);
	}
}

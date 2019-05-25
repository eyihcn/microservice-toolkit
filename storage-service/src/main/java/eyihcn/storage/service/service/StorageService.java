package eyihcn.storage.service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import eyihcn.storage.service.entity.Storage;
import eyihcn.storage.service.repository.StorageMapper;

@Service
public class StorageService extends ServiceImpl<StorageMapper, Storage> {

	@Transactional(rollbackFor = Throwable.class)
	public void deduct(String commodityCode, Integer count) {

		Storage storage = findByCommodityCode(commodityCode);
		UpdateWrapper<Storage> updateWrapper = new UpdateWrapper<Storage>();
		updateWrapper.eq(Storage.ID, storage.getId());
		Storage updateStorage = new Storage();
		updateStorage.setCount(storage.getCount() - count);
		update(updateStorage, updateWrapper);
	}

	public Storage findByCommodityCode(String commodityCode) {

		QueryWrapper<Storage> queryWrapper = new QueryWrapper<Storage>();
		queryWrapper.eq(Storage.COMMODITY_CODE, commodityCode);
		return getOne(queryWrapper);
	}
}

package eyihcn.storage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;

import eyihcn.storage.service.entity.Storage;

public interface IStorageService extends IService<Storage> {

	void deduct(String commodityCode, Integer count);

}

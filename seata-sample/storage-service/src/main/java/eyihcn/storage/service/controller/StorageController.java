package eyihcn.storage.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eyihcn.storage.service.service.StorageService;

@RestController
public class StorageController {

	@Autowired
	private StorageService storageService;

	@GetMapping(path = "/deduct")
	public Boolean deduct(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count) {

		storageService.deduct(commodityCode, count);
		return true;
	}
}


package eyihcn.order.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.seata.spring.annotation.GlobalTransactionScanner;

@Configuration
public class SeataConfiguration {

	@Value("${spring.application.name}")
	private String applicationId;

	@Value("${spring.cloud.alibaba.seata.tx-service-group}")
	private String txServiceGroup;

	/**
	 * 注册一个StatViewServlet
	 *
	 * @return global transaction scanner
	 */
	@Bean
	public GlobalTransactionScanner globalTransactionScanner() {
		GlobalTransactionScanner globalTransactionScanner = new GlobalTransactionScanner(applicationId, txServiceGroup);
		return globalTransactionScanner;
	}
}

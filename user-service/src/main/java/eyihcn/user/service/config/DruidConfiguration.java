
package eyihcn.user.service.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

import io.seata.rm.datasource.DataSourceProxy;

@Configuration
public class DruidConfiguration {

	@Value("${spring.datasource.druid.user}")
	private String druidUser;

	@Value("${spring.datasource.druid.password}")
	private String druidPassword;

	/**
	 * Druid data source druid data source.
	 *
	 * @return the druid data source
	 */
	@Bean(destroyMethod = "close", initMethod = "init")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DruidDataSource druidDataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		return druidDataSource;
	}

	/**
	 * Data source data source.
	 *
	 * @param druidDataSource the druid data source
	 * @return the data source
	 */
	@ConfigurationProperties(prefix = "spring.datasource")
	@Primary
	@Bean("dataSource")
	public DataSource dataSource(DruidDataSource druidDataSource) {
		DataSourceProxy dataSourceProxy = new DataSourceProxy(druidDataSource);
		return dataSourceProxy;
	}

	/**
	 * 注册一个StatViewServlet
	 *
	 * @return servlet registration bean
	 */
	@Bean
	public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
		ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<StatViewServlet>(
				new StatViewServlet(), "/druid/*");

		servletRegistrationBean.addInitParameter("loginUsername", druidUser);
		servletRegistrationBean.addInitParameter("loginPassword", druidPassword);
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	/**
	 * 注册一个：filterRegistrationBean
	 *
	 * @return filter registration bean
	 */
	@Bean
	public FilterRegistrationBean<WebStatFilter> druidStatFilter() {

		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>(
				new WebStatFilter());

		// 添加过滤规则.
		filterRegistrationBean.addUrlPatterns("/*");

		// 添加不需要忽略的格式信息.
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}
}

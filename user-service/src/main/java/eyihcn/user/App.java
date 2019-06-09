package eyihcn.user;

import java.util.TimeZone;

import javax.annotation.Resource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;

import eyihcn.common.core.interceptor.LoginUserInfoInterceptor;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("eyihcn.user.mapper")
@ComponentScan("eyihcn.*")
@EnableTransactionManagement
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Resource
	private LoginUserInfoInterceptor loginUserInfoInterceptor;

	/**
	 * 分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	/**
	 * SQL执行效率插件
	 */
	@Bean
	@Profile({ "dev", "test", "home" }) // 设置 dev test 环境开启
	public PerformanceInterceptor performanceInterceptor() {
		return new PerformanceInterceptor();
	}

	/** 解决时区问题，参考：https://www.jianshu.com/p/085eb3c3120e */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
		return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("GMT+8"))
				.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Configuration
	public class MyWebAppConfigurer extends WebMvcConfigurationSupport {
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(loginUserInfoInterceptor).addPathPatterns("/**").excludePathPatterns(
					"/swagger-resources/**", "*.js", "/**/*.js", "*.css", "/**/*.css", "*.html", "/**/*.html");
			super.addInterceptors(registry);
		}
	}
}

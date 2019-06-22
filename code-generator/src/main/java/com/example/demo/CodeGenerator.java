package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import eyihcn.common.core.model.BaseEntity;
import eyihcn.common.core.utils.JacksonUtil;

//演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
/**
 * Demo class
 *
 * @author xq
 * @date 2019/3/31
 */
public class CodeGenerator {

	@Test
	public void test1() {
		CodeGenerator codeGenerator = new CodeGenerator();
		codeGenerator.getAutoGenerator();
	}

//    driverClassName: com.mysql.jdbc.Driver    
//    driverClassName: com.mysql.cj.jdbc.Driver 

	public static final String URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "root";
//	public static final String PASSWORD = "affuli123";
	// #是 mysql-connector-java 6中的
	public static final String DRIVERNAME = "com.mysql.cj.jdbc.Driver";
	// #是 mysql-connector-java 5中的
//    public static final String  DRIVERNAME = "com.mysql.jdbc.Driver"; 

//	public static final String PACK_NAME = "com.fuli.cloud";
	public static final String PACK_NAME = "eyihcn.user";
	public static String projectPath = System.getProperty("user.dir") + "/src/main/java";

	private static final String author = "chenyi";

	public void getAutoGenerator(String... tableNames) {
		AutoGenerator autoGenerator = new AutoGenerator();

		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setOutputDir(projectPath);
		globalConfig.setAuthor(author);
		globalConfig.setOpen(false);
		globalConfig.setFileOverride(true);
		globalConfig.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
		globalConfig.setEnableCache(false);// XML 二级缓存
		globalConfig.setBaseResultMap(true);// XML ResultMap
		globalConfig.setBaseColumnList(true);// XML columList
		globalConfig.setServiceName("%sService");
		// 1.
		autoGenerator.setGlobalConfig(globalConfig);

		PackageConfig packageConfig = new PackageConfig();
		String moduleName = "test";
		packageConfig.setModuleName(moduleName);
		packageConfig.setParent(PACK_NAME);
		// 2
		autoGenerator.setPackageInfo(packageConfig);

		DataSourceConfig dataSourceConfig = new DataSourceConfig();
		dataSourceConfig.setUrl(URL);
		dataSourceConfig.setDriverName(DRIVERNAME);
		dataSourceConfig.setUsername(USERNAME);
		dataSourceConfig.setPassword(PASSWORD);
		// 3
		autoGenerator.setDataSource(dataSourceConfig);

		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setController("/templates/vm/CrudController.java.vm");
		templateConfig.setService("/templates/vm/service.java.vm");
		templateConfig.setServiceImpl("/templates/vm/serviceImpl.java.vm");
		templateConfig.setEntity("/templates/vm/entity.java.vm");
		templateConfig.setMapper("/templates/vm/mapper.java.vm");
		// 4
		autoGenerator.setTemplate(templateConfig);

		autoGenerator.setCfg(getInjectionConfig(packageConfig));

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		strategy.setEntityLombokModel(true);
		strategy.setInclude(tableNames);
		strategy.setSuperEntityColumns(BaseEntity.CREATE_TIME, BaseEntity.CREATE_USER, BaseEntity.UPDATE_TIME,
				BaseEntity.UPDATE_USER);

		autoGenerator.setStrategy(strategy);
//        使用freemarker模板 ，默认使用的是velocity模板
//        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
		autoGenerator.execute();

	}

	/**
	 *
	 * 配置自定义魔板定义
	 */

	public InjectionConfig getInjectionConfig(PackageConfig packageConfig) {
		InjectionConfig injectionConfig = new InjectionConfig() {
			@Override
			public void initMap() {
//                配置生成异常类的包名${cfg.ExceptionPak}获取属性
				Map<String, Object> map = new HashMap<>();
				map.put("ExceptionPak", PACK_NAME + "." + packageConfig.getModuleName() + ".exception");
				map.put("DtoPak", PACK_NAME + "." + packageConfig.getModuleName() + ".dto");
				map.put("VoPak", PACK_NAME + "." + packageConfig.getModuleName() + ".vo");
				map.put("BeancopyPak", PACK_NAME + "." + packageConfig.getModuleName() + ".beancopy");
				this.setMap(map);
			}
		};
		// 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
		// 如果模板引擎是 velocity
//         String templatePath = "/templates/vm/exception.java.vm";
		// 自定义输出配置
		List<FileOutConfig> focList = new ArrayList<>();
		// 将包路径转化为文件路径
		String pack = PACK_NAME.replace(".", "/");
		// 自定义配置会被优先输出
		focList.add(new FileOutConfig("/templates/vm/pageDto.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				JacksonUtil.dumnToPrettyJson(tableInfo);
				// 自定义输出文件名 按自定义魔板生成对应异常处理类
				return projectPath + "/" + pack + "/" + packageConfig.getModuleName() + "/dto/"
						+ tableInfo.getEntityName() + "PageDto" + StringPool.DOT_JAVA;
			}
		});
		// 自定义配置会被优先输出
//        focList.add(new FileOutConfig(templatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//
//                // 自定义输出文件名 按自定义魔板生成对应异常处理类
//                return projectPath  + "/" + pack + "/"+ packageConfig.getModuleName()
//                        + "/exception/" + tableInfo.getEntityName() + "Exception" + StringPool.DOT_JAVA;
//            }
//        });
		// 自定义配置会被优先输出
//        focList.add(new FileOutConfig("/templates/vm/request.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                System.out.println(packageConfig.getController() +"  =============》       " +   packageConfig.getModuleName());
//                // 自定义输出文件名 按自定义魔板生成对应异常处理类
//                return projectPath + "/" + pack  + "/"+ packageConfig.getModuleName() + "/" + packageConfig.getController()
//                        + "/request/" + tableInfo.getEntityName() + "Request" + StringPool.DOT_JAVA;
//            }
//        });
//        focList.add(new FileOutConfig("/templates/vm/mapper.xml.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                System.out.println("  =============》       " + projectPath.lastIndexOf("/"));
//                // 自定义输入文件名称
//                return  projectPath.substring( 0 , projectPath.lastIndexOf("/")) + "/resources/mapper" + "/" + packageConfig.getModuleName()
//                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
		injectionConfig.setFileOutConfigList(focList);
		return injectionConfig;
	}

}
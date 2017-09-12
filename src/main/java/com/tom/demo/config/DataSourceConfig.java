package com.tom.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by tom.tang on 2017/5/25.
 * 配置分表分库
 */
@Configuration
public class DataSourceConfig {
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.initialSize}")
	private int initialSize;

	@Value("${spring.datasource.minIdle}")
	private int minIdle;

	@Value("${spring.datasource.maxActive}")
	private int maxActive;

	@Value("${spring.datasource.maxWait}")
	private int maxWait;

	@Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;

	@Value("${spring.datasource.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;

	@Value("${spring.datasource.validationQuery}")
	private String validationQuery;

	@Value("${spring.datasource.testWhileIdle}")
	private boolean testWhileIdle;

	@Value("${spring.datasource.testOnBorrow}")
	private boolean testOnBorrow;

	@Value("${spring.datasource.testOnReturn}")
	private boolean testOnReturn;

	@Value("${spring.datasource.poolPreparedStatements}")
	private boolean poolPreparedStatements;

	@Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
	private int maxPoolPreparedStatementPerConnectionSize;

	@Value("${spring.datasource.filters}")
	private String filters;

	@Value("{spring.datasource.connectionProperties}")
	private String connectionProperties;

	@Bean
	public DataSource getDataSource() {
		return buildDataSource();
	}
	private DataSource buildDataSource() {
		//设置分库映射
		Map<String, DataSource> dataSourceMap = new HashMap<>(1);
		//添加两个数据库ds_0,ds_1到map里
		dataSourceMap.put("ds_0", createDataSource());
//		dataSourceMap.put("ds_1", createDataSource("ds_1"));//暂时保留，以后分库用
		//设置默认db为ds_0，也就是为那些没有配置分库分表策略的指定的默认库
		//如果只有一个库，也就是不需要分库的话，map里只放一个映射就行了，只有一个库时不需要指定默认库，但2个及以上时必须指定默认库，否则那些没有配置策略的表将无法操作数据
		DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, "ds_0");

		//设置分表映射，将asin_xx若干个实际的表映射到asin逻辑表
		//0和1两个表是真实的表，t_order是个虚拟不存在的表，只是供使用。如查询所有数据就是select * from asin就能查完所有表
		List<String> actualTables = new ArrayList<String>();
		actualTables.add("asin_" + 31);
		actualTables.add("asin_" + 32);

		TableRule asinTableRule = TableRule.builder("asin")
				.actualTables(actualTables)
				.dataSourceRule(dataSourceRule)
				.build();

		//具体分库分表策略，按什么规则来分
		ShardingRule shardingRule = ShardingRule.builder()
				.dataSourceRule(dataSourceRule)
				.tableRules(Arrays.asList(asinTableRule))
				.tableShardingStrategy(new TableShardingStrategy("week", new AsinTableShardingAlgorithm()))
				.build();
		DataSource dataSource = null;
		//1.5.3
		try {
			dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//1.4.2
//		dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
		return dataSource;
	}

	private DataSource createDataSource() {
		//使用druid连接数据库
		DruidDataSource datasource = new DruidDataSource();
		datasource.setDriverClassName(driverClassName);
		datasource.setUrl(url);
		datasource.setUsername(username);
		datasource.setPassword(password);
		//configuration
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			System.out.println("druid configuration initialization filter");
		}
		datasource.setConnectionProperties(connectionProperties);
		return datasource;
	}
}

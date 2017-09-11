package com.tom.demo.config;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by Mr Tom on 2017/5/28.
 * 分表规则(按周分表)
 */
public class AsinTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Integer> {
	/**
	 *  SELECT * FROM asin WHERE week = 23
	 *          └── SELECT *  FROM asin_23 WHERE week = 23
	 *  SELECT * FROM asin WHERE week = 24
	 *          └── SELECT *  FROM asin_24 WHERE week = 24
	 */
	public String doEqualSharding(final Collection<String> tableNames, final ShardingValue<Integer> shardingValue) {
		for (String each : tableNames) {
			if (each.endsWith(shardingValue.getValue() +  "")) {
				return each;
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 *  SELECT * FROM asin where week in (22,23)
	 *          ├── SELECT *  FROM asin_22 WHERE week IN (22)
	 *          └── SELECT *  FROM asin_23 WHERE week IN (23)
	 *  SELECT * FROM asin where week in (22)
	 *          └── SELECT *  FROM asin_22 WHERE week IN (22)
	 *  SELECT * FROM asin where week in (23)
	 *          └──SELECT *  FROM asin_23 WHERE order_id IN (23)
	 */
	public Collection<String> doInSharding(final Collection<String> tableNames, final ShardingValue<Integer> shardingValue) {
		Collection<String> result = new LinkedHashSet<>(tableNames.size());
		for (Integer value : shardingValue.getValues()) {
			for (String tableName : tableNames) {
				if (tableName.endsWith(value + "")) {
					result.add(tableName);
				}
			}
		}
		return result;
	}

	/**
	 *  SELECT * FROM asin where week between 22 and 25
	 *          ├── SELECT *  FROM asin_22 WHERE week BETWEEN 22 AND 25
	 *          ├── SELECT *  FROM asin_23 WHERE week BETWEEN 22 AND 25
	 *          ├── ....
	 *          └── ....
	 */
	public Collection<String> doBetweenSharding(final Collection<String> tableNames, final ShardingValue<Integer> shardingValue) {
		Collection<String> result = new LinkedHashSet<>(tableNames.size());
		Range<Integer> range = shardingValue.getValueRange();
		for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
			for (String each : tableNames) {
				if (each.endsWith(i + "")) {
					result.add(each);
				}
			}
		}
		return result;
	}
}

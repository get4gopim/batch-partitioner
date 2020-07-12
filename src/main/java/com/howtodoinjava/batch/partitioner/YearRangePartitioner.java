package com.howtodoinjava.batch.partitioner;

import com.howtodoinjava.batch.mapper.YearRowMapper;
import com.howtodoinjava.batch.model.Customer;
import com.howtodoinjava.batch.model.YearCount;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YearRangePartitioner implements Partitioner
{
	private JdbcOperations jdbcTemplate;
	private String table;
	private String column;

	public void setTable(String table) {
		this.table = table;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) 
	{
		List<YearCount> yearCountList = getYearCounts();

		Map<String, ExecutionContext> result = new HashMap<>();

		int number = 0;

		if (!CollectionUtils.isEmpty(yearCountList)) {

			int min = 1;
			int max = yearCountList.size();
			int targetSize = ((max - min) / gridSize) + 1;

			System.out.println("targetSize: " + targetSize + " gridSize: " + gridSize);

			int start = min;
			int end = start + targetSize - 1;

			while (start <= max) {
				if (end >= max) end = max;

				for (int i=start;i<=end;i++) {
					YearCount yearCount = yearCountList.get(i-1);
					System.out.println("Reading start: "  +start + " end: " + end + " viewCount: " + yearCount.toString());

					ExecutionContext value = new ExecutionContext();
					result.put("partition" + number, value);

					value.putInt("minValue", yearCount.getYear());
					value.putInt("maxValue", yearCount.getYear());

					number++;
				}

				start += targetSize;
				end += targetSize;
			}
		}
		return result;
	}

	private List<YearCount> getYearCounts() {
		//String sql = "SELECT EXTRACT(year from " + column + ")::int as yr, count(*) as total FROM " + table + " group by yr order by yr";
		String sql = "SELECT date_part('year', birthdate)::int as yr, count(*)::int as total FROM " + table + " group by yr order by yr";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

		List<YearCount> yearCountList = new ArrayList<>();
		for (Map row : rows) {
			YearCount obj = new YearCount();

			//String year = row.get("yr").toString();

			obj.setYear(((Integer) row.get("yr")).intValue());
			obj.setCustomerCount(((Integer) row.get("total")).intValue());

			yearCountList.add(obj);
		}
		return yearCountList;
	}
}
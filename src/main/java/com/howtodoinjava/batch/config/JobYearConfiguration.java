package com.howtodoinjava.batch.config;

import com.howtodoinjava.batch.CustomerReader;
import com.howtodoinjava.batch.mapper.CustomerRowMapper;
import com.howtodoinjava.batch.model.Customer;
import com.howtodoinjava.batch.partitioner.ColumnRangePartitioner;
import com.howtodoinjava.batch.partitioner.YearRangePartitioner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JobYearConfiguration
{
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private CustomerReader customerItemReader;

	/*@Bean
	//@StepScope
	public ItemReader<Customer> customerItemReader() {

		CustomerReader customerReader = new CustomerReader(dataSource);

		return customerReader;
	}*/

	@Bean
	public YearRangePartitioner partitioner()
	{
		YearRangePartitioner columnRangePartitioner = new YearRangePartitioner();
		columnRangePartitioner.setColumn("id");
		columnRangePartitioner.setDataSource(dataSource);
		columnRangePartitioner.setTable("emp.customer");
		return columnRangePartitioner;
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<Customer> pagingYearReader(
			@Value("#{stepExecutionContext['minValue']}") Integer minValue,
			@Value("#{stepExecutionContext['maxValue']}") Integer maxValue)
	{
		System.out.println("reading " + minValue + " to " + maxValue);

		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("id", Order.ASCENDING);

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, firstName, lastName, birthdate");
		queryProvider.setFromClause("from emp.customer");
		queryProvider.setWhereClause("where extract (year from birthdate) = " + minValue );
		queryProvider.setSortKeys(sortKeys);

		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(this.dataSource);
		reader.setFetchSize(1000);
		reader.setRowMapper(new CustomerRowMapper());
		reader.setQueryProvider(queryProvider);

		return reader;
	}
	
	
	@Bean
	@StepScope
	public JdbcBatchItemWriter<Customer> customerItemWriter()
	{
		JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setSql("INSERT INTO emp.NEW_CUSTOMER VALUES (:id, :firstName, :lastName, :birthdate)");

		itemWriter.setItemSqlParameterSourceProvider
			(new BeanPropertyItemSqlParameterSourceProvider<>());
		itemWriter.afterPropertiesSet();
		
		return itemWriter;
	}
	
	// Master
	@Bean
	public Step step1() 
	{
		return stepBuilderFactory.get("step1")
				.partitioner(slaveStep().getName(), partitioner())
				.step(slaveStep())
				.gridSize(4)
				.taskExecutor(new SimpleAsyncTaskExecutor("thread_batch_"))
				.build();
	}
	
	// slave step
	@Bean
	public Step slaveStep() 
	{
		return stepBuilderFactory.get("slaveStep")
				.<Customer, Customer>chunk(1000)
				.reader(customerItemReader)
				.writer(customerItemWriter())
				.build();
	}
	
	@Bean
	public Job job() 
	{
		return jobBuilderFactory.get("job")
				.start(step1())
				.build();
	}
}
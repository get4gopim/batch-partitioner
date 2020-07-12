package com.howtodoinjava.batch;

import com.howtodoinjava.batch.model.Customer;
import com.howtodoinjava.batch.model.YearCount;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@StepScope
public class CustomerReader implements ItemReader<Customer>, ItemStream {

    private List<Customer> listData;

    private JdbcOperations jdbcTemplate;

    ExecutionContext context;

    private int index = 0;
    private static final String CURRENT_INDEX = "current.index";

    /*@Autowired
    private DataSource dataSource;*/

    //@Value("#{stepExecutionContext['minValue']}")
    //private Integer minValue;

    public CustomerReader(DataSource dataSource) {
        //if (minValue == null) throw new RuntimeException("year value is NUlL");
        //this.year = minValue;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        //initialize();
    }

    private void initialize() {

        if (context == null) throw new RuntimeException("NULL context");

        int year = this.context.getInt("minValue");

        String sql = "SELECT * FROM emp.customer where extract (year from birthdate) = " + year;
        System.out.println(sql);

        if (jdbcTemplate == null) throw new RuntimeException("NULL jdbcTemplate");

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        listData = new ArrayList<>();
        for (Map row : rows) {

            Integer id = Integer.valueOf(row.get("ID").toString());

            Customer obj = new Customer(Long.valueOf(id),
                    row.get("firstname").toString(),
                    row.get("lastname").toString(),
                    row.get("birthdate").toString());

            listData.add(obj);
        }
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        Customer nextStudent = null;

        if (index == 0) {
            initialize();
        }

        if (listData != null && index < listData.size()) {
            nextStudent = listData.get(index);
            index++;
        } else {
            //System.out.println("No items to read");
        }

        return nextStudent;
    }

    @BeforeStep
    public void retrieveInterstepData(StepExecution stepExecution) {
        //System.out.println("BeforeStep - StepExecution: " + stepExecution);
        this.context = stepExecution.getExecutionContext();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        //System.out.println("open context: " + executionContext);

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        //System.out.println("update context: " + executionContext);
        executionContext.putInt(CURRENT_INDEX, index);

        if (index == 1000) { // index == chunkSize
            index = 0;
            System.out.println("Update Reset Index: " + index);
        }
    }

    @Override
    public void close() throws ItemStreamException {
        int oldIndex = index;
        index = 0;
        //System.out.println("close reader. Current Index: " + oldIndex + " newIndex: " + index);
        //this.context = null;
    }
}

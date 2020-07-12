package com.howtodoinjava.batch.mapper;

import com.howtodoinjava.batch.model.Customer;
import com.howtodoinjava.batch.model.YearCount;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class YearRowMapper implements RowMapper<YearCount> {

    @Override
    public YearCount mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new YearCount(rs.getInt("yr"), rs.getInt("total"));

    }

}

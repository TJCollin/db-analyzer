package cn.collin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by collin on 17-5-29.
 */
@Service
public class ConnDB {

    @Autowired
    JdbcTemplate jdbcTemplate;

    String sql;

    public List createRecord (long searchStart, long searchEnd, String contractID) {
        sql = sql = "select invoke_begin::bigint , invoke_end::bigint , check_time::bigint, invoke_id  from tran_data where invoke_begin::bigint < "+
                searchEnd+
                "  and invoke_begin::bigint >"+
                searchStart+
                " and chaincode_id = \'"+
                contractID+
                "\' order by invoke_begin::bigint";
        System.out.println(sql);

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public void insertData (String sql) {
        jdbcTemplate.execute(sql);
    }
}

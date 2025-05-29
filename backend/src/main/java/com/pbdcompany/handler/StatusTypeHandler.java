package com.pbdcompany.handler;

import com.pbdcompany.enums.Status;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class StatusTypeHandler extends BaseTypeHandler<Status> {

    // 设置参数时：Java 枚举 -> 数据库字符串
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Status status, JdbcType jdbcType) throws SQLException {
        ps.setString(i, status.getDescription()); // 存储 description 到数据库
    }

    // 获取结果时：数据库字符串 -> Java 枚举
    @Override
    public Status getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return fromDescription(value);
    }

    @Override
    public Status getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return fromDescription(value);
    }

    @Override
    public Status getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return fromDescription(value);
    }

    // 根据 description 找到对应的枚举
    private Status fromDescription(String description) {
        return Arrays.stream(Status.values())
                .filter(status -> status.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant for description: " + description));
    }
}

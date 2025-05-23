package com.pbdcompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DbTestRunner implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ 成功连接到云服务器上的 MySQL 数据库！");
        } catch (Exception e) {
            System.err.println("❌ 数据库连接失败：" + e.getMessage());
        }
    }
}

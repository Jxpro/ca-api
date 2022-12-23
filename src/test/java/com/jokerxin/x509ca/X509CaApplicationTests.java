package com.jokerxin.x509ca;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jokerxin.x509ca.bean.Demo;
import com.jokerxin.x509ca.service.DemoService;
import com.jokerxin.x509ca.utils.Hash;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class X509CaApplicationTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private DemoService demoService;

    @Test
    void testHash() {
        System.out.println(Hash.sha256("admin"));
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testSelectDemo() {
        System.out.println(("----- selectAll method test ------"));
        List<Demo> userList = demoService.list();
        userList.forEach(System.out::println);
    }

    @Test
    public void testSelectDemoPage(){
        System.out.println(("----- selectPage method test ------"));
        Page<Demo> demoUserPage = new Page<>(1, 2);
        Page<Demo> page = demoService.page(demoUserPage);
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.getCurrent());
        page.getRecords().forEach(System.out::println);
    }
}

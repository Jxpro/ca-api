package com.jokerxin.x509ca;

import com.jokerxin.x509ca.utils.Hash;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class X509CaApplicationTests {
	@Autowired
	private DataSource dataSource;

	@Test
	void testHash() {
		System.out.println(Hash.sha256("admin"));
	}

	@Test
	void testDatabaseConnection() throws SQLException {
		System.out.println(dataSource.getConnection());
	}
}
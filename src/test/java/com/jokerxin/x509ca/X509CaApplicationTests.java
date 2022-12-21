package com.jokerxin.x509ca;

import com.jokerxin.x509ca.utils.Hash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class X509CaApplicationTests {

	@Test
	void testHash() {
		System.out.println(Hash.sha256("admin"));
	}

}
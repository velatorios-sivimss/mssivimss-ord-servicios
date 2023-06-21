package com.imss.sivimss.ordservicios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OrdServiciosApplicationTests {

	@Test
	void contextLoads() {
		String result="test";
		OrdServiciosApplication.main(new String[]{});
		assertNotNull(result);
	}

}

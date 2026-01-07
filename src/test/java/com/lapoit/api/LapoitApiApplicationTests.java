package com.lapoit.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.task.scheduling.enabled=false")
@ActiveProfiles("test")
class LapoitApiApplicationTests {

	@Test
	void contextLoads() {
	}

}

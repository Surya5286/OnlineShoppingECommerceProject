package com.online.shopping;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnlineShoppingECommerceProjectApplicationTests {

	@Test
	void contextLoads() {
		boolean bool = true;
		Assertions.assertThat(bool).isEqualTo(true);

	}

}

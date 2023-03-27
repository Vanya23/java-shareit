package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
class ShareItTests {

	@Test
	void contextLoads() {
		ShareItApp shareItApp = new ShareItApp();
		ShareItApp.main(new String[]{""});
		assertThat(shareItApp, notNullValue());
	}

}

package com.example.techcomp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Используем профиль "test" для тестов
class TechcompApplicationTests {

	@Test
	void contextLoads() {
		// Тест контекста
	}
}
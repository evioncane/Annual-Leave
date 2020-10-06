package com.example.demo;

import com.example.demo.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnnualLeaveApplicationTests {

	@Mock
	TestService testService;

	@Test
	public void contextLoads() {
		when(testService.greet()).thenReturn("Hello");
		assertEquals(testService.greet(), "Hello");
	}

}

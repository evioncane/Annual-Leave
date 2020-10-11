package com.example.demo;

import com.example.demo.model.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnnualLeaveApplicationTests {

	@Test
	public void lombok() {
		UserEntity user = new UserEntity();
		user.setUsername("Joni");
	}

}

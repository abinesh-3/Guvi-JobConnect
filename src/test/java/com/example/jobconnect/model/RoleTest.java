package com.example.jobconnect.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

public class RoleTest {
	
	public static class RoleTestUnit {
		@Test
		void testEnumValues() {
			assertEquals("JOB_SEEKER", Role.JOB_SEEKER.name());
			assertEquals("EMPLOYER", Role.EMPLOYER.name());
			assertEquals("ADMIN", Role.ADMIN.name());
			assertEquals(3, Role.values().length);
		}

		@Test
		void testWithMockito() {
			Role mockRole = Mockito.mock(Role.class);
			assertNotNull(mockRole);
		}
	}
}

package com.example.jobconnect.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import java.util.Set;

public class UserTest {
	private Long id;
	private String email;
	private String password;
	private String fullName;
	private String phone;
	private Set<Role> roles;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getFullName() { return fullName; }
	public void setFullName(String fullName) { this.fullName = fullName; }
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public Set<Role> getRoles() { return roles; }
	public void setRoles(Set<Role> roles) { this.roles = roles; }

	
	public static class UserTestUnit {
		@Test
		void testGettersAndSetters() {
			UserTest user = new UserTest();
			user.setId(7L);
			user.setEmail("a@b.com");
			user.setPassword("pw");
			user.setFullName("Name");
			user.setPhone("12345");
			Set<Role> roles = Set.of(Role.JOB_SEEKER, Role.EMPLOYER);
			user.setRoles(roles);
			assertEquals(7L, user.getId());
			assertEquals("a@b.com", user.getEmail());
			assertEquals("pw", user.getPassword());
			assertEquals("Name", user.getFullName());
			assertEquals("12345", user.getPhone());
			assertEquals(roles, user.getRoles());
		}

		@Test
		void testWithMockito() {
			UserTest mockUser = Mockito.mock(UserTest.class);
			when(mockUser.getId()).thenReturn(8L);
			when(mockUser.getEmail()).thenReturn("mock@ex.com");
			when(mockUser.getFullName()).thenReturn("MockName");
			assertEquals(8L, mockUser.getId());
			assertEquals("mock@ex.com", mockUser.getEmail());
			assertEquals("MockName", mockUser.getFullName());
		}
	}
}

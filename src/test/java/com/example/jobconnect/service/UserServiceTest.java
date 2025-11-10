package com.example.jobconnect.service;

import com.example.jobconnect.model.Role;
import com.example.jobconnect.model.User;
import com.example.jobconnect.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedPassword123");
        testUser.setFullName("Test User");
        testUser.setPhone("+1234567890");
        Set<Role> roles = new HashSet<>();
        roles.add(Role.EMPLOYER);
        testUser.setRoles(roles);
    }

    @Test
    void testSaveUser() {
        when(repository.save(any(User.class))).thenReturn(testUser);

        User saved = service.save(testUser);

        assertNotNull(saved);
        assertEquals(testUser.getId(), saved.getId());
        assertEquals(testUser.getEmail(), saved.getEmail());
        assertEquals(testUser.getPassword(), saved.getPassword());
        assertEquals(testUser.getFullName(), saved.getFullName());
        assertEquals(testUser.getPhone(), saved.getPhone());
        assertEquals(testUser.getRoles(), saved.getRoles());

        verify(repository).save(testUser);
    }

    @Test
    void testSaveNewUser() {
        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setPassword("password123");
        when(repository.save(any(User.class))).thenReturn(newUser);

        User saved = service.save(newUser);

        assertNotNull(saved);
        assertEquals(newUser.getEmail(), saved.getEmail());
        assertEquals(newUser.getPassword(), saved.getPassword());

        verify(repository).save(newUser);
    }

    @Test
    void testFindByEmail() {
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> found = service.findByEmail("test@example.com");

        assertTrue(found.isPresent());
        assertEquals(testUser.getId(), found.get().getId());
        assertEquals(testUser.getEmail(), found.get().getEmail());
        assertEquals(testUser.getPassword(), found.get().getPassword());

        verify(repository).findByEmail("test@example.com");
    }

    @Test
    void testFindByEmailNotFound() {
        when(repository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<User> found = service.findByEmail("nonexistent@example.com");

        assertFalse(found.isPresent());

        verify(repository).findByEmail("nonexistent@example.com");
    }

    @Test
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> found = service.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(testUser.getId(), found.get().getId());
        assertEquals(testUser.getEmail(), found.get().getEmail());

        verify(repository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Optional<User> found = service.findById(999L);

        assertFalse(found.isPresent());

        verify(repository).findById(999L);
    }

    @Test
    void testSaveNullUser() {
        when(repository.save(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> service.save(null));

        verify(repository).save(null);
    }

    @Test
    void testFindByEmailWithNull() {
        when(repository.findByEmail(null)).thenReturn(Optional.empty());

        Optional<User> found = service.findByEmail(null);

        assertFalse(found.isPresent());

        verify(repository).findByEmail(null);
    }

    @Test
    void testSaveUserWithRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.EMPLOYER);
        roles.add(Role.JOB_SEEKER);
        testUser.setRoles(roles);
        when(repository.save(any(User.class))).thenReturn(testUser);

        User saved = service.save(testUser);

        assertNotNull(saved);
        assertEquals(2, saved.getRoles().size());
        assertTrue(saved.getRoles().contains(Role.EMPLOYER));
        assertTrue(saved.getRoles().contains(Role.JOB_SEEKER));

        verify(repository).save(testUser);
    }
}

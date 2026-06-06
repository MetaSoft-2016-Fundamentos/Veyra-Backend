package iam.entities;

import com.metasoft.veyra.platform.iam.domain.model.aggregates.User;
import com.metasoft.veyra.platform.iam.domain.model.entities.Role;
import com.metasoft.veyra.platform.iam.domain.model.valueobjects.Roles;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserAggregateTest {

    @Test
    void shouldCreateUserWithUsernameAndPassword() {
        User user = new User("carlos.dev", "password123");

        assertEquals("carlos.dev", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertNotNull(user.getRoles(), "The roles set should not be null");
        assertTrue(user.getRoles().isEmpty(), "A new user should not have any roles assigned");
    }

    @Test
    void shouldInitializeRolesAsEmptySetWithDefaultConstructor() {
        User user = new User();

        assertNotNull(user.getRoles(), "The roles set should be initialized by default");
        assertTrue(user.getRoles().isEmpty(), "Default constructor should initialize an empty roles set");
    }

    @Test
    void shouldAssignDefaultRoleWhenEmptyRoleListIsProvided() {
        User user = new User("ana.garcia", "pass", List.of());

        assertFalse(user.getRoles().isEmpty(),
                "Should receive the default role when an empty list is provided");
        assertTrue(user.getRoles().stream()
                .anyMatch(r -> r.getName().equals(Roles.ROLE_USER)), "Default role should be ROLE_USER");
    }

    @Test
    void shouldAddRoleAndReturnSameUserInstance() {
        User user = new User("pedro.admin", "pass");
        Role adminRole = new Role(Roles.ROLE_ADMIN);

        User result = user.addRole(adminRole);

        assertSame(user, result, "addRole should return the same User object instance (fluent API)");
        assertTrue(user.getRoles().stream()
                .anyMatch(r -> r.getName().equals(Roles.ROLE_ADMIN)), "The admin role should be present in the user roles");
    }

    @Test
    void shouldNotStoreDuplicateRoles() {
        User user = new User("lucia.test", "pass");
        Role role = new Role(Roles.ROLE_FAMILIAR);

        user.addRole(role);
        user.addRole(role);

        assertEquals(1, user.getRoles().size(),
                "The set should not store duplicate roles");
    }
}
package com.metasoft.veyra.platform.iam.infrastructure.authorization.sfs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metasoft.veyra.platform.iam.domain.model.aggregates.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This class is responsible for providing the user details to the Spring Security framework.
 * It implements the UserDetails interface.
 */
@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {

    /** The username. */
    private final String username;
    /** The password. */
    @JsonIgnore
    private final String password;
    /** Whether the account is non-expired. */
    private final boolean accountNonExpired;
    /** Whether the account is non-locked. */
    private final boolean accountNonLocked;
    /** Whether the credentials are non-expired. */
    private final boolean credentialsNonExpired;
    /** Whether the account is enabled. */
    private final boolean enabled;
    /** The authorities. */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * This constructor initializes the UserDetailsImpl object.
     * @param username The username.
     * @param password The password.
     * @param authorities The authorities.
     */
    public UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    /**
     * This method is responsible for building the UserDetailsImpl object from the User object.
     * @param user The user object.
     * @return The UserDetailsImpl object.
     */
    public static UserDetailsImpl build(User user) {
        var authorities = user.getRoles().stream()
                .map(role -> role.getName().name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

}

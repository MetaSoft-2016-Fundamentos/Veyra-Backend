package com.metasoft.veyra.platform.iam.application.internal.commandservices;

import com.metasoft.veyra.platform.iam.application.internal.outboundservices.acl.ExternalHcmService;
import com.metasoft.veyra.platform.iam.application.internal.outboundservices.acl.ExternalNursingService;
import com.metasoft.veyra.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.metasoft.veyra.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.metasoft.veyra.platform.iam.domain.model.aggregates.User;
import com.metasoft.veyra.platform.iam.domain.model.commands.CreateRelativeAccountCommand;
import com.metasoft.veyra.platform.iam.domain.model.commands.SetPasswordCommand;
import com.metasoft.veyra.platform.iam.domain.model.commands.SignInCommand;
import com.metasoft.veyra.platform.iam.domain.model.commands.SignUpCommand;
import com.metasoft.veyra.platform.iam.domain.model.valueobjects.EntityId;
import com.metasoft.veyra.platform.iam.domain.model.valueobjects.Roles;
import com.metasoft.veyra.platform.iam.domain.model.entities.Role;
import com.metasoft.veyra.platform.iam.domain.services.UserCommandService;
import com.metasoft.veyra.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.metasoft.veyra.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User command service implementation
 * <p>
 *     This class implements the {@link UserCommandService} interface and provides the implementation for the
 *     {@link SignInCommand} and {@link SignUpCommand} commands.
 * </p>
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

  private final UserRepository userRepository;
  private final HashingService hashingService;
  private final TokenService tokenService;
  private final ExternalNursingService externalNursingService;
  private final ExternalHcmService externalHcmService;
  private final RoleRepository roleRepository;

  /**
   * Constructor for UserCommandServiceImpl.
   * @param userRepository the user repository
   * @param hashingService the hashing service
   * @param tokenService the token service
   * @param externalNursingService the external nursing service
   * @param roleRepository the role repository
   */
  public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService, ExternalNursingService externalNursingService,ExternalHcmService externalHcmService, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.hashingService = hashingService;
    this.tokenService = tokenService;
    this.externalNursingService = externalNursingService;
    this.externalHcmService = externalHcmService;
    this.roleRepository = roleRepository;
  }

  /**
   * Handle the sign-in command
   * <p>
   *     This method handles the {@link SignInCommand} command and returns the user and the token.
   * </p>
   * @param command the sign-in command containing the username and password
   * @return and optional containing the user matching the username and the generated token
   * @throws RuntimeException if the user is not found or the password is invalid
   */
  @Override
  public Optional<ImmutableTriple<User, String, Long>> handle(SignInCommand command) {
    var user = userRepository.findByUsername(command.username());
    if (user.isEmpty())
      throw new RuntimeException("User not found");
    if (!hashingService.matches(command.password(), user.get().getPassword()))
      throw new RuntimeException("Invalid password");
    var token = tokenService.generateToken(user.get().getUsername());

    Long entityId = null;
    if (user.get().getRoles().stream().anyMatch(role -> role.getName().equals(Roles.ROLE_ADMIN))) {
      entityId = externalNursingService.fetchAdministratorEntityId(user.get().getId())
        .map(EntityId::entityId)
        .orElse(null);
    } else if (user.get().getRoles().stream().anyMatch(role -> role.getName().equals(Roles.ROLE_DOCTOR))) {
      entityId = externalHcmService.fetchStaffEntityId(user.get().getId())
        .map(EntityId::entityId)
        .orElse(null);
    }

    return Optional.of(ImmutableTriple.of(user.get(), token, entityId));
  }

    /**
     * Handle the sign-up command
     * <p>
     *     This method handles the {@link SignUpCommand} command and returns the user.
     * </p>
     * @param command the sign-up command containing the username and password
     * @return the created user
     */
    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username()))
            throw new RuntimeException("Username already exists");
        var roles = command.roles().stream().map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new RuntimeException("Role name not found"))).toList();
        var user = new User(command.username(), hashingService.encode(command.password()), roles);
        userRepository.save(user);
        return userRepository.findByUsername(command.username());
    }
    @Override
    public Optional<User> handle(SetPasswordCommand command) {
        var user = userRepository.findByActivationToken(command.activationToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired activation token"));

        user.setPassword(hashingService.encode(command.newPassword()));
        user.setActivationToken(null);
        userRepository.save(user);

        return Optional.of(user);
    }
    @Override
    public String handle(CreateRelativeAccountCommand command) {
        if (userRepository.existsByUsername(command.email()))
            throw new RuntimeException("Username already exists");

        var roles = List.of(Role.toRoleFromName("ROLE_RELATIVE"));
        var activationToken = UUID.randomUUID().toString();
        var tempPassword = hashingService.encode(UUID.randomUUID().toString());

        var user = new User(command.email(), tempPassword, roles);
        user.setActivationToken(activationToken);
        userRepository.save(user);

        return activationToken;
    }
}

package com.metasoft.veyra.platform.iam.interfaces.rest.resources;

import java.util.List;

/**
 * Resource representing an authenticated user.
 *
 * @param id       the unique identifier of the user
 * @param username the username of the user
 * @param token    the authentication token
 */
public record AuthenticatedUserResource(Long id, String username, List<String> roles, String token,Long entityId) {

}

package com.metasoft.veyra.platform.iam.interfaces.rest.transform;

import com.metasoft.veyra.platform.iam.domain.model.commands.SignUpCommand;
import com.metasoft.veyra.platform.iam.domain.model.entities.Role;
import com.metasoft.veyra.platform.iam.interfaces.rest.resources.SignUpResource;

import java.util.ArrayList;

/**
 * Assembler to convert a SignUpResource to a SignUpCommand.
 */
public class SignUpCommandFromResourceAssembler {
    /**
     * Private constructor to prevent instantiation.
     */
    private SignUpCommandFromResourceAssembler() {
        // Utility class
    }

    /**
     * Converts a SignUpResource to a SignUpCommand.
     * @param resource the SignUpResource to convert
     * @return the SignUpCommand
     */
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        var roles = resource.roles() != null ? resource.roles().stream().map(name -> Role.toRoleFromName(name)).toList() : new ArrayList<Role>();
        return new SignUpCommand(resource.username(), resource.password(), roles);
    }
}

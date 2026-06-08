package com.metasoft.veyra.platform.iam.interfaces.rest.transform;

import com.metasoft.veyra.platform.iam.domain.model.commands.SignInCommand;
import com.metasoft.veyra.platform.iam.interfaces.rest.resources.SignInResource;

/**
 * Assembler to convert a SignInResource to a SignInCommand.
 */
public class SignInCommandFromResourceAssembler {
    /**
     * Private constructor to prevent instantiation.
     */
    private SignInCommandFromResourceAssembler() {
        // Utility class
    }

    /**
     * Converts a SignInResource to a SignInCommand.
     * @param signInResource the SignInResource to convert
     * @return the SignInCommand
     */
    public static SignInCommand toCommandFromResource(SignInResource signInResource) {
        return new SignInCommand(signInResource.username(), signInResource.password());
    }
}

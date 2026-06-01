package com.metasoft.veyra.platform.iam.interfaces.rest.transform;

import com.metasoft.veyra.platform.iam.domain.model.entities.Role;
import com.metasoft.veyra.platform.iam.interfaces.rest.resources.RoleResource;

/**
 * Assembler to convert a Role entity to a RoleResource.
 */
public class RoleResourceFromEntityAssembler {
    /**
     * Private constructor to prevent instantiation.
     */
    private RoleResourceFromEntityAssembler() {
        // Utility class
    }

    /**
     * Converts a Role entity to a RoleResource.
     * @param role the Role entity to convert
     * @return the RoleResource
     */
    public static RoleResource toResourceFromEntity(Role role) {
        return new RoleResource(role.getId(), role.getStringName());
    }
}

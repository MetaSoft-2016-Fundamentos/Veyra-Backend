package com.metasoft.veyra.platform.iam.interfaces.rest.transform;

import com.metasoft.veyra.platform.iam.domain.model.commands.SetPasswordCommand;
import com.metasoft.veyra.platform.iam.interfaces.rest.resources.SetPasswordResource;

public class SetPasswordCommandFromResourceAssembler {
    public static SetPasswordCommand toCommandFromResource(SetPasswordResource resource){
        return new SetPasswordCommand(resource.token(),resource.password());
    }
}

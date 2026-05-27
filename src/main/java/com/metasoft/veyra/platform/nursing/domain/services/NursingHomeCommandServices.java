package com.metasoft.veyra.platform.nursing.domain.services;


import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateARoomToTheNursingHomeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateNursingHomeCommand;

public interface NursingHomeCommandServices {
    Long handle (CreateNursingHomeCommand command);
    void handle(CreateARoomToTheNursingHomeCommand command);

}

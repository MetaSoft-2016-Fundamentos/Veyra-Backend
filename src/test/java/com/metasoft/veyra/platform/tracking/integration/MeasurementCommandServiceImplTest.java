package com.metasoft.veyra.platform.tracking.integration;

import com.metasoft.veyra.platform.tracking.application.internal.commandservices.MeasurementCommandServiceImpl;
import com.metasoft.veyra.platform.tracking.domain.model.commands.SeedMeasurementCommand;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.mongodb.repositories.MeasurementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeasurementCommandServiceImplTest {
    @Mock private MeasurementRepository repository;
    @Mock private ApplicationEventPublisher publisher;
    @InjectMocks private MeasurementCommandServiceImpl service;

    @Test
    void handle_Seed_Exito() {
        when(repository.count()).thenReturn(0L);
        service.handle(new SeedMeasurementCommand());
        verify(repository, times(240)).save(any());
        verify(publisher, times(240)).publishEvent(any());
    }
}
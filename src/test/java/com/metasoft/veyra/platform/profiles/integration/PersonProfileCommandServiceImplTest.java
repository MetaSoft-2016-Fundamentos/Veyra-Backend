package com.metasoft.veyra.platform.profiles.integration;

import com.metasoft.veyra.platform.profiles.application.internal.commandservices.PersonProfileCommandServiceImpl;
import com.metasoft.veyra.platform.profiles.application.internal.outboundservices.storage.StorageService;
import com.metasoft.veyra.platform.profiles.domain.model.aggregates.PersonProfile;
import com.metasoft.veyra.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.metasoft.veyra.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonProfileCommandServiceImplTest {

    @Mock private PersonProfileRepository repository;
    @Mock private StorageService storageService;

    @InjectMocks
    private PersonProfileCommandServiceImpl service;

    @Test
    @DisplayName("Debe crear perfil exitosamente incluyendo la subida de foto")
    void handle_CreateProfile_Exito() {

        CreatePersonProfileCommand command = new CreatePersonProfileCommand("12345678", "Juan", "Perez",
                LocalDate.of(1990, 1, 1), 30, "test@test.com", "Street", "1", "City", "123", "PE",
                new byte[0], "photo.jpg", "999888777");

        when(storageService.upload(any(), any())).thenReturn(Map.of("url", "http://url", "publicId", "id_123"));

        when(repository.save(any(PersonProfile.class))).thenAnswer(i -> i.getArgument(0));

        Optional<PersonProfile> result = service.handle(command);

        assertThat(result).isPresent();
        assertThat(result.get().getPersonName().firstName()).isEqualTo("Juan");

        verify(storageService, times(1)).upload(any(), any());
        verify(repository, times(1)).save(any(PersonProfile.class));
    }
}
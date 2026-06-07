package com.metasoft.veyra.platform.profiles.aggregates;

import com.metasoft.veyra.platform.profiles.domain.model.aggregates.PersonProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class PersonProfileDomainTest {

    @Test
    @DisplayName("Debe actualizar correctamente los campos de PersonProfile")
    void testUpdatePersonProfile_Exito() {
        PersonProfile profile = new PersonProfile("12345678", "Juan", "Perez",
                LocalDate.of(1990, 1, 1), 34, "juan@test.com", "Calle 1", "123",
                "Lima", "15001", "PE", "url", "id", "999888777");

        var updated = profile.updatePersonProfile("87654321", "Juan", "Perez",
                LocalDate.of(1990, 1, 1), 34, "juan.new@test.com", "Calle 2", "456",
                "Lima", "15001", "PE", "url_new", "id_new", "111222333");

        assertThat(updated.getDni().dni()).isEqualTo("87654321");
        assertThat(updated.getEmailAddress().emailAddress()).isEqualTo("juan.new@test.com");
        assertThat(updated.getPhoto().photoUrl()).isEqualTo("url_new");
    }
}
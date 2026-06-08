package com.metasoft.veyra.platform.nursing.aggregates;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Medication;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.DrugPresentation;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.ExpirationDate;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.Stock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MedicationAggregateTest {
    private Medication crearMedicamentoBase(int cantidadInicial) {
        Resident mockResident = mock(Resident.class);
        when(mockResident.getId()).thenReturn(1L);

        Stock stock = new Stock(cantidadInicial);
        ExpirationDate expDate = new ExpirationDate(LocalDate.now().plusMonths(6));

        return new Medication(
                "Paracetamol", "Para la fiebre", stock, expDate,
                DrugPresentation.TABLET, "1 cada 8 horas", mockResident
        );
    }

    @Test
    @DisplayName("Debe disminuir el stock exitosamente cuando hay cantidad suficiente")
    void testDecreaseStock_Exito() {

        Medication medication = crearMedicamentoBase(50);
        int cantidadADescontar = 10;

        medication.decreaseStock(cantidadADescontar);

        assertThat(medication.getStock().amount()).isEqualTo(40);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al intentar disminuir más stock del disponible")
    void testDecreaseStock_FallaPorStockInsuficiente() {

        Medication medication = crearMedicamentoBase(5); // Solo hay 5 pastillas
        int cantidadADescontar = 10; // Queremos quitar 10

        assertThatThrownBy(() -> medication.decreaseStock(cantidadADescontar))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient stock for medication");
    }

    @Test
    @DisplayName("Debe retornar verdadero si hay stock suficiente y falso si no lo hay")
    void testHasEnoughStock_ValidacionCorrecta() {

        Medication medication = crearMedicamentoBase(20);

        assertThat(medication.hasEnoughStock(10)).isTrue();
        assertThat(medication.hasEnoughStock(25)).isFalse();
    }
}
package com.metasoft.veyra.platform.activities.domain.model.valueobjects;

import java.time.LocalTime;

/**
 * Este es tu "Read Model" (o Proyección).
 * Es un DTO de solo lectura que se llenará con la consulta JPQL
 * para coincidir con tu frontend.
 * Lo ponemos en 'valueobjects' para seguir tu patrón de 'analytics'.
 */
public record ActivityView(
        Long activityId,
        String activityName,
        LocalTime startTime,
        LocalTime endTime,
        String area,
        ActivityStatus status, // Usamos el Enum para consistencia
        Long residentId,
        String residentName, // Nombre resuelto
        Long attendantId,
        String attendantName // Nombre resuelto
) {}
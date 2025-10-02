package com.example.hospitalmvp.dto;

import jakarta.validation.constraints.NotNull;

public class AppointmentDtos {
    public record BookRequest(@NotNull Long patientId, @NotNull Long doctorId, @NotNull Long slotId) {
    }

    public record UpdateRemarks(@NotNull String remarks, String prescription) {
    }
}

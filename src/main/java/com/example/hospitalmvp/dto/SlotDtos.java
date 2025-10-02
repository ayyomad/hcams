package com.example.hospitalmvp.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class SlotDtos {
    public record CreateSlot(@NotNull Long doctorId, @NotNull @Future OffsetDateTime startTime,
            @NotNull @Future OffsetDateTime endTime) {
    }

    public record SlotView(Long id, Long doctorId, String doctorName, OffsetDateTime startTime, OffsetDateTime endTime,
            boolean available) {
    }
}

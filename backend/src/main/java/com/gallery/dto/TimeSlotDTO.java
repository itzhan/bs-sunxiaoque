package com.gallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotDTO {

    @NotNull(message = "展览ID不能为空")
    private Long exhibitionId;

    @NotBlank(message = "时段名称不能为空")
    private String slotName;

    @NotNull(message = "开始时间不能为空")
    private LocalTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalTime endTime;

    private Integer maxCapacity;
}

package com.gallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDTO {

    @NotNull(message = "展览ID不能为空")
    private Long exhibitionId;

    @NotNull(message = "时段ID不能为空")
    private Long timeSlotId;

    @NotNull(message = "预约日期不能为空")
    private LocalDate reservationDate;

    @NotNull(message = "参观人数不能为空")
    private Integer numVisitors;

    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;
}

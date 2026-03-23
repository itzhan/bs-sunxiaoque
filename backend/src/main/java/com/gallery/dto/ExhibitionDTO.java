package com.gallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExhibitionDTO {

    @NotBlank(message = "展览标题不能为空")
    private String title;

    private String description;
    private String coverImage;
    private String images;
    private Long categoryId;
    private String location;
    private String hallName;

    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    private Integer dailyCapacity;
    private BigDecimal ticketPrice;
    private Integer status;
}

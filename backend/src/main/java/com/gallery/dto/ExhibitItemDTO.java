package com.gallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExhibitItemDTO {

    @NotNull(message = "展览ID不能为空")
    private Long exhibitionId;

    @NotBlank(message = "展品名称不能为空")
    private String name;

    private String artist;
    private String era;
    private String description;
    private String coverImage;
    private String audioUrl;
    private Integer sortOrder;
}

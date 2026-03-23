package com.gallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VirtualTourDTO {

    @NotNull(message = "展览ID不能为空")
    private Long exhibitionId;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    @NotBlank(message = "全景URL不能为空")
    private String panoramaUrl;

    private String thumbnail;
    private Integer tourType;
    private Integer sortOrder;
}

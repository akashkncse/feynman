package com.feynman.orch.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrchRequestDto {
    private Integer year;
    private String dep;
    private String section;
    private Long starttime;
    private Long endtime;
    private String date;
}

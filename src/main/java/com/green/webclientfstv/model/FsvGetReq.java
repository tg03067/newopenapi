package com.green.webclientfstv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FsvGetReq {
    @Schema(defaultValue = "1")
    private String pageNo;
    @Schema(defaultValue = "10000")
    private String numOfRows;
    @Schema(defaultValue = "json")
    private String type;
}

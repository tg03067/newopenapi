package com.green.webclientfstv.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FsvGetRes {
    private String fstvlNm;
    private String fstvlStartDate;
    private String fstvlEndDate;
    private String fstvlCo;
    private String mnnstNm;
    private String auspcInsttNm;
    private String phoneNumber;
    private String homepageUrl;
    private String rdnmadr;
    private String lnmadr;
}

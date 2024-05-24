package com.green.webclientfstv;

import com.green.webclientfstv.model.FsvGetReq;
import com.green.webclientfstv.model.FsvGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/openapi")
@Slf4j
public class WebClientFsvController {
    private final WebClientService service;

    @GetMapping
    public List<FsvGetRes> getFsv(@ParameterObject @ModelAttribute FsvGetReq q){
        return service.getFsv(q);
    }
}

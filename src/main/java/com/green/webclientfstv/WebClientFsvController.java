package com.green.webclientfstv;

import com.green.webclientfstv.common.ResultDto;
import com.green.webclientfstv.model.FsvGetReq;
import com.green.webclientfstv.model.FsvEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/openapi")
@Slf4j
public class WebClientFsvController {
    private final WebClientService service;

    @GetMapping
    public ResultDto<List<FsvEntity>> getFsv(@ParameterObject @ModelAttribute FsvGetReq q){
        List<FsvEntity> list = service.getFsv(q);
        return ResultDto.<List<FsvEntity>>builder().
                status(HttpStatus.OK).
                message(HttpStatus.OK.toString()).
                data(list).
                build();
    }

    @PostMapping
    public ResultDto<Integer> postFsv(@RequestBody FsvGetReq q){
        int result = service.insFsv(q);
        return ResultDto.<Integer>builder().
                status(HttpStatus.OK).
                message(HttpStatus.OK.toString()).
                data(result).
                build();
    }
}
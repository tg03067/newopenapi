package com.green.webclientfstv;

import com.green.webclientfstv.model.FsvEntity;
import com.green.webclientfstv.model.FsvGetReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WebClientFstvMapper {
    int insFstv(FsvGetReq p);
    int insFstvEntity(List<FsvEntity> p);
}

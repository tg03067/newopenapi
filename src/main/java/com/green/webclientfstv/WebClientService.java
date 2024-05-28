package com.green.webclientfstv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.webclientfstv.model.FsvGetReq;
import com.green.webclientfstv.model.FsvEntity;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class WebClientService {
    private final WebClient webClient;
    private final String key;
    private final WebClientFstvMapper mapper;

    @Autowired
    public WebClientService(@Value("${properties.key}") String key, WebClientFstvMapper mapper) {
        this.mapper = mapper;
        HttpClient tcpClient = HttpClient.create().
                option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        ExchangeStrategies es = ExchangeStrategies.builder().
                codecs(config -> config.defaultCodecs().maxInMemorySize(-1)).
                build();
        this.webClient = WebClient.builder().
                exchangeStrategies(es).
                clientConnector(new ReactorClientHttpConnector(tcpClient)).
                defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).
                build();
        this.key = key;
    }

    public List<FsvEntity> getFsv(FsvGetReq p) {
        System.out.println(p);
        String json = null;
        String uriString = String.format("http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api?serviceKey=%s&pageNo=%s&numOfRows=%s&type=%s", this.key, p.getPageNo(), p.getNumOfRows(), p.getType());
        try {
            json = webClient.get().
                    uri(new URI(uriString)).
                    retrieve().
                    bodyToMono(String.class).
                    block();
            log.info("Response JSON: {}", json);
        } catch ( Exception e){
            log.error("Unexpected error", e);
        }

        ObjectMapper om = new ObjectMapper().
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<FsvEntity> fsvList = null;

        try{
            JsonNode node = om.readTree(json);
            fsvList = om.convertValue(node.at("/response/body/items"),
                    new TypeReference<List<FsvEntity>>(){});
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return fsvList != null ? fsvList : Collections.emptyList();
    }

    public int insFsv(FsvGetReq p) {
        List<FsvEntity> list = getFsv(p);
        int result = 0;
        result = mapper.insFstvEntity(list);
        return result;
    }
}

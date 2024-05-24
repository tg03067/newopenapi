package com.green.webclientfstv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.webclientfstv.model.FsvGetReq;
import com.green.webclientfstv.model.FsvGetRes;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WebClientService {
    private final WebClient webClient;
    private final String key;



    public WebClientService(@Value("${properties.key}") String key) {
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

    public List<FsvGetRes> getFsv(FsvGetReq p) {
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
        List<FsvGetRes> fsvList = null;

        try{
            JsonNode node = om.readTree(json);
            fsvList = om.convertValue(node.at("/response/body/items"),
                    new TypeReference<List<FsvGetRes>>(){});
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return fsvList;
    }
}

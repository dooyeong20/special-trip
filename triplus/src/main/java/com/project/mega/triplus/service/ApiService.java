package com.project.mega.triplus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ApiService {
    private final String KEY;
    private final String API_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
    private final String APP_NAME = "TRIPLus";

    @Autowired
    public ApiService(@Value("${my.api.key}") String KEY){
        this.KEY = KEY;
    }

    public String getAreaCodeXML() throws IOException {
        StringBuilder urlBuilder;

        urlBuilder = new StringBuilder(API_URL + "areaCode");
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=").append(KEY);

        addParam(urlBuilder, "numOfRows", "10");
        addParam(urlBuilder, "pageNo", "1");
        addParam(urlBuilder, "MobileOS", "ETC");
        addParam(urlBuilder, "MobileApp", APP_NAME);
        addParam(urlBuilder, "contentTypeId", "12");

        return getXMLString(urlBuilder);
    }

    private String getXMLString(StringBuilder urlBuilder) throws IOException {
        URL url;
        BufferedReader rd;
        StringBuilder sb;
        HttpURLConnection conn;
        String line;

        url = new URL(urlBuilder.toString());
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
        }

        sb = new StringBuilder();

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();

        return sb.toString();
    }

    private void addParam(StringBuilder sb, String title, String value){
        sb.append("&").append(URLEncoder.encode(title, StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(value, StandardCharsets.UTF_8));
    }
}

/*
    basic url : http://api.visitkorea.or.kr/openapi/service/rest/KorService/

    < 요청 api 종류 >
    1	국문 관광정보 서비스	areaCode	지역코드 조회
    2	categoryCode	서비스 분류코드 조회
    3	areaBasedList	지역기반 관광정보 조회
    4	locationBasedList	위치기반 관광정보 조회
    5	searchKeyword	키워드 검색 조회
    6	searchFestival	[행사/공연/축제]날짜로 목록 조회
    7	searchStay	[숙박] 베니키아,한옥,굿스테이 목록 조회
    8	detailCommon	상세정보1 - 공통정보 조회
    9	detailIntro	상세정보2 - 소개정보 조회
    10	detailInfo	상세정보3 - 반복정보 조회
    11	detailImage	상세정보4 - 이미지정보 조회

    < 콘텐츠 아이디(타입) 코드표 >
    관광지	        12
    문화시설	        14
    행사/공연/축제	15
    여행코스	        25
    레포츠	        28
    숙박	            32
    쇼핑	            38
    음식점	        39
*/
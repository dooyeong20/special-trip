package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.Accomm;
import com.project.mega.triplus.entity.Activity;
import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.entity.XMLResponse;
import com.project.mega.triplus.entity.XMLResponseItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ApiService {
    private final String PLACE = "12";
    private final String ACTIVITY = "15";
    private final String HOTEL = "32";
    private final String KEY;
    private final String API_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
    private final String APP_NAME = "TRIPLus";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public ApiService(@Value("${my.api.key}") String KEY){
        this.KEY = KEY;
    }

    public boolean loadPlaces() {
        try {
            loadPlaceWithContentType(PLACE);
            loadPlaceWithContentType(ACTIVITY);
            loadPlaceWithContentType(HOTEL);
        } catch (IOException | JAXBException e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    private void loadPlaceWithContentType(String contentType) throws IOException, JAXBException {
        List<XMLResponseItem> items;
        String xmlString;
        XMLResponse response;
        int pageIdx = 0;
        Place place = null;

        while (true) {
            xmlString = getAreaBasedListXML(contentType, null,500, ++pageIdx);
            response = getXMLResponse(xmlString);
            items = response.getBody().getItemContainer().getItems();

            if (items.size() < 1) {
                break;
            }

            for (XMLResponseItem item : items) {

                if(contentType.equals(ACTIVITY)){
                    place = new Activity();
                }
                else if(contentType.equals(PLACE)){
                    place = new Place();
                }
                else if(contentType.equals(HOTEL)){
                    place = new Accomm();
                }

                if(place != null) {
                    place.setContentType(contentType);
                    place.setName(item.getPlaceName());
                    place.setContentId(item.getContentId());
                    place.setMapX(item.getMapX());
                    place.setMapY(item.getMapY());
                    place.setCat1(item.getCat1());
                    place.setCat2(item.getCat2());
                    place.setCat3(item.getCat3());
                    place.setThumbnailUrl(item.getImageUrl());
                    place.setTel(item.getTel());

                    // TODO: repository refactoring
                    em.persist(place);
                    em.flush();
                    em.clear();
                }
            }
        }
    }

    public String getAreaBasedListXML(String contentTypeId, String areaCode, int pageSize, int pageNo) throws IOException{
        StringBuilder urlBuilder = getStringBuilder("areaBasedList");

        if(null != contentTypeId) {
            addParam(urlBuilder, "contentTypeId", contentTypeId);
        }

        if(null != areaCode){
            addParam(urlBuilder, "areaCode", areaCode);
        }

        addParam(urlBuilder, "numOfRows", String.valueOf(pageSize));
        addParam(urlBuilder, "pageNo", String.valueOf(pageNo));

        return getXMLString(urlBuilder);
    }

    // 테스트용 메서드
    public String getAreaCodeXML() throws IOException {
        StringBuilder urlBuilder = getStringBuilder("areaCode");

        addParam(urlBuilder, "numOfRows", "30");
        addParam(urlBuilder, "pageNo", "1");
        addParam(urlBuilder, "contentTypeId", "12");

        return getXMLString(urlBuilder);
    }

    private StringBuilder getStringBuilder(String service) {
        StringBuilder urlBuilder= new StringBuilder(API_URL + service);
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=").append(KEY);

        addParam(urlBuilder, "MobileOS", "ETC");
        addParam(urlBuilder, "MobileApp", APP_NAME);

        return urlBuilder;
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

    public XMLResponse getXMLResponse(String xmlString) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(XMLResponse.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        return (XMLResponse) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));
    }

}

/*
    basic url : http://api.visitkorea.or.kr/openapi/service/rest/KorService/


    -------------------------------------------------------
    < 요청 api 종류 >
    국문 관광정보 서비스

    1   areaCode	지역코드 조회
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
    -------------------------------------------------------

    ------------------------------
    < 콘텐츠 아이디(타입) 코드표 >
    관광지	        12
    문화시설	        14
    행사/공연/축제	15
    여행코스	        25
    레포츠	        28
    숙박	            32
    쇼핑	            38
    음식점	        39
    -------------------------------

    -------------------------------
    < areaCode >

    1 서울
    2 인천
    3 대전
    4 대구
    5 광주
    6 부산
    7 울산
    8 세종특별자치시
    31 경기도
    32 강원도
    33 충청북도
    34 충청남도
    35 경상북도
    36 경상남도
    37 전라북도
    38 전라남도
    39 제주도
    -------------------------------
*/
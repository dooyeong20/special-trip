package com.project.mega.triplus;

import com.project.mega.triplus.entity.*;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest

class TriplusApplicationTests {

	@PersistenceContext
	private EntityManager em;

	@Value("${my.api.key}")
	private String myServiceKey;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	void contextLoads() {
	}

	@Test
	void api_test() throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/categoryCode"); /*URL*/
		urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + myServiceKey); /*Service Key*/
//		urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("s3r5PXGQygPdPN0nbod5z5ro7B44y3ZU0MhLkAwYmKy652rhUS3LYDS4v9GgDbcL53oJtC1XW5Vo3q9lDc%2FndA%3D%3D", "UTF-8")); /*공공데이터포털에서 발급받은 인증키*/
		urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
		urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*현재페이지번호*/
		urlBuilder.append("&" + URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8")); /*IOS(아이폰), AND(안드로이드), WIN(원도우폰), ETC*/
		urlBuilder.append("&" + URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode("TRIPLus", "UTF-8")); /*서비스명=어플명*/
		urlBuilder.append("&" + URLEncoder.encode("contentTypeId","UTF-8") + "=" + URLEncoder.encode("12", "UTF-8")); /*관광타입(관광지, 숙박 등) ID*/
		urlBuilder.append("&" + URLEncoder.encode("cat1","UTF-8") + "=" + URLEncoder.encode("A01", "UTF-8")); /*대분류코드*/
		urlBuilder.append("&" + URLEncoder.encode("cat2","UTF-8") + "=" + URLEncoder.encode("A0101", "UTF-8")); /*중분류코드(cat1필수)*/
//		urlBuilder.append("&" + URLEncoder.encode("cat3","UTF-8") + "=" + URLEncoder.encode("A01010100", "UTF-8")); /*소분류코드(cat1,cat2필수)*/
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/xml");
		System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;

		if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
		}

		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();

		System.out.println(sb.toString());
	}

	@Test
	@Transactional
	@Rollback(value = false)
	void erd_test(){
		User kim = new User();
		Plan planA = new Plan();
		Day day1 = new Day();
		Day day2 = new Day();
		Place seoul = new Place();
		Place ulsan = new Place();

		kim.setNickName("kimkim");
		kim.setPassword("secret");
		kim.setRole(Role.USER);


		seoul.setName("SEOUL");
		seoul.setContent("HAPPY SEOUL");

		ulsan.setName("ULSAN");
		ulsan.setContent("GOOD ULSAN");

		planA.setName("PLAN A");
		planA.setStatus(PlanStatus.WRITING);
		planA.setUser(kim);

		day1.setName("FIRST DAY");
		day1.addPlace(seoul);

		day2.setName("SECOND DAY");
		day2.addPlace(ulsan);

		day1.setPlan(planA);
		day2.setPlan(planA);

		em.persist(kim);
		em.persist(seoul);
		em.persist(ulsan);

		List<Plan> myPlans = kim.getMyPlans();


		for(Plan plan : myPlans){
			logger.info(plan.getName());
			for(Day day : plan.getDays()){
				logger.info(day.getName());
				for(Place place : day.getPlaces()){
					logger.info(place.getName());
				}
			}
		}
	}


}

package com.project.mega.triplus;

import com.project.mega.triplus.entity.Day;
import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.entity.Plan;
import com.project.mega.triplus.entity.PlanStatus;
import com.project.mega.triplus.entity.Role;
import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.entity.XMLResponse;
import com.project.mega.triplus.entity.XMLResponseItem;
import com.project.mega.triplus.service.ApiService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest


class jgTriplusApplicationTests {

	@PersistenceContext
	private EntityManager em;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ApiService apiService;

	@Test
	void contextLoads() {
	}

	@Test
	@Transactional
	@Rollback(value = false)
	void api_test() throws IOException, JAXBException {
		String xmlString;
		XMLResponse response;
		List<XMLResponseItem> items;
		int pageIdx = 0;
		int cnt = 0;

		while(true){
			xmlString = apiService.getAreaBasedListXML("12","7", ++pageIdx);
			response = apiService.getXMLResponse(xmlString);
			items = response.getBody().getItemContainer().getItems();

			if(items.size() < 1){
				break;
			}

			for(XMLResponseItem item : items){
				Place place = new Place();
				place.setName(item.getPlaceName());
				em.persist(place);
				em.flush();
				em.clear();
				logger.info("content id : " + item.getContentId() + ", image URL : " + item.getImageUrl());
				++cnt;
			}
		}

		Assertions.assertThat(response.getBody().getTotalCount()).isEqualTo(String.valueOf(cnt));
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

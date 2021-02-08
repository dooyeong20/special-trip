package com.project.mega.triplus;

import com.project.mega.triplus.entity.*;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest

class TriplusApplicationTests {

	@PersistenceContext
	private EntityManager em;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	void contextLoads() {
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

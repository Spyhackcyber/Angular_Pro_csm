package com.tpms.contoller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tpms.entity.Activity;
import com.tpms.service.ActivityService;

public class ActivityControllerTest {

	@Autowired
	ActivityService activityService;
	
	@Test
	void testGetActivityById(Integer activityId) {
		activityId = 1;
		Activity activity = new Activity();
		activity.setActivityId(activityId);
		activityService.getActivityById(activityId);
	}
}

package com.sogou.admin.workorder.workflow.service;

import com.bupt.workflow.service.process.ProcessManagerService;
import main.WorkflowApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * ProcessManagerService单测
 * 
 * @author shanglonghua
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WorkflowApplication.class })
public class ProcessManagerServiceTest {

	@Resource
	ProcessManagerService processManagerService;

	@Test
	public void testSubmitStartFormDataByProcessDefinitionKey() {
		String definetionKey = "workOrderProcess";
		String userName = "zhangsan@sogou-inc.com";
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("firstCandidateGroups", "1");
		paramsMap.put("firstAssignee", "zengmin@sogou-inc.com");
		String businessKey = this.processManagerService.submitStartFormDataByProcessDefinitionKey(definetionKey,
		        "11301", paramsMap, userName);
		System.out.println(businessKey);
	}
}

package com.fuli.cloud.message.provider.web.controller;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fuli.cloud.FuliCloudMessageProviderApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FuliCloudMessageProviderApplication.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class FuliMqMessageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetMessageByMessageId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api-message/v1/message/11111")
				.accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void testSaveMessageWaitingConfirm() {
		fail("Not yet implemented");
	}

	@Test
	public void testConfirmAndSendMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveAndSendMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testResendMessageById() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetMessageToAlreadyDeadById() {
		fail("Not yet implemented");
	}

	@Test
	public void testReSendAllDeadMessageByQueueName() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteMessageByMessageId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMessagePage() {
		fail("Not yet implemented");
	}

}

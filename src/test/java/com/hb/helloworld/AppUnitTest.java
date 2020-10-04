package com.hb.helloworld;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(value = AppController.class)
public class AppUnitTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void retrieveDetailsForCourse() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/hello")
				.accept(MediaType.ALL_VALUE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse());
		String expected = "Hello Hepsiburada from Oguz !";
		assertNotEquals(expected, result.getResponse().getContentAsString());
	}

}

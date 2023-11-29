package com.example.demo;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleTests {

	@Test
	void test() {

		String checkStr = "/h2-";

		List<String> test1 = Arrays.asList("/h2-console", "/h2-console/", "/h2-console/login.jsp");
		log.info("test1 : " + test1.contains(checkStr));

		String test2 = "/h2-console";
		log.info("test2 : " + test2.contains(checkStr));
	}

}

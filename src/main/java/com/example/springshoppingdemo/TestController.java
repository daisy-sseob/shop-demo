package com.example.springshoppingdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping(value = "/test")
	public UserDto test() {

		UserDto userDto = new UserDto();
		userDto.setAge(1);
		userDto.setName("sseob");
		
		return userDto;
	}
}

package com.nowcoder;

import com.nowcoder.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WendaApplication {

	public static void main(String[] args) {
		/*
		传统编程方式
		User user = new User("xxx");
		user.setName("XXXX");
		user.setFriend(user);
		需要初始化
		 */
		SpringApplication.run(WendaApplication.class, args);
	}
}

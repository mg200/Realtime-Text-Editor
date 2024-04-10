package com.envn8.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import com.envn8.app.repository.UserRepository;
import com.envn8.app.models.User;




@SpringBootApplication
public class AppApplication implements CommandLineRunner{

private UserRepository userRepository = null;

	@Autowired
	public AppApplication(UserRepository userRepository){
		this.userRepository= userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception{

		if(userRepository.findAll().isEmpty()){

			userRepository.save(new User("ahmed","waleed"));
			userRepository.save(new User("omar","waleed"));
		}

		for(User user :userRepository.findAll()){
			System.out.println(user);
			System.out.println("Hello it's Me!");

		}
		//write code about how to read input from the front end
		
	}
}

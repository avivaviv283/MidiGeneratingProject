package com.example.midigeneratorproject;

import com.example.midigeneratorproject.entity.Role;
import com.example.midigeneratorproject.entity.User;
import com.example.midigeneratorproject.repository.RoleRepository;
import com.example.midigeneratorproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class} )
public class MidiGeneratorProjectApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {

		SpringApplication.run(MidiGeneratorProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role role = roleRepository.findByName("ROLE_ADMIN");
		if (role == null) {
			User user = new User("admin user", "admin@gmail.com", passwordEncoder.encode("admin"),
			new Role("ROLE_ADMIN"));
			userRepository.save(user);

//			user = new User("user user", "user@gmail.com", passwordEncoder.encode("user"),
//					new Role("ROLE_USER"));
//			userRepository.save(user);

		}

	}
}

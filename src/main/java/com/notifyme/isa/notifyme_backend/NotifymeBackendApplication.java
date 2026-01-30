package com.notifyme.isa.notifyme_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class NotifymeBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(NotifymeBackendApplication.class, args);

	}

	@Bean
	CommandLineRunner checkBeans(ApplicationContext ctx) {
		return args -> {
			System.out.println("Exchange bean loaded? " + (ctx.containsBean("notifyMeExchange")));
			System.out.println("Queue bean loaded? " + (ctx.containsBean("notifyMeQueue")));
			System.out.println("DLX bean loaded? " + (ctx.containsBean("deadLetterExchange")));
			System.out.println("DLQ bean loaded? " + (ctx.containsBean("deadLetterQueue")));
			System.out.println("RabbitAdmin loaded? " + (ctx.containsBean("rabbitAdmin")));
			System.out.println("RabbitTemplate loaded? " + (ctx.containsBean("rabbitTemplate")));
			System.out.println("Listener factory loaded? " + (ctx.containsBean("rabbitListenerContainerFactory")));
		};
	}


}

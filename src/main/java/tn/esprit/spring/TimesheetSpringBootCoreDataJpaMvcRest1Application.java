package tn.esprit.spring;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;




import tn.esprit.spring.services.EmployeServiceImpl;

@SpringBootApplication
@Component
public class TimesheetSpringBootCoreDataJpaMvcRest1Application {
	@Autowired
	static EmployeServiceImpl controller;

	public static void main(String[] args) {
		SpringApplication.run(TimesheetSpringBootCoreDataJpaMvcRest1Application.class, args);

	}

}

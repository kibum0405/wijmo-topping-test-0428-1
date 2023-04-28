package springbootwijmotopping.common;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import springbootwijmotopping.DepartmentApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = { DepartmentApplication.class })
public class CucumberSpingConfiguration {}

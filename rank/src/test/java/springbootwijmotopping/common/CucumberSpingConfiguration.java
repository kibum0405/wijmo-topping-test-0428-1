package springbootwijmotopping.common;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import springbootwijmotopping.RankApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = { RankApplication.class })
public class CucumberSpingConfiguration {}

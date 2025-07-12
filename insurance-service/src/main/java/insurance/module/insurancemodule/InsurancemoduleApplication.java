package insurance.module.insurancemodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication(scanBasePackages = {
    "insurance.module.insurancemodule.service",
    "insurance.module.insurancemodule.controller",
    "insurance.module.insurancemodule.repository",
    "insurance.module.insurancemodule.entity"
})
@ComponentScan(basePackages = {
    "insurance.module.insurancemodule"
})
 // This annotation now correctly imports
public class InsurancemoduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsurancemoduleApplication.class, args);
    }

}
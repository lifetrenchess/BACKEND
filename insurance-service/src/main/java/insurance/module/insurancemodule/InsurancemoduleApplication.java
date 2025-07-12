package insurance.module.insurancemodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
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
@EnableDiscoveryClient
@EnableFeignClients
public class InsurancemoduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsurancemoduleApplication.class, args);
    }

}
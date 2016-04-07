package za.co.discovery;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "za.co.discovery.dataAccess")
public class DaoConfig {

}

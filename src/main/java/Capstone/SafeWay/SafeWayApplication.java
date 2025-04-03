package Capstone.SafeWay;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(title = "SafeWay API", version = "v1"),
		servers = {
				@Server(url = "http://3.39.8.9:8080", description = "EC2 Public Server")
		}
)
@SpringBootApplication
public class SafeWayApplication {
	public static void main(String[] args) {
		SpringApplication.run(SafeWayApplication.class, args);
	}
}
